package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.Order;
import com.xcphoenix.dto.bean.OrderStatusEnum;
import org.quartz.SchedulerException;

import java.util.Map;

/**
 * 订单服务
 *
 * @author      xuanc
 * @date        2019/10/23 下午3:37
 * @version     1.0
 */ 
public interface OrderService {

    /**
     * 下单
     * @param order  支付方式、收货地址、订单备注
     * @return 订单信息
     * @throws SchedulerException 定时任务出错
     */
    Order purchaseNewOrder(Order order) throws SchedulerException;

    /**
     * 获得下单显示数据
     * @param rstId 店铺信息
     * @return 订单预览信息
     */
    Map<String, Object> getPreviewData(Long rstId);

    /**
     * 订单是否有效
     * @param orderCode 订单编号
     * @return 有效返回 true
     */
    boolean isValid(Long orderCode);

    /**
     * 获取订单状态
     * @param orderCode 订单号码
     * @return 订单状态
     */
    int getOrderStatus(Long orderCode);

    /**
     * 更新订单状态
     * @param orderCode 订单编号
     * @param orderStatusEnum 要更新的订单状态
     */
    void updateOrderStatus(Long orderCode, OrderStatusEnum orderStatusEnum);
}
