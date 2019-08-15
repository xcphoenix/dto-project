package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Food;
import org.apache.ibatis.annotations.Delete;

/**
 * @author      xuanc
 * @date        2019/8/15 上午11:01
 * @version     1.0
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
     * @param foodId 下架的食品 id
     */
    @Delete("DELETE FROM food WHERE food_id = #{foodId} AND restaurant_id = #{restaurantId}")
    void delFood(Integer foodId, Integer restaurantId);

    /**
     * 更新食品信息
     *
     * @param food 要更新的食品信息
     */
    void updateFood(Food food);

}
