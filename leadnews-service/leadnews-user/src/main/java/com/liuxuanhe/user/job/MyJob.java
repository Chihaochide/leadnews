package com.liuxuanhe.user.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 演示定时任务
 */
@Component
public class MyJob {
    /**
     * fixedDelay：固定频率 单位是毫秒
     */
    @Scheduled(fixedDelay = 3000)
    public void testJob(){
        System.out.println("任务被触发了。。。"+new Date());
    }
}
