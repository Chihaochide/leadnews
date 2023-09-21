package com.liuxuanhe.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 把MinIoProperties对象存入IOC容器（也可以直接在MinIoPeroperties上添加@Component）
@EnableConfigurationProperties(MinIOProperties.class)
public class MinIoConfiguration {
    @Autowired
    private MinIOProperties minIoProperties;
    @Bean
    public MinioClient minioClient(){
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minIoProperties.getEndpoint())
                .credentials(minIoProperties.getAccessKey(),minIoProperties.getSecretKey())
                .build();
        return minioClient;
    }
}
