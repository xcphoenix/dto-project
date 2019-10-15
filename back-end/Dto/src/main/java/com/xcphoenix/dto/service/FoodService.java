package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.bean.Foods;

import java.io.IOException;
import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/15 下午3:10
 */
public interface FoodService {

    /**
     * 上架商品
     *
     * @param food 商品信息
     * @throws IOException 图片转码
     */
    void addFood(Food food) throws IOException;

    /**
     * 更新商品
     *
     * @param food 商品信息
     * @throws IOException 图片转码
     */
    void updateFood(Food food) throws IOException;

    /**
     * 获取指定食品信息
     *
     * @param foodId 食品id
     * @return 食品信息
     */
    Food getFoodDetailById(Long foodId);

    /**
     * 获取店铺商品的全部信息
     *
     * @return ..
     */
    List<Foods> getAllFoods();

    /**
     * 获取店铺指定分类下的食品信息
     *
     * @param categoryId 分类 id
     * @return ..
     */
    List<Food> getFoodsByCategory(Long categoryId);

}
