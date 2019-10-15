package com.xcphoenix.dto.service.impl;

import ch.hsr.geohash.GeoHash;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xcphoenix.dto.annotation.ShopperCheck;
import com.xcphoenix.dto.bean.Location;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.bean.SortType;
import com.xcphoenix.dto.bean.TimeItem;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.mapper.RestaurantMapper;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.Base64ImgService;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.util.ContextHolderUtils;
import com.xcphoenix.dto.util.GetUrlUtils;
import com.xcphoenix.dto.util.TimeFormatUtils;
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
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.*;

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
    public Long getUserRestaurantId(Long userId) {
        return restaurantMapper.hasRestaurant(userId);
    }

    @Override
    public boolean isNameUsable(String name) {
        return restaurantMapper.sameNameRsId(name) == null;
    }

    @Override
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
        return restaurant;
    }

    @ShopperCheck
    @Override
    public Restaurant getRestaurantDetail(Long userId) {
        return restaurantMapper.getUserShopDetail(userId);
    }

    @Override
    public Long getLoginShopperResId() {
        Long userId = (Long) ContextHolderUtils.getRequest().getAttribute("userId");
        return getUserRestaurantId(userId);
    }

    @Override
    public void updateRestaurant(Restaurant restaurant) throws IOException {
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
    }

    @Override
    public List<Map<String, Object>> getNearbyRestaurants(double lon, double lat,
                                                          Integer from, Integer size) throws IOException {
        Location.checkValues(lon, lat);

        RestClient restClient = buildSearchRestClient();
        Request request = setRestRequest(size, from);
        request.setJsonEntity(setGeoArgs(NEARBY_DISTANCE, lat, lon));

        Response response = restClient.performRequest(request);
        restClient.close();

        return dealBriefResp(response, 1);
    }

    @Override
    public List<Map<String, Object>> searchRstAsSortType(String text, int type, double lon, double lat,
                                                         Integer from, Integer size) throws IOException {
        Location.checkValues(lon, lat);
        if (type < 0 || type >= SortType.values().length) {
            throw new ServiceLogicException(ErrorCode.INVALID_SEARCH_TYPE);
        }

        RestClient restClient = buildSearchRestClient();
        Request request = setRestRequest(size, from);
        request.setJsonEntity(setSearchArgs(text, SortType.values()[type], lat, lon).toJSONString());

        Response response = restClient.performRequest(request);
        restClient.close();

        return dealBriefResp(response, 2);
    }

    private List<Map<String, Object>> dealBriefResp(Response resp, int type) throws IOException {

        JSONArray responseBody = JSONObject.parseObject(EntityUtils.toString(resp.getEntity()))
                .getJSONObject("hits")
                .getJSONArray("hits");

        int resArraySize = responseBody.size();
        List<Map<String, Object>> restaurantList = new ArrayList<>(resArraySize);

        for (int i = 0; i < resArraySize; i++) {
            JSONObject jsonResData = responseBody.getJSONObject(i);
            Map<String, Object> resMap = jsonResData.getJSONObject("_source").getInnerMap();
            Double distance = getDistance(responseBody, type, i);
            resMap.put("distance", distance);
            resMap.put("out_of_range", Double.parseDouble(resMap.get("delivery_range").toString()) < distance);
            resMap.put("is_rest", isRest(TimeFormatUtils.utcFormat((String) resMap.get("bh_start")),
                    TimeFormatUtils.utcFormat((String) resMap.get("bh_end"))));
            // for single time
            resMap.put("bh_start", TimeFormatUtils.utcFormat((String) resMap.get("bh_start")));
            resMap.put("bh_end", TimeFormatUtils.utcFormat((String) resMap.get("bh_end")));
            restaurantList.add(resMap);
        }

        return restaurantList;
    }

    /**
     * 1: nearby
     * 2: search
     */
    private Double getDistance(JSONArray jsonArray, int type, int pos) {
        String jsonPath;
        if (type == 1) {
            jsonPath = "$[" + pos + "].sort[0]";
        } else {
            jsonPath = "$[" + pos + "].fields.distance[0]";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return Double.parseDouble(df.format(JSONPath.eval(jsonArray,jsonPath)));
    }

    /**
     * 设置地理查询参数
     */
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
                "          \"unit\":          \"m\", \n" +
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

        log.info("requestJson is : " + reqBodyJson.toJSONString());

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
    private JSONObject setSearchArgs(String text, SortType sortType, double lat, double lon) {
        JSONObject query = JSONObject.parseObject("{\n" +
                "  \"_source\": {\n" +
                "    \"excludes\": [\n" +
                "      \"type\",\n" +
                "      \"@timestamp\",\n" +
                "      \"@version\",\n" +
                "      \"addr_*\",\n" +
                "      \"gmt_create\",\n" +
                "      \"contact_man\",\n" +
                "      \"country_id\"\n" +
                "      \"foods\"\n" +
                "    ]\n" +
                "  },\n" +
                "  \"stored_fields\": [\n" +
                "    \"_source\"\n" +
                "  ],\n" +
                "  \"script_fields\": {\n" +
                "    \"distance\": {\n" +
                "      \"script\": {\n" +
                "        \"source\": \"doc['location'].arcDistance(params.lat,params.lon)\",\n" +
                "        \"lang\": \"painless\",\n" +
                "        \"params\": {\n" +
                "          \"lat\": 0.0,\n" +
                "          \"lon\": 0.0\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"must\": {\n" +
                "        \"match_all\": {}\n" +
                "      },\n" +
                "      \"filter\": {\n" +
                "        \"geo_distance\": {\n" +
                "          \"distance\": \"10km\",\n" +
                "          \"location\": \"0.0,0.0\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"post_filter\": {\n" +
                "    \"multi_match\": {\n" +
                "      \"query\": \"\",\n" +
                "      \"fields\": [\n" +
                "        \"restaurant_name^4\",\n" +
                "        \"foods^2\",\n" +
                "        \"tag\"\n" +
                "      ],\n" +
                "      \"minimum_should_match\": \"30%\"\n" +
                "    }\n" +
                "  }\n" +
                "}");
        // 设置搜索字段
        JSONPath.set(query, "$.post_filter.multi_match.query", text);
        // 设置 geo
        JSONPath.set(query, "$query.bool.filter.geo_distance.location", lat + "," + lon);
        JSONPath.set(query, "$.script_fields.distance.script.params.lat", lat);
        JSONPath.set(query, "$.script_fields.distance.script.params.lon", lon);
        // 设置排序参数
        setSortType(query, sortType);
        return query;
    }

    private void setSortType(JSONObject baseDsl, SortType sortType) {
        JSONObject sortArg = JSONObject.parseObject("{ \"sort\": [] }");
        String jsonPath = "$.sort";
        int[] arr = null;
        switch (sortType) {
            case DEFAULT:
                arr = new int[]{2, 3, 1};
                break;
            case SCORE:
                arr = new int[]{1, 2, 3};
                break;
            case DELIVERY_PRICE:
                arr = new int[]{7, 1, 2, 3};
                break;
            case DELIVERY_TIME:
                arr = new int[]{5, 1, 2, 3};
                break;
            case PER_consumption_HIGH:
                arr = new int[]{6, 1, 2, 3};
                break;
            case PER_consumption_LOW:
                arr = new int[]{-6, 1, 2, 3};
                break;
            case MIN_PRICE:
                arr = new int[]{-4, 1, 2, 3};
                break;
            default:
        }
        JSONPath.set(baseDsl, jsonPath, getSortAttrs(arr));
    }

    private List<Map<String, Object>> getSortAttrs(int[] args) {
        String descType = "desc", ascType = "asc";
        String[] keyArr = new String[]{
                "score", "total_sale", "month_sale", "min_price", "delivery_time",
                "ave_consumption", "delivery_price"
        };
        if (args == null) {
            return null;
        }
        List<Map<String, Object>> sortAttrs = new LinkedList<>();
        for (int arg : args) {
            int absValue = Math.abs(arg);
            if (absValue <= keyArr.length && absValue > 0) {
                Map<String, Object> tmpMap = new LinkedHashMap<>();
                tmpMap.put(keyArr[absValue - 1], arg > 0 ? descType : ascType);
                sortAttrs.add(tmpMap);
            }
        }
        log.debug("sort params ==>" + JSON.toJSONString(sortAttrs));
        return sortAttrs;
    }

    /**
     * 检查店铺是否休息中
     */
    private boolean isRest(Time start, Time end) {
        Time now = new Time(System.currentTimeMillis());
        return now.getTime() < end.getTime() && now.getTime() >= start.getTime();
    }

    private boolean isRest(TimeItem[] businessHours) {
        Time now = new Time(System.currentTimeMillis());
        for (TimeItem item : businessHours) {
            if (item.isInItem(now)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置 es RestClient
     */
    private RestClient buildSearchRestClient() {
        RestClientBuilder clientBuilder = RestClient.builder(
                new HttpHost("47.94.5.149", 9200, "http"));
        Header[] defaultHeaders = new Header[] {
                new BasicHeader("ContentType", "application/json")
        };
        clientBuilder.setDefaultHeaders(defaultHeaders);

        return clientBuilder.build();
    }

    private Request setRestRequest(int size, int from) {
        return setRestRequest(size, from, "/restaurant/_search");
    }

    private Request setRestRequest(int size, int from, String searchUrl) {
        size = Math.min(Math.max(0, size), 20);
        from = Math.max(0, from);
        GetUrlUtils getUrlUtils = new GetUrlUtils(searchUrl);
        getUrlUtils.setValue("size", size);
        getUrlUtils.setValue("from", from);

        return new Request(
                "GET",
                getUrlUtils.toString()
        );
    }


}
