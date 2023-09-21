package com.liuxuanhe.wemedia;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@MapperScan("com.liuxuanhe.wemedia.mapper")
@EnableDiscoveryClient
@EnableFeignClients(basePackages="com.liuxuanhe.*.feign") // 开启Feign接口识别，默认启动来同级或者子集合
@EnableAsync // 开启Spring异步调用
@EnableScheduling // 开启Spring定时任务
public class WemediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(WemediaApplication.class,args);
    }
}
