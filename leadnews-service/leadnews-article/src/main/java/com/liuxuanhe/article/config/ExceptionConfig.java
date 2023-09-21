package com.liuxuanhe.article.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描异常拦截器
 */
@Configuration
@ComponentScan("com.liuxuanhe.common.exception")
public class ExceptionConfig {
}
