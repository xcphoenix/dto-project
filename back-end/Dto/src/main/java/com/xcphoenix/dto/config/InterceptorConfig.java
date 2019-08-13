package com.xcphoenix.dto.config;

import com.xcphoenix.dto.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author      xuanc
 * @date        2019/8/2 下午4:49
 * @version     1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * WARN: 拦截器中 bean 会出现 bean 无法注入，值为 null 的情况
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error");
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}
