package com.xxx.chain.adapter;

import com.xxx.chain.adapter.bo.ChainBlockBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.enumerate.ChainTypeEnum;
import com.xxx.chain.ethereum.utils.EthereumUtil;
import com.xxx.chain.tron.utils.TronUtil;

public class BlockAdapter {

    public static ChainBlockBO getLatestBlock(RpcConfigBO rpcConfig, ChainTypeEnum chainType, boolean isProduction) throws Exception {
        if (chainType.equals(ChainTypeEnum.ETHEREUM)) {
            return EthereumUtil.getLatestBlock(rpcConfig);
        } else if (chainType.equals(ChainTypeEnum.TRON)) {
            return TronUtil.getLatestBlock(rpcConfig);
        }
        return null;
    }

}