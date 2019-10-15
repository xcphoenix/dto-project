package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.Restaurant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/2 下午4:33
 */
public interface CollectionMapper {

    /**
     * 收藏店铺
     *
     * @param userId 用户id
     * @param shopId 店铺id
     */
    @Insert("INSERT INTO collection(user_id, restaurant_id, gmt_create) VALUES (#{userId}, #{shopId}, NOW())")
    void collectShop(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 获取店铺收藏信息
     *
     * @param userId 用户id
     * @param shopId 店铺id
     * @return <li>1: 收藏</li> <li>other: 未收藏orError</li>
     */
    @Select("SELECT COUNT(*) from collection WHERE user_id = #{userId} AND restaurant_id = #{shopId}")
    Integer isCollected(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 取消收藏
     *
     * @param userId 用户id
     * @param shopId 店铺id
     */
    @Delete("DELETE from collection WHERE user_id = #{userId} AND restaurant_id = #{shopId}")
    void cancelCollection(@Param("userId") Long userId, @Param("shopId") Long shopId);

    /**
     * 获取用户收藏列表
     *
     * @param userId 用户id
     * @return 店铺list
     */
    List<Restaurant> getCollectedShops(@Param("userId") Long userId);

}
