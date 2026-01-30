package com.xxx.chain.adapter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class CryptoAccountBO implements Serializable {

    private String privateKey;
    private String address;

}
