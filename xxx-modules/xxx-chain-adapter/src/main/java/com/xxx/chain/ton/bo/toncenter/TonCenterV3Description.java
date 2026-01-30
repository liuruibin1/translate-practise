package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * <pre>
 *  "description": {
 *      "type": "ord",
 *      "action": {
 *        "valid": true,
 *        "success": true,
 *        "no_funds": false,
 *        "result_code": 0,
 *        "tot_actions": 1,
 *        "msgs_created": 1,
 *        "spec_actions": 0,
 *        "tot_msg_size": {
 *          "bits": "2316",
 *          "cells": "6"
 *        },
 *        "status_change": "unchanged",
 *        "total_fwd_fees": "1244000",
 *        "skipped_actions": 0,
 *        "action_list_hash": "jNHw/DPh/1cSVWfrWK8RIcDw1pBy2b9sUs9Vm9TEsog=",
 *        "total_action_fees": "414660"
 *      },
 *      "aborted": false,
 *      "credit_ph": {
 *        "credit": "69690355"
 *      },
 *      "destroyed": false,
 *      "compute_ph": {
 *        "mode": 0,
 *        "type": "vm",
 *        "success": true,
 *        "gas_fees": "3606800",
 *        "gas_used": "9017",
 *        "vm_steps": 216,
 *        "exit_code": 0,
 *        "gas_limit": "174225",
 *        "msg_state_used": false,
 *        "account_activated": false,
 *        "vm_init_state_hash": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
 *        "vm_final_state_hash": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
 *      },
 *      "storage_ph": {
 *        "status_change": "unchanged",
 *        "storage_fees_collected": "16654"
 *      },
 *      "credit_first": false
 *  },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Description implements Serializable {

    private String hash;

    private String lt;

    private Long now;

    private Boolean aborted;

    private Boolean destroyed;

    private TonCenterV3Action action;

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

    public Boolean getAborted() {
        return aborted;
    }

    public void setAborted(Boolean aborted) {
        this.aborted = aborted;
    }

    public Boolean getDestroyed() {
        return destroyed;
    }

    public void setDestroyed(Boolean destroyed) {
        this.destroyed = destroyed;
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

    public TonCenterV3Action getAction() {
        return action;
    }

    public void setAction(TonCenterV3Action action) {
        this.action = action;
    }

}