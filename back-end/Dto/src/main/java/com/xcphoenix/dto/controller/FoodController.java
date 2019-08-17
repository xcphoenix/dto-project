package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        data.put("food", foodService.getFoodDetailById(food.getFoodId()));
        return new Result("添加成功", data);
    }

    @UserLoginToken
    @PutMapping("/food")
    public Result updateFood(@Validated @RequestBody Food food) throws IOException {
        foodService.updateFood(food);
        Map<String, Object> data = new HashMap<>(1);
        data.put("food", foodService.getFoodDetailById(food.getFoodId()));
        return new Result("更新成功", data);
    }

    @UserLoginToken
    @GetMapping("/food/{foodId}")
    public Result getFoodDetail(@PathVariable Integer foodId) {
        Food food = foodService.getFoodDetailById(foodId);
        if (food == null) {
            throw new ServiceLogicException(ErrorCode.FOOD_NOT_FOUND);
        }
        Map<String, Object> data = new HashMap<>(1);
        data.put("food", food);
        return new Result("查询成功", data);
    }

    @UserLoginToken
    @GetMapping("/foods")
    public Result getAllFoods() {
        Map<String, Object> data = new HashMap<>(1);
        data.put("foods", foodService.getAllFoods());
        return new Result("查询成功", data);
    }

    @UserLoginToken
    @GetMapping("/foods/category")
    public Result getFoodsByCategory(@RequestBody(required = false) Integer categoryId) {
        Map<String, Object> data = new HashMap<>(1);
        data.put("foods", foodService.getFoodsByCategory(categoryId));
        return new Result("查询成功", data);
    }
}
