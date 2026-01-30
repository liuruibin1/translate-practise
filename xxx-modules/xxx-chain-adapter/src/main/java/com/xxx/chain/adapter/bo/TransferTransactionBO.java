package com.xxx.chain.adapter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransferTransactionBO implements Serializable {

    private String contractAddress;
    private String fromAddress;
    private String toAddress;
    private String amountRaw;
    private Long blockNumber;
    private String blockHash;
    private String transactionHash;
    private Long transactionTimestamp;
    private Long transactionTimestampUtc;
    private Integer transactionStatus;
    private String transactionAdditionalValue;
    private Boolean isNative;

    private Long tonWorkChain;
    private String tonShard;
    private String tonLt;
    private String tonComment;

}
