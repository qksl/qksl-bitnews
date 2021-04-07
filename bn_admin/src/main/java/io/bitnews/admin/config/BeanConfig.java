package io.bitnews.admin.config;

import io.bitnews.framework.file.AliyunOosFileStorage;
import io.bitnews.framework.file.FileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class BeanConfig {

    @Bean
    public AliyunOosFileStorage aliyunOosFileStorage() {
        return new AliyunOosFileStorage();
    }

}