package com.xxx.gateway.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        //if (!SpringProfilesConstant.PROD.equals(springProfilesActive)) {
        //TODO
        corsConfig.addAllowedOrigin("*");
        corsConfig.addAllowedOrigin("http://127.0.0.1:5173");
        corsConfig.addAllowedOrigin("https://127.0.0.1:5173");
        corsConfig.addAllowedOrigin("http://127.0.0.1:3000");
        corsConfig.addAllowedOrigin("http://127.0.0.1:8000");

        corsConfig.addAllowedOrigin("http://localhost:3000");
        corsConfig.addAllowedOrigin("http://localhost:5173");
        corsConfig.addAllowedOrigin("http://localhost:63342");
        corsConfig.addAllowedOrigin("http://localhost:8000");
        //}
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*"); // 允许所有请求头
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(source);
    }

}
