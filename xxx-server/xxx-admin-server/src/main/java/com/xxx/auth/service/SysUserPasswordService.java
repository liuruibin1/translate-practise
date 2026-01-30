package com.xxx.auth.service;

import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.common.core.vo.BaseResponse;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.system.entity.SysUser;
import com.xxx.system.provider.ISysUserLoginLogProvider;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SysUserPasswordService {

    final RedisService redisService;

    final PasswordEncoder passwordEncoder;

    @DubboReference
    ISysUserLoginLogProvider sysUserLoginLogProvider;

    public SysUserPasswordService(RedisService redisService, PasswordEncoder passwordEncoder) {
        this.redisService = redisService;
        this.passwordEncoder = passwordEncoder;
    }

    public BaseResponse validate(SysUser sysUser, String password) {
        String username = sysUser.getUsername();
        Integer retryCount = redisService.opsForValueAndGet(getPasswordErrorCountCacheKey(username));
        if (retryCount == null) {
            retryCount = 0;
        }
        int passwordErrorLimit = SysUserCacheConstant.SYS_USER_PASSWORD_ERROR_LIMIT;
        long lockDurationMinutes = SysUserCacheConstant.SYS_USER_LOCK_DURATION_MINUTES;
        if (retryCount >= passwordErrorLimit) {
            String errMsg = String.format("Entered incorrectly %s times. Account locked for %s minutes", passwordErrorLimit, lockDurationMinutes);
            sysUserLoginLogProvider.create(username, false, errMsg);
            return BaseResponse.error(errMsg);
        }
        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            retryCount = retryCount + 1;
            sysUserLoginLogProvider.create(username, false, String.format("Entered incorrectly %s times", retryCount));
            redisService.opsForValueAndSet(getPasswordErrorCountCacheKey(username), retryCount, lockDurationMinutes, TimeUnit.MINUTES);
            return BaseResponse.error("Incorrect username or password");
        } else {
            clearLoginRecordCache(username);
        }
        return BaseResponse.success();
    }

    public void clearLoginRecordCache(String loginName) {
        if (redisService.hasKey(getPasswordErrorCountCacheKey(loginName))) {
            redisService.delete(getPasswordErrorCountCacheKey(loginName));
        }
    }

    private String getPasswordErrorCountCacheKey(String username) {
        return SysUserCacheConstant.SYS_USER_PASSWORD_ERROR_COUNT + username;
    }

}