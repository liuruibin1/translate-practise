package com.xxx.chain.ton.bo.tonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 *      https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events?initiator=false&subject_only=false&limit=20&start_date=1720539071&end_date=1720539110
 *      "JettonTransfer": {
 *        "sender": {
 *          "address": "0:8e91b50381c36dd42accdb51f5cb5d96848568b4924f8079b1a5ae7e10f43eda",
 *          "name": "Binance Hot Wallet",
 *          "is_scam": false,
 *          "is_wallet": true
 *        },
 *        "recipient": {
 *          "address": "0:44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3",
 *          "is_scam": false,
 *          "is_wallet": true
 *        },
 *        "senders_wallet": "0:10b6023187340b62396b39188be0b5cc253ac0aa161f30e2160e7c4cffdbc525",
 *        "recipients_wallet": "0:6ee23f08ade5a3aae63c56726ed309fca8705b4ae776353fbfda6f7e2272d810",
 *        "amount": "2000000",
 *        "comment": "6161424224",
 *        "jetton": {
 *          "address": "0:b113a994b5024a16719f69139328eb759596c38a25f59028b146fecdc3621dfe",
 *          "name": "Tether USD",
 *          "symbol": "USD₮",
 *          "decimals": 6,
 *          "image": "https://cache.tonapi.io/imgproxy/T3PB4s7oprNVaJkwqbGg54nexKE0zzKhcrPv8jcWYzU/rs:fill:200:200:1/g:no/aHR0cHM6Ly90ZXRoZXIudG8vaW1hZ2VzL2xvZ29DaXJjbGUucG5n.webp",
 *          "verification": "whitelist"
 *        }
 *      },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonApiJettonTransfer implements Serializable {

    private TonApiRecipient sender;

    private TonApiSender recipient;

    private String amount;

    private String comment;

    private TonApiJetton jetton;

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

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TonApiJetton getJetton() {
        return this.jetton;
    }

    public void setJetton(TonApiJetton jetton) {
        this.jetton = jetton;
    }

}