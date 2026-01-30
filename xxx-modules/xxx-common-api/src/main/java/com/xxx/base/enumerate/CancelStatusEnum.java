package com.xxx.base.enumerate;

import lombok.Getter;

/**
 * 取消状态枚举
 */
@Getter
public enum CancelStatusEnum {

    NORMAL(0, "正常"),
    SYSTEM_CANCEL(1, "系统取消"),
    USER_CANCEL(2, "用户取消"),
    MERCHANT_CLIENT_CANCEL(9, "商户端的其它方式取消"),

    ;

    private final Integer status;
    private final String description;

    CancelStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public static CancelStatusEnum getByStatus(Integer status) {
        for (CancelStatusEnum enumerate : CancelStatusEnum.values()) {
            if (enumerate.getStatus().equals(status)) {
                return enumerate;
            }
        }
        return null;
    }

}