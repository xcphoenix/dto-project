package com.xcphoenix.dto.service.impl;

import com.xcphoenix.dto.bean.OrderStatusEnum;
import com.xcphoenix.dto.exception.ServiceLogicException;
import com.xcphoenix.dto.result.ErrorCode;
import com.xcphoenix.dto.service.OrderService;
import com.xcphoenix.dto.service.PayService;
import com.xcphoenix.dto.service.job.JobService;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.xcphoenix.dto.bean.OrderStatusEnum.NEED_PAY;

/**
 * 模拟订单服务
 *
 * @author      xuanc
 * @date        2019/10/22 下午6:39
 * @version     1.0
 */
@Service
public class MockPayServiceImpl implements PayService {

    private JobService jobService;
    private OrderService orderService;

    public MockPayServiceImpl(JobService jobService, OrderService orderService) {
        this.jobService = jobService;
        this.orderService = orderService;
    }

    /**
     * 随机成功...
     */
    @Override
    public boolean payOrder(Long orderCode) throws SchedulerException {
        // 检查订单状态
        int orderStatus = orderService.getOrderStatus(orderCode);
        if (orderStatus != NEED_PAY.getId()) {
            throw new ServiceLogicException(ErrorCode.ORDER_HAS_PAID);
        }

        // 随机... 2/3概率
        boolean isSuccess = new Random().nextInt(10) > 3;

        if (isSuccess) {
            // 将任务从定时器中移除
            jobService.removeJob(orderCode);
            // 修改订单为已支付-等待商家处理
            orderService.updateOrderStatus(orderCode, OrderStatusEnum.WAIT_SHOPPER);
        }
        return isSuccess;
    }

}
