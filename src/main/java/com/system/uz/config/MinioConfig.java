package com.system.uz.config;

import com.system.uz.env.MinioServiceEnv;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class MinioConfig {

    private final MinioServiceEnv minioServiceEnv;

    @Bean
    public MinioClient generateMinioClientS() {
            return MinioClient.builder()
                    .endpoint(minioServiceEnv.getUrl()+":"+minioServiceEnv.getPort())
                    .credentials(minioServiceEnv.getAccess(), minioServiceEnv.getSecret())
                    .build();
    }
}
