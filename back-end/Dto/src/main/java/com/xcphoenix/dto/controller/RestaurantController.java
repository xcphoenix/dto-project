package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Restaurant;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.RestaurantService;
import com.xcphoenix.dto.validator.ValidateGroup;
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
    public Result addNewRestaurant(@Validated(ValidateGroup.addData.class) @RequestBody Restaurant restaurant) throws IOException {
        Restaurant newRst = restaurantService.addNewRestaurant(restaurant);
        return new Result("添加成功").addMap("restaurant", newRst);
    }

    @UserLoginToken
    @GetMapping("/restaurant")
    public Result getRestaurant(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return new Result("查询成功").addMap("restaurant", restaurantService.getRstByShopper(userId));
    }

    @UserLoginToken
    @PutMapping("/restaurant")
    public Result updateRestaurant(@Validated @RequestBody Restaurant restaurant) throws IOException {
        restaurantService.updateRestaurant(restaurant);
        restaurant = restaurantService.getRstByShopper(restaurant.getUserId());
        restaurant.dataConvertToShow();
        return new Result("更新成功").addMap("restaurant", restaurant);
    }


}
