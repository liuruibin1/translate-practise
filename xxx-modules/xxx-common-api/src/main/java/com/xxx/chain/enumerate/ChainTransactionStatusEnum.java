package com.xxx.chain.enumerate;

import lombok.Getter;

@Getter
public enum ChainTransactionStatusEnum {

    UNKNOWN(-1, "未知"),
    FAILED(0, "失败"),
    SUCCESSFUL(1, "成功"),
    ;

    private final Integer status;
    private final String description;

    ChainTransactionStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public static void main(String[] args) {
        for (ChainTransactionStatusEnum e : ChainTransactionStatusEnum.values()) {
            System.out.printf("%s = %d,\n", e.name(), e.getStatus());
        }
        for (ChainTransactionStatusEnum e : ChainTransactionStatusEnum.values()) {
            System.out.printf("[ChainTransactionStatusEnum.%s]: '%s',\n", e.name(), e.getDescription());
        }
    }

}