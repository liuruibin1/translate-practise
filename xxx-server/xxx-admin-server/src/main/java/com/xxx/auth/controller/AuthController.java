package com.xxx.auth.controller;

import com.xxx.auth.constants.SysUserCacheConstant;
import com.xxx.auth.constants.SysUserConstant;
import com.xxx.auth.request.LoginDTO;
import com.xxx.auth.response.AuthResponse;
import com.xxx.auth.response.CaptchaCodeResponse;
import com.xxx.auth.service.SysUserLoginService;
import com.xxx.auth.service.SysUserOnlineService;
import com.xxx.base.controller.BaseController;
import com.xxx.base.vo.Response;
import com.xxx.captcha.service.CaptchaCodeService;
import com.xxx.common.core.constants.JWTClaimKeyConstant;
import com.xxx.common.core.utils.StringUtils;
import com.xxx.common.core.vo.BaseResponse;
import com.xxx.framework.jwt.configuration.properties.JWTProperties;
import com.xxx.framework.jwt.utils.JWTUtil;
import com.xxx.framework.redis.service.RedisService;
import com.xxx.system.bo.SysUserLoginBO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.xxx.auth.constants.SysUserCacheConstant.SYS_USER_LOGIN_CAPTCHA_CODE;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private final RedisService redisService;

    private final JWTProperties jwtProperties;

    private final CaptchaCodeService captchaCodeService;

    private final SysUserLoginService sysUserLoginService;

    private final SysUserOnlineService sysUserOnlineService;

    public AuthController(
            CaptchaCodeService captchaCodeService,
            RedisService redisService,
            JWTProperties jwtProperties,
            SysUserLoginService sysUserLoginService,
            SysUserOnlineService sysUserOnlineService) {
        this.captchaCodeService = captchaCodeService;
        this.redisService = redisService;
        this.jwtProperties = jwtProperties;
        this.sysUserLoginService = sysUserLoginService;
        this.sysUserOnlineService = sysUserOnlineService;
    }

    @PostMapping("/login")
    @ResponseBody
    public AuthResponse login(@RequestBody LoginDTO dtoParam) {
        AuthResponse authResponse = new AuthResponse();
        //检查验证码
        BaseResponse response = captchaCodeService.checkCaptcha(
                SYS_USER_LOGIN_CAPTCHA_CODE,
                dtoParam.getCaptchaCode(),
                dtoParam.getCaptchaCodeUuid());
        if (!response.isSuccess()) {
            authResponse.setSuccess(false);
            authResponse.setMessage(response.getMessage());
            return authResponse;
        }
        // 用户登录
        response = sysUserLoginService.login(dtoParam.getUsername(), dtoParam.getPassword());
        if (!response.isSuccess()) {
            authResponse.setSuccess(false);
            authResponse.setMessage(response.getMessage());
            return authResponse;
        }
        SysUserLoginBO sysUserLoginBO = (SysUserLoginBO) response.getData();
        //强制登出
        if (!Objects.equals(sysUserLoginBO.getUserData().getId(), SysUserConstant.SYS_USER_ADMIN_ID)) {
            sysUserOnlineService.forceLogoutByUserId(sysUserLoginBO.getUserData().getId());
        }
        // 获取登录token
        String token = sysUserLoginService.getTokenAndRefreshCache(sysUserLoginBO);
        authResponse.setSuccess(true);
        authResponse.setAccessToken(token);
        authResponse.setRefreshToken("");
        authResponse.setExpiresTs(JWTUtil.getTokenExpireDatetime(jwtProperties.getTokenValidDuration(), TimeUnit.MINUTES).getTime());
        return authResponse;
    }

    @GetMapping("/logout")
    @ResponseBody
    public Response logout(HttpServletRequest request) {
        String jwtToken = getJWTToken(request);
        if (StringUtils.isNotEmpty(jwtToken)) {
            String sysUserDataCacheKey = JWTUtil.getClaimByTokenClaimKeyAsString(jwtToken, JWTClaimKeyConstant.SYS_USER_DATA_CACHE_KEY);
            redisService.delete(SysUserCacheConstant.SYS_USER_LOGGED_DATA + sysUserDataCacheKey);
        }
        return Response.success();
    }

    @GetMapping("/obtain-login-captcha-code")
    @ResponseBody
    public CaptchaCodeResponse obtainLoginCaptchaCode() {
        CaptchaCodeResponse captchaCodeResponse = new CaptchaCodeResponse();
        BaseResponse response = captchaCodeService.createCaptcha(SYS_USER_LOGIN_CAPTCHA_CODE);
        captchaCodeResponse.setSuccess(response.isSuccess());
        captchaCodeResponse.setMessage(response.getMessage());
        captchaCodeResponse.setUuid(response.get("uuid").toString());
        captchaCodeResponse.setImg(response.get("img").toString());
        return captchaCodeResponse;
    }

}