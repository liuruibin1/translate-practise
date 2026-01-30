package com.xxx.chain.adapter;

import com.xxx.chain.adapter.bo.CryptoCurrencyBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.constant.PrivateKey;
import com.xxx.chain.enumerate.ChainTypeEnum;
import com.xxx.chain.ethereum.abi.ERC20;
import com.xxx.chain.ethereum.utils.EthereumUtil;
import com.xxx.chain.tron.utils.TronUtil;
import com.xxx.common.core.utils.BigDecimalUtil;
import org.tron.trident.core.ApiWrapper;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CryptoCurrencyAdapter {

    public static CryptoCurrencyBO queryInfo(RpcConfigBO rpcConfig, ChainTypeEnum chainType, String tokenAddress) throws Exception {
        CryptoCurrencyBO cryptoCurrency = new CryptoCurrencyBO();
        if (chainType.equals(ChainTypeEnum.ETHEREUM)) {
            Web3j web3j = EthereumUtil.createWeb3jClient(rpcConfig.isEnableProxy(), rpcConfig.getProxyHost(), rpcConfig.getProxyPort(), rpcConfig.getRpcUrl());
            Credentials credentials = Credentials.create(PrivateKey.EMPTY);
            ContractGasProvider contractGasProvider = new DefaultGasProvider();
            ERC20 erc20 = ERC20.load(tokenAddress, web3j, credentials, contractGasProvider);
            cryptoCurrency.setName(erc20.name().send());
            cryptoCurrency.setSymbol(erc20.symbol().send());
            cryptoCurrency.setDecimals(erc20.decimals().send().intValue());
        }
        return cryptoCurrency;
    }

    public static String queryBalance(
            RpcConfigBO rpcConfig,
            ChainTypeEnum chainType,
            String coinAddress,
            String accountAddress,
            boolean isNative) {
        if (chainType.equals(ChainTypeEnum.ETHEREUM)) {
            Web3j web3j = EthereumUtil.createWeb3jClient(rpcConfig.isEnableProxy(), rpcConfig.getProxyHost(), rpcConfig.getProxyPort(), rpcConfig.getRpcUrl());
            if (isNative) {
                return EthereumUtil.getEthBalance(web3j, accountAddress);
            } else {
                return EthereumUtil.getErc20Balance(web3j, accountAddress, coinAddress);
            }
        }else if(chainType.equals(ChainTypeEnum.TRON)){
            ApiWrapper apiWrapper = TronUtil.createApiWrapper(rpcConfig);
            if (isNative) {
                return TronUtil.trxBalance(apiWrapper, accountAddress);
            } else {
                return TronUtil.trc20Balance(apiWrapper, accountAddress, coinAddress);
            }
        }
        return null;
    }

    /**
     * 数额 乘 精度次幂
     *
     * @param decimals 小数位
     * @param amount   数额
     * @return
     */
    public static BigDecimal decimalAmountToRaw(Integer decimals, BigDecimal amount) {
        BigDecimal power = BigDecimalUtil.get10Power(decimals);
        return amount.multiply(power);
    }

    /**
     * 数额 除 精度次幂
     *
     * @param amount   数额
     * @param decimals 小数位
     * @return
     */
    public static BigDecimal rawAmountToDecimal(BigDecimal amount, Integer decimals) {
        BigDecimal power = BigDecimalUtil.get10Power(decimals);
        return amount.divide(power, decimals, RoundingMode.DOWN);
    }

}