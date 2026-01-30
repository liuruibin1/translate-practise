package com.xxx.chain.tron.utils;

import com.alibaba.fastjson2.JSON;
import com.google.protobuf.ByteString;
import com.xxx.chain.adapter.bo.ChainBlockBO;
import com.xxx.chain.adapter.bo.CryptoAccountBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.adapter.bo.TransferTransactionBO;
import com.xxx.chain.constant.PrivateKey;
import com.xxx.chain.enumerate.ChainTransactionStatusEnum;
import com.xxx.chain.tron.bo.Block;
import com.xxx.chain.tron.bo.Contract;
import com.xxx.chain.tron.bo.Transaction;
import com.xxx.chain.tron.bo.Value;
import com.xxx.common.core.utils.HttpAsyncClientUtil;
import com.xxx.common.core.utils.HttpClientUtil;
import com.xxx.common.core.utils.JSONUtil;
import com.xxx.common.core.utils.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tron.trident.abi.datatypes.Address;
import org.tron.trident.core.ApiWrapper;
import org.tron.trident.core.contract.Trc20Contract;
import org.tron.trident.core.exceptions.IllegalException;
import org.tron.trident.core.key.KeyPair;
import org.tron.trident.crypto.SECP256K1;
import org.tron.trident.proto.Chain;
import org.tron.trident.proto.Response;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import static com.xxx.common.core.utils.HttpAsyncClientUtil.postAsyncWithCallback;
import static org.tron.trident.core.ApiWrapper.parseAddress;

public class TronUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TronUtil.class);

    private static final Map<String, String> REQUEST_HEADER = Map.of(
            "accept", "application/json",
            "content-type", "application/json"
    );

    // 常量定义
    private static final int DEFAULT_BATCH_SIZE = 50;
    private static final int MAX_RETRY_COUNT = 10;
    private static final long BATCH_DELAY_MS = 1000;
    private static final int THREAD_POOL_SIZE = 10;
    private static final String TRANSFER_METHOD_ID = "a9059cbb";
    private static final int TRANSFER_DATA_LENGTH = 136;
    private static final String TRANSFER_CONTRACT = "TransferContract";
    private static final String TRIGGER_SMART_CONTRACT = "TriggerSmartContract";

    // 线程池用于并发处理
    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(THREAD_POOL_SIZE, r -> {
                Thread t = new Thread(r, "TronUtil-Worker");
                t.setDaemon(true);
                return t;
            });

    // secp256k1 曲线的阶 (N)
    private static final BigInteger SECP256K1_N = new BigInteger(
            "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);

    /**
     * 判断一个十六进制字符串是否是合法的 TRON 私钥
     * Accepts with or without 0x prefix, case-insensitive.
     */
    public static boolean isValidPrivateKeyHex(String hex) {
        if (hex == null) return false;
        hex = hex.trim();
        if (hex.startsWith("0x") || hex.startsWith("0X")) {
            hex = hex.substring(2);
        }
        // 长度检查
        if (hex.isEmpty() || hex.length() > 64) return false;
        // 是否 hex 格式
        if (!hex.matches("(?i)^[0-9a-f]+$")) return false;

        BigInteger priv;
        try {
            priv = new BigInteger(hex, 16);
        } catch (NumberFormatException e) {
            return false;
        }
        // 校验范围：0 < priv < N
        return priv.compareTo(BigInteger.ZERO) > 0 && priv.compareTo(SECP256K1_N) < 0;
    }

    /**
     * 验证 TRON 地址格式是否正确
     */
    public static boolean isValidAddress(String address) {
        if (!StringUtils.hasText(address)) {
            return false;
        }

        try {
            ByteString byteString = parseAddress(address);
            Address parsedAddress = new Address(ApiWrapper.toHex(byteString));
            return parsedAddress.getValue().equals(address);
        } catch (Exception e) {
            LOGGER.debug("地址验证失败: {}", address, e);
            return false;
        }
    }

    /**
     * 生成 TRON 私钥和地址对
     */
    public static Optional<CryptoAccountBO> generatePrivateKeyAndAddress() {
        try {
            SECP256K1.KeyPair keyPair = SECP256K1.KeyPair.generate();
            String privateKey = Hex.toHexString(keyPair.getPrivateKey().getEncoded());
            String address = KeyPair.publicKeyToBase58CheckAddress(keyPair.getPublicKey());

            if (StringUtils.hasText(privateKey) && StringUtils.hasText(address)) {
                CryptoAccountBO account = new CryptoAccountBO();
                account.setPrivateKey(privateKey);
                account.setAddress(address);
                return Optional.of(account);
            }
        } catch (Exception e) {
            LOGGER.error("生成密钥对失败", e);
        }

        return Optional.empty();
    }

    /**
     * 根据私钥获取地址
     */
    public static Optional<String> getAddressByPrivateKey(String privateKey) {
        if (!StringUtils.hasText(privateKey)) {
            return Optional.empty();
        }

        try {
            KeyPair keyPair = new KeyPair(privateKey);
            String address = KeyPair.publicKeyToBase58CheckAddress(keyPair.getRawPair().getPublicKey());
            return Optional.of(address);
        } catch (Exception e) {
            LOGGER.error("根据私钥获取地址失败: {}", privateKey, e);
            return Optional.empty();
        }
    }

    /**
     * 获取最新区块信息
     */
    public static ChainBlockBO getLatestBlock(RpcConfigBO rpcConfig) throws Exception {
        Objects.requireNonNull(rpcConfig, "RPC配置不能为空");
        Objects.requireNonNull(rpcConfig.getRpcUrl(), "RPC URL不能为空");

        ApiWrapper apiWrapper = createApiWrapper(rpcConfig);
        Chain.Block nowBlock = apiWrapper.getNowBlock();
        Chain.BlockHeader.raw rawData = nowBlock.getBlockHeader().getRawData();

        ChainBlockBO blockInfo = new ChainBlockBO();
        blockInfo.setBlockNumber(rawData.getNumber());
        blockInfo.setBlockHash(ApiWrapper.toHex(rawData.getParentHash()));
        blockInfo.setBlockTimestamp(rawData.getTimestamp());

        return blockInfo;
    }

    /**
     * 查询交易状态
     */
    public static ChainTransactionStatusEnum queryTransactionStatus(RpcConfigBO rpcConfig, String txHash) {
        if (!StringUtils.hasText(txHash)) {
            LOGGER.warn("交易哈希为空");
            return ChainTransactionStatusEnum.UNKNOWN;
        }

        try {
            String url = String.format("%s/walletsolidity/gettransactionbyid?value=%s",
                    rpcConfig.getRpcUrl(), txHash);

            String jsonString = HttpClientUtil.doGet(
                    url,
                    null,
                    null,
                    rpcConfig.isEnableProxy(),
                    rpcConfig.getProxyHost(),
                    rpcConfig.getProxyPort());

            return parseTransactionStatus(jsonString);
        } catch (Exception e) {
            LOGGER.error("查询交易状态失败: {}", txHash, e);
            return ChainTransactionStatusEnum.UNKNOWN;
        }
    }

    /**
     * 批量获取转账交易信息
     */
    public static List<TransferTransactionBO> fetchTransferTransactions(RpcConfigBO rpcConfig, long fromBlockNumber, long toBlockNumber) throws Exception {

        if (fromBlockNumber > toBlockNumber) {
            throw new IllegalArgumentException("起始区块号不能大于结束区块号");
        }
        if (fromBlockNumber < 0) {
            throw new IllegalArgumentException("区块号不能为负数");
        }

        List<TransferTransactionBO> transferTransactions = new ArrayList<>();
        List<Transaction> transactions = fetchTransactionsFromBlocks(rpcConfig, fromBlockNumber, toBlockNumber);

        // 并行解析交易
        transactions.parallelStream()
                .map(TronUtil::parseTransaction)
                .filter(Objects::nonNull)
                .forEach(transferTransactions::add);

        LOGGER.info("成功解析 {} 个转账交易", transferTransactions.size());
        return new ArrayList<>(transferTransactions);
    }

    /**
     * 从区块范围内获取所有交易
     */
    private static List<Transaction> fetchTransactionsFromBlocks(
            RpcConfigBO rpcConfig,
            long fromBlockNumber,
            long toBlockNumber
    ) throws Exception {

        List<Long> blockNumberList = LongStream.rangeClosed(fromBlockNumber, toBlockNumber)
                .boxed()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        List<Transaction> allTransactionList = new ArrayList<>();
        Set<Long> failedBlockList = ConcurrentHashMap.newKeySet();

        // 分批处理区块
        processBatchesWithRetry(rpcConfig, blockNumberList, allTransactionList, failedBlockList);

        if (!failedBlockList.isEmpty()) {
            //LOGGER.warn("有 {} 个区块获取失败: {}", failedBlockList.size(), failedBlockList);
            //重试多次，还是有区块抓取错误，需要抛出异常
            throw new Exception(String.format("有 %d 个区块获取失败: %s", failedBlockList.size(), failedBlockList));
        }

        return new ArrayList<>(allTransactionList);
    }

    /**
     * 分批处理区块，包含重试机制
     */
    private static void processBatchesWithRetry(
            RpcConfigBO rpcConfig,
            List<Long> blockNumberList,
            Collection<Transaction> allTransactions,
            Set<Long> failedBlockList) throws Exception {

        // 分批处理
        List<List<Long>> batcheList = partitionList(blockNumberList);

        for (int i = 0; i < batcheList.size(); i++) {
            List<Long> batch = batcheList.get(i);
            Set<Long> batchFailedBlockList = processBatch(rpcConfig, batch, allTransactions);
            failedBlockList.addAll(batchFailedBlockList);

            // 批次间延迟
            if (i < batcheList.size() - 1) {
                Thread.sleep(BATCH_DELAY_MS);
            }
        }

        // 重试失败的区块
        retryFailedBlockList(rpcConfig, failedBlockList, allTransactions);
    }

    /**
     * 处理单个批次的区块
     */
    private static Set<Long> processBatch(
            RpcConfigBO rpcConfig,
            List<Long> blockNumberList,
            Collection<Transaction> allTransactionList) throws Exception {

        Set<Long> failedBlockList = ConcurrentHashMap.newKeySet();

        try (CloseableHttpAsyncClient client = createHttpClient(rpcConfig)) {
            client.start();

            CountDownLatch latch = new CountDownLatch(blockNumberList.size());
            AtomicInteger successCount = new AtomicInteger();

            for (Long blockNumber : blockNumberList) {
                processBlockAsync(client, rpcConfig, blockNumber, allTransactionList, failedBlockList, latch, successCount);
            }

            latch.await();
            LOGGER.info("批次处理完成，成功: {}, 失败: {}", successCount.get(), failedBlockList.size());
        }

        return failedBlockList;
    }

    /**
     * 异步处理单个区块
     */
    private static void processBlockAsync(
            CloseableHttpAsyncClient client,
            RpcConfigBO rpcConfig,
            Long blockNumber,
            Collection<Transaction> allTransactionList,
            Set<Long> failedBlockList,
            CountDownLatch countDownLatch,
            AtomicInteger successCount) {

        String jsonData = String.format("{\"num\":%d}", blockNumber);
        String url = rpcConfig.getRpcUrl() + "/wallet/getblockbynum";

        postAsyncWithCallback(client, url, jsonData, REQUEST_HEADER, new FutureCallback<>() {
            @Override
            public void completed(HttpResponse response) {
                try {
                    processBlockResponse(response, blockNumber, allTransactionList, successCount);
                } catch (Exception e) {
                    LOGGER.error("处理区块 {} 响应失败", blockNumber, e);
                    failedBlockList.add(blockNumber);
                } finally {
                    countDownLatch.countDown();
                }
            }

            @Override
            public void failed(Exception ex) {
                LOGGER.error("区块 {} 请求失败", blockNumber, ex);
                failedBlockList.add(blockNumber);
                countDownLatch.countDown();
            }

            @Override
            public void cancelled() {
                LOGGER.warn("区块 {} 请求被取消", blockNumber);
                failedBlockList.add(blockNumber);
                countDownLatch.countDown();
            }
        });
    }

    /**
     * 处理区块响应
     */
    private static void processBlockResponse(
            HttpResponse response,
            Long blockNumber,
            Collection<Transaction> allTransactions,
            AtomicInteger successCount) throws Exception {

        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        if (!JSON.isValid(responseBody)) {
            throw new IllegalStateException("响应格式错误");
        }

        Block block = JSONUtil.stringToObject(responseBody, Block.class);
        if (block == null || block.getTransactions() == null) {
            LOGGER.debug("区块 {} 无交易数据", blockNumber);
            return;
        }

        // 设置交易的区块信息
        List<Transaction> transactions = block.getTransactions();
        transactions.forEach(tx -> {
            tx.setBlockNumber(block.getBlockHeader().getRawData().getBlockNumber());
            tx.setBlockHash(block.getBlockID());
            tx.setBlockTimeStamp(block.getBlockHeader().getRawData().getTimestamp());
        });

        allTransactions.addAll(transactions);
        successCount.incrementAndGet();

        LOGGER.debug("区块 {} 处理成功，获得 {} 个交易", blockNumber, transactions.size());
    }

    /**
     * 重试失败的区块
     */
    private static void retryFailedBlockList(
            RpcConfigBO rpcConfig,
            Set<Long> failedBlockList,
            Collection<Transaction> allTransactions) throws Exception {

        List<Long> retryList = new ArrayList<>(failedBlockList);
        int retryCount = 0;

        while (!retryList.isEmpty() && retryCount < MAX_RETRY_COUNT) {
            LOGGER.info("第 {} 次重试，重试 {} 个区块", retryCount + 1, retryList.size());

            Set<Long> currentFailedBlockList = processBatch(rpcConfig, retryList, allTransactions);
            retryList = new ArrayList<>(currentFailedBlockList);

            if (!retryList.isEmpty()) {
                Thread.sleep(BATCH_DELAY_MS);
            }

            retryCount++;
        }

        failedBlockList.clear();
        failedBlockList.addAll(retryList);
    }

    /**
     * 解析交易为转账交易对象
     */
    private static TransferTransactionBO parseTransaction(Transaction transaction) {
        if (!isValidTransaction(transaction)) {
            return null;
        }

        Contract contract = transaction.getRawData().getContract().get(0);
        String contractType = contract.getType();
        Value value = contract.getParameter().getValue();

        return switch (contractType) {
            case TRANSFER_CONTRACT -> parseTrxTransaction(transaction, value);
            case TRIGGER_SMART_CONTRACT -> parseTrc20Transaction(transaction, value);
            default -> null;
        };
    }

    /**
     * 解析 TRX 转账交易
     */
    private static TransferTransactionBO parseTrxTransaction(Transaction transaction, Value value) {
        if (!StringUtils.hasText(value.getAmount()) ||
                !StringUtils.hasText(value.getOwnerAddress()) ||
                !StringUtils.hasText(value.getToAddress())) {
            return null;
        }

        TransferTransactionBO transferTx = createBaseTransferTransaction(transaction);
        transferTx.setContractAddress("");
        transferTx.setFromAddress(new Address(value.getOwnerAddress()).getValue());
        transferTx.setToAddress(new Address(value.getToAddress()).getValue());
        transferTx.setAmountRaw(value.getAmount());
        transferTx.setIsNative(true);

        return transferTx;
    }

    /**
     * 解析 TRC20 转账交易
     */
    private static TransferTransactionBO parseTrc20Transaction(Transaction transaction, Value value) {
        if (!StringUtils.hasText(value.getContractAddress()) ||
                !StringUtils.hasText(value.getOwnerAddress()) ||
                !StringUtils.hasText(value.getData())) {
            return null;
        }

        String data = value.getData();
        if (!isValidTransferData(data)) {
            return null;
        }

        TransferTransactionBO transferTx = createBaseTransferTransaction(transaction);
        transferTx.setContractAddress(new Address(value.getContractAddress()).getValue());
        transferTx.setFromAddress(new Address(value.getOwnerAddress()).getValue());
        transferTx.setIsNative(false);

        // 解析转账目标地址和金额
        parseTransferData(data, transferTx);

        return transferTx;
    }

    /**
     * 创建基础转账交易对象
     */
    private static TransferTransactionBO createBaseTransferTransaction(Transaction transaction) {
        TransferTransactionBO transferTx = new TransferTransactionBO();
        transferTx.setBlockNumber(transaction.getBlockNumber());
        transferTx.setBlockHash(transaction.getBlockHash());
        transferTx.setTransactionHash(transaction.getTxID());
        transferTx.setTransactionTimestamp(transaction.getBlockTimeStamp());
        transferTx.setTransactionTimestampUtc(transaction.getBlockTimeStamp());
        transferTx.setTransactionStatus(ChainTransactionStatusEnum.UNKNOWN.getStatus());
        return transferTx;
    }

    /**
     * 解析转账数据中的地址和金额
     */
    private static void parseTransferData(String data, TransferTransactionBO transferTx) {
        try {
            String toAddressHex = data.substring(9, 72).replace("000000000000000000000", "");
            Address toAddress = new Address(toAddressHex);
            transferTx.setToAddress(toAddress.getValue());

            String amountHex = data.substring(73, 136);
            BigInteger amount = new BigInteger(amountHex, 16);
            transferTx.setAmountRaw(amount.toString());
        } catch (Exception e) {
            LOGGER.error("解析转账数据失败: {}", data, e);
        }
    }

    /**
     * 验证交易是否有效
     */
    private static boolean isValidTransaction(Transaction transaction) {
        return transaction != null &&
                transaction.getRet() != null &&
                !transaction.getRet().isEmpty() &&
                "SUCCESS".equals(transaction.getRet().get(0).getContractRet()) &&
                transaction.getRawData() != null &&
                transaction.getRawData().getContract() != null &&
                !transaction.getRawData().getContract().isEmpty() &&
                transaction.getRawData().getContract().get(0) != null &&
                transaction.getRawData().getContract().get(0).getParameter() != null &&
                transaction.getRawData().getContract().get(0).getParameter().getValue() != null;
    }

    /**
     * 验证是否为有效的转账数据
     */
    private static boolean isValidTransferData(String data) {
        return data.length() == TRANSFER_DATA_LENGTH &&
                data.substring(0, 8).equalsIgnoreCase(TRANSFER_METHOD_ID);
    }

    /**
     * 解析交易状态
     */
    private static ChainTransactionStatusEnum parseTransactionStatus(String jsonString) {
        if (!StringUtils.hasText(jsonString)) {
            return ChainTransactionStatusEnum.UNKNOWN;
        }

        try {
            Transaction transaction = JSONUtil.stringToObject(jsonString, Transaction.class);
            if (transaction != null &&
                    transaction.getRet() != null &&
                    !transaction.getRet().isEmpty() &&
                    "SUCCESS".equals(transaction.getRet().get(0).getContractRet())) {
                return ChainTransactionStatusEnum.SUCCESSFUL;
            }
            return ChainTransactionStatusEnum.FAILED;
        } catch (Exception e) {
            LOGGER.error("解析交易状态失败", e);
            return ChainTransactionStatusEnum.UNKNOWN;
        }
    }

    public static String trc20Balance(ApiWrapper wrapper, String accountAddress, String trc20Address) {
        org.tron.trident.core.contract.Contract contract = wrapper.getContract(trc20Address);
        Trc20Contract trc20Contract = new Trc20Contract(contract, accountAddress, wrapper);
        BigInteger fromBalance = trc20Contract.balanceOf(accountAddress);
        return fromBalance.toString();
    }

    public static String trxBalance(ApiWrapper wrapper, String accountAddress) {
        return wrapper.getAccountBalance(accountAddress) + "";
    }

    public static String transferTRC20(ApiWrapper wrapper, String fromAddress, String toAddress, String amountRaw, String trc20Address) {
        String fromBalanceRaw = trc20Balance(wrapper, trc20Address, fromAddress);
        if (new BigInteger(fromBalanceRaw).compareTo(new BigInteger(amountRaw)) > 0) {
            org.tron.trident.core.contract.Contract contract = wrapper.getContract(trc20Address);
            Trc20Contract trc20Contract = new Trc20Contract(contract, fromAddress, wrapper);
            return trc20Contract.transfer(toAddress, Long.parseLong(amountRaw), 1000000, "transfer", 10000000);
        } else {
            return null;
        }
    }

    public static String transferTRX(ApiWrapper wrapper, String fromAddress, String toAddress, String amountRaw) throws IllegalException {
        String accountBalanceRaw = trxBalance(wrapper, fromAddress);
        if (new BigInteger(accountBalanceRaw).compareTo(new BigInteger(amountRaw)) > 0) {
            Response.TransactionExtention transaction = wrapper.transfer(fromAddress, toAddress, Long.parseLong(amountRaw));
            //签名
            Chain.Transaction signTransaction = wrapper.signTransaction(transaction);
            long bandwidth = wrapper.estimateBandwidth(signTransaction);
            if (bandwidth > 500) {
                return null;
            } else {
                return wrapper.broadcastTransaction(signTransaction);
            }
        } else {
            return null;
        }
    }

    /**
     * 创建 ApiWrapper
     */
    public static ApiWrapper createApiWrapper(RpcConfigBO rpcConfig) {
        if (rpcConfig.getRpcUrl().toLowerCase().contains("shasta")) {
            return ApiWrapper.ofShasta(PrivateKey.EMPTY);
        } else {
            return ApiWrapper.ofMainnet(PrivateKey.EMPTY, rpcConfig.getApiKey());
        }
    }

    /**
     * 创建 HTTP 客户端
     */
    private static CloseableHttpAsyncClient createHttpClient(RpcConfigBO rpcConfig) throws IOReactorException {
        return HttpAsyncClientUtil.createHttpClient(
                rpcConfig.isEnableProxy(),
                rpcConfig.getProxyHost(),
                rpcConfig.getProxyPort()
        );
    }

    /**
     * 将列表分割为指定大小的批次
     */
    private static <T> List<List<T>> partitionList(List<T> list) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += DEFAULT_BATCH_SIZE) {
            int end = Math.min(i + DEFAULT_BATCH_SIZE, list.size());
            batches.add(list.subList(i, end));
        }
        return batches;
    }

    /**
     * 关闭线程池（在应用关闭时调用）
     */
    public static void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    //    public static void fetchTransferTransaction(
    //            List<TransferTransactionBO> transferTransactionBOList,
    //            RpcConfigBO rpcConfigBO,
    //            long fromBlockNumber,
    //            long toBlockNumber
    //    ) throws Exception {
    //        // 使用线程安全的集合
    //        List<Transaction> transactionInfoByBlockNumList = Collections.synchronizedList(new ArrayList<>());
    //        getTransactionInfoByBlockNum(
    //                transactionInfoByBlockNumList,
    //                rpcConfigBO,
    //                fromBlockNumber,
    //                toBlockNumber
    //        );
    //        if (!transactionInfoByBlockNumList.isEmpty()) {
    //            // 使用线程安全的集合
    //            List<Transaction> transactionList = Collections.synchronizedList(new ArrayList<>());
    //            getTransactionById(transactionList, transactionInfoByBlockNumList, rpcConfigBO);
    //            //System.out.println("获取到 " + transactionList.size() + " 个交易详情");
    //
    //            for (Transaction transactionById : transactionList) {
    //                TransferTransactionBO transferTransactionBO = parseTransaction(transactionById);
    //                if (transferTransactionBO != null) {
    //                    transferTransactionBOList.add(transferTransactionBO);
    //                }
    //            }
    //        }
    //    }

    //private static void getTransactionInfoByBlockNum(
    //        List<Transaction> transactionInfoByBlockNumList,
    //        RpcConfigBO rpcConfigBO,
    //        long fromBlockNumber,
    //        long toBlockNumber
    //) throws Exception {
    //    int batchSize = 20;
    //
    //    // 参数验证
    //    if (fromBlockNumber > toBlockNumber) {
    //        throw new IllegalArgumentException("起始区块号不能大于结束区块号");
    //    }
    //
    //    CloseableHttpAsyncClient client = HttpAsyncClientUtil.createHttpClient(
    //            rpcConfigBO.isEnableProxy(),
    //            rpcConfigBO.getProxyHost(),
    //            rpcConfigBO.getProxyPort()
    //    );
    //    client.start();
    //
    //    // 计算总区块数和批次数量
    //    long totalBlocks = toBlockNumber - fromBlockNumber + 1;
    //    int totalBatches = (int) Math.ceil((double) totalBlocks / batchSize);
    //
    //    Map<String, String> headers = new HashMap<>();
    //    headers.put("accept", "application/json");
    //    headers.put("content-type", "application/json");
    //
    //    // 全局统计
    //    AtomicInteger overallSuccess = new AtomicInteger(0);
    //    AtomicInteger overallFailure = new AtomicInteger(0);
    //
    //    // 逐批处理
    //    for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
    //        long batchStartBlock = fromBlockNumber + (long) batchIndex * batchSize;
    //        long batchEndBlock = Math.min(batchStartBlock + batchSize - 1, toBlockNumber);
    //        int currentBatchSize = (int) (batchEndBlock - batchStartBlock + 1);
    //
    //        //为每个批次创建独立的CountDownLatch
    //        CountDownLatch batchLatch = new CountDownLatch(currentBatchSize);
    //        AtomicInteger batchSuccess = new AtomicInteger(0);
    //        AtomicInteger batchFailure = new AtomicInteger(0);
    //
    //        //System.out.println("处理批次 " + (batchIndex + 1) + "/" + totalBatches + ", 区块范围: " + batchStartBlock + "-" + batchEndBlock);
    //
    //        //先发送所有请求，再等待
    //        for (long blockNum = batchStartBlock; blockNum <= batchEndBlock; blockNum++) {
    //            // final变量用于lambda
    //            String jsonData = String.format("{\"num\":%d}", blockNum);
    //
    //            postAsyncWithCallback(client, rpcConfigBO.getRpcUrl() + "/walletsolidity/gettransactioninfobyblocknum", jsonData, headers, new FutureCallback<>() {
    //                @Override
    //                public void completed(HttpResponse response) {
    //                    try {
    //                        String responseBodyJSONString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    //                        if (JSON.isValid(responseBodyJSONString)) {
    //                            List<Transaction> TransactionTempList = JSONUtil.stringToList(responseBodyJSONString, Transaction.class);
    //                            if (TransactionTempList != null && !TransactionTempList.isEmpty()) {
    //                                //使用synchronized确保线程安全
    //                                synchronized (transactionInfoByBlockNumList) {
    //                                    transactionInfoByBlockNumList.addAll(TransactionTempList);
    //                                }
    //                            }
    //                            batchSuccess.incrementAndGet();
    //                            //System.out.println("✅ 区块 " + currentBlockNum + " 处理成功，获得 " + (TransactionTempList != null ? TransactionTempList.size() : 0) + " 个交易");
    //                        } else {
    //                            //System.err.println("❌ 区块 " + currentBlockNum + " 响应格式错误");
    //                            batchFailure.incrementAndGet();
    //                        }
    //                    } catch (Exception e) {
    //                        //System.err.println("❌ 区块 " + currentBlockNum + " 处理异常: " + e.getMessage());
    //                        batchFailure.incrementAndGet();
    //                    } finally {
    //                        batchLatch.countDown(); //确保总是调用countDown
    //                    }
    //                }
    //
    //                @Override
    //                public void failed(Exception ex) {
    //                    //System.err.println("❌ 区块 " + currentBlockNum + " 请求失败: " + ex.getMessage());
    //                    batchFailure.incrementAndGet();
    //                    batchLatch.countDown();
    //                }
    //
    //                @Override
    //                public void cancelled() {
    //                    //System.err.println("⚠️ 区块 " + currentBlockNum + " 请求被取消");
    //                    batchFailure.incrementAndGet();
    //                    batchLatch.countDown();
    //                }
    //            });
    //        }
    //
    //        //等待当前批次所有请求完成
    //        batchLatch.await();
    //
    //        // 更新全局统计
    //        overallSuccess.addAndGet(batchSuccess.get());
    //        overallFailure.addAndGet(batchFailure.get());
    //
    //        //System.out.println("批次 " + (batchIndex + 1) + " 完成: 成功 " + batchSuccess.get() + ", 失败 " + batchFailure.get());
    //
    //        // 批次间暂停，避免API限流
    //        if (batchIndex < totalBatches - 1) {
    //            Thread.sleep(2000);
    //        }
    //    }
    //
    //    // 检查是否有失败的请求
    //    if (overallFailure.get() > 0) {
    //        System.err.println("⚠️ 有 " + overallFailure.get() + " 个请求失败");
    //        // 可以选择抛异常或者继续处理
    //        throw new Exception("部分请求失败: " + overallFailure.get() + " 个");
    //    }
    //
    //    //System.out.println("✅ 总计获取 " + transactionInfoByBlockNumList.size() + " 个交易信息");
    //}

    //    private static void getTransactionById(
    //            List<Transaction> transactionList,
    //            List<Transaction> transactionInfoByBlockNumList,
    //            RpcConfigBO rpcConfigBO
    //    ) throws Exception {
    //        int batchSize = 20;
    //
    //        CloseableHttpAsyncClient client = HttpAsyncClientUtil.createHttpClient(
    //                rpcConfigBO.isEnableProxy(),
    //                rpcConfigBO.getProxyHost(),
    //                rpcConfigBO.getProxyPort()
    //        );
    //        client.start();
    //
    //        int total = transactionInfoByBlockNumList.size();
    //        int totalBatches = (int) Math.ceil((double) total / batchSize);
    //
    //        Map<String, String> headers = new HashMap<>();
    //        headers.put("accept", "application/json");
    //        headers.put("content-type", "application/json");
    //
    //        // 全局统计
    //        AtomicInteger overallSuccess = new AtomicInteger(0);
    //        AtomicInteger overallFailure = new AtomicInteger(0);
    //
    //        // 逐批处理
    //        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
    //            int startIndex = batchIndex * batchSize;
    //            int endIndex = Math.min(startIndex + batchSize, total);
    //
    //            List<Transaction> currentBatch = transactionInfoByBlockNumList.subList(startIndex, endIndex);
    //            int currentBatchSize = currentBatch.size();
    //
    //            //为每个批次创建独立的CountDownLatch
    //            CountDownLatch batchLatch = new CountDownLatch(currentBatchSize);
    //            AtomicInteger batchSuccess = new AtomicInteger(0);
    //            AtomicInteger batchFailure = new AtomicInteger(0);
    //
    //            //System.out.println("处理交易批次 " + (batchIndex + 1) + "/" + totalBatches + ", 交易数量: " + currentBatchSize);
    //
    //            //先发送所有请求，再等待
    //            for (int i = 0; i < currentBatchSize; i++) {
    //                Transaction transactionInfo = currentBatch.get(i);
    //                final String transactionId = transactionInfo.getId();
    //                String jsonData = String.format("{\"value\":\"%s\"}", transactionId);
    //
    //                postAsyncWithCallback(client, rpcConfigBO.getRpcUrl() + "/walletsolidity/gettransactionbyid", jsonData, headers, new FutureCallback<>() {
    //                    @Override
    //                    public void completed(HttpResponse response) {
    //                        try {
    //                            String responseBodyJSONString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    //                            if (JSON.isValid(responseBodyJSONString)) {
    //                                Transaction transaction = JSONUtil.stringToObject(responseBodyJSONString, Transaction.class);
    //                                if (transaction != null) {
    //                                    //使用synchronized确保线程安全
    //                                    synchronized (transactionList) {
    //                                        transactionList.add(transaction);
    //                                    }
    //                                }
    //                                batchSuccess.incrementAndGet();
    //                            } else {
    //                                System.err.println("❌ 交易 " + transactionId + " 响应格式错误");
    //                                batchFailure.incrementAndGet();
    //                            }
    //                        } catch (Exception e) {
    //                            System.err.println("❌ 交易 " + transactionId + " 处理异常: " + e.getMessage());
    //                            batchFailure.incrementAndGet();
    //                        } finally {
    //                            batchLatch.countDown();
    //                        }
    //                    }
    //
    //                    @Override
    //                    public void failed(Exception ex) {
    //                        System.err.println("❌ 交易 " + transactionId + " 请求失败: " + ex.getMessage());
    //                        batchFailure.incrementAndGet();
    //                        batchLatch.countDown();
    //                    }
    //
    //                    @Override
    //                    public void cancelled() {
    //                        System.err.println("⚠️ 交易 " + transactionId + " 请求被取消");
    //                        batchFailure.incrementAndGet();
    //                        batchLatch.countDown();
    //                    }
    //                });
    //            }
    //
    //            //等待当前批次所有请求完成
    //            batchLatch.await();
    //
    //            // 更新全局统计
    //            overallSuccess.addAndGet(batchSuccess.get());
    //            overallFailure.addAndGet(batchFailure.get());
    //
    //            //System.out.println("交易批次 " + (batchIndex + 1) + " 完成: 成功 " + batchSuccess.get() + ", 失败 " + batchFailure.get());
    //
    //            // 批次间暂停
    //            if (batchIndex < totalBatches - 1) {
    //                Thread.sleep(2000);
    //            }
    //        }
    //
    //        if (overallFailure.get() > 0) {
    //            System.err.println("⚠️ 有 " + overallFailure.get() + " 个交易请求失败");
    //            throw new Exception("部分请求失败: " + overallFailure.get() + " 个");
    //        }
    //
    //        //System.out.println("✅ 总计获取 " + transactionList.size() + " 个交易详情");
    //    }

    //    private static void getWalletBlockByNum(List<Transaction> transactionList, RpcConfigBO rpcConfigBO, long fromBlockNumber, long toBlockNumber) throws Exception {
    //        int batchSize = 20;
    //
    //        // 参数验证
    //        if (fromBlockNumber > toBlockNumber) {
    //            throw new IllegalArgumentException("起始区块号不能大于结束区块号");
    //        }
    //
    //        CloseableHttpAsyncClient client = HttpAsyncClientUtil.createHttpClient(
    //                rpcConfigBO.isEnableProxy(),
    //                rpcConfigBO.getProxyHost(),
    //                rpcConfigBO.getProxyPort()
    //        );
    //        client.start();
    //
    //        // 计算总区块数和批次数量
    //        long totalBlocks = toBlockNumber - fromBlockNumber + 1;
    //        int totalBatches = (int) Math.ceil((double) totalBlocks / batchSize);
    //
    //        Map<String, String> headers = new HashMap<>();
    //        headers.put("accept", "application/json");
    //        headers.put("content-type", "application/json");
    //
    //        // 全局统计
    //        AtomicInteger overallSuccess = new AtomicInteger(0);
    //        AtomicInteger overallFailure = new AtomicInteger(0);
    //
    //        // 逐批处理
    //        for (int batchIndex = 0; batchIndex < totalBatches; batchIndex++) {
    //            long batchStartBlock = fromBlockNumber + (long) batchIndex * batchSize;
    //            long batchEndBlock = Math.min(batchStartBlock + batchSize - 1, toBlockNumber);
    //            int currentBatchSize = (int) (batchEndBlock - batchStartBlock + 1);
    //
    //            //为每个批次创建独立的CountDownLatch
    //            CountDownLatch batchLatch = new CountDownLatch(currentBatchSize);
    //            AtomicInteger batchSuccess = new AtomicInteger(0);
    //            AtomicInteger batchFailure = new AtomicInteger(0);
    //
    //            //System.out.println("处理批次 " + (batchIndex + 1) + "/" + totalBatches + ", 区块范围: " + batchStartBlock + "-" + batchEndBlock);
    //
    //            //先发送所有请求，再等待
    //            for (long blockNum = batchStartBlock; blockNum <= batchEndBlock; blockNum++) {
    //                // final变量用于lambda
    //                String jsonData = String.format("{\"num\":%d}", blockNum);
    //
    //                postAsyncWithCallback(client, rpcConfigBO.getRpcUrl() + "/wallet/getblockbynum", jsonData, headers, new FutureCallback<>() {
    //                    @Override
    //                    public void completed(HttpResponse response) {
    //                        try {
    //                            String responseBodyJSONString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    //                            if (JSON.isValid(responseBodyJSONString)) {
    //                                Block block = JSONUtil.stringToObject(responseBodyJSONString, Block.class);
    //                                if (block != null && block.getTransactions() != null) {
    //                                    List<Transaction> transactions = block.getTransactions();
    //                                    transactions.forEach(transaction -> {
    //                                        transaction.setBlockNumber(block.getBlockHeader().getRawData().getBlockNumber());
    //                                        transaction.setBlockHash(block.getBlockID());
    //                                        transaction.setBlockTimeStamp(block.getBlockHeader().getRawData().getTimestamp());
    //                                    });
    //                                    //使用synchronized确保线程安全
    //                                    synchronized (transactionList) {
    //                                        transactionList.addAll(transactions);
    //                                    }
    //                                }
    //                                batchSuccess.incrementAndGet();
    //                                //System.out.println("✅ 区块 " + currentBlockNum + " 处理成功，获得 " + ((block != null && block.getTransactions() != null) ? block.getTransactions().size() : 0) + " 个交易");
    //                            } else {
    //                                //System.err.println("❌ 区块 " + currentBlockNum + " 响应格式错误");
    //                                batchFailure.incrementAndGet();
    //                            }
    //                        } catch (Exception e) {
    //                            //System.err.println("❌ 区块 " + currentBlockNum + " 处理异常: " + e.getMessage());
    //                            batchFailure.incrementAndGet();
    //                        } finally {
    //                            batchLatch.countDown(); //确保总是调用countDown
    //                        }
    //                    }
    //
    //                    @Override
    //                    public void failed(Exception ex) {
    //                        //System.err.println("❌ 区块 " + currentBlockNum + " 请求失败: " + ex.getMessage());
    //                        batchFailure.incrementAndGet();
    //                        batchLatch.countDown();
    //                    }
    //
    //                    @Override
    //                    public void cancelled() {
    //                        //System.err.println("⚠️ 区块 " + currentBlockNum + " 请求被取消");
    //                        batchFailure.incrementAndGet();
    //                        batchLatch.countDown();
    //                    }
    //                });
    //            }
    //
    //            //等待当前批次所有请求完成
    //            batchLatch.await();
    //
    //            // 更新全局统计
    //            overallSuccess.addAndGet(batchSuccess.get());
    //            overallFailure.addAndGet(batchFailure.get());
    //
    //            //System.out.println("批次 " + (batchIndex + 1) + " 完成: 成功 " + batchSuccess.get() + ", 失败 " + batchFailure.get());
    //
    //            // 批次间暂停，避免API限流
    //            if (batchIndex < totalBatches - 1) {
    //                Thread.sleep(2000);
    //            }
    //        }
    //
    //        // 检查是否有失败的请求
    //        if (overallFailure.get() > 0) {
    //            //System.err.println("⚠️ 有 " + overallFailure.get() + " 个请求失败");
    //            // 可以选择抛异常或者继续处理
    //            throw new Exception("部分请求失败: " + overallFailure.get() + " 个");
    //        }
    //        //System.out.println("✅ 总计获取 " + transactionList.size() + " 个交易信息");
    //    }

    //    public static void main(String[] args) {
    //        RpcConfigBO rpcConfig = new RpcConfigBO(
    //                true,
    //                "127.0.0.1",
    //                7890,
    //                "https://api.trongrid.io",
    //                null,
    //                null,
    //                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
    //        );
    //        List<TransferTransactionBO> transferTransactionBOList = new ArrayList<>();
    //        try {
    //            fetchTransferTransaction(
    //                    transferTransactionBOList,
    //                    rpcConfig,
    //                    58141518,
    //                    58141518
    //            );
    //        } catch (Exception e) {
    //            System.out.println("执行异常: " + e.getMessage());
    //            e.printStackTrace();
    //        }
    //        System.out.println(transferTransactionBOList.size());
    //    }

    //    public static void main(String[] args) {
    //        RpcConfigBO rpcConfig = new RpcConfigBO(
    //                true,
    //                "127.0.0.1",
    //                7890,
    //                "https://api.trongrid.io",
    //                null,
    //                null,
    //                "bbc0ea92c500dcf8ced8fd16d13c1cc9d51d0a17f56d0f588e160467dca6c629"
    //        );
    //        List<Transaction> transactionList = new ArrayList<>();
    //        try {
    //            getWalletBlockByNum(
    //                    transactionList,
    //                    rpcConfig,
    //                    75098134,
    //                    75098134
    //            );
    //        } catch (Exception e) {
    //            System.out.println("执行异常: " + e.getMessage());
    //            e.printStackTrace();
    //        }
    //        for (Transaction transaction : transactionList) {
    //            TransferTransactionBO transferTransactionBO = parseTransaction(transaction);
    //            if (transferTransactionBO != null) {
    //                System.out.println(transferTransactionBO.getContractAddress());
    //            }
    //        }
    //        System.out.println(transactionList.size());
    //    }

}