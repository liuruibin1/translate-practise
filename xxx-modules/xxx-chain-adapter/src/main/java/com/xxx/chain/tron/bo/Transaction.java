package com.xxx.chain.tron.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction extends Base {

    private String id;

    private List<Ret> ret;

    private List<String> signature;

    private String txID;

    @JsonProperty("raw_data")
    private RawData rawData;

    @JsonProperty("raw_data_hex")
    private String rawDataHex;

    private long fee;

    private long blockNumber;

    private String blockHash;

    private long blockTimeStamp;

}
