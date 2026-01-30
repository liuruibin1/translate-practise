package com.xxx.chain.ton.utils;


import com.xxx.chain.adapter.bo.ChainBlockBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.adapter.bo.TransferTransactionBO;
import com.xxx.chain.enumerate.ChainTransactionStatusEnum;
import com.xxx.chain.ton.bo.tonapi.*;
import com.xxx.common.core.utils.HttpClientUtil;
import com.xxx.common.core.utils.JSONUtil;
import com.xxx.common.core.utils.ObjectUtils;
import com.xxx.common.core.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TonUtilTonApi {

    private final static String JETTON_TRANSFER = "JettonTransfer";

    private final static String TON_TRANSFER = "TonTransfer";

    public static ChainBlockBO queryNewest(RpcConfigBO rpcConfig) {
        try {
            String url = rpcConfig.getRpcUrl() + "/v2/liteserver/get_masterchain_info";
            //header
            Map<String, String> headerParamMap = new HashMap<>();
            headerParamMap.put("accept", "application/json");
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
                TonApiResponseBody responseBody = JSONUtil.stringToObject(response, TonApiResponseBody.class);
                if (ObjectUtils.isNotNull(responseBody)) {
                    TonApiLast last = responseBody.getLast();
                    if (ObjectUtils.isNotNull(last)) {
                        ChainBlockBO blockInfoBO = new ChainBlockBO();
                        blockInfoBO.setBlockNumber(last.getSeqno());
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
        return null;
    }

    public static void fetchTonJettonTransaction(
            List<TransferTransactionBO> transferTransactionBOList,
            boolean isProduction,
            RpcConfigBO rpcConfig,
            List<String> cryptoCurrencyAddressList,
            List<String> toAddressList,
            Long startTimestamp) {
        try {
            for (String toAddress : toAddressList) {
                String url = rpcConfig.getRpcUrl() + "/v2/accounts/{account_id}/events";
                String accountId = TonUtil.getAccountId(toAddress);
                url = url.replace("{account_id}", accountId);
                //
                //request
                Map<String, Object> requestParamMap = new HashMap<>();
                //curl -X 'GET' \
                //'https://tonapi.io/v2/accounts/0%3A44ca54babd057b310a8c9a5bf6a1ce043a56c5bfbb868ddfcce9d6fd860c00b3/events
                //?initiator=false
                //&subject_only=false
                //&limit=20
                //&start_date=1720539071\
                //-H 'accept: application/json' \
                //-H 'Accept-Language: ru-RU,ru;q=0.5'
                requestParamMap.put("initiator", "false");
                requestParamMap.put("subject_only", "false");
                requestParamMap.put("limit", "20");
                requestParamMap.put("start_date", startTimestamp - 1);
                //requestParamMap.put("start_date", 1720539072 - 1);//TODO 测试
                //requestParamMap.put("end_date", 1720606050 + 1);//TODO 测试
                //header
                Map<String, String> headerParamMap = new HashMap<>();
                headerParamMap.put("accept", "application/json");
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
                    TonApiResponseBody responseBody = JSONUtil.stringToObject(response, TonApiResponseBody.class);
                    if (ObjectUtils.isNull(responseBody)) {
                        return;
                    }
                    List<TonApiEvent> events = responseBody.getEvents();
                    if (ObjectUtils.isNull(events) || events.isEmpty()) {
                        return;
                    }
                    for (TonApiEvent event : events) {
                        List<TonApiAction> actions = event.getActions();
                        if (ObjectUtils.isNull(actions) || actions.isEmpty()) {
                            continue;
                        }
                        String eventId = event.getEventId();
                        Long timestamp = event.getTimestamp();
                        for (TonApiAction action : actions) {
                            String type = action.getType();
                            if (StringUtils.isEmpty(type)) {
                                continue;
                            }

                            if (type.equals(JETTON_TRANSFER)) {
                                TonApiJettonTransfer jettonTransfer = action.getJettonTransfer();
                                if (ObjectUtils.isNull(jettonTransfer)) {
                                    continue;
                                }
                                TonApiJetton jetton = jettonTransfer.getJetton();
                                if (ObjectUtils.isNull(jetton)) {
                                    continue;
                                }
                                String contractAddress = TonUtil.toChecksumAddress(isProduction, jetton.getAddress(), true);
                                //判断 jetton 合约
                                if (!cryptoCurrencyAddressList.contains(contractAddress)) {
                                    continue;
                                }
                                TransferTransactionBO transferTransactionBO = new TransferTransactionBO();
                                transferTransactionBO.setContractAddress(contractAddress);
                                //
                                String senderAddress = jettonTransfer.getSender().getAddress();
                                senderAddress = TonUtil.toChecksumAddress(isProduction, senderAddress, false);
                                transferTransactionBO.setFromAddress(senderAddress);
                                //
                                String recipientAddress = jettonTransfer.getRecipient().getAddress();
                                recipientAddress = TonUtil.toChecksumAddress(isProduction, recipientAddress, false);
                                transferTransactionBO.setToAddress(recipientAddress);
                                //
                                transferTransactionBO.setAmountRaw(jettonTransfer.getAmount());
                                transferTransactionBO.setTransactionHash(eventId);
                                transferTransactionBO.setTransactionTimestamp(timestamp);
                                transferTransactionBO.setTransactionTimestampUtc(timestamp * 1000);
                                transferTransactionBO.setTransactionStatus(action.getStatus().equals("ok")
                                        ? ChainTransactionStatusEnum.SUCCESSFUL.getStatus()
                                        : ChainTransactionStatusEnum.UNKNOWN.getStatus()
                                );
                                transferTransactionBO.setIsNative(false);
                                transferTransactionBO.setTonComment(jettonTransfer.getComment());
                                //
                                transferTransactionBOList.add(transferTransactionBO);
                            }
                            if (type.equals(TON_TRANSFER)) {
                                TonApiTonTransfer tonTransfer = action.getTonTransfer();
                                if (ObjectUtils.isNull(tonTransfer)) {
                                    continue;
                                }
                                TransferTransactionBO transferTransactionBO = new TransferTransactionBO();
                                transferTransactionBO.setContractAddress("");
                                //
                                String senderAddress = tonTransfer.getSender().getAddress();
                                senderAddress = TonUtil.toChecksumAddress(isProduction, senderAddress, false);
                                transferTransactionBO.setFromAddress(senderAddress);
                                //
                                String recipientAddress = tonTransfer.getRecipient().getAddress();
                                recipientAddress = TonUtil.toChecksumAddress(isProduction, recipientAddress, false);
                                transferTransactionBO.setToAddress(recipientAddress);
                                //
                                transferTransactionBO.setAmountRaw(tonTransfer.getAmount().toString());
                                transferTransactionBO.setTransactionHash(eventId);
                                transferTransactionBO.setTransactionTimestamp(timestamp);
                                transferTransactionBO.setTransactionTimestampUtc(timestamp * 1000);
                                transferTransactionBO.setTransactionStatus(action.getStatus().equals("ok")
                                        ? ChainTransactionStatusEnum.SUCCESSFUL.getStatus()
                                        : ChainTransactionStatusEnum.UNKNOWN.getStatus()
                                );
                                transferTransactionBO.setIsNative(true);
                                transferTransactionBO.setTonComment(tonTransfer.getComment());
                                //
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
            TransferTransactionBO transferTransactionBO,
            Boolean isProduction
    ) {
        try {
            String url = rpcConfig.getRpcUrl() + "/v2/events/" + transferTransactionBO.getTransactionHash();
            //request
            //curl -X 'GET' \
            //  'https://tonapi.io/v2/events/d159bc3a5d49103578519cd5b32564243457afde76004d594844f6703f80af59' \
            //  -H 'accept: application/json' \
            //  -H 'Accept-Language: ru-RU,ru;q=0.5'
            //header
            Map<String, String> headerParamMap = new HashMap<>();
            headerParamMap.put("accept", "application/json");
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
                TonApiEvent event = JSONUtil.stringToObject(response, TonApiEvent.class);
                if (ObjectUtils.isNull(event)) {
                    return ChainTransactionStatusEnum.UNKNOWN;
                }
                List<TonApiAction> actions = event.getActions();
                if (ObjectUtils.isNull(actions) || actions.isEmpty()) {
                    return ChainTransactionStatusEnum.UNKNOWN;
                }
                for (TonApiAction action : actions) {
                    String type = action.getType();
                    if (StringUtils.isEmpty(type)) {
                        continue;
                    }

                    if (type.equals(JETTON_TRANSFER)) {
                        TonApiJettonTransfer jettonTransfer = action.getJettonTransfer();
                        if (ObjectUtils.isNull(jettonTransfer)) {
                            continue;
                        }
                        TonApiJetton jetton = jettonTransfer.getJetton();
                        if (ObjectUtils.isNull(jetton)) {
                            continue;
                        }
                        //contract address
                        String contractAddress = TonUtil.toChecksumAddress(isProduction, jetton.getAddress(), true);
                        if (!StringUtils.equals(contractAddress, transferTransactionBO.getContractAddress())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //from address
                        String senderAddress = jettonTransfer.getSender().getAddress();
                        senderAddress = TonUtil.toChecksumAddress(isProduction, senderAddress, false);
                        if (!StringUtils.equals(senderAddress, transferTransactionBO.getFromAddress())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //to address
                        String recipientAddress = jettonTransfer.getRecipient().getAddress();
                        recipientAddress = TonUtil.toChecksumAddress(isProduction, recipientAddress, false);
                        if (!StringUtils.equals(recipientAddress, transferTransactionBO.getToAddress())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //amount
                        if (!StringUtils.equals(transferTransactionBO.getAmountRaw(), jettonTransfer.getAmount())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //comment
                        if (!StringUtils.equals(transferTransactionBO.getTonComment(), jettonTransfer.getComment())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //
                        return action.getStatus().equals("ok") ? ChainTransactionStatusEnum.SUCCESSFUL : ChainTransactionStatusEnum.UNKNOWN;
                    }

                    if (type.equals(TON_TRANSFER)) {
                        TonApiTonTransfer tonTransfer = action.getTonTransfer();
                        if (ObjectUtils.isNull(tonTransfer)) {
                            continue;
                        }
                        //from address
                        String senderAddress = tonTransfer.getSender().getAddress();
                        senderAddress = TonUtil.toChecksumAddress(isProduction, senderAddress, false);
                        if (!StringUtils.equals(senderAddress, transferTransactionBO.getFromAddress())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //to address
                        String recipientAddress = tonTransfer.getRecipient().getAddress();
                        recipientAddress = TonUtil.toChecksumAddress(isProduction, recipientAddress, false);
                        if (!StringUtils.equals(recipientAddress, transferTransactionBO.getToAddress())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //amount
                        if (!StringUtils.equals(transferTransactionBO.getAmountRaw(), tonTransfer.getAmount().toString())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //comment
                        if (!StringUtils.equals(transferTransactionBO.getTonComment(), tonTransfer.getComment())) {
                            return ChainTransactionStatusEnum.UNKNOWN;
                        }
                        //
                        return action.getStatus().equals("ok") ? ChainTransactionStatusEnum.SUCCESSFUL : ChainTransactionStatusEnum.UNKNOWN;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ChainTransactionStatusEnum.UNKNOWN;
    }

    /*public static void main(String[] args) {
        RpcConfigBO rpcConfig = new RpcConfigBO(
                true,
                "127.0.0.1",
                7890,
                "https://tonapi.io/",
                null,
                null,
                null,
                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
        );
        List<String> tonAddressList = new ArrayList<>();
        tonAddressList.add("UQBEylS6vQV7MQqMmlv2oc4EOlbFv7uGjd_M6db9hgwAs1Wo");
        //
        List<String> cryptoCurrencyAddressList = new ArrayList<>();
        cryptoCurrencyAddressList.add("EQCxE6mUtQJKFnGfaROTKOt1lZbDiiX1kCixRv7Nw2Id_sDs");
        //
        List<TransferTransactionBO> transferTransactionBOList = new ArrayList<>();
        fetchTonJettonTransaction(
                transferTransactionBOList,
                true,
                rpcConfig,
                cryptoCurrencyAddressList,
                tonAddressList,
                1719923586L
        );
        System.out.println(transferTransactionBOList.size());
    }*/

}