package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <pre>
 *  "in_msg": {
 *    "hash": "Q8YfcZlbUHYxEH9fspWbObXjHjQP0fgAAHIBuqqkxCM=",
 *    "source": "0:E1CB4616EDBDD7F663F295CF611FC82917839CD2A3775CBC2194DF43BAF6601A",
 *    "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *    "value": "1000000000",
 *    "fwd_fee": "266669",
 *    "ihr_fee": "0",
 *    "created_lt": "22161216000002",
 *    "created_at": "1717046266",
 *    "opcode": "0x00000000",
 *    "ihr_disabled": true,
 *    "bounce": false,
 *    "bounced": false,
 *    "import_fee": null,
 *    "message_content": {
 *      "hash": "9JEOs0WWtr+Mr8hQOCnU+1TQ5UwlYNwwnubB51ggQG8=",
 *      "body": "te6cckEBAQEAEAAAHAAAAAAxOTg1NTk3NjUxSmLi/w==",
 *      "decoded": {
 *        "type": "text_comment",
 *        "comment": "1985597651"
 *      }
 *    },
 *    "init_state": null
 *  },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3InMsg implements Serializable {

    private String hash;

    private String source;

    private String destination;

    private String value;

    private String opcode;

    @JsonProperty("message_content")
    private TonCenterV3MessageContent messageContent;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public TonCenterV3MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(TonCenterV3MessageContent messageContent) {
        this.messageContent = messageContent;
    }
}