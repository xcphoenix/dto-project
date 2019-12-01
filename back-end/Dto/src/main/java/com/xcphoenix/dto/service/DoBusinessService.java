package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.dao.Order;

/**
 * 营业服务
 *
 * @author      xuanc
 * @date        2019/11/28 下午2:08
 * @version     1.0
 */ 
public interface DoBusinessService {

    /**
     * 同步订单数据
     * @param order 要同步的订单信息
     */
    void syncOrdered(Order order);

}
