//package com.xxx.gateway.filter;
//
//import com.xxx.common.core.constants.SpringProfilesConstant;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Component
//public class CorsFilter implements GlobalFilter, Ordered {
//
//    @Value("${spring.profiles.active}")
//    private String springProfilesActive;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        ServerHttpRequest request = exchange.getRequest();
//
//        ServerHttpResponse response = exchange.getResponse();
//
//        // 获取请求路径
//        String path = request.getPath().toString();
//
//        // 设置允许的源（可根据路径动态设置）
//        //String allowedOrigin = getAllowedOrigin(path);
//
//        // 设置CORS头
//        HttpHeaders headers = response.getHeaders();
//        //headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin);
//        if (!SpringProfilesConstant.PROD.equals(springProfilesActive)) {
//            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//        }
//        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, DELETE, OPTIONS");
//        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");
//        //headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
//        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
//
//        // 处理OPTIONS请求
//        if (request.getMethod() == HttpMethod.OPTIONS) {
//            response.setStatusCode(HttpStatus.OK);
//            return Mono.empty();
//        }
//
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        // 设置为高优先级，确保在其他过滤器之前执行
//        return Ordered.HIGHEST_PRECEDENCE;
//    }
//
//    ///**
//    // * 根据路径动态返回允许的Origin
//    // */
//    //private String getAllowedOrigin(String path) {
//    //    // 示例逻辑：不同路径允许不同的源
//    //    if (path.startsWith("/api/public/")) {
//    //        return "*"; // 公共API允许所有源
//    //    } else if (path.startsWith("/api/internal/")) {
//    //        return "https://internal.company.com"; // 内部API限制源
//    //    } else {
//    //        return "https://default.allowed.com"; // 默认允许的源
//    //    }
//    //}
//
//}