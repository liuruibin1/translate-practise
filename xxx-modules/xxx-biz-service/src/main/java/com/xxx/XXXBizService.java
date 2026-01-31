package com.xxx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.xxx.user.mapper"})
public class XXXBizService {

    public static void main(String[] args) {
        SpringApplication.run(XXXBizService.class, args);
    }

}