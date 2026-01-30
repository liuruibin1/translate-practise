//package com.xxx.chain.solana.utils;
//
//import com.alibaba.fastjson2.JSON;
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.xxx.chain.solana.config.SolanaConfig;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * Solana RPC 客户端工具类
// */
//@Slf4j
//public class SolanaRpcClient {
//
//    private final SolanaConfig config;
//    private final OkHttpClient httpClient;
//    private final String rpcUrl;
//
//    public SolanaRpcClient(SolanaConfig config) {
//        this.config = config;
//        this.rpcUrl = getRpcUrl();
//
//        this.httpClient = new OkHttpClient.Builder()
//            .connectTimeout(config.getNetwork().getConnectionTimeout(), TimeUnit.MILLISECONDS)
//            .readTimeout(config.getNetwork().getReadTimeout(), TimeUnit.MILLISECONDS)
//            .writeTimeout(config.getNetwork().getReadTimeout(), TimeUnit.MILLISECONDS)
//            .build();
//    }
//
//    /**
//     * 获取RPC URL
//     */
//    private String getRpcUrl() {
//        switch (config.getNetwork().getNetworkType()) {
//            case "mainnet-beta":
//                return config.getNetwork().getRpcUrl();
//            case "testnet":
//                return config.getNetwork().getTestnetRpcUrl();
//            case "devnet":
//                return config.getNetwork().getDevnetRpcUrl();
//            default:
//                return config.getNetwork().getRpcUrl();
//        }
//    }
//
//    /**
//     * 获取最新区块高度
//     */
//    public Long getLatestSlot() {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getSlot");
//            request.put("params", new JSONArray());
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    return jsonResponse.getLong("result");
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取最新区块高度失败", e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取区块信息
//     */
//    public JSONObject getBlock(Long slot) {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getBlock");
//
//            JSONArray params = new JSONArray();
//            params.add(slot);
//            params.add(new JSONObject().fluentPut("encoding", "json").fluentPut("maxSupportedTransactionVersion", 0));
//            request.put("params", params);
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    return jsonResponse.getJSONObject("result");
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取区块信息失败, slot: {}", slot, e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取交易信息
//     */
//    public JSONObject getTransaction(String signature) {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getTransaction");
//
//            JSONArray params = new JSONArray();
//            params.add(signature);
//            params.add(new JSONObject().fluentPut("encoding", "json").fluentPut("maxSupportedTransactionVersion", 0));
//            request.put("params", params);
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    return jsonResponse.getJSONObject("result");
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取交易信息失败, signature: {}", signature, e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取账户信息
//     */
//    public JSONObject getAccountInfo(String address) {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getAccountInfo");
//
//            JSONArray params = new JSONArray();
//            params.add(address);
//            params.add(new JSONObject().fluentPut("encoding", "json"));
//            request.put("params", params);
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    return jsonResponse.getJSONObject("result");
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取账户信息失败, address: {}", address, e);
//        }
//        return null;
//    }
//
//    /**
//     * 获取代币账户余额
//     */
//    public BigDecimal getTokenAccountBalance(String tokenAccountAddress) {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getTokenAccountBalance");
//
//            JSONArray params = new JSONArray();
//            params.add(tokenAccountAddress);
//            request.put("params", params);
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    JSONObject result = jsonResponse.getJSONObject("result");
//                    JSONObject value = result.getJSONObject("value");
//                    if (value != null) {
//                        String amount = value.getString("amount");
//                        Integer decimals = value.getInteger("decimals");
//                        if (StringUtils.isNotBlank(amount) && decimals != null) {
//                            return new BigDecimal(amount).divide(BigDecimal.valueOf(Math.pow(10, decimals)));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取代币账户余额失败, address: {}", tokenAccountAddress, e);
//        }
//        return BigDecimal.ZERO;
//    }
//
//    /**
//     * 获取账户的代币账户列表
//     */
//    public List<String> getTokenAccountsByOwner(String ownerAddress, String mintAddress) {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getTokenAccountsByOwner");
//
//            JSONArray params = new JSONArray();
//            params.add(ownerAddress);
//
//            JSONObject mintFilter = new JSONObject();
//            mintFilter.put("mint", mintAddress);
//
//            JSONObject config = new JSONObject();
//            config.put("encoding", "json");
//
//            params.add(mintFilter);
//            params.add(config);
//            request.put("params", params);
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    JSONObject result = jsonResponse.getJSONObject("result");
//                    JSONArray accounts = result.getJSONArray("value");
//                    List<String> tokenAccounts = new ArrayList<>();
//
//                    for (int i = 0; i < accounts.size(); i++) {
//                        JSONObject account = accounts.getJSONObject(i);
//                        String pubkey = account.getString("pubkey");
//                        if (StringUtils.isNotBlank(pubkey)) {
//                            tokenAccounts.add(pubkey);
//                        }
//                    }
//                    return tokenAccounts;
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取账户代币账户列表失败, owner: {}, mint: {}", ownerAddress, mintAddress, e);
//        }
//        return new ArrayList<>();
//    }
//
//    /**
//     * 获取签名状态
//     */
//    public String getSignatureStatuses(List<String> signatures) {
//        try {
//            JSONObject request = new JSONObject();
//            request.put("jsonrpc", "2.0");
//            request.put("id", 1);
//            request.put("method", "getSignatureStatuses");
//
//            JSONArray params = new JSONArray();
//            params.add(signatures);
//            params.add(new JSONObject().fluentPut("searchTransactionHistory", true));
//            request.put("params", params);
//
//            String response = sendRequest(request);
//            if (StringUtils.isNotBlank(response)) {
//                JSONObject jsonResponse = JSON.parseObject(response);
//                if (jsonResponse.containsKey("result")) {
//                    return response;
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取签名状态失败, signatures: {}", signatures, e);
//        }
//        return null;
//    }
//
//    /**
//     * 发送RPC请求
//     */
//    private String sendRequest(JSONObject request) throws IOException {
//        RequestBody body = RequestBody.create(
//            MediaType.parse("application/json"),
//            request.toJSONString()
//        );
//
//        Request httpRequest = new Request.Builder()
//            .url(rpcUrl)
//            .post(body)
//            .addHeader("Content-Type", "application/json")
//            .build();
//
//        try (Response response = httpClient.newCall(httpRequest).execute()) {
//            if (response.isSuccessful() && response.body() != null) {
//                return response.body().string();
//            } else {
//                log.error("RPC请求失败, code: {}, message: {}", response.code(), response.message());
//            }
//        } catch (Exception e) {
//            log.error("发送RPC请求异常", e);
//        }
//        return null;
//    }
//
//    /**
//     * 批量获取交易信息
//     */
//    public List<JSONObject> getTransactions(List<String> signatures) {
//        List<JSONObject> transactions = new ArrayList<>();
//
//        // 分批处理，避免请求过大
//        int batchSize = config.getMonitor().getBatchSize();
//        for (int i = 0; i < signatures.size(); i += batchSize) {
//            int end = Math.min(i + batchSize, signatures.size());
//            List<String> batch = signatures.subList(i, end);
//
//            try {
//                JSONObject request = new JSONObject();
//                request.put("jsonrpc", "2.0");
//                request.put("id", 1);
//                request.put("method", "getTransactions");
//
//                JSONArray params = new JSONArray();
//                params.add(batch);
//                params.add(new JSONObject().fluentPut("encoding", "json").fluentPut("maxSupportedTransactionVersion", 0));
//                request.put("params", params);
//
//                String response = sendRequest(request);
//                if (StringUtils.isNotBlank(response)) {
//                    JSONObject jsonResponse = JSON.parseObject(response);
//                    if (jsonResponse.containsKey("result")) {
//                        JSONArray results = jsonResponse.getJSONArray("result");
//                        for (int j = 0; j < results.size(); j++) {
//                            JSONObject result = results.getJSONObject(j);
//                            if (result != null) {
//                                transactions.add(result);
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                log.error("批量获取交易信息失败, batch: {}", batch, e);
//            }
//        }
//
//        return transactions;
//    }
//
//    /**
//     * 关闭客户端
//     */
//    public void close() {
//        if (httpClient != null) {
//            httpClient.dispatcher().executorService().shutdown();
//            httpClient.connectionPool().evictAll();
//        }
//    }
//}
