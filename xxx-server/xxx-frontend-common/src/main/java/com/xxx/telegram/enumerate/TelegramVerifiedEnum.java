package com.xxx.telegram.enumerate;

import lombok.Getter;

@Getter
public enum TelegramVerifiedEnum {

    UNKNOWN(-1, "未知"),
    INVALID(0, "无效"),
    VALID(1, "有效"),
    ;

    private final Integer value;
    private final String description;

    TelegramVerifiedEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

}