package com.xxx.base.controller;

import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.common.core.exception.BusinessRuntimeException;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.system.bo.SysUserLoginBO;
import com.xxx.system.vo.SysUserVO;
import com.xxx.user.constants.UserCacheConstant;
import com.xxx.user.constants.UserConstant;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static com.xxx.common.core.enumerate.BizErrorEnum._500_002;

public class BaseController extends AbsController {

    @Resource
    protected JWTProperties jwtProperties;

    @Resource
    protected RedisService redisService;

    protected String getJWTToken(HttpServletRequest request) {
        return JWTUtil.getToken(request);
    }

    protected String getLanguageCode(HttpServletRequest request) {
        return request.getHeader("language_code");
    }

    protected Integer getCurrencyId(HttpServletRequest request) {
        String currencyId = request.getHeader("currency_id");
        if (StringUtils.isNotEmpty(currencyId)) {
            return Integer.valueOf(request.getHeader("currency_id"));
        } else {
            return null;
        }
    }

    protected Long getSysUserId(HttpServletRequest request) {
        return JWTUtil.getClaimByTokenClaimKeyAsLong(getJWTToken(request), jwtProperties.getUserIdClaimKey());
    }

    protected SysUserLoginBO getLoginData(HttpServletRequest request) {
        Long sysUserId = getSysUserId(request);
        // 取缓存用户登录数据
        return redisService.opsForValueAndGet(SysUserCacheConstant.SYS_USER_LOGGED_DATA + sysUserId, SysUserLoginBO.class);
    }

    protected Long getCurrentMerchantUserId(HttpServletRequest request) {
        SysUserLoginBO sysUserLoginBO = getLoginData(request);
        if (sysUserLoginBO == null) {
            throw new BusinessRuntimeException(_500_002); //登录令牌无效或已过期
        }
        SysUserVO sysUserVO = sysUserLoginBO.getUserData();
        if (sysUserVO == null) {
            throw new BusinessRuntimeException(_500_002); //登录令牌无效或已过期
        }
        return sysUserVO.getMerchantUserId();
    }

    /**
     * 获取在线人数
     *
     * @return 用户信息
     */
    public long getOnlineUsersForParent(Long userId, List<Long> childList) {
        //如果 parentId 为空，则返回所有在线用户
        if (userId == UserConstant.USER_ADMIN_ID) {
            return redisService.opsForSetAndMembers(UserCacheConstant.USER_LOGIN_TODAY).size();
        }
        long count = 0L;
        for (Long child : childList) {
            Boolean isExist = redisService.hasKey(UserCacheConstant.USER_LOGIN_TODAY + child.toString());
            if (isExist) {
                count++;
            }
        }
        return count;
    }


}