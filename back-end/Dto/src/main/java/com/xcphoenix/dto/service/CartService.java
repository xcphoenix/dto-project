package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.dao.Cart;

/**
 * @author      xuanc
 * @date        2019/10/12 下午9:57
 * @version     1.0
 */ 
public interface CartService {

    /**
     * 清空购物车
     * @param userId 用户id
     * @param rstId 店铺id
     */
    void cleanCart(Long userId, Long rstId);

    /**
     * 更新购物车信息
     *
     * @param cart 购物车信息
     */
    void updateCart(Cart cart);

    /**
     * 获取购物车信息
     * @param userId 用户id
     * @param rstId 店铺id
     * @return 购物车信息
     */
    Cart getCart(Long userId, Long rstId);
}
