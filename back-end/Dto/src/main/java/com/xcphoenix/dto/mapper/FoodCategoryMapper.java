package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.FoodCategory;
import org.apache.ibatis.annotations.*;

import java.sql.SQLIntegrityConstraintViolationException;
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
    void updateCategory(FoodCategory foodCategory);

    /**
     * 删除分类信息，若有引用此分类的其他食品则删除失败
     *
     * @param categoryId   分类 id
     * @param restaurantId 店铺 id
     * @return 影响的行数=> 0:分类不存在
     */
    @Delete("DELETE FROM food_category WHERE category_id = #{categoryId} " +
            "AND restaurant_id = #{restaurantId}")
    int deleteCategory(Long categoryId, Long restaurantId) throws SQLIntegrityConstraintViolationException;

    /**
     * 获取所有的分类信息
     *
     * @param restaurantId 　店铺 id
     * @return 店铺所有的分类信息
     */
    @Select("SELECT category_id, restaurant_id, name, description FROM food_category " +
            "WHERE restaurant_id = #{restaurantId} ")
    List<FoodCategory> getCategories(Long restaurantId);

    /**
     * 店铺是否有该分类
     *
     * @param categoryId   分类 id
     * @param restaurantId 店铺 id
     * @return ..
     */
    @Select("SELECT COUNT(*) FROM food_category " +
            "WHERE category_id = #{categoryId} AND restaurant_id = #{restaurantId}")
    Integer checkHaveCategories(Long categoryId, Long restaurantId);

    /**
     * 获取 id 对应的名称
     *
     * @param id 分类 id
     * @return 名称
     */
    @Select("SELECT name FROM food_category WHERE category_id = #{id} ")
    String getCategoryName(@Param("id") Integer id);

}
