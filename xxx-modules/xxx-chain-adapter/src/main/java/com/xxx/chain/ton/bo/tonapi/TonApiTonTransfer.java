package com.xxx.chain.ton.bo.tonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * <pre>
 *      https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events?initiator=false&subject_only=false&limit=20&start_date=1720327670&end_date=1720327693
 *      "TonTransfer": {
 *        "sender": {
 *          "address": "0:97ad93444915089e812238ff10abe9066d0b03ea3dba2a8630fb9c9f88aa455c",
 *          "is_scam": false,
 *          "is_wallet": true
 *        },
 *        "recipient": {
 *          "address": "0:44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3",
 *          "is_scam": false,
 *          "is_wallet": true
 *        },
 *        "amount": 100000000,
 *        "comment": "6633025775"
 *      },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonApiTonTransfer implements Serializable {

    private TonApiRecipient sender;

    private TonApiSender recipient;

    private BigInteger amount;

    private String comment;

    public TonApiRecipient getSender() {
        return this.sender;
    }

    public void setSender(TonApiRecipient sender) {
        this.sender = sender;
    }

    public TonApiSender getRecipient() {
        return this.recipient;
    }

    public void setRecipient(TonApiSender recipient) {
        this.recipient = recipient;
    }

    public BigInteger getAmount() {
        return this.amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}