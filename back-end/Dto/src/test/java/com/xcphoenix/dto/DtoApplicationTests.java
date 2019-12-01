package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.xcphoenix.dto.bean.bo.DeliveryType;
import com.xcphoenix.dto.bean.bo.PayTypeEnum;
import com.xcphoenix.dto.bean.dao.Order;
import com.xcphoenix.dto.bean.dao.Restaurant;
import com.xcphoenix.dto.mapper.FoodCategoryMapper;
import com.xcphoenix.dto.mapper.OrderMapper;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@Disabled
@Slf4j
@SpringBootTest
public class DtoApplicationTests {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private FoodCategoryMapper foodCategoryMapper;

    @Autowired
    private OrderMapper orderMapper;

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

    @Test
    void testGetOrder() {
        Order order = orderMapper.getOrderById(451L, 1111111111L);
        log.info("order ==> " + JSON.toJSON(order));
    }

    @Test
    void testTmp() {
        SerializeConfig serializeConfig = new SerializeConfig();
        //noinspection unchecked
        serializeConfig.configEnumAsJavaBean(PayTypeEnum.class, DeliveryType.class);

        Map<String, Object> map = new HashMap<>(4);
        map.put("name1", 1);
        map.put("name2", "value2");
        map.put("payType", PayTypeEnum.values());
        map.put("deliveryType", DeliveryType.values());

        log.info(JSON.toJSONString(map, serializeConfig));
    }

}
