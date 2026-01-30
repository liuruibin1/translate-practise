package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 *  [
 *    "cell",
 *    {
 *      "bytes": "te6cckEBAQEAJAAAQ4AE+j5HqExJ7kfoVXXjpH34XMdL2PYcJcqXYE13whqExhCie9Ca",
 *      "object": {
 *        "data": {
 *          "b64": "gAT6PkeoTEnuR+hVdeOkffhcx0vY9hwlypdgTXfCGoTGAA==",
 *          "len": 267
 *        },
 *        "refs": [],
 *        "special": false
 *      }
 *    }
 *  ],
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV2StackCell implements Serializable {


}