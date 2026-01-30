package com.xxx.chain.ton.bo.tonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <pre>
 *      https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events?initiator=false&subject_only=false&limit=20&start_date=1720327670&end_date=1720327693
 *      "recipient": {
 *        "address": "0:44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3",
 *        "is_scam": false,
 *        "is_wallet": true
 *      },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonApiRecipient implements Serializable {

    private String address;

    @JsonProperty("is_scam")
    private Boolean isScam;

    @JsonProperty("is_wallet")
    private Boolean isWallet;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsScam() {
        return this.isScam;
    }

    public void setIsScam(Boolean scam) {
        this.isScam = scam;
    }

    public Boolean getIsWallet() {
        return this.isWallet;
    }

    public void setIsWallet(Boolean wallet) {
        this.isWallet = wallet;
    }

}