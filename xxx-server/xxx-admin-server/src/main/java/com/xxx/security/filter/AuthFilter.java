package com.xxx.security.filter;

import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.common.core.constants.JWTClaimKeyConstant;
import com.xxx.common.core.utils.JSONUtil;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.system.bo.SysUserLoginBO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthFilter extends OncePerRequestFilter {

    final JWTProperties jwtProperties;

    final RedisService redisService;

    public AuthFilter(JWTProperties jwtProperties, RedisService redisService) {
        this.jwtProperties = jwtProperties;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = JWTUtil.getToken(request);
        try {
            if (StringUtils.isEmpty(token) || !JWTUtil.verify(token, jwtProperties.getSecret())) {
                chain.doFilter(request, response);
                return;
            }
            String sysUserDataCacheKey = JWTUtil.getClaimByTokenClaimKeyAsString(token, JWTClaimKeyConstant.SYS_USER_DATA_CACHE_KEY);
            String sysUserLoggedDataCacheKey = SysUserCacheConstant.SYS_USER_LOGGED_DATA + sysUserDataCacheKey;
            Object sysUserLoggedDataCacheObj = redisService.opsForValueAndGet(sysUserLoggedDataCacheKey);
            if (sysUserLoggedDataCacheObj == null) {
                chain.doFilter(request, response);
                return;
            }
            SysUserLoginBO sysUserLoginBO = JSONUtil.objectToObject(sysUserLoggedDataCacheObj, SysUserLoginBO.class);
            if (sysUserLoginBO == null) {
                chain.doFilter(request, response);
                return;
            }
            List<String> permissionCodeList = sysUserLoginBO.getPermissionCodeList();
            if (permissionCodeList == null || CollectionUtils.isEmpty(permissionCodeList)) {
                chain.doFilter(request, response);
                return;
            }
            Set<String> permissionCodeSet = permissionCodeList.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            Set<SimpleGrantedAuthority> authorities = permissionCodeSet.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    sysUserLoginBO.getUserData().getId(),
                    null,
                    authorities);
            // 自定义 UserPrincipal
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception ignore) {
            // token 过期/签名错等：不设置认证，交给后续 .authenticated() 触发 401
        }
        chain.doFilter(request, response);
    }

}