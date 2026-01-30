package com.xxx.chain.enumerate;

import lombok.Getter;

@Getter
public enum ChainTypeEnum {

    BTC("BTC"),
    ETHEREUM("ETHEREUM"),
    TRON("TRON"),
    TON("TON"),
    ;

    private final String value;

    ChainTypeEnum(String value) {
        this.value = value;
    }

    public static ChainTypeEnum getByValue(String value) {
        for (ChainTypeEnum enumerate : ChainTypeEnum.values()) {
            if (enumerate.getValue().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}