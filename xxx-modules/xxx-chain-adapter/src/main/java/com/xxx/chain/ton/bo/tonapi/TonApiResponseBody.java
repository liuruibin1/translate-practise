package com.xxx.chain.ton.bo.tonapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *      https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events?initiator=false&subject_only=false&limit=20&start_date=1720327670&end_date=1720327693
 *      {
 *        "events": [
 *          {
 *            "event_id": "213384867b4da9abd9065048fd9f532b61ae9b427f8e835d8366a236c7b2084b",
 *            "account": {
 *              "address": "0:44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3",
 *              "is_scam": false,
 *              "is_wallet": true
 *            },
 *            "timestamp": 1720327677,
 *            "actions": [
 *              {
 *                "type": "TonTransfer",
 *                "status": "ok",
 *                "TonTransfer": {
 *                  "sender": {
 *                    "address": "0:97ad93444915089e812238ff10abe9066d0b03ea3dba2a8630fb9c9f88aa455c",
 *                    "is_scam": false,
 *                    "is_wallet": true
 *                  },
 *                  "recipient": {
 *                    "address": "0:44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3",
 *                    "is_scam": false,
 *                    "is_wallet": true
 *                  },
 *                  "amount": 100000000,
 *                  "comment": "6633025775"
 *                },
 *                "simple_preview": {
 *                  "name": "Ton Transfer",
 *                  "description": "Перевод 0.1 TON",
 *                  "value": "0.1 TON",
 *                  "accounts": [
 *                    {
 *                      "address": "0:97ad93444915089e812238ff10abe9066d0b03ea3dba2a8630fb9c9f88aa455c",
 *                      "is_scam": false,
 *                      "is_wallet": true
 *                    }
 *                  ]
 *                },
 *                "base_transactions": [
 *                  "8b478c05edf712acd000dc2c382853860d4d8a466fc218e61acbcd9501f38ffd"
 *                ]
 *              }
 *            ],
 *            "is_scam": false,
 *            "lt": 47587000000001,
 *            "in_progress": false,
 *            "extra": -426219
 *          }
 *        ],
 *        "next_from": 47587000000001
 *      }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonApiResponseBody implements Serializable {


    private List<TonApiEvent> events;

    private TonApiLast last;

    public List<TonApiEvent> getEvents() {
        return this.events;
    }

    public void setEvents(List<TonApiEvent> events) {
        this.events = events;
    }

    public TonApiLast getLast() {
        return this.last;
    }

    public void setLast(TonApiLast last) {
        this.last = last;
    }

}