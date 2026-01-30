package com.xxx.chain.tron.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawData {

    private long blockNumber;

    private String txTrieRoot;

    private long timestamp;

    private List<Contract> contract;

}
