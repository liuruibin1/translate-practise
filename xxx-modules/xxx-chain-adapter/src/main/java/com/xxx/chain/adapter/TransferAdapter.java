package com.xxx.chain.adapter;

import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.enumerate.ChainTypeEnum;
import com.xxx.chain.ethereum.utils.EthereumUtil;
import com.xxx.chain.tron.utils.TronUtil;
import org.tron.trident.core.ApiWrapper;
import org.web3j.protocol.Web3j;

public class TransferAdapter {

    public static String handle(RpcConfigBO rpcConfigBO, ChainTypeEnum chainType, String coinAddress, String toAddress, String transferAmountRaw, String fromPrivateKey, boolean isNative, boolean isProduction) throws Exception {
        if (chainType.equals(ChainTypeEnum.ETHEREUM)) {
            Web3j web3j = EthereumUtil.getWeb3jByRpcUrl(rpcConfigBO.isEnableProxy(), rpcConfigBO.getRpcUrl());
            if (isNative) {
                return EthereumUtil.transferETH(web3j, toAddress, transferAmountRaw, fromPrivateKey);
            } else {
                return EthereumUtil.transferERC20(web3j, coinAddress, toAddress, transferAmountRaw, fromPrivateKey);
            }
        } else if (chainType.equals(ChainTypeEnum.TRON)) {
            ApiWrapper apiWrapper = TronUtil.createApiWrapper(rpcConfigBO);
            String fromAddress = String.valueOf(TronUtil.getAddressByPrivateKey(fromPrivateKey));
            if (isNative) {
                return TronUtil.transferTRX(apiWrapper, fromAddress, toAddress, transferAmountRaw);
            } else {
                return TronUtil.transferTRC20(apiWrapper, fromAddress, toAddress, transferAmountRaw, coinAddress);
            }
        } else if (chainType.equals(ChainTypeEnum.BTC)) {
//            return BTCUtil.transferBTC(rpcConfig.getRpcUrl(), fromPrivateKey, toAddress, Long.parseLong(transferAmountRaw), isProduction);
        }
        return null;
    }


}