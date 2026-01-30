package com.xxx.framework.jwt.configuration;

import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({JWTProperties.class})
public class JWTConfiguration {

}