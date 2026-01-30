package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * {
 *   "jetton_transfers": [
 *     {
 *       "query_id": "2632245",
 *       "source": "0:1F5806C73D03BBC96EB7EEBF16067F1F2E8E270CFA16D57001AA9EAF7456F008",
 *       "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *       "amount": "17000000",
 *       "source_wallet": "0:B7F5CD75F68A8F91A07E7E203CFC429F7A33F6E3F232282344C8A154B2F5EC7C",
 *       "jetton_master": "0:B113A994B5024A16719F69139328EB759596C38A25F59028B146FECDC3621DFE",
 *       "transaction_hash": "exkKjJjzWuZCh8zER2b8dVcSJRGR/LBQeG+YeyMfeOM=",
 *       "transaction_lt": "46760680000003",
 *       "transaction_now": 1716867695,
 *       "response_destination": "0:1F5806C73D03BBC96EB7EEBF16067F1F2E8E270CFA16D57001AA9EAF7456F008",
 *       "custom_payload": null,
 *       "forward_ton_amount": "1",
 *       "forward_payload": null
 *     },
 *     {
 *       "query_id": "2632140",
 *       "source": "0:1F5806C73D03BBC96EB7EEBF16067F1F2E8E270CFA16D57001AA9EAF7456F008",
 *       "destination": "0:169C9367C01C8B4E1471962DB32E99E3E4473AAFB47E1A6A643A4177A6758F16",
 *       "amount": "10000000",
 *       "source_wallet": "0:B7F5CD75F68A8F91A07E7E203CFC429F7A33F6E3F232282344C8A154B2F5EC7C",
 *       "jetton_master": "0:B113A994B5024A16719F69139328EB759596C38A25F59028B146FECDC3621DFE",
 *       "transaction_hash": "CtfOmq2/OCSwyUObxsnvixxO/GJu5/qhG5jDEEC3NW4=",
 *       "transaction_lt": "46760679000001",
 *       "transaction_now": 1716867691,
 *       "response_destination": "0:1F5806C73D03BBC96EB7EEBF16067F1F2E8E270CFA16D57001AA9EAF7456F008",
 *       "custom_payload": null,
 *       "forward_ton_amount": "1",
 *       "forward_payload": null
 *     }
 *   ]
 * }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV3JettonTransfers implements Serializable {

    @JsonProperty("jetton_transfers")
    private List<TonCenterV3JettonTransfer> jettonTransfers;

    public List<TonCenterV3JettonTransfer> getJettonTransfers() {
        return jettonTransfers;
    }

    public void setJettonTransfers(List<TonCenterV3JettonTransfer> jettonTransfers) {
        this.jettonTransfers = jettonTransfers;
    }

}