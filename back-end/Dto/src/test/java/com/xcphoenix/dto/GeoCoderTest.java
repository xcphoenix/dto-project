package com.xcphoenix.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xcphoenix.dto.service.GeoCoderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/9/2 下午9:40
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class GeoCoderTest {

    @Autowired
    private GeoCoderService geoCoderService;

    @Test
    @DisplayName("腾讯地图api::逆地址解析")
    void testTencentGeoCoder() {
        Map<String, BigDecimal> loc = randomLonLat(100, 120, 20, 40);
        JSONObject jsonObject = geoCoderService.getLocMsg(loc.get("lat"), loc.get("lng"));
        log.info("msg: \n" + jsonObject.toString(SerializerFeature.PrettyFormat));
        int status = jsonObject.getInteger("status");
        assertEquals(status, 0);
    }

    static Map<String, BigDecimal> randomLonLat(double MinLng, double MaxLng, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLng - MinLng) + MinLng);
        BigDecimal lng = db.setScale(6, RoundingMode.HALF_UP);
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        BigDecimal lat = db.setScale(6, RoundingMode.HALF_UP);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("lng", lng);
        map.put("lat", lat);
        return map;
    }
}
