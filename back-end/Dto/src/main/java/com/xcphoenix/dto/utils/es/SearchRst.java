package com.xcphoenix.dto.utils.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xcphoenix.dto.bean.bo.SortType;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/10/18 下午2:35
 * @version     1.0
 */
@Slf4j
public class SearchRst {

    private JSONObject root = JSONObject.parseObject("{}");
    private static final double DEFAULT_QUERY_DISTANCE = 10.0;
    private double lat;
    private double lon;

    private SearchRst(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static SearchRst init(double lat, double lon) {
        return new SearchRst(lat, lon);
    }

    /**
     * 建立请求 Json 数据
     * @return json数据
     */
    public String buildReqJson() {
        return this.root.toJSONString();
    }

    /**
     * 设置距离参数
     */
    public SearchRst setDistanceField() {
        JSONArray stored = JSONArray.parseArray("[ \"_source\" ]");
        JSONObject script = JSONObject.parseObject("{\n" +
                "   \"distance\":{\n" +
                "       \"script\":{\n" +
                "           \"source\":\"doc['location'].arcDistance(params.lat,params.lon)\",\n" +
                "           \"lang\":\"painless\",\n" +
                "           \"params\":{\n" +
                "               \"lon\": 0.0,\n" +
                "               \"lat\": 0.0\n" +
                "           }\n" +
                "       }\n" +
                "   }\n" +
                "}");
        JSONPath.set(script, "$.distance.script.params.lat", this.lat);
        JSONPath.set(script, "$.distance.script.params.lon", this.lon);
        JSONPath.set(this.root, "$.stored_fields", stored);
        JSONPath.set(this.root, "$.script_fields", script);

        log.info("[Es Query::build distance fields] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置查询数据字段参数
     */
    public SearchRst setIncludeFields() {
        JSONObject source = JSONObject.parseObject("{\n" +
                "   \"includes\": [\n" +
                "       \"restaurant_id\", \n" +
                "       \"restaurant_name\", \n" +
                "       \"score\", \n" +
                "       \"logo\", \n" +
                "       \"month_sale\", \n" +
                "       \"min_price\", \n" +
                "       \"bh_start\", \n" +
                "       \"bh_end\", \n" +
                "       \"delivery_price\", \n" +
                "       \"delivery_range\"\n" +
                "   ]\n" +
                "}");
        JSONPath.set(this.root, "$._source", source);

        log.info("[Es Query::build include fields] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置搜索关键字
     */
    public SearchRst setSearchKeywords(String keywords) {
        JSONObject postFilter = JSONObject.parseObject("{\n" +
                "   \"multi_match\":{\n" +
                "       \"query\":\"test\",\n" +
                "       \"minimum_should_match\":\"30%\",\n" +
                "       \"fields\":[\n" +
                "           \"restaurant_name^4\",\n" +
                "           \"foods^2\",\n" +
                "           \"tag\"\n" +
                "       ]\n" +
                "   }\n" +
                "}");
        JSONPath.set(postFilter, "$.multi_match.query", keywords);
        JSONPath.set(root, "$.post_filter", postFilter);

        log.info("[Es Query::build search keyword] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置搜索 query
     */
    public SearchRst setQuery(double distance) {
        JSONObject query = JSONObject.parseObject("{\n" +
                "   \"function_score\": {\n" +
                "       \"query\": {\n" +
                "           \"bool\": {\n" +
                "               \"filter\": {\n" +
                "                   \"geo_distance\": {\n" +
                "                       \"distance\": \"10.0km\",\n" +
                "                       \"location\": {\n" +
                "                           \"lon\": 108.887407,\n" +
                "                           \"lat\": 34.163527\n" +
                "                       }\n" +
                "                   }\n" +
                "               },\n" +
                "               \"must\": {\n" +
                "                   \"match_all\": {}\n" +
                "               }\n" +
                "           }\n" +
                "       },\n" +
                "      \"functions\": [\n" +
                "       {\n" +
                "           \"script_score\": {\n" +
                "               \"script\": {\n" +
                "                   \"params\": {\n" +
                "                       \"sale\": 0.37,\n" +
                "                       \"score\": 0.6,\n" +
                "                       \"distance\": 0.5,\n" +
                "                       \"min_price\": 0.09,\n" +
                "                       \"delivery_price\": 0.5,\n" +
                "                       \"delivery_time\": 0.07,\n" +
                "                       \"lat\": 0.00,\n" +
                "                       \"lon\": 0.00\n" +
                "                   },\n" +
                "                   \"source\": \"100 + Math.log1p(doc['month_sale'].value)" +
                "                   - doc['location'].arcDistance(params.lat,params.lon) / 1000 * params.distance " +
                "                   + doc['score'].value * params.score - params.min_price * doc['min_price'].value " +
                "                   - params.delivery_price * doc['delivery_price'].value" +
                "                   - params.delivery_time * doc['delivery_time'].value\"\n" +
                "               }\n" +
                "           }\n" +
                "       }\n" +
                "      ]\n" +
                "   }" +
                "}");
        JSONPath.set(query, "$.function_score.query.bool.filter.geo_distance.distance", distance + "km");
        JSONPath.set(query, "$.function_score.query.bool.filter.geo_distance.location.lat", this.lat);
        JSONPath.set(query, "$.function_score.query.bool.filter.geo_distance.location.lon", this.lon);
        JSONPath.set(query, "$.function_score.functions[0].script_score.script.params.lat", this.lat);
        JSONPath.set(query, "$.function_score.functions[0].script_score.script.params.lon", this.lon);


        JSONPath.set(this.root, "$.query", query);
        // 防止后面排序导致自定义评分失效
        JSONPath.set(this.root, "$.track_scores", Boolean.valueOf(true));

        log.info("[Es Query::build query] root ==> " + root.toJSONString());

        return this;
    }

    public SearchRst setQuery() {
        return setQuery(DEFAULT_QUERY_DISTANCE);
    }

    /**
     * 设置排序
     */
    public SearchRst setSort(int type) {
        if (type < 0 || SortType.values().length <= type) {
            throw new ServiceLogicException(ErrorCode.INVALID_SEARCH_TYPE);
        }
        return setSortType(SortType.values()[type]);
    }

    private SearchRst setSortType(SortType sortType) {
        String jsonPath = "$.sort";
        int[] arr = null;
        switch (sortType) {
            case DISTANCE:
                return setDistanceAttr();
            case SCORE:
                arr = new int[]{0};
                break;
            case SALE:
                arr = new int[]{1};
            case DELIVERY_PRICE:
                arr = new int[]{-5};
                break;
            case DELIVERY_TIME:
                arr = new int[]{-3};
                break;
            case PER_consumption_HIGH:
                arr = new int[]{4};
                break;
            case PER_consumption_LOW:
                arr = new int[]{-4};
                break;
            case MIN_PRICE:
                arr = new int[]{2};
                break;
            case DEFAULT:
                return this;
            default:
        }
        JSONPath.set(this.root, jsonPath, setSortAttrs(arr));
        return this;
    }

    private List<Map<String, Object>> setSortAttrs(int[] args) {
        String descType = "desc", ascType = "asc";
        String[] keyArr = new String[]{
                "score", "month_sale", "min_price", "delivery_time",
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

    private SearchRst setDistanceAttr() {
        JSONArray sorts = JSONArray.parseArray("[\n" +
                "    {\n" +
                "      \"_geo_distance\": {\n" +
                "        \"unit\": \"km\",\n" +
                "        \"distance_type\": \"plane\",\n" +
                "        \"location\": {\n" +
                "          \"lon\": 0.00,\n" +
                "          \"lat\": 0.00\n" +
                "        },\n" +
                "        \"order\": \"asc\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]");
        JSONPath.set(sorts, "$[0]._geo_distance.location.lon", this.lon);
        JSONPath.set(sorts, "$[0]._geo_distance.location.lat", this.lat);

        JSONPath.set(this.root, "$.sort", sorts);
        return this;
    }


}
