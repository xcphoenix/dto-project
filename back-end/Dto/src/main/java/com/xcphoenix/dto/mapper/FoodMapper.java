package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.bean.Foods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/15 上午11:01
 */
public interface FoodMapper {

    /**
     * 上架食品
     *
     * @param food 食品
     */
    void addFood(Food food);

    /**
     * 下架食品
     *
     * @param restaurantId 店铺 id
     * @param foodId       下架的食品 id
     */
    @Delete("DELETE FROM food WHERE food_id = #{foodId} AND restaurant_id = #{restaurantId}")
    void delFood(Integer foodId, Integer restaurantId);

    /**
     * 更新食品信息
     *
     * @param food 要更新的食品信息
     */
    void updateFood(Food food);

    /**
     * 获取食品信息
     *
     * @param foodId       食品 id
     * @param restaurantId 店铺 id
     * @return 信息
     */
    Food getFoodById(@Param("foodId") Long foodId, @Param("restaurantId") Long restaurantId);

    /**
     * 获取店铺的所有食品信息
     *
     * @param restaurantId 店铺 id
     * @return list
     */
    List<Foods> getAllFoods(@Param("restaurantId") Long restaurantId);

    /**
     * 获取指定分类的所有食品
     *
     * @param categoryId   分类 id
     * @param restaurantId 店铺 id
     * @return 信息
     */
    List<Food> getFoodsByCategory(@Param("categoryId") Long categoryId, @Param("restaurantId") Long restaurantId);

    /**
     * 获取m默认分类的食品
     *
     * @param restaurantId 店铺 id
     * @param defaultCategory 默认分类名
     * @return 信息
     */
    List<Food> getFoodsCategoryNull(@Param("restaurantId") Long restaurantId, @Param("defaultCategory") String defaultCategory);

}
