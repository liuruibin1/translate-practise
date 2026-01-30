package com.xxx.chain.enumerate;

import lombok.Getter;

@Getter
public enum CryptoCurrencyProposalEnum {

    ERC20(1),
    BEP20(2),
    TRC20(3),
    JETTON(5),
    ;

    private final Integer value;

    CryptoCurrencyProposalEnum(Integer value) {
        this.value = value;
    }

}