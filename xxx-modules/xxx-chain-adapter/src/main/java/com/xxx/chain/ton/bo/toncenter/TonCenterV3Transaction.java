package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * <pre>
 *  {
 *    "account": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *    "hash": "pMfiVnQ7vDxMivBMVpS7s+X8/2jnPNrY/4DupIXTQWs=",
 *    "lt": "22161219000001",
 *    "now": 1717046274,
 *    "orig_status": "nonexist",
 *    "end_status": "uninit",
 *    "total_fees": "0",
 *    "prev_trans_hash": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
 *    "prev_trans_lt": "0",
 *    "description": {
 *      "type": "ord",
 *      "aborted": true,
 *      "credit_ph": {
 *        "credit": "1000000000"
 *      },
 *      "destroyed": false,
 *      "compute_ph": {
 *        "type": "skipped",
 *        "skip_reason": "no_state"
 *      },
 *      "storage_ph": {
 *        "status_change": "unchanged",
 *        "storage_fees_collected": "0"
 *      },
 *      "credit_first": true
 *    },
 *    "block_ref": {
 *      "workchain": 0,
 *      "shard": "2000000000000000",
 *      "seqno": 21160665
 *    },
 *    "in_msg": {
 *      "hash": "Q8YfcZlbUHYxEH9fspWbObXjHjQP0fgAAHIBuqqkxCM=",
 *      "source": "0:E1CB4616EDBDD7F663F295CF611FC82917839CD2A3775CBC2194DF43BAF6601A",
 *      "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *      "value": "1000000000",
 *      "fwd_fee": "266669",
 *      "ihr_fee": "0",
 *      "created_lt": "22161216000002",
 *      "created_at": "1717046266",
 *      "opcode": "0x00000000",
 *      "ihr_disabled": true,
 *      "bounce": false,
 *      "bounced": false,
 *      "import_fee": null,
 *      "message_content": {
 *        "hash": "9JEOs0WWtr+Mr8hQOCnU+1TQ5UwlYNwwnubB51ggQG8=",
 *        "body": "te6cckEBAQEAEAAAHAAAAAAxOTg1NTk3NjUxSmLi/w==",
 *        "decoded": {
 *          "type": "text_comment",
 *          "comment": "1985597651"
 *        }
 *      },
 *      "init_state": null
 *    },
 *    "out_msgs": [],
 *    "account_state_before": {
 *      "hash": "kK7Illr6uxbrw8ubQI665xthjXh4i8gNCYQ1k8rJjaQ=",
 *      "balance": null,
 *      "account_status": null,
 *      "frozen_hash": null,
 *      "code_hash": null,
 *      "data_hash": null
 *    },
 *    "account_state_after": {
 *      "hash": "1cG4vriEhDJN3fGlrhR52vuLZhAJq4jsLhxisPx36ys=",
 *      "balance": "1000000000",
 *      "account_status": "uninit",
 *      "frozen_hash": null,
 *      "code_hash": null,
 *      "data_hash": null
 *    },
 *    "mc_block_seqno": 19702476
 *  }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Transaction implements Serializable {

    private String hash;

    private String lt;

    private Long now;

    private TonCenterV3Description description;

    @JsonProperty("block_ref")
    private TonCenterV3BlockRef blockRef;

    @JsonProperty("in_msg")
    private TonCenterV3InMsg inMsg;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getLt() {
        return lt;
    }

    public void setLt(String lt) {
        this.lt = lt;
    }

    public Long getNow() {
        return now;
    }

    public void setNow(Long now) {
        this.now = now;
    }

    public TonCenterV3Description getDescription() {
        return description;
    }

    public void setDescription(TonCenterV3Description description) {
        this.description = description;
    }

    public TonCenterV3BlockRef getBlockRef() {
        return blockRef;
    }

    public void setBlockRef(TonCenterV3BlockRef blockRef) {
        this.blockRef = blockRef;
    }

    public TonCenterV3InMsg getInMsg() {
        return inMsg;
    }

    public void setInMsg(TonCenterV3InMsg inMsg) {
        this.inMsg = inMsg;
    }

}