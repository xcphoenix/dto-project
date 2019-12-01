package com.xcphoenix.dto.mapper;

import com.xcphoenix.dto.bean.dao.Cart;
import org.apache.ibatis.annotations.*;

/**
 * @author xuanc
 * @version 1.0
 * @date 2019/10/11 下午9:59
 */
public interface CartMapper {

    /**
     * 判断购物车信息是否存在
     *
     * @param userId 用户id
     * @param rstId  店铺id
     * @return 数量 1:存在 0:不存在 其他:异常
     */
    @Select("SELECT COUNT(*) FROM cart WHERE user_id = #{userId} AND restaurant_id = #{rstId}")
    int isExist(@Param("userId") Integer userId, @Param("rstId") Integer rstId);

    /**
     * 判断购物车信息是否存在
     *
     * @param userId 用户id
     * @param cartId 购物车id
     * @return 数量 1:存在 0:不存在 其他:异常
     */
    @Select("SELECT COUNT(*) FROM cart WHERE user_id = #{userId} AND restaurant_id = #{cartId} ")
    int isExistByCartId(@Param("userId") Integer userId, @Param("cartId") Integer cartId);

    /**
     * 删除购物车 - <b>需要先清空购物车</b>
     *
     * @param userId 用户id - 确认身份
     * @param cartId 购物车id
     * @return 其他：删除成功 0：删除失败
     */
    @Delete("DELETE FROM cart WHERE user_id = #{userId} AND cart_id = #{cartId}")
    int delCart(@Param("userId") Integer userId, @Param("cartId") Integer cartId);

    /**
     * 清空购物车
     *
     * @param cartId 购物车id
     * @return 0：删除失败 其他：删除成功
     */
    @Delete("DELETE FROM cart_item WHERE cart_id = #{cartId}")
    int clearCart(Integer cartId);

    /**
     * 初始化购物车 - 之后使用 id 来更新购物车
     *
     * @param cart 购物车信息
     */
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    @Insert("INSERT cart(user_id, restaurant_id, gmt_create, gmt_modified) " +
            "VALUES (#{cart.userId}, #{cart.restaurantId}, NOW(), NOW())")
    void initCart(@Param("cart") Cart cart);

    /**
     * 更新购物车信息
     *
     * @param cart 购物车信息，
     */
    void updateCart(@Param("cart") Cart cart);

}
