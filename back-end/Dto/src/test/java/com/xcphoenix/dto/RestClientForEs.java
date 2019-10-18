package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .getRstRemark(2,108.887407, 34.163527, 0, 20);
        log.info(JSON.toJSONString(restaurants));
    }

    @Test
    void searchRstTest() throws IOException {
        String text = "麻辣";
        List<Map<String, Object>> rstList = restaurantService
                .getRstRemarkWithSearch(text, 2, 108.887407, 34.163527,  0, 20);
        log.info(JSON.toJSONString(rstList));
    }

    @Test
    void searchRstTypeTest() throws IOException {
        String text = "麻辣";
        assertThrows(ServiceLogicException.class, () -> {
            List<Map<String, Object>> rstList = restaurantService
                    .getRstRemarkWithSearch(text, 9, 108.887407, 34.163527,  0, 20);
            log.info(JSON.toJSONString(rstList));
        }, ErrorCode.INVALID_SEARCH_TYPE.getMsg());
    }

}
