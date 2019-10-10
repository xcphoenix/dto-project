package com.xcphoenix.dto;

import com.xcphoenix.dto.service.impl.RestaurantServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author      xuanc
 * @date        2019/10/9 下午9:25
 * @version     1.0
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class JsonPathTest {

    @Autowired
    private RestaurantServiceImpl restaurantService;

    @Disabled
    @Test
    void testSearchJsonBuild() {
        // String text = "麻辣烫";
        // double lat = 12.0, lon = 12.0;
        // JSONObject jsonBody = restaurantService.setSearchArgs(text, SortType.MIN_PRICE, lat, lon);
        // log.info(jsonBody.toJSONString());
    }

}
