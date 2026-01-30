//package com.xxx.chain.tron.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xxx.chain.tron.config.TronConfig;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.concurrent.TimeUnit;
//
///**
// * Tron HTTP客户端服务
// * 用于与Tron网络API交互
// */
//@Slf4j
//public class TronHttpClient {
//
//    private final TronConfig config;
//    private final OkHttpClient httpClient;
//    private final ObjectMapper objectMapper;
//    private final String baseUrl;
//
//    public TronHttpClient(TronConfig config) {
//        this.config = config;
//        this.baseUrl = config.getCurrentApiUrl();
//        this.objectMapper = new ObjectMapper();
//
//        // 配置HTTP客户端
//        this.httpClient = new OkHttpClient.Builder()
//            .connectTimeout(config.getNetwork().getTimeout(), TimeUnit.MILLISECONDS)
//            .readTimeout(config.getNetwork().getTimeout(), TimeUnit.MILLISECONDS)
//            .writeTimeout(config.getNetwork().getTimeout(), TimeUnit.MILLISECONDS)
//            .build();
//
//        log.info("Tron HTTP客户端初始化完成，基础URL: {}", baseUrl);
//    }
//
//    /**
//     * 获取最新区块信息
//     */
//    public JsonNode getLatestBlock() throws IOException {
//        String url = baseUrl + "/v1/blocks/latest";
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 根据区块号获取区块信息
//     */
//    public JsonNode getBlockByNumber(long blockNumber) throws IOException {
//        String url = baseUrl + "/v1/blocks/" + blockNumber;
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 根据区块哈希获取区块信息
//     */
//    public JsonNode getBlockByHash(String blockHash) throws IOException {
//        String url = baseUrl + "/v1/blocks/hash/" + blockHash;
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 获取账户信息
//     */
//    public JsonNode getAccountInfo(String address) throws IOException {
//        String url = baseUrl + "/v1/accounts/" + address;
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 获取账户TRX余额
//     */
//    public BigInteger getTrxBalance(String address) throws IOException {
//        JsonNode accountInfo = getAccountInfo(address);
//        if (accountInfo != null && accountInfo.has("balance")) {
//            return new BigInteger(accountInfo.get("balance").asText());
//        }
//        return BigInteger.ZERO;
//    }
//
//    /**
//     * 获取账户USDT余额
//     */
//    public BigInteger getUsdtBalance(String address) throws IOException {
//        String url = baseUrl + "/v1/contracts/" + config.getCurrentUsdtAddress() + "/balanceOf";
//
//        // 构建请求体
//        String requestBody = String.format("{\"address\":\"%s\"}", address);
//
//        try {
//            JsonNode response = executePostRequest(url, requestBody);
//            if (response != null && response.has("data")) {
//                String balanceHex = response.get("data").asText();
//                if (balanceHex.startsWith("0x")) {
//                    balanceHex = balanceHex.substring(2);
//                }
//                return new BigInteger(balanceHex, 16);
//            }
//        } catch (Exception e) {
//            log.error("获取USDT余额失败，地址: {}", address, e);
//        }
//
//        return BigInteger.ZERO;
//    }
//
//    /**
//     * 获取交易信息
//     */
//    public JsonNode getTransactionInfo(String txId) throws IOException {
//        String url = baseUrl + "/v1/transactions/" + txId;
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 获取区块中的交易
//     */
//    public JsonNode getBlockTransactions(long blockNumber) throws IOException {
//        String url = baseUrl + "/v1/blocks/" + blockNumber + "/transactions";
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 获取账户交易历史
//     */
//    public JsonNode getAccountTransactions(String address, int limit, long minTimestamp) throws IOException {
//        String url = baseUrl + "/v1/accounts/" + address + "/transactions";
//        url += "?limit=" + limit + "&min_timestamp=" + minTimestamp;
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 获取合约事件日志
//     */
//    public JsonNode getContractEvents(String contractAddress, String eventName,
//                                    long fromBlock, long toBlock, String address) throws IOException {
//        String url = baseUrl + "/v1/contracts/" + contractAddress + "/events";
//        url += "?event_name=" + eventName;
//        url += "&from_block=" + fromBlock;
//        url += "&to_block=" + toBlock;
//        if (address != null) {
//            url += "&address=" + address;
//        }
//
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 获取USDT Transfer事件
//     */
//    public JsonNode getUsdtTransferEvents(long fromBlock, long toBlock, String toAddress) throws IOException {
//        return getContractEvents(
//            config.getCurrentUsdtAddress(),
//            "Transfer",
//            fromBlock,
//            toBlock,
//            toAddress
//        );
//    }
//
//    /**
//     * 执行GET请求
//     */
//    private JsonNode executeGetRequest(String url) throws IOException {
//        Request.Builder requestBuilder = new Request.Builder()
//            .url(url)
//            .get();
//
//        // 添加API Key（如果有）
//        if (config.getNetwork().getApiKey() != null &&
//            !config.getNetwork().getApiKey().equals("YOUR_TRON_API_KEY")) {
//            requestBuilder.addHeader("TRON-PRO-API-KEY", config.getNetwork().getApiKey());
//        }
//
//        Request request = requestBuilder.build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("HTTP请求失败: " + response.code() + " " + response.message());
//            }
//
//            String responseBody = response.body().string();
//            if (responseBody == null || responseBody.isEmpty()) {
//                return null;
//            }
//
//            return objectMapper.readTree(responseBody);
//
//        } catch (Exception e) {
//            log.error("执行GET请求失败，URL: {}", url, e);
//            throw e;
//        }
//    }
//
//    /**
//     * 执行POST请求
//     */
//    private JsonNode executePostRequest(String url, String requestBody) throws IOException {
//        RequestBody body = RequestBody.create(
//            requestBody,
//            MediaType.parse("application/json")
//        );
//
//        Request.Builder requestBuilder = new Request.Builder()
//            .url(url)
//            .post(body);
//
//        // 添加API Key（如果有）
//        if (config.getNetwork().getApiKey() != null &&
//            !config.getNetwork().getApiKey().equals("YOUR_TRON_API_KEY")) {
//            requestBuilder.addHeader("TRON-PRO-API-KEY", config.getNetwork().getApiKey());
//        }
//
//        Request request = requestBuilder.build();
//
//        try (Response response = httpClient.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new IOException("HTTP请求失败: " + response.code() + " " + response.message());
//            }
//
//            String responseBody = response.body().string();
//            if (responseBody == null || responseBody.isEmpty()) {
//                return null;
//            }
//
//            return objectMapper.readTree(responseBody);
//
//        } catch (Exception e) {
//            log.error("执行POST请求失败，URL: {}, Body: {}", url, requestBody, e);
//            throw e;
//        }
//    }
//
//    /**
//     * 检查网络连接
//     */
//    public boolean checkConnection() {
//        try {
//            getLatestBlock();
//            return true;
//        } catch (Exception e) {
//            log.error("检查Tron网络连接失败", e);
//            return false;
//        }
//    }
//
//    /**
//     * 获取网络状态
//     */
//    public JsonNode getNetworkStatus() throws IOException {
//        String url = baseUrl + "/v1/network";
//        return executeGetRequest(url);
//    }
//
//    /**
//     * 关闭HTTP客户端
//     */
//    public void shutdown() {
//        if (httpClient != null) {
//            httpClient.dispatcher().executorService().shutdown();
//            httpClient.connectionPool().evictAll();
//        }
//        log.info("Tron HTTP客户端已关闭");
//    }
//}
