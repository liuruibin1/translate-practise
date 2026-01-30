package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum OperatorTypeEnum {

    USER(1, "用户"),
    MERCHANT(2, "商户"),
    SYSTEM(11, "系统用户"),
    JOB(19, "(系统服务)");

    private final Integer type;
    private final String description;

    OperatorTypeEnum(Integer type, String description) {
        this.type = type;
        this.description = description;
    }

    public static OperatorTypeEnum getByValue(Integer value) {
        for (OperatorTypeEnum enumerate : OperatorTypeEnum.values()) {
            if (enumerate.getType().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}