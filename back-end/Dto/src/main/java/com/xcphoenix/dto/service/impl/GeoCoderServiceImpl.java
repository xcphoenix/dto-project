package com.xcphoenix.dto.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.service.GeoCoderService;
import org.slf4j.LoggerFactory;
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
@Service
public class GeoCoderServiceImpl implements GeoCoderService {

    private RestTemplate restTemplate;
    @Value("${qq.map.api.geocoder.url}")
    private String url;
    @Value("${qq.map.api.development.key}")
    private String key;

    @Autowired
    public GeoCoderServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public JSONObject getLocMsg(BigDecimal lat, BigDecimal lng) {
        Map<String, java.io.Serializable> map = new HashMap<String, java.io.Serializable>(5);
        map.put("location", lat + "," + lng);
        map.put("get_poi", 1);
        map.put("key", key);
        String jsonStr = restTemplate.getForObject(url, String.class, map);
        LoggerFactory.getLogger(this.getClass()).debug(jsonStr);
        return JSONObject.parseObject(jsonStr);
    }

    @Override
    public String getCityCode(BigDecimal lat, BigDecimal lng) {
        return this.getLocMsg(lat, lng)
                .getJSONObject("result")
                .getJSONObject("ad_info")
                .getString("adcode") + "000000";
    }

}
