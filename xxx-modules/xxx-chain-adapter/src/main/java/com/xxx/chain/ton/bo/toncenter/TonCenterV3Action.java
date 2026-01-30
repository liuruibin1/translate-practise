package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * <pre>
 *  "action": {
 *    "valid": true,
 *    "success": true,
 *    "no_funds": false,
 *    "result_code": 0,
 *    "tot_actions": 1,
 *    "msgs_created": 1,
 *    "spec_actions": 0,
 *    "tot_msg_size": {
 *      "bits": "2316",
 *      "cells": "6"
 *    },
 *    "status_change": "unchanged",
 *    "total_fwd_fees": "1244000",
 *    "skipped_actions": 0,
 *    "action_list_hash": "jNHw/DPh/1cSVWfrWK8RIcDw1pBy2b9sUs9Vm9TEsog=",
 *    "total_action_fees": "414660"
 *  },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Action implements Serializable {

    private Boolean valid;

    private Boolean success;

    @JsonProperty("result_code")
    private Integer resultCode;

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

}