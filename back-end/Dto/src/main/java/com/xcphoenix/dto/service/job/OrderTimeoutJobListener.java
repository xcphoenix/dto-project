package com.xcphoenix.dto.service.job;

import com.xcphoenix.dto.service.OrderService;
import com.xcphoenix.dto.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

/**
 * @author      xuanc
 * @date        2019/11/29 下午8:54
 * @version     1.0
 */
@Slf4j
@Component
public class OrderTimeoutJobListener implements JobListener {

    private static final String DEFAULT_NAME = "OrderTimeoutJobListener";
    private OrderService orderService;
    private StockService stockService;

    public OrderTimeoutJobListener(OrderService orderService, StockService stockService) {
        this.orderService = orderService;
        this.stockService = stockService;
    }

    @Override
    public String getName() {
        return DEFAULT_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        log.debug("job exec finished");

        if (jobException != null) {
            log.warn("Exception thrown by job[" + getName() + "]\n" +
                    "\tException: " + jobException.getMessage()) ;
            return;
        }

        Long orderCode = context.getJobDetail().getJobDataMap().getLongValue("orderCode");
        // 处理已过期订单
        orderService.dealOrderTimeout(orderCode);
    }

}
