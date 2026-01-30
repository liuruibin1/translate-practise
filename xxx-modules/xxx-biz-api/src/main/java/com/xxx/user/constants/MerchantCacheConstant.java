package com.xxx.user.constants;

public interface MerchantCacheConstant {

    /**
     * (商户)用户密码错误次数
     */
    int MERCHANT_PASSWORD_ERROR_LIMIT = 5;

    /**
     * (商户)用户锁定时长(分钟)
     */
    long MERCHANT_LOCK_DURATION_MINUTES = 10;

    /**
     * (商户)用户登录验证码
     * cache object
     */
    String MERCHANT_LOGIN_CAPTCHA_CODE = "merchant:login_captcha_code:";

    /**
     * (商户)用户登录密码错误次数
     * cache object
     */
    String MERCHANT_PASSWORD_ERROR_COUNT = "merchant:password_error_count:";

    /**
     * (商户)用户登录后数据
     * cache object
     */
    String MERCHANT_LOGGED_DATA = "merchant:logged_data:";

}