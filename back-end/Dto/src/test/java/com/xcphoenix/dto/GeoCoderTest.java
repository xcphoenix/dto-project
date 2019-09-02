package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.service.GeoCoderService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/2 下午9:40
 */
@SpringBootTest
class GeoCoderTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private GeoCoderService geoCoderService;

    @Test
    void testTencentGeoCoder() {
        Map<String, BigDecimal> loc = randomLonLat(85, 122, 29, 90);
        String jsonStr = geoCoderService.getLocMsg(loc.get("lat"), loc.get("lng"));
        logger.info("msg: \n" + jsonStr);
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        int status = jsonObject.getInteger("status");
        assertEquals(status, 0);
    }

    static Map<String, BigDecimal> randomLonLat(double MinLng, double MaxLng, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLng - MinLng) + MinLng);
        BigDecimal lng = db.setScale(6, BigDecimal.ROUND_HALF_UP);
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        BigDecimal lat = db.setScale(6, BigDecimal.ROUND_HALF_UP);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("lng", lng);
        map.put("lat", lat);
        return map;
    }
}
