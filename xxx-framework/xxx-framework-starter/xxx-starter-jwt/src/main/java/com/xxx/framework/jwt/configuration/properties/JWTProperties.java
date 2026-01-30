package com.xxx.framework.jwt.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "framework.jwt")
public class JWTProperties {

    private String secret;

    private String issuer = "com.xxx";

    private String userIdClaimKey = "user_id";

    private String visitorIdClaimKey = "visitor_id";

    private Long tokenRefreshExpire = 720L;

    private Long tokenValidDuration = 10080L;

    private Long tokenRefreshDuration = 720L;

    private String refreshTokenFormat = "JWT_REFRESH_TOKEN::%s";

    private String blacklistFormat = "JWT_BLACKLIST::%s";

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getIssuer() {
        return this.issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getUserIdClaimKey() {
        return userIdClaimKey;
    }

    public void setUserIdClaimKey(String userIdClaimKey) {
        this.userIdClaimKey = userIdClaimKey;
    }

    public String getVisitorIdClaimKey() {
        return visitorIdClaimKey;
    }

    public void setVisitorIdClaimKey(String visitorIdClaimKey) {
        this.visitorIdClaimKey = visitorIdClaimKey;
    }

    public Long getTokenValidDuration() {
        return tokenValidDuration;
    }

    public void setTokenValidDuration(Long tokenValidDuration) {
        this.tokenValidDuration = tokenValidDuration;
    }

    public Long getTokenRefreshDuration() {
        return tokenRefreshDuration;
    }

    public void setTokenRefreshDuration(Long tokenRefreshDuration) {
        this.tokenRefreshDuration = tokenRefreshDuration;
    }

    public String getRefreshTokenFormat() {
        return this.refreshTokenFormat;
    }

    public void setRefreshTokenFormat(String refreshTokenFormat) {
        this.refreshTokenFormat = refreshTokenFormat;
    }

    public String getBlacklistFormat() {
        return this.blacklistFormat;
    }

    public void setBlacklistFormat(String blacklistFormat) {
        this.blacklistFormat = blacklistFormat;
    }

    public Long getTokenRefreshExpire() {
        return tokenRefreshExpire;
    }

    public void setTokenRefreshExpire(Long tokenRefreshExpire) {
        this.tokenRefreshExpire = tokenRefreshExpire;
    }
}
