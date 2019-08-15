package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.FoodCategory;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/8/14 下午9:16
 */
public interface FoodCategoryMapper {

    /**
     * 添加分类
     *
     * @param foodCategory 分类信息
     */
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    @Insert("INSERT INTO food_category(restaurant_id, name, description) " +
            "VALUES (#{restaurantId}, #{name}, #{description})")
    void addNewCategory(FoodCategory foodCategory);

    /**
     * 更新分类
     *
     * @param foodCategory 新的分类信息
     */
    @Update("UPDATE food_category " +
            "SET name = #{name}, description = #{description} " +
            "WHERE category_id = #{categoryId} AND restaurant_id = #{restaurantId}")
    void updateCategory(FoodCategory foodCategory);

    /**
     * 删除分类信息，引用此分类的其他食品标签都将被置空（数据库外键设置）
     *
     * @param categoryId   分类 id
     * @param restaurantId 店铺 id
     * @return 影响的行数=> 0:分类不存在
     */
    @Delete("DELETE FROM food_category WHERE category_id = #{categoryId} " +
            "AND restaurant_id = #{restaurantId}")
    int deleteCategory(Integer categoryId, Integer restaurantId);

    /**
     * 获取所有的分类信息
     *
     * @param restaurantId 　店铺 id
     * @return 店铺所有的分类信息
     */
    @Select("SELECT category_id, restaurant_id, name, description FROM food_category " +
            "WHERE restaurant_id = #{restaurantId} ")
    List<FoodCategory> getCategories(Integer restaurantId);

    /**
     * 店铺是否有该分类
     *
     * @param categoryId 分类 id
     * @param restaurantId 店铺 id
     * @return ..
     */
    @Select("SELECT COUNT(*) FROM food_category " +
            "WHERE category_id = #{categoryId} AND restaurant_id = #{restaurantId}")
    Integer checkHaveCategories(Integer categoryId, Integer restaurantId);

}
