package com.xxx.chain.tron.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {

    private String blockID;

    @JsonProperty("block_header")
    private BlockHeader blockHeader;

    List<Transaction> transactions;

}
