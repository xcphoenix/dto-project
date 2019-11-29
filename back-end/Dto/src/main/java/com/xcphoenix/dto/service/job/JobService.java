package com.xcphoenix.dto.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.quartz.DateBuilder.futureDate;

/**
 * 定时任务服务
 *   - 添加定时任务
 *   - 移除定时任务
 *
 * @author      xuanc
 * @date        2019/11/28 下午6:04
 * @version     1.0
 */
@Slf4j
@Service
public class JobService {

    @Value("${order.timeout:15}")
    private static final int EXEC_TIME = 15;

    private Scheduler scheduler;
    private final String defaultJobGroup = "orderTimeoutJob";

    public JobService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private JobDetail buildJobDetail(Long orderCode) {
        log.info("build job detail");
        return JobBuilder.newJob(OrderTimeoutJob.class)
                .withIdentity(String.valueOf(orderCode), defaultJobGroup)
                .storeDurably()
                .build();
    }

    /**
     * 添加定时服务（订单过期）
     */
    public void addJob(Long orderCode) throws SchedulerException {
        log.debug("start and job...");
        JobDetail jobDetail = buildJobDetail(orderCode);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("timeoutTrigger", "order")
                .usingJobData("orderCode", orderCode)
                .startAt(futureDate(EXEC_TIME, DateBuilder.IntervalUnit.MINUTE))
                .build();
        log.debug("create trigger");
        scheduler.scheduleJob(jobDetail, trigger);
        log.info("add in scheduler");
    }

    /**
     * 删除定时服务（订单已支付）
     *  - 取消
     */
    public void removeJob(Long orderCode) throws SchedulerException {
        JobKey jobKey = new JobKey(String.valueOf(orderCode), defaultJobGroup);
        scheduler.deleteJob(jobKey);
    }

}
