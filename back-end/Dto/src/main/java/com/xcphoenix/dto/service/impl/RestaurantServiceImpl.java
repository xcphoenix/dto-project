package com.xcphoenix.dto.service.impl;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.Location;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import com.xcphoenix.dto.util.TimeFormatUtils;
import com.xcphoenix.dto.util.es.EsRestBuilder;
import com.xcphoenix.dto.util.es.SearchRst;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO 设置多个开店时间
 *
 * @author xuanc
 * @version 1.0
 * @date 2019/8/13 上午9:03
 */
@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private RestaurantMapper restaurantMapper;
    private Base64ImgService base64ImgService;

    @Value("${upload.image.directory.store}")
    private String storeImgDire;
    @Value("${upload.image.directory.instore}")
    private String inShoreImgDire;
    @Value("${upload.image.directory.logo}")
    private String logoImgDire;
    @Value("${upload.image.directory.banner}")
    private String bannerImgDire;

    private final int precision = 12;

    @Autowired
    public RestaurantServiceImpl(RestaurantMapper restaurantMapper, Base64ImgService base64ImgService) {
        this.restaurantMapper = restaurantMapper;
        this.base64ImgService = base64ImgService;
    }

    @Override
    public boolean isExists(Long rstId) {
        return restaurantMapper.isShopExists(rstId) == 1;
    }

    @Override
    public Long getUserRestaurantId(Long userId) {
        return restaurantMapper.hasRestaurant(userId);
    }

    @Override
    public boolean isNameUsable(String name) {
        return restaurantMapper.sameNameRsId(name) == null;
    }

    @Override
    @CachePut(value = "rstCacheManager", key = "'rst:' + #result.restaurantId + ':detail'")
    public Restaurant addNewRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setUserId((Long) ContextHolderUtils.getRequest().getAttribute("userId"));
        restaurant.setStoreImg(base64ImgService.convertPicture(restaurant.getStoreImg(), storeImgDire));
        restaurant.setLogo(base64ImgService.convertPicture(restaurant.getLogo(), inShoreImgDire));
        restaurant.setBannerImg(base64ImgService.convertPicture(restaurant.getBannerImg(), bannerImgDire));
        restaurant.rangeFormat();

        // pictures
        StringBuilder builder = new StringBuilder();
        for (String instore : restaurant.getInstoreImgs()) {
            String path = base64ImgService.convertPicture(instore, inShoreImgDire);
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(path);
        }
        restaurant.setInstoreImg(builder.toString());

        // geohash
        GeoHash geoHash = GeoHash.withCharacterPrecision(
                restaurant.getAddrLat().doubleValue(),
                restaurant.getAddrLng().doubleValue(), precision
        );
        restaurant.setGeohash(geoHash.toBase32());

        try {
            restaurantMapper.addRestaurant(restaurant);
        } catch (DuplicateKeyException dke) {
            throw new ServiceLogicException(ErrorCode.USER_HAVE_SHOP);
        }

        restaurant.dataConvertToShow();
        return restaurant;
    }

    @ShopperCheck
    @Override
    public Restaurant getRstByShopper(Long userId) {
        return restaurantMapper.getUserShopDetail(userId);
    }

    @Override
    @Cacheable(value = "rstCacheManager", key = "'rst:' + #rstId + ':detail'")
    public Restaurant getRstDetail(Long rstId) {
        return restaurantMapper.getShopDetailById(rstId);
    }

    @ShopperCheck
    @Override
    public Long getLoginShopperResId() {
        Long userId = (Long) ContextHolderUtils.getRequest().getAttribute("userId");
        return getUserRestaurantId(userId);
    }

    @Override
    @CachePut(value = "rstCacheManager", key = "'rst:' + #restaurant.restaurantId + ':detail'")
    public Restaurant updateRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setUserId((Long) ContextHolderUtils.getRequest().getAttribute("userId"));
        restaurant.setRestaurantId(getLoginShopperResId());
        restaurant.rangeFormat();

        // picture
        if (restaurant.getStoreImg() != null) {
            restaurant.setStoreImg(base64ImgService.convertPicture(restaurant.getStoreImg(), storeImgDire));
        }
        if (restaurant.getLogo() != null) {
            restaurant.setLogo(base64ImgService.convertPicture(restaurant.getLogo(), inShoreImgDire));
        }
        if (restaurant.getBannerImg() != null) {
            restaurant.setBannerImg(base64ImgService.convertPicture(restaurant.getBannerImg(), bannerImgDire));
        }

        if (restaurant.getInstoreImgs() != null) {
            StringBuilder builder = new StringBuilder();
            for (String instore : restaurant.getInstoreImgs()) {
                String path = base64ImgService.convertPicture(instore, inShoreImgDire);
                if (builder.length() != 0) {
                    builder.append(",");
                }
                builder.append(path);
            }
            restaurant.setInstoreImg(builder.toString());
        }

        // geohash
        if (restaurant.getAddrLng() != null && restaurant.getAddrLat() != null) {
            GeoHash geoHash = GeoHash.withCharacterPrecision(
                    restaurant.getAddrLat().doubleValue(),
                    restaurant.getAddrLng().doubleValue(), precision
            );
            restaurant.setGeohash(geoHash.toBase32());
        }

        restaurantMapper.updateRestaurant(restaurant);
        return getRstDetail(restaurant.getRestaurantId());
    }

    @Override
    public List<Map<String, Object>> getRstRemark(int type, double lon, double lat, Integer from, Integer size)
            throws IOException {
        Location.checkValues(lon, lat);

        RestClient restClient = EsRestBuilder.buildSearchRestClient();
        Request request = EsRestBuilder.setRestRequest(size, from);
        request.setJsonEntity(
                SearchRst.init(lat, lon)
                        .setDistanceField()
                        .setIncludeFields()
                        .setQuery()
                        .setSort(type)
                        .buildReqJson()
        );

        Response response = restClient.performRequest(request);
        restClient.close();

        return dealBriefResp(response);
    }

    @Override
    public List<Map<String, Object>> getRstRemarkWithSearch(String text, int type, double lon, double lat,
                                                            Integer from, Integer size) throws IOException {
        RestClient restClient = EsRestBuilder.buildSearchRestClient();
        Request request = EsRestBuilder.setRestRequest(size, from);
        request.setJsonEntity(
                SearchRst.init(lat, lon)
                        .setDistanceField()
                        .setIncludeFields()
                        .setQuery()
                        .setSort(type)
                        .setSearchKeywords(text)
                        .buildReqJson()
        );

        Response response = restClient.performRequest(request);
        restClient.close();

        return dealBriefResp(response);
    }

    /**
     * 处理 response
     */
    private List<Map<String, Object>> dealBriefResp(Response resp) throws IOException {

        log.debug("[Es::deal resp] response ==> " + EntityUtils.toString(resp.getEntity()));

        JSONArray responseBody = JSONObject.parseObject(EntityUtils.toString(resp.getEntity()))
                .getJSONObject("hits")
                .getJSONArray("hits");

        int resArraySize = responseBody.size();
        List<Map<String, Object>> restaurantList = new ArrayList<>(resArraySize);

        for (int i = 0; i < resArraySize; i++) {
            JSONObject jsonResData = responseBody.getJSONObject(i);
            Map<String, Object> resMap = jsonResData.getJSONObject("_source").getInnerMap();
            Double distance = getDistance(responseBody, i);
            resMap.put("distance", distance);
            resMap.put("out_of_range", Double.parseDouble(resMap.get("delivery_range").toString()) < distance);
            resMap.put("is_rest", Restaurant.isRest(TimeFormatUtils.utcFormat((String) resMap.get("bh_start")),
                    TimeFormatUtils.utcFormat((String) resMap.get("bh_end"))));
            // for single time
            resMap.put("bh_start", TimeFormatUtils.utcFormat((String) resMap.get("bh_start")));
            resMap.put("bh_end", TimeFormatUtils.utcFormat((String) resMap.get("bh_end")));
            restaurantList.add(resMap);
        }

        return restaurantList;
    }

    /**
     * 从 Json 数据中获取距离
     * 1: nearby
     * 2: search
     */
    private Double getDistance(JSONArray jsonArray, int pos) {
        String jsonPath = "$[" + pos + "].fields.distance[0]";
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.parseDouble(df.format(JSONPath.eval(jsonArray,jsonPath)));
    }

}
