package com.xcphoenix.dto.service.job;

import com.xcphoenix.dto.service.OrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * 订单超时任务
 *
 * @author      xuanc
 * @date        2019/11/28 下午5:38
 * @version     1.0
 */
@Data
@Slf4j
@Component
public class OrderTimeoutJob implements Job {

    private OrderService orderService;

    public OrderTimeoutJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        long orderCode = context.getJobDetail().getJobDataMap().getLongValue("orderCode");
        long userId = context.getJobDetail().getJobDataMap().getLongValue("userId");
        log.info("order: " + orderCode + " by user: " + userId + " timeout");
        // 处理已过期订单
        orderService.dealOrderTimeout(orderCode, userId);
    }

}
