package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum ReceiverTypeEnum {

    USER(1, "用户表"),
    SYSTEM(11, "系统用户表");

    private final Integer value;
    private final String description;

    ReceiverTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ReceiverTypeEnum getByValue(Integer value) {
        for (ReceiverTypeEnum enumerate : ReceiverTypeEnum.values()) {
            if (enumerate.getValue().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}