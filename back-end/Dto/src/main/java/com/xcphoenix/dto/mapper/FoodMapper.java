package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Food;
import com.xcphoenix.dto.bean.Foods;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     * 判断商品是否存在
     *
     * @param foodId 商品id
     * @param rstId  店铺id
     * @return 1: 存在 0: 不存在 其他：数据库设置异常
     */
    @Select("SELECT COUNT(*) FROM food WHERE food_id = #{foodId} AND restaurant_id = #{rstId}")
    Integer exists(Long foodId, Long rstId);

    /**
     * 下架食品
     *
     * @param restaurantId 店铺 id
     * @param foodId       下架的食品 id
     */
    @Delete("DELETE FROM food WHERE food_id = #{foodId} AND restaurant_id = #{restaurantId}")
    void delFood(Long foodId, Long restaurantId);

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
     * 更新分类
     * @param foodId 商品id
     * @param newCategoryId 新分类
     */
    @Update("UPDATE food SET category_id = #{newCategoryId} WHERE food_id = #{foodId}")
    void changeCategory(@Param("foodId") Long foodId, @Param("newCategoryId") Long newCategoryId);

    /**
     * 获取店铺商品库存量
     * @param rstId　店铺id
     * @return 店铺id以及库存量
     */
    @Select("SELECT `food_id`, `residual_amount` FROM food WHERE restaurant_id = #{rstId} ORDER BY food_id")
    List<Food> getFoodsStock(@Param("rstId") Long rstId);

}
