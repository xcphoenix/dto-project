package com.xcphoenix.dto.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author      xuanc
 * @date        2019/9/2 下午10:04
 * @version     1.0
 */
@Configuration
public class IocConfig {
    private final RestTemplateBuilder restTemplateBuilder;

    public IocConfig(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    /**
     * 在配置类中使用restTemplateBuilder构建RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(){
        return restTemplateBuilder.build();
    }
}
