package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * <pre>
 *  {
 *    "workchain": 0,
 *    "shard": "2000000000000000",
 *    "seqno": 21188229,
 *    "root_hash": "nGN9Fws0RhLR2nDGDgpM4t+xLrE8gASQH5+5Nlk1vmg=",
 *    "file_hash": "GqR4TxmvNN+vAzPMpuU54WZXoTBNPIrDd/Bc0VDRlLU=",
 *    "global_id": -3,
 *    "version": 0,
 *    "after_merge": false,
 *    "before_split": false,
 *    "after_split": false,
 *    "want_merge": true,
 *    "want_split": false,
 *    "key_block": false,
 *    "vert_seqno_incr": false,
 *    "flags": 1,
 *    "gen_utime": "1717130279",
 *    "start_lt": "22190293000000",
 *    "end_lt": "22190293000001",
 *    "validator_list_hash_short": 398799315,
 *    "gen_catchain_seqno": 272437,
 *    "min_ref_mc_seqno": 19728372,
 *    "prev_key_block_seqno": 19728026,
 *    "vert_seqno": 0,
 *    "master_ref_seqno": 19728372,
 *    "rand_seed": "cz03qgwE327iPlUE56Hr+JnC0O8Hawodv02mMmUaBKE=",
 *    "created_by": "tR42SfEfCaVUK/YbJMMkhwAeANz+0fLAvnzy87RlOyo=",
 *    "tx_count": 0,
 *    "masterchain_block_ref": {
 *      "workchain": -1,
 *      "shard": "8000000000000000",
 *      "seqno": 19728375
 *    },
 *    "prev_blocks": [
 *      {
 *        "workchain": 0,
 *        "shard": "2000000000000000",
 *        "seqno": 21188228
 *      }
 *    ]
 *  },
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Block implements Serializable {

    private Long workchain;

    private String shard;

    private Long seqno;

    @JsonProperty("root_hash")
    private String rootHash;

    @JsonProperty("gen_utime")
    private String genUtime;

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

    public String getRootHash() {
        return rootHash;
    }

    public void setRootHash(String rootHash) {
        this.rootHash = rootHash;
    }

    public String getGenUtime() {
        return genUtime;
    }

    public void setGenUtime(String genUtime) {
        this.genUtime = genUtime;
    }

}