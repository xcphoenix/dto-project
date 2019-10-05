package com.xcphoenix.dto.service.impl;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.Location;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.bean.SortType;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static final double NEARBY_DISTANCE = 10;

    @Autowired
    public RestaurantServiceImpl(RestaurantMapper restaurantMapper, Base64ImgService base64ImgService) {
        this.restaurantMapper = restaurantMapper;
        this.base64ImgService = base64ImgService;
    }

    @Override
    public Integer getUserRestaurantId(Integer userId) {
        return restaurantMapper.hasRestaurant(userId);
    }

    @Override
    public boolean isNameUsable(String name) {
        return restaurantMapper.sameNameRsId(name) == null;
    }

    @Override
    public Restaurant addNewRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setUserId((Integer)ContextHolderUtils.getRequest().getAttribute("userId"));
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
        return restaurant;
    }

    @ShopperCheck
    @Override
    public Restaurant getRestaurantDetail(Integer userId) {
        return restaurantMapper.getUserShopDetail(userId);
    }

    @Override
    public Integer getLoginShopperResId() {
        Integer userId = (Integer) ContextHolderUtils.getRequest().getAttribute("userId");
        return getUserRestaurantId(userId);
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) throws IOException {
        restaurant.setUserId((Integer) ContextHolderUtils.getRequest().getAttribute("userId"));
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
    }

    @Override
    public List<Map<String, Object>> getNearbyRestaurants(double lon, double lat,
                                                          Integer offset, Integer limit) throws IOException {
        Location userLoc = new Location(lon, lat);

        // 规范分页参数
        offset = (offset == null || offset < 0) ? 0 : offset;
        limit = (limit == null || limit < 0 || limit > 50) ? 50 : limit;

        RestClientBuilder clientBuilder = RestClient.builder(
                new HttpHost("47.94.5.149", 9200, "http"));
        Header[] defaultHeaders = new Header[]{new BasicHeader("ContentType", "application/json")};
        clientBuilder.setDefaultHeaders(defaultHeaders);
        RestClient restClient = clientBuilder.build();

        Request request = new Request(
                "GET",
                "/restaurant/_search"
        );
        // 设置参数
        request.addParameter("size", String.valueOf(limit));
        request.addParameter("from", String.valueOf(offset));
        request.setJsonEntity(setGeoArgs(NEARBY_DISTANCE, userLoc.getLat().doubleValue(), userLoc.getLon().doubleValue()));

        Response response = restClient.performRequest(request);
        restClient.close();

        JSONArray responseBody = JSONObject.parseObject(EntityUtils.toString(response.getEntity()))
                .getJSONObject("hits")
                .getJSONArray("hits");

        int resArraySize = responseBody.size();
        List<Map<String, Object>> restaurantList = new ArrayList<>(resArraySize);

        // TODO 为不在营业时间的店铺，添加标记
        for (int i = 0; i < resArraySize; i++) {
            JSONObject jsonResData = responseBody.getJSONObject(i);
            Map<String, Object> resMap = jsonResData.getJSONObject("_source").getInnerMap();
            Double distance = jsonResData.getJSONArray("sort").getDouble(0);
            resMap.put("distance", distance);
            resMap.put("out_of_range", Double.parseDouble(resMap.get("delivery_range").toString()) > distance);
            restaurantList.add(resMap);
        }

        return restaurantList;
    }

    private String setGeoArgs(double kl, double lat, double lon) {
        String reqBody = "{\n" +
                "    \"_source\": {\n" +
                "        \"excludes\": [ \"@*\", \"type\", \"gmt_create\", \"addr*\", \"*revenue\", \"foods\"]\n" +
                "    },\n" +
                "    \"query\": {\n" +
                "        \"bool\" : {\n" +
                "            \"must\" : {\n" +
                "                \"match_all\" : {}\n" +
                "            },\n" +
                "            \"filter\" : {\n" +
                "                \"geo_distance\" : {\n" +
                "                    \"distance\" : \"0km\",\n" +
                "                    \"location\" : {\n" +
                "                        \"lat\" : 0.0,\n" +
                "                        \"lon\" : 0.0\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"sort\": [{\n" +
                "        \"_geo_distance\": {\n" +
                "          \"location\": { \n" +
                "            \"lat\": 0,\n" +
                "            \"lon\": 0\n" +
                "          },\n" +
                "          \"order\":         \"asc\",\n" +
                "          \"unit\":          \"km\", \n" +
                "          \"distance_type\": \"plane\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "}\n";
        JSONObject reqBodyJson = JSONObject.parseObject(reqBody);
        JSONObject geoDistance = reqBodyJson
                .getJSONObject("query")
                .getJSONObject("bool")
                .getJSONObject("filter")
                .getJSONObject("geo_distance");
        JSONObject sortSetting = reqBodyJson
                .getJSONArray("sort")
                .getJSONObject(0)
                .getJSONObject("_geo_distance")
                .getJSONObject("location");
        geoDistance.put("distance", kl + "km");
        JSONObject qLocation = geoDistance.getJSONObject("location");
        qLocation.put("lat", lat);
        qLocation.put("lon", lon);
        sortSetting.put("lat", lat);
        sortSetting.put("lon", lon);

        log.info("requestJson is : " +  reqBodyJson.toJSONString());

        return reqBodyJson.toJSONString();
    }

    /**
     * 分类
     * - 综合排序(相关度)
     * - 好评优先(评分)
     * - 起送价最低
     * - 配送最快
     * - 人均消费低到高
     * - 人均消费高到低
     */
    private String setSearchArgs(String text, SortType sortType) {
        JSONObject query = JSONObject.parseObject("" +
                "{\n" +
                "    \"query\": {\n" +
                "        \"dis_max\": {\n" +
                "            \"queries\": [\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}");
        JSONArray matches = query.getJSONObject("dis_max").getJSONArray("queries");
        matches.add(0, new HashMap<String, Object>(1).put("restaurant_name", text));
        matches.add(1, new HashMap<String, Object>(1).put("foods", text));
        return null;
    }

}
