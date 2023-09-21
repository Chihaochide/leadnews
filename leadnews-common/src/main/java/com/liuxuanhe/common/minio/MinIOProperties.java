package com.liuxuanhe.common.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "minio") // 给配置添加了一个访问前缀
public class MinIOProperties {
    private String accessKey;//账户名称
    private String secretKey;//账户密码
    private String endpoint;//MinIO连接地址   http://192.168.177.119:9000/

    private String bucket;//桶名称
    private String readPath;//访问文件的地址  http://192.168.177.119:9000/
}
