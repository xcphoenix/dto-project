package com.xcphoenix.dto;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author xuanc
 */
@MapperScan(basePackages = "com.xcphoenix.dto.mapper")
@SpringBootApplication
@EnableCaching
public class DtoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DtoApplication.class, args);
    }

}
