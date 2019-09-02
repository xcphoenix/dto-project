package com.xcphoenix.dto.service;

import org.springframework.beans.factory.annotation.Autowired;
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
public class GeoCoderService {

    private RestTemplate restTemplate;
    private String url = "https://apis.map.qq.com/ws/geocoder/v1/?location={location}&get_poi={get_poi}&key={key}";

    @Autowired
    public GeoCoderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLocMsg(BigDecimal lat, BigDecimal lng) {
        Map<String, java.io.Serializable> map = new HashMap<String, java.io.Serializable>(5);
        map.put("location", lat + "," + lng);
        map.put("get_poi", 1);
        map.put("key", "NKOBZ-4NL34-RPCUS-D7FTI-OUMBO-4TB52");
        return restTemplate.getForObject(url, String.class, map);
    }
}
