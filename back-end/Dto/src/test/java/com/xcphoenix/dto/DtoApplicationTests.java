package com.xcphoenix.dto;

import com.alibaba.fastjson.JSON;
import com.xcphoenix.dto.bean.Restaurant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DtoApplicationTests {

    @Test
    public void tmpTest() {
        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantDesc("");
        restaurant.setRestaurantName("test");
        System.out.println(JSON.toJSON(restaurant));
    }

}
