package com.xxx.auth.service;

import com.xxx.auth.bo.UserLoginBO;
import com.xxx.common.core.constants.JWTClaimKeyConstant;
import com.xxx.common.core.utils.DateMillisUtils;
import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.user.constants.UserCacheConstant;
import com.xxx.user.constants.UserConstant;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.xxx.common.core.utils.DateMillisUtils.MINUTE_MILLIS;

@Component
public class UserLoginService {

    private final RedisService redisService;

    private final JWTProperties jwtProperties;

    public UserLoginService(RedisService redisService, JWTProperties jwtProperties) {
        this.redisService = redisService;
        this.jwtProperties = jwtProperties;
    }

    public String getTokenAndRefreshCache(UserLoginBO userLoginBO) {
        Long userId = userLoginBO.getUser().getId();

        // 删除用户缓存数据
        redisService.delete(UserCacheConstant.USER_LOGGED_DATA + userId);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(JWTClaimKeyConstant.USER_DATA_CACHE_KEY, userId);
        claimsMap.put(jwtProperties.getUserIdClaimKey(), userId);
        claimsMap.put("_ts", new Date().getTime());

        // 记录今日登录: 计算今天剩余时间（秒），确保 Redis 数据在 00:00 自动过期
        long dayFirstMillisecondByMillisecondsAndDays = DateMillisUtils.getDayFirstMillisByMillisAndDays(System.currentTimeMillis(), 1);
        long dueTimeMs = dayFirstMillisecondByMillisecondsAndDays - new Date().getTime();
        if (userId == UserConstant.USER_ADMIN_ID) {
            userLoginActive(userId.toString(), UserConstant.USER_ADMIN_ID + "", dueTimeMs, TimeUnit.MILLISECONDS);
        } else {
            userLoginActive(userId.toString(), userLoginBO.getUser().getParentId().toString(), dueTimeMs, TimeUnit.MILLISECONDS);
        }

        String token = JWTUtil.buildToken(
                claimsMap,
                jwtProperties.getSecret(),
                jwtProperties.getIssuer(),
                jwtProperties.getTokenValidDuration(),
                TimeUnit.MINUTES
        );

        userLoginBO.setJwtToken(token);
        userLoginBO.setLoginTimestamp(System.currentTimeMillis());
        userLoginBO.setExpireTimestamp(new Date().getTime() + jwtProperties.getTokenValidDuration() * MINUTE_MILLIS);

        // 缓存用户登录数据
        redisService.opsForValueAndSet(
                UserCacheConstant.USER_LOGGED_DATA + userId,
                userLoginBO,
                jwtProperties.getTokenValidDuration(),
                TimeUnit.MINUTES
        );
        return token;
    }

    /**
     * 获取在线人数
     *
     * @return 用户信息
     */
    public long getOnlineUsersForParent(Long userId, List<Long> childList) {
        //如果 parentId 为空，则返回所有在线用户
        if (userId == UserConstant.USER_ADMIN_ID) {
            return redisService.opsForSetAndMembers(UserCacheConstant.USER_LOGGED_DATA).size();
        }
        long count = 0L;
        for (Long child : childList) {
            Boolean isExist = redisService.hasKey(UserCacheConstant.USER_LOGGED_DATA + child.toString());
            if (isExist) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取今日注册人数
     *
     * @return 用户信息
     */
    public long getUserRegisterCount(Long userId, List<Long> childList) {
        // 获取所有在线用户
        Set<Object> onlineUsers = redisService.opsForSetAndMembers(UserCacheConstant.USER_REGISTER_TODAY_COUNT);
        if (onlineUsers == null) return 0;
        // 如果 userId 为空，则返回所有在线用户
        if (userId == UserConstant.USER_ADMIN_ID) {
            return onlineUsers.size();
        }
        // 统计 childList 中在 onlineUsers 里的数量
        return childList
                .stream()
                .filter(id -> onlineUsers.contains(id.toString()))  // 确保匹配类型一致
                .count();
    }

    /**
     * 获取今日登录人数
     *
     * @return 用户信息
     */
    public long getUserLoginCount(Long userId, List<Long> childList) {
        // 获取所有在线用户
        Set<Object> onlineUsers = redisService.opsForSetAndMembers(UserCacheConstant.USER_LOGIN_TODAY);
        if (onlineUsers == null) return 0;
        // 如果 userId 为空，则返回所有在线用户
        if (userId == UserConstant.USER_ADMIN_ID) {
            return onlineUsers.size();
        }
        // 统计 childList 中在 onlineUsers 里的数量
        return childList
                .stream()
                .filter(id -> onlineUsers.contains(id.toString()))  // 确保匹配类型一致
                .count();
    }

    private void userLoginActive(String userId, String parentId, Long dueTime, TimeUnit timeUnit) {
        redisService.opsForSetAndAddAndExpire(UserCacheConstant.USER_LOGIN_TODAY, userId, dueTime, timeUnit);
        redisService.opsForHashAndPutAndExpire(UserCacheConstant.USER_PARENT_MAP_FOR_LOGIN, userId, parentId, dueTime, timeUnit);
    }

}