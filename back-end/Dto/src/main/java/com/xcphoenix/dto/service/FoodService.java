package com.xcphoenix.dto.service;

import com.xcphoenix.dto.annotation.ShopperCheck;
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
     * 判断商品是否存在
     *
     * @param foodId 商品id
     * @param rstId 店铺id
     * @return 是否存在
     */
    boolean isExists(Long foodId, Long rstId);

    /**
     * 上架商品
     *
     * @param food 商品信息
     * @return 添加的商品信息
     * @throws IOException 图片转码
     */
    Food addFood(Food food) throws IOException;

    /**
     * 更新商品
     *
     * @param food 商品信息
     * @throws IOException 图片转码
     */
    Food updateFood(Food food) throws IOException;

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
    List<Foods> getAllFoodsByShopper();

    /**
     * 获取店铺的所有商品信息
     *
     * @param rstId 店铺id
     * @return 所有商品信息
     */
    List<Foods> getAllFoodsByRstId(Long rstId);

    /**
     * 获取店铺指定分类下的食品信息
     *
     * @param categoryId 分类 id
     * @return ..
     */
    List<Food> getFoodsByCategory(Long categoryId);

    /**
     * 删除商品
     * @param foodId 商品id
     */
    @ShopperCheck
    void delFoodById(Long foodId);

    /**
     * 更改分类id
     *
     * @param foodId 商品id
     * @param newCategoryId 新分类id
     * @return 更新后的商品，用于缓存
     */
    @ShopperCheck
    Food changeCategory(Long foodId, Long newCategoryId);
}
