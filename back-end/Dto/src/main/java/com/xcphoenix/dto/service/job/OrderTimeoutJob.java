package com.xcphoenix.dto.service.job;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 订单超时任务
 *
 * @author      xuanc
 * @date        2019/11/28 下午5:38
 * @version     1.0
 */
@Data
@Slf4j
public class OrderTimeoutJob implements Job {

    private Long orderCode;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /*
         * 订单超时未支付
         * - 取消订单
         * - 恢复库存
         */
        log.debug("order [" + orderCode + "] timeout");

    }

}
