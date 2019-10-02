package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.service.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/9/28 下午3:47
 * @version     1.0
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RestClientForEs {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getNearbyResTest() throws IOException {
        List<Map<String, Object>> restaurants =  restaurantService
                .getNearbyRestaurants(108.887407, 34.163527, 0, 20);
        log.info(JSON.toJSONString(restaurants));
    }

}
