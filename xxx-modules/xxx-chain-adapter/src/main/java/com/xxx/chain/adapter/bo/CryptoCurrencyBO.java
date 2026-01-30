package com.xxx.chain.adapter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CryptoCurrencyBO implements Serializable {

    private String name;
    private String symbol;
    private String address;
    private Integer decimals;

}
