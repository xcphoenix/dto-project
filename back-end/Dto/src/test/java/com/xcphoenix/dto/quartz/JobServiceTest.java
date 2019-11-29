package com.xcphoenix.dto.quartz;

import com.xcphoenix.dto.service.job.JobService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author      xuanc
 * @date        2019/11/29 下午1:19
 * @version     1.0
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("定时服务测试")
@Disabled("just for test")
class JobServiceTest {

    @Autowired
    private JobService jobService;

    @Test
    void testAddJob() throws SchedulerException, InterruptedException {
        jobService.addJob(1L);
        Thread.sleep(100000);
    }

}
