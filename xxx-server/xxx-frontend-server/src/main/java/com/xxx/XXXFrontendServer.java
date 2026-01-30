package com.xxx;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class XXXFrontendServer {

    public static void main(String[] args) {
        SpringApplication.run(XXXFrontendServer.class, args);
    }

}