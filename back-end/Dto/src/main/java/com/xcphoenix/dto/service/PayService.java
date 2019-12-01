package com.xcphoenix.dto.service;

import com.xcphoenix.dto.exception.ServiceLogicException;
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
     * @param payType 支付类型
     * @return 是否支付成功
     * @throws SchedulerException 定时器异常
     * @throws ServiceLogicException 支付方式非法、订单状态非法
     */
    boolean payOrder(Long orderId, int payType) throws SchedulerException, ServiceLogicException;

}
