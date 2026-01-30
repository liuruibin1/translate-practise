package com.xxx.chain.ton.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * https://tonviewer.com/EQARK5MKz_MK51U5AZjK3hxhLg1SmQG2Z-4Pb7Zapi_xwmrN?section=method
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPoolDataBO implements Serializable {

    private Long reserve0;

    private Long reserve1;

    private String token0Address;

    private String token1Address;

    public Long getReserve0() {
        return reserve0;
    }

    public void setReserve0(Long reserve0) {
        this.reserve0 = reserve0;
    }

    public Long getReserve1() {
        return reserve1;
    }

    public void setReserve1(Long reserve1) {
        this.reserve1 = reserve1;
    }

    public String getToken0Address() {
        return token0Address;
    }

    public void setToken0Address(String token0Address) {
        this.token0Address = token0Address;
    }

    public String getToken1Address() {
        return token1Address;
    }

    public void setToken1Address(String token1Address) {
        this.token1Address = token1Address;
    }
}