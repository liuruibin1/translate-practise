package com.xxx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = {"com.xxx.user.mapper"})
public class XXXBizService {

    public static void main(String[] args) {
        SpringApplication.run(XXXBizService.class, args);
    }

}