package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xcphoenix.dto.bean.dao.Order;
import com.xcphoenix.dto.bean.dao.Restaurant;
import com.xcphoenix.dto.mapper.FoodCategoryMapper;
import com.xcphoenix.dto.mapper.OrderMapper;
import com.xcphoenix.dto.service.OrderService;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.utils.SnowFlakeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private OrderService orderService;

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
        PageHelper.offsetPage(2, 4);
        List<Order> orders = orderMapper.getCurrentOrders(1L, Arrays.asList(0, 1, 2, 3, 4, 5));
        int a = 3 + 2;
        PageInfo pageIno = new PageInfo(orders);
        log.info("orders = " + JSON.toJSON(orders));
        log.info("pageInfo = " + JSON.toJSON(pageIno));
    }

    @Test
    void testFastjsonSerialSuperClass() {
        log.info(JSON.toJSONString(
                new TestSub(1, "2", 3, 4.0)
        ));
    }

}

@Data
class TestSuper {
    private int attr1;
    private String attr2;

    TestSuper(int attr1, String attr2) {
        this.attr1 = attr1;
        this.attr2 = attr2;
    }
}

@EqualsAndHashCode(callSuper = true)
@Data
class TestSub extends TestSuper {

    private int subAttr1;
    private double subAttr2;

    public TestSub(int attr1, String attr2, int subAttr1, double subAttr2) {
        super(attr1, attr2);
        this.subAttr1 = subAttr1;
        this.subAttr2 = subAttr2;
    }

}