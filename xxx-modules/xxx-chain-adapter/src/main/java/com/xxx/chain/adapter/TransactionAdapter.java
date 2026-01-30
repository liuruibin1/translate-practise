package com.xxx.chain.adapter;

import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.adapter.bo.TransferTransactionBO;
import com.xxx.chain.enumerate.ChainEnum;
import com.xxx.chain.enumerate.ChainTransactionStatusEnum;
import com.xxx.chain.enumerate.ChainTypeEnum;
import com.xxx.chain.ethereum.utils.EthereumUtil;
import com.xxx.chain.tron.utils.TronUtil;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter {

    public static List<TransferTransactionBO> fetchTransferTransaction(
            ChainEnum chainEnum,
            RpcConfigBO rpcConfigBO,
            long fromBlockNumber,
            long toBlockNumber,
            List<String> contractAddressList
    ) throws Exception {
        List<TransferTransactionBO> transferTransactionBOList = new ArrayList<>();
        if (chainEnum.getType().equals(ChainTypeEnum.ETHEREUM)) {
            if (contractAddressList == null || contractAddressList.isEmpty()) {
                EthereumUtil.fetchEthTransferTransactions(transferTransactionBOList, rpcConfigBO, fromBlockNumber, toBlockNumber);
            } else {
                EthereumUtil.fetchErc20TransferTransactions(
                        transferTransactionBOList,
                        rpcConfigBO,
                        BigInteger.valueOf(fromBlockNumber),
                        BigInteger.valueOf(toBlockNumber),
                        contractAddressList
                );
            }
        } else if (chainEnum.getType().equals(ChainTypeEnum.TRON)) {
            transferTransactionBOList = TronUtil.fetchTransferTransactions(rpcConfigBO, fromBlockNumber, toBlockNumber);
        }
        return transferTransactionBOList;
    }

    public static ChainTransactionStatusEnum queryTransactionStatus(
            RpcConfigBO rpcConfig,
            ChainTypeEnum chainType,
            String txHash) throws Exception {
        if (chainType.equals(ChainTypeEnum.ETHEREUM)) {
            return EthereumUtil.getTransactionStatus(rpcConfig, txHash);
        } else if (chainType.equals(ChainTypeEnum.TRON)) {
            return TronUtil.queryTransactionStatus(rpcConfig, txHash);
        }
        return ChainTransactionStatusEnum.UNKNOWN;
    }

}