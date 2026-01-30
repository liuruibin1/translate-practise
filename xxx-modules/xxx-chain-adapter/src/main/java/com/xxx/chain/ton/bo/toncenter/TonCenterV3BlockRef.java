package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 *      "block_ref": {
 *        "workchain": 0,
 *        "shard": "2000000000000000",
 *        "seqno": 21166920
 *      },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3BlockRef implements Serializable {

    private Long workchain;

    private String shard;

    private Long seqno;

    public Long getWorkchain() {
        return workchain;
    }

    public void setWorkchain(Long workchain) {
        this.workchain = workchain;
    }

    public String getShard() {
        return shard;
    }

    public void setShard(String shard) {
        this.shard = shard;
    }

    public Long getSeqno() {
        return seqno;
    }

    public void setSeqno(Long seqno) {
        this.seqno = seqno;
    }

}