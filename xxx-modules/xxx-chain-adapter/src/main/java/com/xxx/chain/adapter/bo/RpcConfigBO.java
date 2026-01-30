package com.xxx.chain.adapter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcConfigBO implements Serializable {

    private boolean enableProxy;
    private String proxyHost;
    private int proxyPort;
    private String rpcUrl;
    private String apiKey;

    public RpcConfigBO(boolean enableProxy, String proxyHost, int proxyPort, String rpcUrl, String apiKey) {
        this.enableProxy = enableProxy;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.rpcUrl = rpcUrl;
        this.apiKey = apiKey;
    }

}
