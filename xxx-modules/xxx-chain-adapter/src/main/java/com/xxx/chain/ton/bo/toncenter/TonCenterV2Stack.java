package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * <pre>
 *  "stack": [
 *    [
 *      "num",
 *      "0x1727081ba551172"
 *    ],
 *    [
 *      "num",
 *      "0x2703855b53e"
 *    ],
 *    [
 *      "cell",
 *      {
 *        "bytes": "te6cckEBAQEAJAAAQ4AE+j5HqExJ7kfoVXXjpH34XMdL2PYcJcqXYE13whqExhCie9Ca",
 *        "object": {
 *          "data": {
 *            "b64": "gAT6PkeoTEnuR+hVdeOkffhcx0vY9hwlypdgTXfCGoTGAA==",
 *            "len": 267
 *          },
 *          "refs": [],
 *          "special": false
 *        }
 *      }
 *    ],
 *    [
 *      "cell",
 *      {
 *        "bytes": "te6cckEBAQEAJAAAQ4AJ3ZJDc6mq1BsozsAtqThNZzY68gNPwqfMwGfijUEQ3pApJ6pn",
 *        "object": {
 *          "data": {
 *            "b64": "gAndkkNzqarUGyjOwC2pOE1nNjryA0/Cp8zAZ+KNQRDegA==",
 *            "len": 267
 *          },
 *          "refs": [],
 *          "special": false
 *        }
 *      }
 *    ],
 *    [
 *      "num",
 *      "0x46"
 *    ],
 *    [
 *      "num",
 *      "0x1e"
 *    ],
 *    [
 *      "num",
 *      "0x1e"
 *    ],
 *    [
 *      "cell",
 *      {
 *        "bytes": "te6cckEBAQEAJAAAQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBK7IKd",
 *        "object": {
 *          "data": {
 *            "b64": "gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==",
 *            "len": 267
 *          },
 *          "refs": [],
 *          "special": false
 *        }
 *      }
 *    ],
 *    [
 *      "num",
 *      "0x152d7aa074dbfa"
 *    ],
 *    [
 *      "num",
 *      "0xce420ed1b"
 *    ]
 *  ]
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV2Stack implements Serializable {

}