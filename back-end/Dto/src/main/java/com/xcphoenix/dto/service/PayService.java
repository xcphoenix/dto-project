package com.xcphoenix.dto.service;

import org.quartz.SchedulerException;

/**
 * @author      xuanc
 * @date        2019/10/22 下午6:38
 * @version     1.0
 */ 
public interface PayService {

    /**
     * 下单
     * @param orderId 订单id
     * @return 是否支付成功
     * @throws SchedulerException 定时器异常
     */
    boolean payOrder(Long orderId) throws SchedulerException;

}
