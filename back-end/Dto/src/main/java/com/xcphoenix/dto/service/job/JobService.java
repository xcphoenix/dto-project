package com.xcphoenix.dto.service.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    private Scheduler scheduler;
    private final String defaultJobGroup = "orderTimeoutJob";

    public JobService(Scheduler scheduler) throws SchedulerException {
        this.scheduler = scheduler;
        scheduler.start();
    }

    private JobDetail buildJobDetail(Long orderCode, Long userId) {
        log.info("build job detail");
        return JobBuilder.newJob(OrderTimeoutJob.class)
                .withIdentity(String.valueOf(orderCode), defaultJobGroup)
                .usingJobData("orderCode", orderCode)
                .usingJobData("userId", userId)
                .storeDurably()
                .build();
    }

    /**
     * 添加定时服务（订单过期）
     */
    public void addJob(Long orderCode, Long userId, Date invalidTime) throws SchedulerException {
        log.debug("start and job...");
        JobDetail jobDetail = buildJobDetail(orderCode, userId);
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(orderCode), "order")
                .startAt(invalidTime)
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
