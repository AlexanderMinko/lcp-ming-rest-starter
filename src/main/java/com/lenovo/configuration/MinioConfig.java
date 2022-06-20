package com.lenovo.configuration;

import com.lenovo.service.MinioStoreService;

import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

  @Bean
  MinioClient minioClient(MinioProperties properties) {
    return MinioClient.builder()
        .endpoint(properties.getUrl())
        .region(properties.getRegion())
        .credentials(properties.getAccessKey(), properties.getSecretKey())
        .build();
  }

  @Bean
  MinioStoreService minioStoreService(MinioClient minioClient, MinioProperties properties) {
    return new MinioStoreService(minioClient, properties);
  }
}
