package com.xxx.chain.enumerate;

import lombok.Getter;

@Getter
public enum CryptoAccountAlgorithmEnum {

    ETH_ECDSA(1),
    TRON_ECDSA(2),
    TON_CRC16_CCITT(3),
    ;

    private final Integer value;

    CryptoAccountAlgorithmEnum(Integer value) {
        this.value = value;
    }

}