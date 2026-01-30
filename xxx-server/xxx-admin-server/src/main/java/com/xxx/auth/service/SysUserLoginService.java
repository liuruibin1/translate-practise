package com.xxx.auth.service;

import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.common.core.constants.JWTClaimKeyConstant;
import com.xxx.common.core.utils.BeanUtil;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.common.core.vo.BaseResponse;
import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.system.bo.SysUserLoginBO;
import com.xxx.system.entity.SysUser;
import com.xxx.system.provider.ISysPermissionProvider;
import com.xxx.system.provider.ISysRoleProvider;
import com.xxx.system.provider.ISysUserLoginLogProvider;
import com.xxx.system.provider.ISysUserProvider;
import com.xxx.system.vo.SysUserVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.xxx.common.core.utils.DateMillisUtils.MINUTE_MILLIS;

@Component
public class SysUserLoginService {

    @DubboReference
    ISysUserProvider sysUserProvider;

    @DubboReference
    ISysRoleProvider sysRoleProvider;

    @DubboReference
    ISysPermissionProvider sysPermissionProvider;

    @DubboReference
    ISysUserLoginLogProvider sysUserLoginLogProvider;

    private final RedisService redisService;

    private final JWTProperties jwtProperties;

    private final SysUserPasswordService sysUserPasswordService;

    public SysUserLoginService(
            RedisService redisService,
            JWTProperties jwtProperties,
            SysUserPasswordService sysUserPasswordService) {
        this.redisService = redisService;
        this.jwtProperties = jwtProperties;
        this.sysUserPasswordService = sysUserPasswordService;
    }

    public BaseResponse login(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            return BaseResponse.error("用户与密码必填");
        }
        // 查询用户信息
        SysUser dbSysUser = sysUserProvider.queryOneByUsername(username);
        if (ObjectUtils.isNull(dbSysUser)) {
            return BaseResponse.error("用户：" + username + " 不存在");
        }
        if (dbSysUser.getIsDeleted()) {
            sysUserLoginLogProvider.create(username, false, "账号已被删除");
            return BaseResponse.error("账号：" + username + " 已被删除");
        }
        if (!dbSysUser.getIsEnabled()) {
            sysUserLoginLogProvider.create(username, false, "用户已禁用");
            return BaseResponse.error("账号：" + username + " 已禁用");
        }
        BaseResponse response = sysUserPasswordService.validate(dbSysUser, password);
        if (!response.isSuccess()) {
            return response;
        }
        sysUserLoginLogProvider.create(username, true, "登录成功");
        SysUserLoginBO sysUserLoginBO = new SysUserLoginBO();
        dbSysUser.setPassword(null);//TODO 密码
        sysUserLoginBO.setUserData(BeanUtil.copyToNewBean(dbSysUser, SysUserVO.class));
        // 角色集合
        List<Integer> roleIdList = sysRoleProvider.queryDistinctIdBySysUserIdIsEnabledTrue(dbSysUser.getId());
        sysUserLoginBO.setRoleIdList(roleIdList);
        // 权限码集合
        List<String> permissionCodeList = sysPermissionProvider.queryDistinctCodeBySysUserIdIsEnabledTrue(dbSysUser.getId());
        sysUserLoginBO.setPermissionCodeList(permissionCodeList);
        return BaseResponse.success(sysUserLoginBO);
    }

    public String getTokenAndRefreshCache(SysUserLoginBO sysUserLoginBO) {
        Long sysUserId = sysUserLoginBO.getUserData().getId();

        //删除用户缓存数据
        redisService.delete(SysUserCacheConstant.SYS_USER_LOGGED_DATA + sysUserId.toString());

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(JWTClaimKeyConstant.SYS_USER_DATA_CACHE_KEY, sysUserId);
        claimsMap.put(jwtProperties.getUserIdClaimKey(), sysUserId);
        claimsMap.put("_ts", new Date().getTime());

        String token = JWTUtil.buildToken(
                claimsMap,
                jwtProperties.getSecret(),
                jwtProperties.getIssuer(),
                jwtProperties.getTokenValidDuration(),
                TimeUnit.MINUTES);

        //更新用户缓存数据
        sysUserLoginBO.setJwtToken(token);
        sysUserLoginBO.setLoginTimestamp(System.currentTimeMillis());
        sysUserLoginBO.setExpireTimestamp(new Date().getTime() + jwtProperties.getTokenValidDuration() * MINUTE_MILLIS);

        // 缓存用户登录数据
        redisService.opsForValueAndSet(
                SysUserCacheConstant.SYS_USER_LOGGED_DATA + sysUserId,
                sysUserLoginBO,
                jwtProperties.getTokenValidDuration(),
                TimeUnit.MINUTES);

        return token;
    }

}