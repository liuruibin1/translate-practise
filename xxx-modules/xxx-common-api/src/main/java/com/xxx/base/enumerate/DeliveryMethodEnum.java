package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum DeliveryMethodEnum {

    EMAIL(1, "邮件"),
    SITE_MESSAGE(2, "站内信"),
    SMS(3, "短信"),
    TELEGRAM(4, "电报"),
    ;

    private final Integer method;
    private final String description;

    DeliveryMethodEnum(Integer method, String description) {
        this.method = method;
        this.description = description;
    }

    public static DeliveryMethodEnum getByMethod(Integer method) {
        for (DeliveryMethodEnum enumerate : DeliveryMethodEnum.values()) {
            if (enumerate.getMethod().equals(method)) {
                return enumerate;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (DeliveryMethodEnum e : DeliveryMethodEnum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.getMethod());
        }
        for (DeliveryMethodEnum e : DeliveryMethodEnum.values()) {
            System.out.printf("[DeliveryMethodEnum.%s]: '%s',\n", e.name(), e.getDescription());
        }
    }

}
