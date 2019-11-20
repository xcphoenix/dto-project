package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.mapper.FoodCategoryMapper;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@Slf4j
@SpringBootTest
public class DtoApplicationTests {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    @Test
    void testCache() {
        Restaurant rst = restaurantService.getRstDetail(99L);
        log.info("rst ==> " + JSON.toJSONString(rst));
    }

    @Test
    void testSnowFlake() {
        for (int i = 0; i < 100; i++) {
            log.info("snow flake id : " + SnowFlakeUtils.nextId());
        }
    }


}
