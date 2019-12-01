package com.xcphoenix.dto.controller;

import com.xcphoenix.dto.annotation.UserLoginToken;
import com.xcphoenix.dto.bean.dao.FoodCategory;
import com.xcphoenix.dto.result.Result;
import com.xcphoenix.dto.service.FoodCategoryService;
import com.xcphoenix.dto.validator.ValidateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 下午4:32
 */
@RestController
@RequestMapping("/shop/food")
public class FoodCategoryController {

    private final FoodCategoryService foodCategoryService;

    @Autowired
    public FoodCategoryController(FoodCategoryService foodCategoryService) {
        this.foodCategoryService = foodCategoryService;
    }

    @PostMapping("/category")
    @UserLoginToken
    public Result addCategory(@Validated(ValidateGroup.AddData.class) @RequestBody FoodCategory foodCategory) {
        foodCategoryService.addNewCategory(foodCategory);
        return new Result("添加成功")
                .addMap("category", foodCategory);
    }

    @PutMapping("/category")
    @UserLoginToken
    public Result updateCategory(@Validated @RequestBody FoodCategory foodCategory) {
        foodCategoryService.updateCategory(foodCategory);
        return new Result("更新成功", null);
    }

    @DeleteMapping("/category/{categoryId}")
    @UserLoginToken
    public Result deleteCategory(@PathVariable Long categoryId) {
        foodCategoryService.deleteCategory(categoryId);
        return new Result("删除成功", null);
    }

    @GetMapping("/categories")
    @UserLoginToken
    public Result getCategories() {
        List<FoodCategory> categories = foodCategoryService.getCategories();
        return new Result("查询成功").addMap("categories", categories);
    }

}
