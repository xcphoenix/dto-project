package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.dao.FoodCategory;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 下午9:43
 */
public interface FoodCategoryService {

    /**
     * 添加新的分类：会发生唯一性约束异常
     *
     * @param foodCategory 分类信息
     */
    void addNewCategory(FoodCategory foodCategory);

    /**
     * 更新分类：会发生唯一性约束异常
     *
     * @param foodCategory 分类信息
     */
    void updateCategory(FoodCategory foodCategory);

    /**
     * 删除分类：外建约束
     *
     * @param categoryId 分类id
     */
    void deleteCategory(Long categoryId);

    /**
     * 获取店铺下的所有分类信息
     *
     * @return 分类信息
     */
    List<FoodCategory> getCategories();

    /**
     * 断言指定的[分类（可为空）]为[商家]创立
     *
     * @param categoryId 分类 id
     */
    void assertBelongShop(Long categoryId);
}
