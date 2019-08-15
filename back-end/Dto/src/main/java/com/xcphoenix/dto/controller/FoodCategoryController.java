package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.FoodCategory;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.FoodCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author      xuanc
 * @date        2019/8/14 下午4:32
 * @version     1.0
 */
@RestController
@RequestMapping("/food")
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @Autowired
    public FoodCategoryController(FoodCategoryService foodCategoryService) {
        this.foodCategoryService = foodCategoryService;
    }

    @PostMapping("/category")
    @UserLoginToken
    public Result addCategory(@Validated @RequestBody FoodCategory foodCategory) {
        foodCategoryService.addNewCategory(foodCategory);
        Map<String, Object> data = new HashMap<>(1);
        data.put("category", foodCategory);
        return new Result("添加成功", data);
    }

    @PutMapping("/category")
    @UserLoginToken
    public Result updateCategory(@Validated @RequestBody FoodCategory foodCategory) {
        foodCategoryService.updateCategory(foodCategory);
        return new Result("更新成功", null);
    }

    @DeleteMapping("/category/{categoryId}")
    @UserLoginToken
    public Result deleteCategory(@PathVariable Integer categoryId) {
        foodCategoryService.deleteCategory(categoryId);
        return new Result("删除成功", null);
    }

    @GetMapping("/categories")
    @UserLoginToken
    public Result getCategories() {
        List<FoodCategory> categories = foodCategoryService.getCategories();
        Map<String, Object> data = new HashMap<>(1);
        data.put("categories", categories);
        return new Result("查询成功", data);
    }

}
