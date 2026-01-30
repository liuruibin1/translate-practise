package com.xxx.chain.adapter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransactionBO implements Serializable {

    private String chainInfoId;
    private Long blockNumber;
    private String blockHash;
    private String txHash;
    private Long txTimestamp;
    private String fromAddress;
    private String interactedContractAddress;
    private String topic0;
    private String topic1;
    private String topic2;
    private String topic3;
    private String data0;
    private String eventName;
    private String topicName;
    private Long utcTimestamp;

}