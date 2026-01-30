package com.xxx.captcha.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "framework.captcha")
public class CaptchaProperties {

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 验证码类型（math 数值计算 char 字符）
     */
    private String type;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}