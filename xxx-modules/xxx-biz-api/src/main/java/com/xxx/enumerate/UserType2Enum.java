package com.xxx.enumerate;

import lombok.Getter;

@Getter
public enum UserType2Enum {

    TELEGRAM(1, "电报"),
    CRYPTO_ACCOUNT(2, "加密账户"),
    FACEBOOK(3, "脸书"),
    GOOGLE(4, "谷歌用户"),
    EMAIL(5, "邮件"),
    PHONE_NUMBER(6, "手机号"),
    TWITTER(7, "推特"),
    LINE(8, "Line"),
    MERCHANT_CREATION(91, "商户创建"),
    ;

    private final Integer type;
    private final String description;

    UserType2Enum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static UserType2Enum getByType(Integer type) {
        for (UserType2Enum enumerate : UserType2Enum.values()) {
            if (enumerate.getType().equals(type)) {
                return enumerate;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (UserType2Enum e : UserType2Enum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.getType());
        }
        for (UserType2Enum e : UserType2Enum.values()) {
            System.out.printf("[UserType2Enum.%s]: '%s',\n", e.name(), e.getDescription());
        }
    }

}