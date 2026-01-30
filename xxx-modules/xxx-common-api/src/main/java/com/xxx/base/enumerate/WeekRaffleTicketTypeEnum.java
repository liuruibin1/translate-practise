package com.xxx.base.enumerate;

import lombok.Getter;

@Getter
public enum WeekRaffleTicketTypeEnum {
    DAY(0, "每天下注"),
    AMOUNT(1, "每下注"),
    ;

    private final Integer code;
    private final String description;

    WeekRaffleTicketTypeEnum(Integer code,
            String description) {
        this.code = code;
        this.description = description;
    }
}
