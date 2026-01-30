package com.xxx.chain.ton.utils;


import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.xxx.base.vo.Response;
import com.xxx.chain.adapter.bo.ChainBlockBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.adapter.bo.TransferTransactionBO;
import com.xxx.chain.enumerate.ChainTransactionStatusEnum;
import com.xxx.chain.ton.bo.GetPoolDataBO;
import com.xxx.chain.ton.bo.toncenter.*;
import com.xxx.common.core.utils.HttpClientUtil;
import com.xxx.common.core.utils.JSONUtil;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;
import org.ton.ton4j.address.Address;
import org.ton.ton4j.cell.CellBuilder;
import org.ton.ton4j.smartcontract.token.nft.NftUtils;
import org.ton.ton4j.utils.Utils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TonUtilTonCenter {

    public static ChainBlockBO queryNewest(RpcConfigBO rpcConfig) {
        try {
            String url = rpcConfig.getRpcUrl() + "/api/v2/getConsensusBlock";
            //header
            Map<String, String> headerParamMap = new HashMap<>();
            headerParamMap.put("Content-Type", "application/json");
            headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
            //
            String response = HttpClientUtil.doGet(
                    url,
                    null,
                    headerParamMap,
                    rpcConfig.isEnableProxy(),
                    rpcConfig.getProxyHost(),
                    rpcConfig.getProxyPort()
            );

            if (StringUtils.isNotEmpty(response)) {
                TonCenterV2GetConsensusBlock tonCenterV2GetConsensusBlock = JSONUtil.stringToObject(response, TonCenterV2GetConsensusBlock.class);
                if (ObjectUtils.isNotNull(tonCenterV2GetConsensusBlock)) {
                    TonCenterV2Result result = tonCenterV2GetConsensusBlock.getResult();
                    if (ObjectUtils.isNotNull(result)) {
                        ChainBlockBO blockInfoBO = new ChainBlockBO();
                        blockInfoBO.setBlockNumber(result.getConsensusBlock());
                        blockInfoBO.setBlockTimestamp(result.getTimestamp().longValue());
                        return blockInfoBO;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static ChainBlockBO queryNewestByWorkChainShard(RpcConfigBO rpcConfig, Long workChain, String shard) {
        try {
            String url = rpcConfig.getRpcUrl() + "/api/v3/blocks";
            //request
            Map<String, Object> requestParamMap = new HashMap<>();
            requestParamMap.put("workchain", workChain);
            requestParamMap.put("shard", shard);
            requestParamMap.put("limit", 1);
            requestParamMap.put("sort", "desc");
            //header
            Map<String, String> headerParamMap = new HashMap<>();
            headerParamMap.put("Content-Type", "application/json");
            headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
            //
            String response = HttpClientUtil.doGet(
                    url,
                    requestParamMap,
                    headerParamMap,
                    rpcConfig.isEnableProxy(),
                    rpcConfig.getProxyHost(),
                    rpcConfig.getProxyPort()
            );

            if (StringUtils.isNotEmpty(response)) {
                TonCenterV3Blocks tonCenterV3Blocks = JSONUtil.stringToObject(response, TonCenterV3Blocks.class);
                if (ObjectUtils.isNotNull(tonCenterV3Blocks)) {
                    List<TonCenterV3Block> blocks = tonCenterV3Blocks.getBlocks();
                    if (ObjectUtils.isNotNull(blocks) && !blocks.isEmpty()) {
                        TonCenterV3Block tonCenterV3Block = blocks.get(0);
                        ChainBlockBO blockInfoBO = new ChainBlockBO();
                        blockInfoBO.setBlockNumber(tonCenterV3Block.getSeqno());
                        blockInfoBO.setBlockTimestamp(Long.parseLong(tonCenterV3Block.getGenUtime()));
                        blockInfoBO.setBlockHash(tonCenterV3Block.getRootHash());
                        return blockInfoBO;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void fetchTonTransaction(
            List<TransferTransactionBO> transferTransactionBOList,
            boolean isProduction,
            RpcConfigBO rpcConfig,
            List<String> toAddressList,
            Long lt
    ) {
        try {
            String url = rpcConfig.getRpcUrl() + "/api/v3/transactions";
            //request
            Map<String, Object> requestParamMap = new HashMap<>();
            //https://testnet.toncenter.com/api/v3/transactions
            // ?account=0QAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFgcx
            // &start_lt=22164808000001
            // &limit=128
            // &offset=0
            // &sort=desc
            requestParamMap.put("account", toAddressList);
            requestParamMap.put("start_lt", lt);
            requestParamMap.put("limit", "100");
            requestParamMap.put("offset", "0");
            requestParamMap.put("sort", "desc");
            //header
            Map<String, String> headerParamMap = new HashMap<>();
            headerParamMap.put("Content-Type", "application/json");
            headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
            //
            String response = HttpClientUtil.doGet(
                    url,
                    requestParamMap,
                    headerParamMap,
                    rpcConfig.isEnableProxy(),
                    rpcConfig.getProxyHost(),
                    rpcConfig.getProxyPort()
            );

            if (StringUtils.isNotEmpty(response)) {
                TonCenterV3Transactions tonCenterV3Transactions = JSONUtil.stringToObject(response, TonCenterV3Transactions.class);
                if (ObjectUtils.isNotNull(tonCenterV3Transactions)
                        && ObjectUtils.isNotNull(tonCenterV3Transactions.getTransactions())
                        && !tonCenterV3Transactions.getTransactions().isEmpty()) {

                    List<TonCenterV3Transaction> tonCenterV3TransactionList = tonCenterV3Transactions.getTransactions();

                    List<TonCenterV3Transaction> filterTonCenterV3TransactionList = tonCenterV3TransactionList.stream()
                            .filter(i ->
                                    ObjectUtils.isNotNull(i.getInMsg())
                                            && ObjectUtils.isNotNull(i.getInMsg().getValue())
                                            && new BigInteger(i.getInMsg().getValue()).compareTo(BigInteger.ZERO) > 0
                                            &&
                                            (
                                                    (ObjectUtils.isNull(i.getInMsg().getOpcode())
                                                            || i.getInMsg().getOpcode().equals("0x00000000")
                                                            || i.getInMsg().getOpcode().equals("0x2167da4b")
                                                    )
                                            )
                            )
                            .collect(Collectors.toList());

                    List<TonCenterV3Transaction> filter2TonCenterV3TransactionList = filterTonCenterV3TransactionList.stream()
                            .filter(i -> TonUtil.isContains(toAddressList, i.getInMsg().getDestination()))
                            .collect(Collectors.toList());

                    for (TonCenterV3Transaction tonCenterV3Transaction : filter2TonCenterV3TransactionList) {
                        TransferTransactionBO transferTransactionBO = new TransferTransactionBO();
                        //
                        //Address contractAddress = Address.of(jettonTransferBO.getJettonMaster());
                        transferTransactionBO.setContractAddress("");
                        //
                        String fromAddressStr = tonCenterV3Transaction.getInMsg().getSource();
                        transferTransactionBO.setFromAddress(TonUtil.toChecksumAddress(isProduction, fromAddressStr, false));
                        //
                        String toAddressStr = tonCenterV3Transaction.getInMsg().getDestination();
                        transferTransactionBO.setToAddress(TonUtil.toChecksumAddress(isProduction, toAddressStr, false));
                        //
                        transferTransactionBO.setAmountRaw(tonCenterV3Transaction.getInMsg().getValue());
                        //
                        TonCenterV3BlockRef blockRef = tonCenterV3Transaction.getBlockRef();
                        transferTransactionBO.setTonWorkChain(blockRef.getWorkchain());
                        transferTransactionBO.setTonShard(blockRef.getShard());
                        transferTransactionBO.setBlockNumber(blockRef.getSeqno());
                        transferTransactionBO.setBlockHash(null);
                        transferTransactionBO.setTransactionHash(Utils.base64ToHexString(tonCenterV3Transaction.getHash()));
                        transferTransactionBO.setTransactionTimestamp(tonCenterV3Transaction.getNow());
                        transferTransactionBO.setTransactionTimestampUtc(tonCenterV3Transaction.getNow() * 1000);
                        transferTransactionBO.setTransactionStatus(ChainTransactionStatusEnum.UNKNOWN.getStatus());
                        transferTransactionBO.setIsNative(true);
                        //
                        TonCenterV3InMsg inMsg = tonCenterV3Transaction.getInMsg();
                        if (ObjectUtils.isNotNull(inMsg)) {
                            TonCenterV3MessageContent messageContent = inMsg.getMessageContent();
                            if (ObjectUtils.isNotNull(messageContent)) {
                                TonCenterV3Decoded decoded = messageContent.getDecoded();
                                if (ObjectUtils.isNotNull(decoded)) {
                                    String comment = decoded.getComment();
                                    if (comment.contains("\0")) {
                                        comment = comment.replace("\0", "");
                                        comment = comment.replace("0x00", "");
                                    }
                                    transferTransactionBO.setTonComment(comment);
                                }
                            }
                        }
                        transferTransactionBO.setTonLt(tonCenterV3Transaction.getLt());

                        transferTransactionBOList.add(transferTransactionBO);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void fetchJettonTransaction(
            List<TransferTransactionBO> transferTransactionBOList,
            boolean isProduction,
            RpcConfigBO rpcConfig,
            List<String> cryptoCurrencyAddressList,
            List<String> toAddressList,
            Long lt
    ) {
        try {
            for (String address : toAddressList) {
                String url = rpcConfig.getRpcUrl() + "/api/v3/jetton/transfers";
                //request
                Map<String, Object> requestParamMap = new HashMap<>();
                //https://testnet.toncenter.com/api/v3/jetton/transfers?
                // address=0QAWnJNnwByLThRxli2zLpnj5Ec6r7R-GmpkOkF3pnWPFgcx
                // &direction=both
                // &start_lt=22164808000001
                // &limit=10
                // &offset=0
                // &sort=desc
                requestParamMap.put("address", address);
                requestParamMap.put("direction", "both");
                requestParamMap.put("start_lt", lt);
                requestParamMap.put("limit", "100");
                requestParamMap.put("offset", "0");
                requestParamMap.put("sort", "desc");
                //header
                Map<String, String> headerParamMap = new HashMap<>();
                headerParamMap.put("Content-Type", "application/json");
                headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
                //
                String response = HttpClientUtil.doGet(
                        url,
                        requestParamMap,
                        headerParamMap,
                        rpcConfig.isEnableProxy(),
                        rpcConfig.getProxyHost(),
                        rpcConfig.getProxyPort()
                );

                if (StringUtils.isNotEmpty(response)) {
                    TonCenterV3JettonTransfers tonCenterV3JettonTransfers = JSONUtil.stringToObject(response, TonCenterV3JettonTransfers.class);
                    if (ObjectUtils.isNotNull(tonCenterV3JettonTransfers)
                            && ObjectUtils.isNotNull(tonCenterV3JettonTransfers.getJettonTransfers())
                            && !tonCenterV3JettonTransfers.getJettonTransfers().isEmpty()) {

                        List<TonCenterV3JettonTransfer> tonCenterV3JettonTransferList = tonCenterV3JettonTransfers.getJettonTransfers();

                        for (TonCenterV3JettonTransfer jettonTransfer : tonCenterV3JettonTransferList) {
                            //Address contractAddress = Address.of(tonCenterV3JettonTransfer.getJettonMaster());
                            String contractAddressStr = jettonTransfer.getJettonMaster();
                            String contractAddress = TonUtil.toChecksumAddress(isProduction, contractAddressStr, true);
                            //判断 jetton 合约
                            if (cryptoCurrencyAddressList.contains(contractAddress)) {
                                TransferTransactionBO transferTransactionBO = new TransferTransactionBO();
                                //
                                //Address contractAddress = Address.of(tonCenterV3JettonTransfer.getJettonMaster());
                                transferTransactionBO.setContractAddress(contractAddress);
                                //
                                //Address fromAddress = Address.of(tonCenterV3JettonTransfer.getSource());
                                //transferTransactionBO.setFromAddress(fromAddress.toString(true, true, false, !isProduction));
                                String fromAddressStr = jettonTransfer.getSource();
                                transferTransactionBO.setFromAddress(TonUtil.toChecksumAddress(isProduction, fromAddressStr, false));
                                //
                                //Address toAddress = Address.of(tonCenterV3JettonTransfer.getDestination());
                                //transferTransactionBO.setToAddress(toAddress.toString(true, true, false, !isProduction));
                                String toAddressStr = jettonTransfer.getDestination();
                                transferTransactionBO.setToAddress(TonUtil.toChecksumAddress(isProduction, toAddressStr, false));
                                //
                                transferTransactionBO.setAmountRaw(jettonTransfer.getAmount());
                                //
                                //transferTransactionBO.setTonWorkChain(); TODO 当前接口没有提供块信息
                                //transferTransactionBO.setTonShard(); TODO 当前接口没有提供块信息
                                //transferTransactionBO.setBlockNumber(); TODO 当前接口没有提供块信息
                                //transferTransactionBO.setBlockHash();
                                transferTransactionBO.setTransactionHash(Utils.base64ToHexString(jettonTransfer.getTransactionHash()));
                                transferTransactionBO.setTransactionTimestamp(jettonTransfer.getTransactionNow());
                                transferTransactionBO.setTransactionTimestampUtc(jettonTransfer.getTransactionNow() * 1000);
                                transferTransactionBO.setTransactionStatus(ChainTransactionStatusEnum.UNKNOWN.getStatus());
                                transferTransactionBO.setIsNative(false);
                                //
                                String comment = TonUtil.parseTonComment(jettonTransfer.getForwardPayload());
                                if (StringUtils.isNotEmpty(comment) && comment.contains("\0")) {
                                    comment = comment.replace("\0", "");
                                    comment = comment.replace("0x00", "");
                                }
                                transferTransactionBO.setTonComment(comment);
                                transferTransactionBO.setTonLt(jettonTransfer.getTransactionLt());

                                transferTransactionBOList.add(transferTransactionBO);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ChainTransactionStatusEnum queryTransactionStatus(
            RpcConfigBO rpcConfig,
            TransferTransactionBO transferTXBO,
            boolean isProduction
    ) {
        try {
            //1、TON
            if (transferTXBO.getIsNative()) {
                String url = rpcConfig.getRpcUrl() + "/api/v3/transactions";
                //request
                Map<String, Object> requestParamMap = new HashMap<>();
                //https://toncenter.com/api/v3/transactions
                // ?account=UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo
                // &start_lt=46942682000003
                // &end_lt=46942682000003
                // &limit=1
                // &offset=0
                // &sort=desc
                requestParamMap.put("account", transferTXBO.getToAddress());
                requestParamMap.put("start_lt", transferTXBO.getTonLt());
                requestParamMap.put("end_lt", transferTXBO.getTonLt());
                requestParamMap.put("limit", "1");
                requestParamMap.put("offset", "0");
                requestParamMap.put("sort", "desc");
                //header
                Map<String, String> headerParamMap = new HashMap<>();
                headerParamMap.put("Content-Type", "application/json");
                headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
                //
                String response = HttpClientUtil.doGet(
                        url,
                        requestParamMap,
                        headerParamMap,
                        rpcConfig.isEnableProxy(),
                        rpcConfig.getProxyHost(),
                        rpcConfig.getProxyPort()
                );

                if (StringUtils.isNotEmpty(response)) {
                    TonCenterV3Transactions tonCenterV3Transactions = JSONUtil.stringToObject(response, TonCenterV3Transactions.class);
                    if (ObjectUtils.isNotNull(tonCenterV3Transactions)
                            && ObjectUtils.isNotNull(tonCenterV3Transactions.getTransactions())
                            && !tonCenterV3Transactions.getTransactions().isEmpty()) {

                        List<TonCenterV3Transaction> tonCenterV3TransactionList = tonCenterV3Transactions.getTransactions();

                        List<TonCenterV3Transaction> filterTonCenterV3TransactionList = tonCenterV3TransactionList.stream()
                                .filter(i ->
                                        ObjectUtils.isNotNull(i.getInMsg())
                                                && ObjectUtils.isNotNull(i.getInMsg().getValue())
                                                && new BigInteger(i.getInMsg().getValue()).compareTo(BigInteger.ZERO) > 0
                                                &&
                                                (
                                                        (ObjectUtils.isNull(i.getInMsg().getOpcode())
                                                                || i.getInMsg().getOpcode().equals("0x00000000")
                                                                || i.getInMsg().getOpcode().equals("0x2167da4b")
                                                        )
                                                )
                                )
                                .collect(Collectors.toList());

                        List<TonCenterV3Transaction> filter2TonCenterV3TransactionList = filterTonCenterV3TransactionList.stream()
                                .filter(i -> TonUtil.addressEquals(transferTXBO.getToAddress(), i.getInMsg().getDestination()))
                                .collect(Collectors.toList());

                        for (TonCenterV3Transaction tonCenterV3Transaction : filter2TonCenterV3TransactionList) {
                            //contractAddress
                            //from
                            String fromAddressStr = tonCenterV3Transaction.getInMsg().getSource();
                            String fromAddress = TonUtil.toChecksumAddress(isProduction, fromAddressStr, false);
                            if (!transferTXBO.getFromAddress().equals(fromAddress)) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //to
                            String toAddressStr = tonCenterV3Transaction.getInMsg().getDestination();
                            String toAddress = TonUtil.toChecksumAddress(isProduction, toAddressStr, false);
                            if (!transferTXBO.getToAddress().equals(toAddress)) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //amount
                            if (!transferTXBO.getAmountRaw().equals(tonCenterV3Transaction.getInMsg().getValue())) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //forward_payload TODO 不对比 备注（有的充值没有备注，但交易是成功的）
                                /*TonCenterV3InMsg inMsg = tonCenterV3Transaction.getInMsg();
                                if (ObjectUtils.isNotNull(inMsg)) {
                                    TonCenterV3MessageContent messageContent = inMsg.getMessageContent();
                                    if (ObjectUtils.isNotNull(messageContent)) {
                                        TonCenterV3Decoded decoded = messageContent.getDecoded();
                                        if (ObjectUtils.isNotNull(decoded)) {
                                            if (!transferTXBO.getTonComment().equals(decoded.getComment())) {
                                                return TxStatusEnum.UNKNOWN;
                                            }
                                        } else {
                                            return TxStatusEnum.UNKNOWN;
                                        }
                                    } else {
                                        return TxStatusEnum.UNKNOWN;
                                    }
                                } else {
                                    return TxStatusEnum.UNKNOWN;
                                }*/
                            return ChainTransactionStatusEnum.SUCCESSFUL;
                        }
                    }
                }
            }
            //2、Jetton
            if (!transferTXBO.getIsNative()) {
                //curl -X 'GET' \
                //'https://toncenter.com/api/v3/jetton/transfers?address=UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo&direction=both&start_lt=46942710000003&end_lt=46942710000003&limit=1&offset=0&sort=desc' \
                //-H 'accept: application/json'

                String url = rpcConfig.getRpcUrl() + "/api/v3/jetton/transfers";
                //request
                Map<String, Object> requestParamMap = new HashMap<>();
                //https://toncenter.com/api/v3/jetton/transfers
                // ?address=UQBN9GMPmHZmfD9wc_-wIFAe6kEH6AQG1A9cAOigldeJXx7y
                // &direction=both
                // &start_lt=46942710000003
                // &end_lt=46942710000003
                // &limit=1
                // &offset=0
                // &sort=desc
                requestParamMap.put("address", transferTXBO.getToAddress());
                requestParamMap.put("direction", "both");
                requestParamMap.put("start_lt", transferTXBO.getTonLt());
                requestParamMap.put("end_lt", transferTXBO.getTonLt());
                requestParamMap.put("limit", "1");
                requestParamMap.put("offset", "0");
                requestParamMap.put("sort", "desc");
                //header
                Map<String, String> headerParamMap = new HashMap<>();
                headerParamMap.put("Content-Type", "application/json");
                headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
                //
                String response = HttpClientUtil.doGet(
                        url,
                        requestParamMap,
                        headerParamMap,
                        rpcConfig.isEnableProxy(),
                        rpcConfig.getProxyHost(),
                        rpcConfig.getProxyPort()
                );

                if (StringUtils.isNotEmpty(response)) {
                    TonCenterV3JettonTransfers tonCenterV3JettonTransfers = JSONUtil.stringToObject(response, TonCenterV3JettonTransfers.class);
                    if (ObjectUtils.isNotNull(tonCenterV3JettonTransfers)
                            && ObjectUtils.isNotNull(tonCenterV3JettonTransfers.getJettonTransfers())
                            && !tonCenterV3JettonTransfers.getJettonTransfers().isEmpty()) {

                        List<TonCenterV3JettonTransfer> tonCenterV3JettonTransferList = tonCenterV3JettonTransfers.getJettonTransfers();
                        if (ObjectUtils.isNotNull(tonCenterV3JettonTransferList) && !tonCenterV3JettonTransferList.isEmpty()) {
                            TonCenterV3JettonTransfer tonCenterV3JettonTransfer = tonCenterV3JettonTransferList.get(0);

                            //contractAddress
                            String contractAddressStr = tonCenterV3JettonTransfer.getJettonMaster();
                            String contractAddress = TonUtil.toChecksumAddress(isProduction, contractAddressStr, true);
                            if (!transferTXBO.getContractAddress().equals(contractAddress)) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //from
                            String fromAddressStr = tonCenterV3JettonTransfer.getSource();
                            String fromAddress = TonUtil.toChecksumAddress(isProduction, fromAddressStr, false);
                            if (!transferTXBO.getFromAddress().equals(fromAddress)) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //to
                            String toAddressStr = tonCenterV3JettonTransfer.getDestination();
                            String toAddress = TonUtil.toChecksumAddress(isProduction, toAddressStr, false);
                            if (!transferTXBO.getToAddress().equals(toAddress)) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //amount
                            if (!transferTXBO.getAmountRaw().equals(tonCenterV3JettonTransfer.getAmount())) {
                                return ChainTransactionStatusEnum.UNKNOWN;
                            }
                            //forward_payload TODO 不对比 备注（有的充值没有备注，但交易是成功的）
                                /*if (StringUtils.isNotEmpty(tonCenterV3JettonTransfer.getForwardPayload())
                                        && StringUtils.isNotEmpty(transferTXBO.getTonComment())
                                        && !transferTXBO.getTonComment().equals(parseTonComment(tonCenterV3JettonTransfer.getForwardPayload()))) {
                                    return TxStatusEnum.UNKNOWN;
                                }*/
                            return ChainTransactionStatusEnum.SUCCESSFUL;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ChainTransactionStatusEnum.UNKNOWN;
    }

    public static Response runGetMethodGetPoolData(RpcConfigBO rpcConfig, boolean isProduction, String contractAddress) throws Exception {
        String url = rpcConfig.getRpcUrl() + "/api/v2/runGetMethod";
        //request
        Map<String, Object> requestParamMap = new HashMap<>();
        //  CURL -X 'POST' \
        //  'https://toncenter.com/api/v2/runGetMethod' \
        //  -H 'accept: application/json' \
        //  -H 'Content-Type: application/json' \
        //  -d '{
        //      "address": "EQARK5MKz_MK51U5AZjK3hxhLg1SmQG2Z-4Pb7Zapi_xwmrN",
        //      "method": "get_pool_data",
        //      "stack": []
        //  }'
        requestParamMap.put("address", contractAddress);
        requestParamMap.put("method", "get_pool_data");
        String[] arr = new String[]{};
        requestParamMap.put("stack", arr);
        //header
        Map<String, String> headerParamMap = new HashMap<>();
        headerParamMap.put("accept", "application/json");
        headerParamMap.put("Content-Type", "application/json");
        headerParamMap.put("X-API-Key", rpcConfig.getApiKey());
        //
        String responseString = HttpClientUtil.doPostJSON(
                url,
                requestParamMap,
                headerParamMap,
                rpcConfig.isEnableProxy(),
                rpcConfig.getProxyHost(),
                rpcConfig.getProxyPort()
        );
        Response response = null;
        //System.out.println(response);
        JSONObject jsonObject = JSONObject.parseObject(responseString);
        if (ObjectUtils.isNotNull(jsonObject)) {
            Boolean ok = jsonObject.getBoolean("ok");
            if (ok) {
                JSONObject result = jsonObject.getJSONObject("result");
                if (ObjectUtils.isNotNull(result)) {
                    JSONArray stack = result.getJSONArray("stack");
                    if (ObjectUtils.isNotNull(stack) && !stack.isEmpty()) {
                        //
                        GetPoolDataBO getPoolDataBO = new GetPoolDataBO();
                        response = Response.success();
                        response.setData(getPoolDataBO);
                        // reserve0
                        JSONArray reserve0 = stack.getJSONArray(0);
                        if (ObjectUtils.isNotNull(reserve0) && reserve0.size() > 1) {
                            String reserveHex = reserve0.getString(1);
                            long reserveLong = Long.parseLong(reserveHex.substring(2), 16);
                            getPoolDataBO.setReserve0(reserveLong);
                        }

                        // reserve1
                        JSONArray reserve1 = stack.getJSONArray(1);
                        if (ObjectUtils.isNotNull(reserve1) && reserve1.size() > 1) {
                            String reserveHex = reserve1.getString(1);
                            long reserveLong = Long.parseLong(reserveHex.substring(2), 16);
                            getPoolDataBO.setReserve1(reserveLong);
                        }

                        //token0_address
                        JSONArray token0Address = stack.getJSONArray(2);
                        if (ObjectUtils.isNotNull(token0Address) && token0Address.size() > 1) {
                            JSONObject token0Address_1 = token0Address.getJSONObject(1);
                            String token0Address_1_bytes = token0Address_1.getString("bytes");
                            //
                            byte[] bytes = Utils.base64ToSignedBytes(token0Address_1_bytes);
                            Address address = NftUtils.parseAddress(CellBuilder.beginCell().fromBoc(bytes).endCell());
                            //getPoolDataBO.setToken0Address(address.toString(true, true, true, !isProduction));
                            getPoolDataBO.setToken0Address(TonUtil.toChecksumAddress(isProduction, address, true));
                        }

                        //token1_address
                        JSONArray token1Address = stack.getJSONArray(3);
                        if (ObjectUtils.isNotNull(token1Address) && token1Address.size() > 1) {
                            JSONObject token1Address_1 = token1Address.getJSONObject(1);
                            String token1Address_1_bytes = token1Address_1.getString("bytes");
                            //
                            byte[] bytes = Utils.base64ToSignedBytes(token1Address_1_bytes);
                            Address address = NftUtils.parseAddress(CellBuilder.beginCell().fromBoc(bytes).endCell());
                            //getPoolDataBO.setToken1Address(address.toString(true, true, true, !isProduction));
                            getPoolDataBO.setToken1Address(TonUtil.toChecksumAddress(isProduction, address, true));
                        }

                    }
                }
            }
        }
        if (response == null) {
            response = Response.error();
        }
        return response;
    }

    /*public static void main(String[] args) {
        RpcConfigBO rpcConfig = new RpcConfigBO(
                true,
                "127.0.0.1",
                7890,
                "https://toncenter.com",
                null,
                null,
                null,
                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
        );
        ChainBlockBO blockInfoBO = queryNewest(rpcConfig);
        System.out.println(123);
    }*/

    /*public static void main(String[] args) {
        RpcConfigBO rpcConfig = new RpcConfigBO(
                true,
                "127.0.0.1",
                7890,
                "https://toncenter.com",
                null,
                null,
                null,
                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
        );
        List<TransferTransactionBO> transferTransactionBOList = new ArrayList<>();
        List<String> toAddressList = new ArrayList<>();
        toAddressList.add("UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo");
        TonUtil.fetchTonTransaction(
                transferTransactionBOList,
                true,
                rpcConfig,
                toAddressList,
                46942682000000L
        );
        System.out.println(123);
    }*/

     /*public static void main(String[] args) {
        //TODO 正确转换 curl -X 'GET' \
        //  'https://toncenter.com/api/v3/transactions?account=UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo&lt=47011056000001&limit=3&offset=0&sort=desc' \
        //  -H 'accept: application/json'
        Cell cell = CellBuilder.beginCell().fromBoc(Utils.base64ToSignedBytes("te6cckEBAQEADwAAGgAAAAAxNzcyODc0OTLqD1FP")).endCell();
        CellSlice cellSlice = CellSlice.beginParse(cell);
        System.out.println(cellSlice.loadString(32));
        System.out.println(cellSlice.loadSnakeString());
    }*/

    /*public static void main(String[] args) {
        //TODO 正确转换 curl -X 'GET' \
        //  'https://toncenter.com/api/v3/transactions?account=UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo&lt=47011056000001&limit=3&offset=0&sort=desc' \
        //  -H 'accept: application/json'
        Cell cell = CellBuilder.beginCell().fromBoc(Utils.base64ToSignedBytes("te6cckEBAgEARAABYnNi0JxUbeTvUCbdrTLcbAgBuLp25DrgVPNl9AZJeo45EURTBTdKl/poDoMOImnP15EBABwAAAAANjU3ODg0MjIxMI5OnCg=")).endCell();
        CellSlice cellSlice = CellSlice.beginParse(cell);
        System.out.println(cellSlice.loadUint(32));//op
        System.out.println(cellSlice.loadUint(64));//query_id
        System.out.println(cellSlice.loadCoins());//coin
        System.out.println(cellSlice.loadAddress());//from_address
        String id3 = cellSlice.loadSnakeString();
        System.out.println(id3);
    }*/

    /*public static void main(String[] args) {
        //TODO 正确转换 curl -X 'GET' \
        //  'https://toncenter.com/api/v3/transactions?account=UQDhy0YW7b3X9mPylc9hH8gpF4Oc0qN3XLwhlN9DuvZgGj-E&start_lt=46960000000000&limit=256&offset=0&sort=asc' \
        //  -H 'accept: application/json'
        //Cell cell = CellBuilder.beginCell().fromBoc(Utils.base64ToSignedBytes("te6cckEBAgEARAABYnNi0JxUbeTvjxt4yDEtregALTkmz4A5Fpwo4yxbZl0zx8iOdV9o/DTUyHSC70zrHi0BABwAAAAAMTk4NTU5NzY1MWNfjoM=")).endCell();
        //Cell cell = CellBuilder.beginCell().fromBoc(Utils.base64ToSignedBytes("te6cckEBAgEAZwABpxeNRRlUbeTvjxt4yDEtregALTkmz4A5Fpwo4yxbZl0zx8iOdV9o/DTUyHSC70zrHi0ABack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WEBwEAHAAAAAAxOTg1NTk3NjUxEYy1+w==")).endCell();
        Cell cell = CellBuilder.beginCell().fromBoc(Utils.base64ToSignedBytes("te6cckEBAgEAZwABqA+KfqVUbeTvjxt4yDEtregBw5aMLdt7r+zH5Suewj+QUi8HOaVG7rl4Qym+h3XswDUABack2fAHItOFHGWLbMumePkRzqvtH4aamQ6QXemdY8WCAwEAHAAAAAAxOTg1NTk3NjUxfuChDA==")).endCell();

        CellSlice cellSlice = CellSlice.beginParse(cell);
        BigInteger op = cellSlice.loadUint(32);
        //
        System.out.println("op = " + op);//op
        System.out.println("query_id = " + cellSlice.loadUint(64));//query_id
        System.out.println("coin = " + cellSlice.loadCoins());//coin
        System.out.println("from_address = " + cellSlice.loadAddress());//from_address

        boolean b = cellSlice.loadBit();
        Cell forwardPayloadCell = cellSlice.loadRef();
        CellSlice forwardPayloadCellSlice = CellSlice.beginParse(forwardPayloadCell);
        BigInteger forwardPayloadOp = forwardPayloadCellSlice.loadUint(32);
        String forward_payload_comment = forwardPayloadCellSlice.loadSnakeString();
        System.out.println("forward_payload_op = " + forwardPayloadOp);
        System.out.println("forward_payload_comment = " + forward_payload_comment);
    }*/

    /*public static void main(String[] args) {
        //TODO 正确转换 curl -X 'GET' \
        //  'https://toncenter.com/api/v3/transactions?account=UQDhy0YW7b3X9mPylc9hH8gpF4Oc0qN3XLwhlN9DuvZgGj-E&start_lt=46960000000000&limit=256&offset=0&sort=asc' \
        //  -H 'accept: application/json'
        Cell cell = CellBuilder.beginCell().fromBoc(Utils.base64ToSignedBytes("te6cckEBAQEAEAAAHAAAAAAxOTg1NTk3NjUxSmLi/w==")).endCell();
        CellSlice cellSlice = CellSlice.beginParse(cell);
        boolean b = cellSlice.loadBit();
        if (b) {
            Cell forwardPayloadCell = cellSlice.loadRef();
            CellSlice forwardPayloadCellSlice = CellSlice.beginParse(forwardPayloadCell);
            BigInteger forwardPayloadOP = forwardPayloadCellSlice.loadUint(32);
            String forwardPayloadComment = forwardPayloadCellSlice.loadSnakeString();
            System.out.println(forwardPayloadOP);
            System.out.println(forwardPayloadComment);
        } else {
            BigInteger forwardPayloadOP = cellSlice.loadUint(32);
            System.out.println(forwardPayloadOP);
            BigInteger bigInteger = cellSlice.loadUint(64);
            System.out.println(bigInteger);
        }
    }*/

    /*public static void main(String[] args) {
        RpcConfigBO rpcConfig = new RpcConfigBO(
                true,
                "127.0.0.1",
                7890,
                "https://toncenter.com",
                null,
                null,
                null,
                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
        );
        TransferTransactionBO transferTransactionBOList = new TransferTransactionBO();
        transferTransactionBOList.setNative(false);
        transferTransactionBOList.setContractAddress("EQCxE6mUtQJKFnGfaROTKOt1lZbDiiX1kCixRv7Nw2Id_sDs");
        transferTransactionBOList.setFromAddress("UQBN9GMPmHZmfD9wc_-wIFAe6kEH6AQG1A9cAOigldeJXx7y");
        transferTransactionBOList.setToAddress("UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo");
        transferTransactionBOList.setAmountRaw("1000000");
        transferTransactionBOList.setTonComment("6578396015");
        transferTransactionBOList.setTonLt("46942710000003");
        TxStatusEnum txStatusEnum = queryTransactionStatus(
                rpcConfig,
                transferTransactionBOList,
                true
        );
        System.out.println(txStatusEnum.getValue());
    }*/

    /*public static void main(String[] args) throws Exception {
        RpcConfigBO rpcConfig = new RpcConfigBO(
                true,
                "127.0.0.1",
                7890,
                "https://toncenter.com",
                null,
                null,
                null,
                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
        );
        List<String> tonAddressList = new ArrayList<>();
        tonAddressList.add("UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo");
        //tonAddressList.add("UQAfWAbHPQO7yW637r8WBn8fLo4nDPoW1XABqp6vdFbwCFsx");
        //
        List<String> cryptoCurrencyAddressList = new ArrayList<>();
        cryptoCurrencyAddressList.add("EQCxE6mUtQJKFnGfaROTKOt1lZbDiiX1kCixRv7Nw2Id_sDs");
        //
        List<TransferTransactionBO> transferTransactionBOList = new ArrayList<>();
        fetchJettonTransaction(
                transferTransactionBOList,
                true,
                rpcConfig,
                cryptoCurrencyAddressList,
                tonAddressList,
                47642355000000L
        );
        System.out.println(transferTransactionBOList.size());
    }*/

}