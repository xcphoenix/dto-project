package com.xcphoenix.dto.service;

import com.xcphoenix.dto.bean.ao.OrderModify;
import com.xcphoenix.dto.bean.dao.Order;
import com.xcphoenix.dto.exception.ServiceLogicException;
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
     * 取消订单
     * @param orderCode 订单编号
     * @throws ServiceLogicException 当订单不为未支付状态时抛出
     */
    void cancelOrder(Long orderCode) throws ServiceLogicException;

    /**
     * 修改订单
     * @param orderCode 订单编号
     * @param modData 要修改的数据
     * @throws ServiceLogicException 订单状态不为未支付状态时抛出异常
     */
    void modifyOrder(Long orderCode, OrderModify modData) throws ServiceLogicException;

    /**
     * 删除订单
     * @param orderCode 要删除的订单编号
     * @throws ServiceLogicException 订单状态为已取消订单
     */
    void delOrder(Long orderCode) throws ServiceLogicException;

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
     * 获取订单信息
     * @param orderCode 订单号
     * @return 订单信息
     */
    Order getOrderById(Long orderCode);

    /**
     * 处理订单过期
     * @param orderCode 订单编号
     */
    void dealOrderTimeout(Long orderCode);

    /**
     * 处理已支付订单
     * @param orderCode 订单编号
     * @param payType 支付方式
     */
    void dealOrderPaid(Long orderCode, int payType);

}
