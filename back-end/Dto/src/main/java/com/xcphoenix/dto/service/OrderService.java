package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Order;

import java.util.Map;

/**
 * 订单服务
 *
 * @author      xuanc
 * @date        2019/10/23 下午3:37
 * @version     1.0
 */ 
public interface OrderService {

    Order purchaseNewOrder(Order order);

    Map<String, Object> getPreviewData(Long rstId);
}
