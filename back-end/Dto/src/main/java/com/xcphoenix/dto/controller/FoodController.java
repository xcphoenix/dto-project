package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/8/14 下午4:32
 * @version     1.0
 */
@RestController
@RequestMapping("/shop")
public class FoodController {

    private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @UserLoginToken
    @PostMapping("/food")
    public Result addFood(@Validated @RequestBody Food food) throws IOException {
        foodService.addFood(food);
        Map<String, Object> data = new HashMap<>(1);
        data.put("food", food);
        return new Result("添加成功", data);
    }

}
