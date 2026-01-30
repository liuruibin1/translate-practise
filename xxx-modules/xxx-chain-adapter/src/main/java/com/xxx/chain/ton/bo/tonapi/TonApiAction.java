package com.xxx.chain.ton.bo.tonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <pre>
 *      https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events?initiator=false&subject_only=false&limit=20&start_date=1720327670&end_date=1720327693
 *      "actions": [
 *        {
 *          "type": "TonTransfer",
 *          "status": "ok",
 *          "TonTransfer": {
 *            "sender": {
 *              "address": "0:97ad93444915089e812238ff10abe9066d0b03ea3dba2a8630fb9c9f88aa455c",
 *              "is_scam": false,
 *              "is_wallet": true
 *            },
 *            "recipient": {
 *              "address": "0:44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3",
 *              "is_scam": false,
 *              "is_wallet": true
 *            },
 *            "amount": 100000000,
 *            "comment": "6633025775"
 *          },
 *          "simple_preview": {
 *            "name": "Ton Transfer",
 *            "description": "Перевод 0.1 TON",
 *            "value": "0.1 TON",
 *            "accounts": [
 *              {
 *                "address": "0:97ad93444915089e812238ff10abe9066d0b03ea3dba2a8630fb9c9f88aa455c",
 *                "is_scam": false,
 *                "is_wallet": true
 *              }
 *            ]
 *          },
 *          "base_transactions": [
 *            "8b478c05edf712acd000dc2c382853860d4d8a466fc218e61acbcd9501f38ffd"
 *          ]
 *        }
 *      ],
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonApiAction implements Serializable {

    private String type;

    private String status;

    @JsonProperty("TonTransfer")
    private TonApiTonTransfer tonTransfer;

    @JsonProperty("JettonTransfer")
    private TonApiJettonTransfer jettonTransfer;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TonApiTonTransfer getTonTransfer() {
        return this.tonTransfer;
    }

    public void setTonTransfer(TonApiTonTransfer tonTransfer) {
        this.tonTransfer = tonTransfer;
    }

    public TonApiJettonTransfer getJettonTransfer() {
        return this.jettonTransfer;
    }

    public void setJettonTransfer(TonApiJettonTransfer jettonTransfer) {
        this.jettonTransfer = jettonTransfer;
    }

}