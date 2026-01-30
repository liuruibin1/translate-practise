package com.xxx.base.enumerate;

import lombok.Getter;

/**
 * 结算状态枚举
 */
@Getter
public enum SettlementStatusEnum {

    UNSETTLED(1, "未结算"),
    SETTLED(2, "已结算"),

    ;

    private final Integer status;
    private final String description;

    SettlementStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public static SettlementStatusEnum getByStatus(Integer id) {
        for (SettlementStatusEnum enumerate : SettlementStatusEnum.values()) {
            if (enumerate.getStatus().equals(id)) {
                return enumerate;
            }
        }
        return null;
    }

}