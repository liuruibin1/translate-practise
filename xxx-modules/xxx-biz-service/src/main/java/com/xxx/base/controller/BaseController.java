package com.xxx.base.controller;


import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class BaseController extends AbsController {


    @Resource
    protected JWTProperties jwtProperties;

    @Resource
    protected RedisService redisService;

    protected Long getUserIdAsLong(ServerWebExchange exchange) {
        String token = JWTUtil.getToken(exchange);
        return JWTUtil.getClaimByTokenClaimKeyAsLong(token, jwtProperties.getUserIdClaimKey());
    }

    //    /**
    //     * 获取客户端真实IP地址
    //     */
    //    protected String getClientIpAddress(HttpServletRequest request) {
    //        String ip = null;

    //        // 1. 检查X-Forwarded-For头（经过代理/负载均衡器）
    //        ip = request.getHeader("X-Forwarded-For");
    //        if (isValidIp(ip)) {
    //            // X-Forwarded-For可能包含多个IP，取第一个
    //            return ip.split(",")[0].trim();
    //        }

    //        // 2. 检查X-Real-IP头（Nginx代理常用）
    //        ip = request.getHeader("X-Real-IP");
    //        if (isValidIp(ip)) {
    //            return ip;
    //        }

    //        // 3. 检查Proxy-Client-IP头
    //        ip = request.getHeader("Proxy-Client-IP");
    //        if (isValidIp(ip)) {
    //            return ip;
    //        }

    //        // 4. 检查WL-Proxy-Client-IP头（WebLogic）
    //        ip = request.getHeader("WL-Proxy-Client-IP");
    //        if (isValidIp(ip)) {
    //            return ip;
    //        }

    //        // 5. 检查HTTP_CLIENT_IP头
    //        ip = request.getHeader("HTTP_CLIENT_IP");
    //        if (isValidIp(ip)) {
    //            return ip;
    //        }

    //        // 6. 检查HTTP_X_FORWARDED_FOR头
    //        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    //        if (isValidIp(ip)) {
    //            return ip;
    //        }

    //        // 7. 最后使用request.getRemoteAddr()
    //        ip = request.getRemoteAddr();

    //        // 处理IPv6的本地地址
    //        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
    //            ip = "127.0.0.1";
    //        }

    //        return ip;
    //    }

}