package com.xxx.base.enumerate;

import lombok.Getter;

/**
 * 开奖状态枚举
 */
@Getter
public enum DrawStatusEnum {

    UNKNOWN(-2, "未知"),
    LOSE(-1, "输"),
    DRAW(0, "平"),
    WIN(1, "赢"),

    ;

    private final Integer status;
    private final String description;

    DrawStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public static DrawStatusEnum getByStatus(Integer value) {
        for (DrawStatusEnum enumerate : DrawStatusEnum.values()) {
            if (enumerate.getStatus().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}