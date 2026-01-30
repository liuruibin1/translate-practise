package com.xxx.gateway.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置
 */
@Configuration
@ConfigurationProperties(prefix = "application.gateway")
public class ApplicationGatewayProperties {

    /**
     * 不拦截URL
     */
    private List<String> allowedUrls = new ArrayList<>();

    /**
     * 动作url
     */
    private List<String> actionUrls = new ArrayList<>();

    /**
     * 不拦截正则
     */
    private String allowedPattern;

    public List<String> getAllowedUrls() {
        return allowedUrls;
    }

    public void setAllowedUrls(List<String> allowedUrls) {
        this.allowedUrls = allowedUrls;
    }

    public String getAllowedPattern() {
        return allowedPattern;
    }

    public void setAllowedPattern(String allowedPattern) {
        this.allowedPattern = allowedPattern;
    }

    public List<String> getActionUrls() {
        return actionUrls;
    }

    public void setActionUrls(List<String> actionUrls) {
        this.actionUrls = actionUrls;
    }
}

