package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.dao.Order;

/**
 * 库存处理
 *
 * @author xuanc
 * @version 1.0
 * @date 2019/11/25 上午10:53
 */
public interface StockService {

    /**
     * 恢复库存
     * @param order 订单信息
     */
    void resume(Order order);

    /**
     * 占用库存
     * @param order 订单信息
     */
    void lock(Order order);

}
