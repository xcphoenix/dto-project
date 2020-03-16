package com.xcphoenix.dto.utils.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.xcphoenix.dto.bean.bo.SortType;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

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

    private static JSONArray stored = new JSONArray(Collections.singletonList("_source"));
    private static JsonObj distanceField;
    private static JsonObj defaultIncludeField;
    private static JsonObj searchKeywordAttr;
    private static JsonObj searchQuery;
    private static JsonObj geoDistance;

    static {
        try {
            String location = "es";

            distanceField = JsonObj.ofResource(location, "distanceField.json");
            defaultIncludeField = JsonObj.ofResource(location, "defaultIncludeFields.json");
            searchKeywordAttr = JsonObj.ofResource(location, "searchKeyAttr.json");
            searchQuery = JsonObj.ofResource(location, "query.json");
            geoDistance = JsonObj.ofResource(location, "geoDistance.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        JSONObject script = (JSONObject) distanceField.getJsonObject().clone();
        JSONPath.set(script, distanceField.getRule("lat"), this.lat);
        JSONPath.set(script, distanceField.getRule("lon"), this.lon);
        JSONPath.set(this.root, "$.stored_fields", stored);
        JSONPath.set(this.root, "$.script_fields", script);

        log.info("[Es Query::build distance fields] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置查询数据字段参数
     */
    public SearchRst setIncludeFields() {
        JSONPath.set(this.root, "$._source", defaultIncludeField.getJsonObject());

        log.info("[Es Query::build include fields] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置查询数据字段参数
     */
    public SearchRst setIncludeFields(String ... args) {
        JSONObject fields = new JSONObject();
        fields.put("includes", Arrays.asList(args));
        JSONPath.set(this.root, "$._source", fields);

        log.info("[Es Query::build include fields] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置搜索关键字
     */
    public SearchRst setSearchKeywords(String keywords) {
        JSONObject postFilter = (JSONObject) searchKeywordAttr.getJsonObject().clone();
        JSONPath.set(postFilter, searchKeywordAttr.getRule("keywords"), keywords);
        JSONPath.set(root, "$.post_filter", postFilter);

        log.info("[Es Query::build search keyword] root ==> " + root.toJSONString());

        return this;
    }

    /**
     * 设置搜索 query
     */
    public SearchRst setQuery(double distance) {
        JSONObject query = (JSONObject) searchQuery.getJsonObject().clone();
        JSONPath.set(query, searchQuery.getRule("distance"), distance + "km");
        for (String rule : searchQuery.getRules("lat")) {
            JSONPath.set(query, rule, this.lat);
        }
        for (String rule : searchQuery.getRules("lon")) {
            JSONPath.set(query, rule, this.lon);
        }

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
        JSONArray sorts = new JSONArray();
        sorts.add(geoDistance.getJsonObject().clone());
        JSONPath.set(sorts, geoDistance.getRule("lon"), this.lon);
        JSONPath.set(sorts, geoDistance.getRule("lat"), this.lat);

        JSONPath.set(this.root, "$.sort", sorts);
        return this;
    }

    @Getter
    public static class JsonObj {
        private Map<String, Object> pathRule;
        private JSONObject jsonObject;
        private static String flag = "_attr";

        public JsonObj(JSONObject json) {
            JSONObject rules;
            if ((rules = json.getJSONObject(flag)) != null) {
                this.pathRule = rules.getInnerMap();
                json.remove(flag);
            }
            this.jsonObject = json;
        }

        public static JsonObj ofResource(String dire, String file) throws IOException {
            String res = dire.endsWith("/") ? dire + file : dire + "/" + file;
            return ofResource(res);
        }

        public static JsonObj ofResource(String res) throws IOException {
            URL url = SearchRst.class.getClassLoader().getResource(res);
            File file = new File(URLDecoder.decode(
                    Objects.requireNonNull(url).getFile(),
                    StandardCharsets.UTF_8.toString()
            ));
            String jsonStr = StreamUtils.copyToString(
                    new FileInputStream(file), StandardCharsets.UTF_8
            );
            JSONObject json = JSONObject.parseObject(jsonStr);
            return new JsonObj(json);
        }

        public String getRule(String val) {
            Object obj = this.pathRule.get(val);
            if (obj instanceof String) {
                return (String) obj;
            }
            return null;
        }

        public List<String> getRules(String val) {
            Object obj = this.pathRule.get(val);
            if (obj instanceof JSONArray) {
                Object[] objects = ((JSONArray) obj).toArray();
                return Arrays.stream(objects)
                        .filter((o) -> o instanceof String)
                        .map(o -> (String)o)
                        .collect(Collectors.toList());
            }
            return null;
        }

    }

}
