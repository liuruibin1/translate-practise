package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 * "message_content": {
 *   "hash": "9JEOs0WWtr+Mr8hQOCnU+1TQ5UwlYNwwnubB51ggQG8=",
 *   "body": "te6cckEBAQEAEAAAHAAAAAAxOTg1NTk3NjUxSmLi/w==",
 *   "decoded": {
 *     "type": "text_comment",
 *     "comment": "1985597651"
 *   }
 * },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3MessageContent implements Serializable {

    private String hash;

    private String body;

    private TonCenterV3Decoded decoded;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public TonCenterV3Decoded getDecoded() {
        return decoded;
    }

    public void setDecoded(TonCenterV3Decoded decoded) {
        this.decoded = decoded;
    }

}