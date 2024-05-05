package com.system.uz.env;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "minio")
public class MinioServiceEnv {
    private String url;
    private String port;
    private String bucket;
    private String access;
    private String secret;
}
