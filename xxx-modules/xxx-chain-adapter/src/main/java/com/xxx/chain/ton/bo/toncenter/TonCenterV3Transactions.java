package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *  {
 *   "transactions": [
 *     {
 *       "account": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *       "hash": "O72pZ970c3Y9ILcPPNQ+pryo4lFzc3XmhuVdNmoF6mU=",
 *       "lt": "22164816000001",
 *       "now": 1717058024,
 *       "orig_status": "uninit",
 *       "end_status": "uninit",
 *       "total_fees": "109",
 *       "prev_trans_hash": "pMfiVnQ7vDxMivBMVpS7s+X8/2jnPNrY/4DupIXTQWs=",
 *       "prev_trans_lt": "22161219000001",
 *       "description": {
 *         "type": "ord",
 *         "aborted": true,
 *         "credit_ph": {
 *           "credit": "1"
 *         },
 *         "destroyed": false,
 *         "compute_ph": {
 *           "type": "skipped",
 *           "skip_reason": "no_gas"
 *         },
 *         "storage_ph": {
 *           "status_change": "unchanged",
 *           "storage_fees_collected": "109"
 *         },
 *         "credit_first": true
 *       },
 *       "block_ref": {
 *         "workchain": 0,
 *         "shard": "2000000000000000",
 *         "seqno": 21163829
 *       },
 *       "in_msg": {
 *         "hash": "FifQ8pIMn5P9nTFOpnz0680LI5e5/2AonoX9xXow0fo=",
 *         "source": "0:C2026C7E6748ACE187AD104FE58D9CF008A01C3B148A2EC79BA75AF573D18B72",
 *         "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *         "value": "1",
 *         "fwd_fee": "458671",
 *         "ihr_fee": "0",
 *         "created_lt": "22164811000002",
 *         "created_at": "1717058009",
 *         "opcode": "0x7362d09c",
 *         "ihr_disabled": true,
 *         "bounce": false,
 *         "bounced": false,
 *         "import_fee": null,
 *         "message_content": {
 *           "hash": "8Deq5qgrXQEz86gPJCfTqhNlwMaksAWg0SgmDIpIfzE=",
 *           "body": "te6cckEBAgEARgABZnNi0JxUbeTvArii21GdgdlgCAHDlowt23uv7MflK57CP5BSLwc5pUbuuXhDKb6HdezANQEAHAAAAAAxOTg1NTk3NjUx3hELmw==",
 *           "decoded": null
 *         },
 *         "init_state": null
 *       },
 *       "out_msgs": [],
 *       "account_state_before": {
 *         "hash": "1cG4vriEhDJN3fGlrhR52vuLZhAJq4jsLhxisPx36ys=",
 *         "balance": "1000000000",
 *         "account_status": "uninit",
 *         "frozen_hash": null,
 *         "code_hash": null,
 *         "data_hash": null
 *       },
 *       "account_state_after": {
 *         "hash": "0xtRu+DDgAUZxqvxVUSfkIGYjV4Vq8gSdH/1yQh6kR0=",
 *         "balance": "999999892",
 *         "account_status": "uninit",
 *         "frozen_hash": null,
 *         "code_hash": null,
 *         "data_hash": null
 *       },
 *       "mc_block_seqno": 19705707
 *     },
 *     {
 *       "account": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *       "hash": "pMfiVnQ7vDxMivBMVpS7s+X8/2jnPNrY/4DupIXTQWs=",
 *       "lt": "22161219000001",
 *       "now": 1717046274,
 *       "orig_status": "nonexist",
 *       "end_status": "uninit",
 *       "total_fees": "0",
 *       "prev_trans_hash": "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=",
 *       "prev_trans_lt": "0",
 *       "description": {
 *         "type": "ord",
 *         "aborted": true,
 *         "credit_ph": {
 *           "credit": "1000000000"
 *         },
 *         "destroyed": false,
 *         "compute_ph": {
 *           "type": "skipped",
 *           "skip_reason": "no_state"
 *         },
 *         "storage_ph": {
 *           "status_change": "unchanged",
 *           "storage_fees_collected": "0"
 *         },
 *         "credit_first": true
 *       },
 *       "block_ref": {
 *         "workchain": 0,
 *         "shard": "2000000000000000",
 *         "seqno": 21160665
 *       },
 *       "in_msg": {
 *         "hash": "Q8YfcZlbUHYxEH9fspWbObXjHjQP0fgAAHIBuqqkxCM=",
 *         "source": "0:E1CB4616EDBDD7F663F295CF611FC82917839CD2A3775CBC2194DF43BAF6601A",
 *         "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *         "value": "1000000000",
 *         "fwd_fee": "266669",
 *         "ihr_fee": "0",
 *         "created_lt": "22161216000002",
 *         "created_at": "1717046266",
 *         "opcode": "0x00000000",
 *         "ihr_disabled": true,
 *         "bounce": false,
 *         "bounced": false,
 *         "import_fee": null,
 *         "message_content": {
 *           "hash": "9JEOs0WWtr+Mr8hQOCnU+1TQ5UwlYNwwnubB51ggQG8=",
 *           "body": "te6cckEBAQEAEAAAHAAAAAAxOTg1NTk3NjUxSmLi/w==",
 *           "decoded": {
 *             "type": "text_comment",
 *             "comment": "1985597651"
 *           }
 *         },
 *         "init_state": null
 *       },
 *       "out_msgs": [],
 *       "account_state_before": {
 *         "hash": "kK7Illr6uxbrw8ubQI665xthjXh4i8gNCYQ1k8rJjaQ=",
 *         "balance": null,
 *         "account_status": null,
 *         "frozen_hash": null,
 *         "code_hash": null,
 *         "data_hash": null
 *       },
 *       "account_state_after": {
 *         "hash": "1cG4vriEhDJN3fGlrhR52vuLZhAJq4jsLhxisPx36ys=",
 *         "balance": "1000000000",
 *         "account_status": "uninit",
 *         "frozen_hash": null,
 *         "code_hash": null,
 *         "data_hash": null
 *       },
 *       "mc_block_seqno": 19702476
 *     }
 *   ],
 *   "address_book": {
 *     "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16": {
 *       "user_friendly": "0QAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFgcx"
 *     },
 *     "0:C2026C7E6748ACE187AD104FE58D9CF008A01C3B148A2EC79BA75AF573D18B72": {
 *       "user_friendly": "kQDCAmx-Z0is4YetEE_ljZzwCKAcOxSKLsebp1r1c9GLcgJo"
 *     },
 *     "0:E1CB4616EDBDD7F663F295CF611FC82917839CD2A3775CBC2194DF43BAF6601A": {
 *       "user_friendly": "0QDhy0YW7b3X9mPylc9hH8gpF4Oc0qN3XLwhlN9DuvZgGoQO"
 *     }
 *   }
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3Transactions implements Serializable {

    private List<TonCenterV3Transaction> transactions;

    public List<TonCenterV3Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TonCenterV3Transaction> transactions) {
        this.transactions = transactions;
    }

}