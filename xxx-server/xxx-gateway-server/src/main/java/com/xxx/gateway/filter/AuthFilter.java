package com.xxx.gateway.filter;

import com.xxx.auth.bo.UserLoginBO;
import com.xxx.base.vo.Response;
import com.xxx.common.core.constants.JWTClaimKeyConstant;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.gateway.configuration.properties.ApplicationGatewayProperties;
import com.xxx.gateway.utils.WebFluxUtils;
import com.xxx.user.constants.UserCacheConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xxx.common.core.enumerate.BizErrorEnum._500_001;
import static com.xxx.common.core.enumerate.BizErrorEnum._500_002;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    final ApplicationGatewayProperties applicationGatewayProperties;

    final RedisService redisService;

    final JWTProperties jwtProperties;

    public AuthFilter(
            ApplicationGatewayProperties applicationGatewayProperties,
            RedisService redisService,
            JWTProperties jwtProperties) {
        this.applicationGatewayProperties = applicationGatewayProperties;
        this.redisService = redisService;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().getPath();
        // 匹配正则 和 不需要验证的路径
        if (matchPrefix(applicationGatewayProperties.getAllowedUrls(), url) || matchPattern(url)) {
            return chain.filter(exchange);
        }
        String token = JWTUtil.getToken(exchange);
        if (StringUtils.isEmpty(token) || !JWTUtil.verify(token, jwtProperties.getSecret())) {
            return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), Response.error(_500_001));
        }
        String userDataCacheKey = JWTUtil.getClaimByTokenClaimKeyAsString(token, JWTClaimKeyConstant.USER_DATA_CACHE_KEY);
        String userLoggedDataCacheKey = UserCacheConstant.USER_LOGGED_DATA + userDataCacheKey;
        //Object userLoggedDataCacheObj = redisService.opsForValueAndGet(userLoggedDataCacheKey,UserLoginBO.class);
        //UserLoginBO userLoginBO = JSONUtil.objectToObject(userLoggedDataCacheObj, UserLoginBO.class);
        UserLoginBO userLoginBO = redisService.opsForValueAndGet(userLoggedDataCacheKey, UserLoginBO.class);
        if (userLoginBO == null || !token.equals(userLoginBO.getJwtToken())) {//不允许用户重复登录
            return WebFluxUtils.webFluxResponseWriter(exchange.getResponse(), Response.error(_500_002));
        }
        if (matchPrefix(applicationGatewayProperties.getActionUrls(), url)) {
            redisService.expire(userLoggedDataCacheKey, jwtProperties.getTokenValidDuration(), TimeUnit.MINUTES);
        }
        ServerHttpRequest.Builder mutate = request.mutate();
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }

    private boolean matchPrefix(List<String> urlRuleList, String currentUrl) {
        for (String urlRule : urlRuleList) {
            if (currentUrl.startsWith(urlRule)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchPattern(String url) {
        Pattern p = Pattern.compile(applicationGatewayProperties.getAllowedPattern());
        Matcher m = p.matcher(url);
        return m.matches();
    }

}