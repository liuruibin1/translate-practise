package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 * {
 *   "ok": true,
 *   "result": {
 *     "consensus_block": 19726430,
 *     "timestamp": 1717124043.111841
 *   }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV2GetConsensusBlock implements Serializable {

    private Boolean ok;

    private TonCenterV2Result result;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public TonCenterV2Result getResult() {
        return result;
    }

    public void setResult(TonCenterV2Result result) {
        this.result = result;
    }

}