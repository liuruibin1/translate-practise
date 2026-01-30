package com.xxx.chain.enumerate;

import lombok.Getter;

@Getter
public enum ChainSiteEnum {

    TON_CENTER_COM("toncenter.com"),

    TON_API_IO("tonapi.io");

    private final String value;

    ChainSiteEnum(String value) {
        this.value = value;
    }

    public static ChainSiteEnum getByValue(String value) {
        for (ChainSiteEnum enumerate : ChainSiteEnum.values()) {
            if (enumerate.getValue().equals(value)) {
                return enumerate;
            }
        }
        return null;
    }

}
