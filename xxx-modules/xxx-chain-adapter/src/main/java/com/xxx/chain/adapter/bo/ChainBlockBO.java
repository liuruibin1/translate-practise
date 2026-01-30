package com.xxx.chain.adapter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChainBlockBO implements Serializable {

    private Long blockNumber;
    private String blockHash;
    private Long blockTimestamp;
    private Boolean transactionChecked;

}