package com.xxx.chain.ton.bo.toncenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 *   "result": {
 *     "consensus_block": 19726430,
 *     "timestamp": 1717124043.111841
 *
 *     "@type": "smc.runResult",
 *     "gas_used": 2132,
 *     "stack": [
 *       [
 *         "num",
 *         "0x1727081ba551172"
 *       ],
 *       [
 *         "num",
 *         "0x2703855b53e"
 *       ],
 *       [
 *         "cell",
 *         {
 *           "bytes": "te6cckEBAQEAJAAAQ4AE+j5HqExJ7kfoVXXjpH34XMdL2PYcJcqXYE13whqExhCie9Ca",
 *           "object": {
 *             "data": {
 *               "b64": "gAT6PkeoTEnuR+hVdeOkffhcx0vY9hwlypdgTXfCGoTGAA==",
 *               "len": 267
 *             },
 *             "refs": [],
 *             "special": false
 *           }
 *         }
 *       ],
 *       [
 *         "cell",
 *         {
 *           "bytes": "te6cckEBAQEAJAAAQ4AJ3ZJDc6mq1BsozsAtqThNZzY68gNPwqfMwGfijUEQ3pApJ6pn",
 *           "object": {
 *             "data": {
 *               "b64": "gAndkkNzqarUGyjOwC2pOE1nNjryA0/Cp8zAZ+KNQRDegA==",
 *               "len": 267
 *             },
 *             "refs": [],
 *             "special": false
 *           }
 *         }
 *       ],
 *       [
 *         "num",
 *         "0x46"
 *       ],
 *       [
 *         "num",
 *         "0x1e"
 *       ],
 *       [
 *         "num",
 *         "0x1e"
 *       ],
 *       [
 *         "cell",
 *         {
 *           "bytes": "te6cckEBAQEAJAAAQ4AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBK7IKd",
 *           "object": {
 *             "data": {
 *               "b64": "gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==",
 *               "len": 267
 *             },
 *             "refs": [],
 *             "special": false
 *           }
 *         }
 *       ],
 *       [
 *         "num",
 *         "0x152d7aa074dbfa"
 *       ],
 *       [
 *         "num",
 *         "0xce420ed1b"
 *       ]
 *     ],
 *     "exit_code": 0,
 *     "@extra": "1717491737.23206:7:0.6858903195729591"
 *   }
 * </pre>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TonCenterV2Result implements Serializable {

    @JsonProperty("@type")
    private String type;

    @JsonProperty("gas_used")
    private Long gasUsed;

    @JsonProperty("exit_code")
    private Long exitCode;

    @JsonProperty("consensus_block")
    private Long consensusBlock;

    private BigDecimal timestamp;

    private List<TonCenterV2Stack> stack;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(Long gasUsed) {
        this.gasUsed = gasUsed;
    }

    public Long getExitCode() {
        return exitCode;
    }

    public void setExitCode(Long exitCode) {
        this.exitCode = exitCode;
    }

    public Long getConsensusBlock() {
        return consensusBlock;
    }

    public void setConsensusBlock(Long consensusBlock) {
        this.consensusBlock = consensusBlock;
    }

    public BigDecimal getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(BigDecimal timestamp) {
        this.timestamp = timestamp;
    }

    public List<TonCenterV2Stack> getStack() {
        return stack;
    }

    public void setStack(List<TonCenterV2Stack> stack) {
        this.stack = stack;
    }

}