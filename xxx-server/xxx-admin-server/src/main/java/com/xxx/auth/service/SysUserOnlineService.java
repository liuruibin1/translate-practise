package com.xxx.auth.service;

import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.common.core.utils.SpringProfilesUtils;
import com.xxx.framework.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SysUserOnlineService {

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    private final RedisService redisService;

    public SysUserOnlineService(RedisService redisService) {
        this.redisService = redisService;
    }

    public void forceLogoutByUserId(Long sysUserId) {
        if (SpringProfilesUtils.isProduction(springProfilesActive)) {
            redisService.delete(SysUserCacheConstant.SYS_USER_LOGGED_DATA + sysUserId);
        }
    }

    //public void forceLogoutByRoleId(Integer sysRoleId) {
    //    if (SpringProfilesUtils.isProduction(springProfilesActive)) {
    //        forceLogoutByRoleIdList(Collections.singletonList(sysRoleId));
    //    }
    //}

    //public void forceLogoutByRoleIdList(List<Integer> sysRoleIdList) {
    //    if (SpringProfilesUtils.isProduction(springProfilesActive)) {
    //        Collection<String> keys = redisService.scanAll(SysUserCacheConstant.SYS_USER_LOGGED_DATA);
    //        for (String key : keys) {
    //            Object cacheObject = redisService.opsForValueAndGet(key);
    //            SysUserLoginBO loginUserBO = JSONUtil.objectToObject(cacheObject, SysUserLoginBO.class);
    //            List<Integer> roleIdList = loginUserBO.getRoleIdList();
    //            if (CollectionUtils.isNotEmpty(roleIdList)) {
    //                for (Integer roleId : roleIdList) {
    //                    for (Integer sysRoleId : sysRoleIdList) {
    //                        if (roleId.equals(sysRoleId)) {
    //                            redisService.delete(key);
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //    }
    //}

}
