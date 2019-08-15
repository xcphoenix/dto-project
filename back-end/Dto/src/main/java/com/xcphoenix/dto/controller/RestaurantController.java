package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/12 下午9:09
 */
@RestController
@RequestMapping("/shop")
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @UserLoginToken
    @PostMapping("/restaurant")
    public Result addNewRestaurant(@Validated @RequestBody Restaurant restaurant, HttpServletRequest request) throws IOException {
        Integer userId = (Integer) request.getAttribute("userId");
        restaurant.setUserId(userId);
        restaurantService.addNewRestaurant(restaurant);
        restaurant.dataConvertToShow();
        return new Result("添加成功", restaurant);
    }

    @UserLoginToken
    @GetMapping("/restaurant")
    public Result getRestaurant(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("userId");
        return new Result("查询成功", restaurantService.getRestaurantDetail(userId));
    }

}
