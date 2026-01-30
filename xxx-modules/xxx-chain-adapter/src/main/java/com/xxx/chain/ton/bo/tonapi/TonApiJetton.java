package com.xxx.chain.ton.bo.tonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 *      https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events?initiator=false&subject_only=false&limit=20&start_date=1720539071&end_date=1720539110
 *      "jetton": {
 *        "address": "0:b113a994b5024a16719f69139328eb759596c38a25f59028b146fecdc3621dfe",
 *        "name": "Tether USD",
 *        "symbol": "USD₮",
 *        "decimals": 6,
 *        "image": "https://cache.tonapi.io/imgproxy/T3PB4s7oprNVaJkwqbGg54nexKE0zzKhcrPv8jcWYzU/rs:fill:200:200:1/g:no/aHR0cHM6Ly90ZXRoZXIudG8vaW1hZ2VzL2xvZ29DaXJjbGUucG5n.webp",
 *        "verification": "whitelist"
 *      }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonApiJetton implements Serializable {

    private String address;

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}