package com.xcphoenix.dto.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.mapper.area.CountryMapper;
import com.xcphoenix.dto.service.GeoService;
import com.xcphoenix.dto.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/9/2 下午9:39
 * @version     1.0
 */
@Slf4j
@Service
public class GeoServiceImpl implements GeoService {

    private RestTemplate restTemplate;
    private CountryMapper countryMapper;

    @Value("${qq.map.api.geocoder.url}")
    private String geocoderUrl;
    @Value("${qq.map.api.ip.url}")
    private String ipUrl;
    @Value("${qq.map.api.suggestion.url}")
    private String suggestionUrl;
    @Value("${qq.map.api.development.key}")
    private String key;

    private String tencentSuccessFlag = "status";
    private Integer successValue = 0;


    @Autowired
    public GeoServiceImpl(RestTemplate restTemplate, CountryMapper countryMapper) {
        this.restTemplate = restTemplate;
        this.countryMapper = countryMapper;
    }

    @Override
    public JSONObject getSuggestion(String keyword) {
        String location = "";
        String region = "";
        JSONObject ipJson = this.getLocAsIp();
        if (ipJson.getInteger(tencentSuccessFlag).equals(successValue)) {
            JSONObject locationJson = ipJson.getJSONObject("result").getJSONObject("location");
            location = locationJson.getString("lat") + "," + locationJson.getString("lng");
            JSONObject adInfo = ipJson.getJSONObject("result").getJSONObject("ad_info");
            region = adInfo.getString("district");
            if (region.length() == 0) {
                region = adInfo.getString("city");
            }
            if (region.length() == 0) {
                region = adInfo.getString("province");
            }
        }

        Map<String, java.io.Serializable> map = new HashMap<>(1);
        map.put("word", keyword);
        map.put("key", key);
        map.put("region", region);
        map.put("location", location);

        String jsonStr = restTemplate.getForObject(suggestionUrl, String.class, map);
        return JSONObject.parseObject(jsonStr);
    }

    @Override
    public JSONObject getLocation() {
        JSONObject jsonObject = this.getLocAsIp();
        JSONObject resultJson = new JSONObject();

        if (jsonObject.getInteger(tencentSuccessFlag).equals(successValue)) {
            JSONObject location = jsonObject.getJSONObject("result")
                    .getJSONObject("location");
            jsonObject = this.getLocMsg(
                    location.getBigDecimal("lat"), location.getBigDecimal("lng")
            );
            String address = jsonObject.getJSONObject("result").getString("address")
                    .replaceAll("^.{2}省.{2}市", "");
            resultJson.put("address", address);
            resultJson.put("location", location);
            return resultJson;
        }
        return null;
    }

    @Override
    public JSONObject getLocAsIp() {
        Map<String, java.io.Serializable> map = new HashMap<>(2);
        map.put("ip", IpUtils.getIp());
        map.put("key", key);

        String jsonStr = restTemplate.getForObject(ipUrl, String.class, map);

        log.info("GeoService -> request ip: " + map.get("ip"));
        log.info("GeoService -> after call tencent api result: " + jsonStr);

        return JSONObject.parseObject(jsonStr);
    }

    @Override
    public JSONObject getLocMsg(BigDecimal lat, BigDecimal lng) {
        Map<String, java.io.Serializable> map = new HashMap<>(5);
        map.put("location", lat + "," + lng);
        map.put("get_poi", 1);
        map.put("key", key);
        String jsonStr = restTemplate.getForObject(geocoderUrl, String.class, map);
        return JSONObject.parseObject(jsonStr);
    }

    @Override
    public String getAreaCode(BigDecimal lat, BigDecimal lng) {
        return this.getLocMsg(lat, lng)
                .getJSONObject("result")
                .getJSONObject("ad_info")
                .getString("adcode") + "000000";
    }

    @Override
    public Integer getAreaId(BigDecimal lat, BigDecimal lng) {
        String code = getAreaCode(lat, lng);
        return countryMapper.getCountryId(code);
    }

}
