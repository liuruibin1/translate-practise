package com.xxx.auth.constants;

public interface SysUserCacheConstant {

    /**
     * (系统)用户密码错误次数
     */
    int SYS_USER_PASSWORD_ERROR_LIMIT = 5;

    /**
     * (系统)用户锁定时长(分钟)
     */
    long SYS_USER_LOCK_DURATION_MINUTES = 10;

    /**
     * (系统)用户登录验证码
     * cache object
     */
    String SYS_USER_LOGIN_CAPTCHA_CODE = "sys_user:login_captcha_code:";

    /**
     * (系统)用户登录密码错误次数
     * cache object
     */
    String SYS_USER_PASSWORD_ERROR_COUNT = "sys_user:password_error_count:";

    /**
     * (系统)用户登录后数据
     * cache object
     */
    String SYS_USER_LOGGED_DATA = "sys_user:logged_data:";

}