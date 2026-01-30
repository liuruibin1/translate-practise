package com.xxx.common.core.enumerate;

import lombok.Getter;

@Getter
public enum ExpiredTimeEnum {

    VERIFICATION_EXPIRED_TIME(3),
    EXPIRED_TIME(30),
    ;

    private final int value;

    ExpiredTimeEnum(int value) {
        this.value = value;
    }

}