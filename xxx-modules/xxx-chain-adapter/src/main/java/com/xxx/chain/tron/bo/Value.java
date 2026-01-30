package com.xxx.chain.tron.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private String data;

    @JsonProperty("owner_address")
    private String ownerAddress;

    @JsonProperty("contract_address")
    private String contractAddress;

    @JsonProperty("to_address")
    private String toAddress;

    private String amount;

}