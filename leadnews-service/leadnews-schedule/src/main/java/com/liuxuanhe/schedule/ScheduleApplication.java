package com.liuxuanhe.schedule;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 延迟队列微服务
 */
@SpringBootApplication
@MapperScan("com.liuxuanhe.schedule.mapper")
@EnableDiscoveryClient
@EnableScheduling // 开启Spring定时任务
public class ScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }

    /**
     * 配置MyBatisPlus乐观锁拦截器
     * 底层原理
     * 1）没有添加乐观锁拦截器之前SQL：
     *      update taskinfo_logs set status=? where task_id = 1
     * 2）添加乐观锁拦截器之后的SQL：
     *      select version from taskinfo_logs where task_id = 1
     *      update taskinfo_logs set status=? where task_id = 1 and version = ?
     *      update taskinfo_logs set version = version+1 where task_id = 1
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
        return new OptimisticLockerInterceptor();
    }
}
