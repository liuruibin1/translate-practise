package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * {
 *   "blocks": [
 *     {
 *       "workchain": 0,
 *       "shard": "2000000000000000",
 *       "seqno": 21188229,
 *       "root_hash": "nGN9Fws0RhLR2nDGDgpM4t+xLrE8gASQH5+5Nlk1vmg=",
 *       "file_hash": "GqR4TxmvNN+vAzPMpuU54WZXoTBNPIrDd/Bc0VDRlLU=",
 *       "global_id": -3,
 *       "version": 0,
 *       "after_merge": false,
 *       "before_split": false,
 *       "after_split": false,
 *       "want_merge": true,
 *       "want_split": false,
 *       "key_block": false,
 *       "vert_seqno_incr": false,
 *       "flags": 1,
 *       "gen_utime": "1717130279",
 *       "start_lt": "22190293000000",
 *       "end_lt": "22190293000001",
 *       "validator_list_hash_short": 398799315,
 *       "gen_catchain_seqno": 272437,
 *       "min_ref_mc_seqno": 19728372,
 *       "prev_key_block_seqno": 19728026,
 *       "vert_seqno": 0,
 *       "master_ref_seqno": 19728372,
 *       "rand_seed": "cz03qgwE327iPlUE56Hr+JnC0O8Hawodv02mMmUaBKE=",
 *       "created_by": "tR42SfEfCaVUK/YbJMMkhwAeANz+0fLAvnzy87RlOyo=",
 *       "tx_count": 0,
 *       "masterchain_block_ref": {
 *         "workchain": -1,
 *         "shard": "8000000000000000",
 *         "seqno": 19728375
 *       },
 *       "prev_blocks": [
 *         {
 *           "workchain": 0,
 *           "shard": "2000000000000000",
 *           "seqno": 21188228
 *         }
 *       ]
 *     },
 *     {
 *       "workchain": 0,
 *       "shard": "2000000000000000",
 *       "seqno": 21188228,
 *       "root_hash": "yJNqSNJTqBr4PPCoRZ/YzIJPp839r7cO1a/VNFdKlRA=",
 *       "file_hash": "ixoJgfTPnMO5l0/uCMGv0iU6cpxbJgwm3RiJfjC5CBM=",
 *       "global_id": -3,
 *       "version": 0,
 *       "after_merge": false,
 *       "before_split": false,
 *       "after_split": false,
 *       "want_merge": true,
 *       "want_split": false,
 *       "key_block": false,
 *       "vert_seqno_incr": false,
 *       "flags": 1,
 *       "gen_utime": "1717130276",
 *       "start_lt": "22190292000000",
 *       "end_lt": "22190292000001",
 *       "validator_list_hash_short": 398799315,
 *       "gen_catchain_seqno": 272437,
 *       "min_ref_mc_seqno": 19728371,
 *       "prev_key_block_seqno": 19728026,
 *       "vert_seqno": 0,
 *       "master_ref_seqno": 19728371,
 *       "rand_seed": "jriXyEFJEsqSUV6Ld+qkkpjC7RNTS1V8NE1iiq1lAJk=",
 *       "created_by": "YvVYSdnFuYHqqIn4t89QdZnANDy40MNKhII8vywIR20=",
 *       "tx_count": 0,
 *       "masterchain_block_ref": {
 *         "workchain": -1,
 *         "shard": "8000000000000000",
 *         "seqno": 19728374
 *       },
 *       "prev_blocks": [
 *         {
 *           "workchain": 0,
 *           "shard": "2000000000000000",
 *           "seqno": 21188227
 *         }
 *       ]
 *     }
 *   ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Blocks implements Serializable {

    private List<TonCenterV3Block> blocks;

    public List<TonCenterV3Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<TonCenterV3Block> blocks) {
        this.blocks = blocks;
    }

}