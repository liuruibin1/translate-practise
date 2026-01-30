package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <pre>
 *  {
 *      "query_id": "2632245",
 *      "source": "0:1F5806C73D03BBC96EB7EEBF16067F1F2E8E270CFA16D57001AA9EAF7456F008",
 *      "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *      "amount": "17000000",
 *      "source_wallet": "0:B7F5CD75F68A8F91A07E7E203CFC429F7A33F6E3F232282344C8A154B2F5EC7C",
 *      "jetton_master": "0:B113A994B5024A16719F69139328EB759596C38A25F59028B146FECDC3621DFE",
 *      "transaction_hash": "exkKjJjzWuZCh8zER2b8dVcSJRGR/LBQeG+YeyMfeOM=",
 *      "transaction_lt": "46760680000003",
 *      "transaction_now": 1716867695,
 *      "response_destination": "0:1F5806C73D03BBC96EB7EEBF16067F1F2E8E270CFA16D57001AA9EAF7456F008",
 *      "custom_payload": null,
 *      "forward_ton_amount": "1",
 *      "forward_payload": null
 *  },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3JettonTransfer implements Serializable {

    @JsonProperty("query_id")
    private String queryId;

    private String source;

    private String destination;

    private String amount;

    @JsonProperty("source_wallet")
    private String sourceWallet;

    @JsonProperty("jetton_master")
    private String jettonMaster;

    @JsonProperty("transaction_hash")
    private String transactionHash;

    @JsonProperty("transaction_lt")
    private String transactionLt;

    @JsonProperty("transaction_now")
    private Long transactionNow;

    @JsonProperty("response_destination")
    private String responseDestination;

    @JsonProperty("custom_payload")
    private String customPayload;

    @JsonProperty("forward_ton_amount")
    private String forwardTonAmount;

    @JsonProperty("forward_payload")
    private String forwardPayload;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSourceWallet() {
        return sourceWallet;
    }

    public void setSourceWallet(String sourceWallet) {
        this.sourceWallet = sourceWallet;
    }

    public String getJettonMaster() {
        return jettonMaster;
    }

    public void setJettonMaster(String jettonMaster) {
        this.jettonMaster = jettonMaster;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getTransactionLt() {
        return transactionLt;
    }

    public void setTransactionLt(String transactionLt) {
        this.transactionLt = transactionLt;
    }

    public Long getTransactionNow() {
        return transactionNow;
    }

    public void setTransactionNow(Long transactionNow) {
        this.transactionNow = transactionNow;
    }

    public String getResponseDestination() {
        return responseDestination;
    }

    public void setResponseDestination(String responseDestination) {
        this.responseDestination = responseDestination;
    }

    public String getCustomPayload() {
        return customPayload;
    }

    public void setCustomPayload(String customPayload) {
        this.customPayload = customPayload;
    }

    public String getForwardTonAmount() {
        return forwardTonAmount;
    }

    public void setForwardTonAmount(String forwardTonAmount) {
        this.forwardTonAmount = forwardTonAmount;
    }

    public String getForwardPayload() {
        return forwardPayload;
    }

    public void setForwardPayload(String forwardPayload) {
        this.forwardPayload = forwardPayload;
    }
}
