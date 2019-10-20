package com.xcphoenix.dto.controller;

import com.alibaba.fastjson.JSONObject;
import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.FoodService;
import com.xcphoenix.dto.validator.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public Result addFood(@Validated(ValidateGroup.addData.class) @RequestBody Food food) throws IOException {
        Food newFood = foodService.addFood(food);
        return new Result("添加成功")
                .addMap("food", newFood);
    }

    @UserLoginToken
    @PutMapping("/food")
    public Result updateFood(@Validated @RequestBody Food food) throws IOException {
        Food updateFood = foodService.updateFood(food);
        return new Result("更新成功")
                .addMap("food", updateFood);
    }

    @UserLoginToken
    @GetMapping("/food/{foodId}")
    public Result getFoodDetail(@PathVariable Long foodId) {
        Food food = foodService.getFoodDetailById(foodId);
        return new Result("查询成功")
                .addMap("food", food);
    }

    @UserLoginToken
    @GetMapping("/foods")
    public Result getAllFoods() {
        return new Result("查询成功").addMap("foods", foodService.getAllFoodsByShopper());
    }

    @UserLoginToken
    @GetMapping("/foods/category")
    public Result getFoodsByCategory(@RequestBody(required = false) JSONObject jsonObject) {
        Long categoryId = jsonObject == null ? null : jsonObject.getLong("categoryId");
        return new Result("查询成功")
                .addMap("foods", foodService.getFoodsByCategory(categoryId));
    }
}
