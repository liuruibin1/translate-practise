//package com.xxx.chain.tron.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xxx.chain.tron.config.TronConfig;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * Tron交易监控服务
// * 用于监控TRX和USDT转账交易
// */
//@Slf4j
//public class TronTransactionMonitor {
//
//    private final TronConfig config;
//    private final TronHttpClient httpClient;
//    private final ObjectMapper objectMapper;
//
//    // 监控控制
//    private final AtomicBoolean isMonitoring;
//    private final AtomicLong lastProcessedBlock;
//    private final ScheduledExecutorService monitorExecutor;
//    private final ExecutorService transactionProcessor;
//
//    // 交易事件处理器
//    private final List<TransactionEventHandler> eventHandlers;
//
//    // 统计信息
//    private final Map<String, Long> transactionCounts;
//    private final Map<String, BigDecimal> totalAmounts;
//
//    public TronTransactionMonitor(TronConfig config) {
//        this.config = config;
//        this.httpClient = new TronHttpClient(config);
//        this.objectMapper = new ObjectMapper();
//
//        this.isMonitoring = new AtomicBoolean(false);
//        this.lastProcessedBlock = new AtomicLong(0);
//        this.monitorExecutor = Executors.newSingleThreadScheduledExecutor();
//        this.transactionProcessor = Executors.newFixedThreadPool(5);
//
//        this.eventHandlers = new CopyOnWriteArrayList<>();
//        this.transactionCounts = new ConcurrentHashMap<>();
//        this.totalAmounts = new ConcurrentHashMap<>();
//
//        log.info("Tron交易监控服务初始化完成");
//    }
//
//    /**
//     * 启动监控服务
//     */
//    public void startMonitoring() {
//        if (isMonitoring.compareAndSet(false, true)) {
//            log.info("启动Tron交易监控服务...");
//
//            // 初始化最后处理的区块号
//            initializeLastProcessedBlock();
//
//            // 启动监控任务
//            startMonitoringTask();
//
//            log.info("Tron交易监控服务启动完成");
//        } else {
//            log.warn("监控服务已在运行中");
//        }
//    }
//
//    /**
//     * 初始化最后处理的区块号
//     */
//    private void initializeLastProcessedBlock() {
//        try {
//            JsonNode latestBlock = httpClient.getLatestBlock();
//            if (latestBlock != null && latestBlock.has("block_header")) {
//                JsonNode header = latestBlock.get("block_header");
//                if (header.has("raw_data")) {
//                    JsonNode rawData = header.get("raw_data");
//                    if (rawData.has("number")) {
//                        long blockNumber = rawData.get("number").asLong();
//                        // 从确认数之前的区块开始
//                        long startBlock = Math.max(0, blockNumber - config.getNetwork().getConfirmations());
//                        lastProcessedBlock.set(startBlock);
//                        log.info("初始化最后处理区块号: {} (当前区块: {})", startBlock, blockNumber);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.error("初始化最后处理区块号失败", e);
//            lastProcessedBlock.set(0);
//        }
//    }
//
//    /**
//     * 启动监控任务
//     */
//    private void startMonitoringTask() {
//        monitorExecutor.scheduleAtFixedRate(() -> {
//            try {
//                if (isMonitoring.get()) {
//                    monitorNewBlocks();
//                }
//            } catch (Exception e) {
//                log.error("监控新区块异常", e);
//            }
//        }, 0, config.getMonitor().getInterval(), TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 监控新区块
//     */
//    private void monitorNewBlocks() {
//        try {
//            JsonNode latestBlock = httpClient.getLatestBlock();
//            if (latestBlock == null) {
//                return;
//            }
//
//            JsonNode header = latestBlock.get("block_header");
//            if (header == null || !header.has("raw_data")) {
//                return;
//            }
//
//            JsonNode rawData = header.get("raw_data");
//            if (!rawData.has("number")) {
//                return;
//            }
//
//            long currentBlock = rawData.get("number").asLong();
//            long lastProcessed = lastProcessedBlock.get();
//
//            // 检查是否有新区块
//            if (currentBlock > lastProcessed + config.getNetwork().getConfirmations()) {
//                long fromBlock = lastProcessed + 1;
//                long toBlock = currentBlock - config.getNetwork().getConfirmations();
//
//                log.info("检测到新区块，处理范围: {} - {} (当前: {})", fromBlock, toBlock, currentBlock);
//
//                // 处理区块范围
//                processBlockRange(fromBlock, toBlock);
//
//                // 更新最后处理的区块号
//                lastProcessedBlock.set(toBlock);
//            }
//
//        } catch (Exception e) {
//            log.error("监控新区块失败", e);
//        }
//    }
//
//    /**
//     * 处理区块范围
//     */
//    private void processBlockRange(long fromBlock, long toBlock) {
//        // 将区块范围分割成多个批次
//        int batchSize = config.getMonitor().getBlockRange();
//
//        for (long start = fromBlock; start <= toBlock; start += batchSize) {
//            final long batchStart = start;
//            long end = Math.min(start + batchSize - 1, toBlock);
//
//            // 异步处理区块批次
//            CompletableFuture.runAsync(() -> {
//                try {
//                    processBlockBatch(batchStart, end);
//                } catch (Exception e) {
//                    log.error("处理区块批次 [{}, {}] 失败", batchStart, end, e);
//                }
//            }, transactionProcessor);
//        }
//    }
//
//    /**
//     * 处理区块批次
//     */
//    private void processBlockBatch(long fromBlock, long toBlock) {
//        for (long blockNumber = fromBlock; blockNumber <= toBlock; blockNumber++) {
//            try {
//                processBlock(blockNumber);
//            } catch (Exception e) {
//                log.error("处理区块 {} 失败", blockNumber, e);
//            }
//        }
//    }
//
//    /**
//     * 处理单个区块
//     */
//    private void processBlock(long blockNumber) {
//        try {
//            // 获取区块信息
//            JsonNode blockInfo = httpClient.getBlockByNumber(blockNumber);
//            if (blockInfo == null) {
//                return;
//            }
//
//            // 处理区块中的交易
//            processBlockTransactions(blockInfo, blockNumber);
//
//            // 处理区块中的事件日志
//            processBlockEvents(blockNumber);
//
//        } catch (Exception e) {
//            log.error("处理区块 {} 失败", blockNumber, e);
//        }
//    }
//
//    /**
//     * 处理区块中的交易
//     */
//    private void processBlockTransactions(JsonNode blockInfo, long blockNumber) {
//        try {
//            if (!blockInfo.has("transactions")) {
//                return;
//            }
//
//            JsonNode transactions = blockInfo.get("transactions");
//            if (!transactions.isArray()) {
//                return;
//            }
//
//            for (JsonNode tx : transactions) {
//                try {
//                    processTransaction(tx, blockNumber);
//                } catch (Exception e) {
//                    log.error("处理交易失败", e);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理区块 {} 交易失败", blockNumber, e);
//        }
//    }
//
//    /**
//     * 处理交易
//     */
//    private void processTransaction(JsonNode tx, long blockNumber) {
//        try {
//            if (!tx.has("raw_data") || !tx.has("txID")) {
//                return;
//            }
//
//            JsonNode rawData = tx.get("raw_data");
//            String txId = tx.get("txID").asText();
//
//            if (!rawData.has("contract")) {
//                return;
//            }
//
//            JsonNode contracts = rawData.get("contract");
//            if (!contracts.isArray()) {
//                return;
//            }
//
//            for (JsonNode contract : contracts) {
//                try {
//                    processContract(contract, txId, blockNumber);
//                } catch (Exception e) {
//                    log.error("处理合约失败", e);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理交易失败", e);
//        }
//    }
//
//    /**
//     * 处理合约
//     */
//    private void processContract(JsonNode contract, String txId, long blockNumber) {
//        try {
//            if (!contract.has("type") || !contract.has("parameter")) {
//                return;
//            }
//
//            String contractType = contract.get("type").asText();
//            JsonNode parameter = contract.get("parameter");
//
//            switch (contractType) {
//                case "TransferContract":
//                    processTransferContract(parameter, txId, blockNumber);
//                    break;
//                case "TriggerSmartContract":
//                    processSmartContract(parameter, txId, blockNumber);
//                    break;
//                default:
//                    // 其他类型的合约
//                    break;
//            }
//
//        } catch (Exception e) {
//            log.error("处理合约失败", e);
//        }
//    }
//
//    /**
//     * 处理TRX转账合约
//     */
//    private void processTransferContract(JsonNode parameter, String txId, long blockNumber) {
//        try {
//            if (!parameter.has("value")) {
//                return;
//            }
//
//            JsonNode value = parameter.get("value");
//            if (!value.has("owner_address") || !value.has("to_address") || !value.has("amount")) {
//                return;
//            }
//
//            String fromAddress = value.get("owner_address").asText();
//            String toAddress = value.get("to_address").asText();
//            BigInteger amount = new BigInteger(value.get("amount").asText());
//
//            // 检查是否是转入目标地址
//            if (config.getCurrentTargetAddress().equals(toAddress)) {
//                // 转换为TRX金额（1 TRX = 1,000,000 Sun）
//                BigDecimal trxAmount = new BigDecimal(amount).divide(BigDecimal.valueOf(1000000));
//
//                // 检查是否达到最小监控金额
//                BigInteger minAmount = new BigInteger(config.getMonitor().getMinTrxAmount());
//                if (amount.compareTo(minAmount) >= 0) {
//                    // 创建TRX转账事件
//                    TransactionEvent event = new TransactionEvent();
//                    event.setTxId(txId);
//                    event.setFromAddress(fromAddress);
//                    event.setToAddress(toAddress);
//                    event.setTokenSymbol("TRX");
//                    event.setAmount(trxAmount);
//                    event.setBlockNumber(blockNumber);
//                    event.setEventType(TransactionEvent.EventType.TRX_TRANSFER);
//                    event.setTimestamp(System.currentTimeMillis());
//
//                    // 处理事件
//                    processTransactionEvent(event);
//
//                    log.info("检测到TRX转入: 从 {} 到 {}，金额: {} TRX，交易ID: {}，区块: {}",
//                        fromAddress, toAddress, trxAmount, txId, blockNumber);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理TRX转账合约失败", e);
//        }
//    }
//
//    /**
//     * 处理智能合约（USDT等）
//     */
//    private void processSmartContract(JsonNode parameter, String txId, long blockNumber) {
//        try {
//            if (!parameter.has("value")) {
//                return;
//            }
//
//            JsonNode value = parameter.get("value");
//            if (!value.has("contract_address") || !value.has("data")) {
//                return;
//            }
//
//            String contractAddress = value.get("contract_address").asText();
//
//            // 检查是否是USDT合约
//            if (config.getCurrentUsdtAddress().equals(contractAddress)) {
//                // USDT转账通过事件日志处理，这里只记录合约调用
//                log.debug("检测到USDT合约调用: 交易ID: {}, 区块: {}", txId, blockNumber);
//            }
//
//        } catch (Exception e) {
//            log.error("处理智能合约失败", e);
//        }
//    }
//
//    /**
//     * 处理区块事件日志
//     */
//    private void processBlockEvents(long blockNumber) {
//        try {
//            // 获取USDT Transfer事件
//            JsonNode events = httpClient.getUsdtTransferEvents(blockNumber, blockNumber, null);
//            if (events != null && events.has("data")) {
//                JsonNode data = events.get("data");
//                if (data.isArray()) {
//                    for (JsonNode event : data) {
//                        try {
//                            processUsdtTransferEvent(event, blockNumber);
//                        } catch (Exception e) {
//                            log.error("处理USDT Transfer事件失败", e);
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理区块 {} 事件失败", blockNumber, e);
//        }
//    }
//
//    /**
//     * 处理USDT Transfer事件
//     */
//    private void processUsdtTransferEvent(JsonNode event, long blockNumber) {
//        try {
//            if (!event.has("from") || !event.has("to") || !event.has("value") || !event.has("transaction_id")) {
//                return;
//            }
//
//            String fromAddress = event.get("from").asText();
//            String toAddress = event.get("to").asText();
//            String value = event.get("value").asText();
//            String txId = event.get("transaction_id").asText();
//
//            // 检查是否是转入目标地址
//            if (config.getCurrentTargetAddress().equals(toAddress)) {
//                // 转换为USDT金额（6位小数）
//                BigDecimal usdtAmount = new BigDecimal(value).divide(BigDecimal.valueOf(1000000));
//
//                // 检查是否达到最小监控金额
//                BigInteger minAmount = new BigInteger(config.getMonitor().getMinUsdtAmount());
//                if (new BigInteger(value).compareTo(minAmount) >= 0) {
//                    // 创建USDT转账事件
//                    TransactionEvent tronEvent = new TransactionEvent();
//                    tronEvent.setTxId(txId);
//                    tronEvent.setFromAddress(fromAddress);
//                    tronEvent.setToAddress(toAddress);
//                    tronEvent.setTokenSymbol("USDT");
//                    tronEvent.setAmount(usdtAmount);
//                    tronEvent.setBlockNumber(blockNumber);
//                    tronEvent.setEventType(TransactionEvent.EventType.USDT_TRANSFER);
//                    tronEvent.setTimestamp(System.currentTimeMillis());
//                    tronEvent.setContractAddress(config.getCurrentUsdtAddress());
//
//                    // 处理事件
//                    processTransactionEvent(tronEvent);
//
//                    log.info("检测到USDT转入: 从 {} 到 {}，金额: {} USDT，交易ID: {}，区块: {}",
//                        fromAddress, toAddress, usdtAmount, txId, blockNumber);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理USDT Transfer事件失败", e);
//        }
//    }
//
//    /**
//     * 处理交易事件
//     */
//    private void processTransactionEvent(TransactionEvent event) {
//        try {
//            // 更新统计信息
//            updateStatistics(event);
//
//            // 通知事件处理器
//            notifyEventHandlers(event);
//
//        } catch (Exception e) {
//            log.error("处理交易事件失败", e);
//        }
//    }
//
//    /**
//     * 更新统计信息
//     */
//    private void updateStatistics(TransactionEvent event) {
//        String key = event.getTokenSymbol();
//
//        // 更新交易次数
//        transactionCounts.merge(key, 1L, Long::sum);
//
//        // 更新总金额
//        totalAmounts.merge(key, event.getAmount(), BigDecimal::add);
//    }
//
//    /**
//     * 通知事件处理器
//     */
//    private void notifyEventHandlers(TransactionEvent event) {
//        for (TransactionEventHandler handler : eventHandlers) {
//            try {
//                handler.onTransactionEvent(event);
//            } catch (Exception e) {
//                log.error("事件处理器异常", e);
//            }
//        }
//    }
//
//    /**
//     * 添加事件处理器
//     */
//    public void addEventHandler(TransactionEventHandler handler) {
//        eventHandlers.add(handler);
//        log.info("添加交易事件处理器: {}", handler.getClass().getSimpleName());
//    }
//
//    /**
//     * 移除事件处理器
//     */
//    public void removeEventHandler(TransactionEventHandler handler) {
//        eventHandlers.remove(handler);
//        log.info("移除交易事件处理器: {}", handler.getClass().getSimpleName());
//    }
//
//    /**
//     * 获取监控统计信息
//     */
//    public Map<String, Object> getMonitoringStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("isMonitoring", isMonitoring.get());
//        stats.put("lastProcessedBlock", lastProcessedBlock.get());
//        stats.put("transactionCounts", new HashMap<>(transactionCounts));
//        stats.put("totalAmounts", new HashMap<>(totalAmounts));
//        stats.put("eventHandlers", eventHandlers.size());
//        return stats;
//    }
//
//    /**
//     * 手动处理指定区块
//     */
//    public void processBlockManually(long blockNumber) {
//        try {
//            log.info("手动处理区块: {}", blockNumber);
//            processBlock(blockNumber);
//        } catch (Exception e) {
//            log.error("手动处理区块 {} 失败", blockNumber, e);
//        }
//    }
//
//    /**
//     * 停止监控服务
//     */
//    public void stopMonitoring() {
//        if (isMonitoring.compareAndSet(true, false)) {
//            log.info("停止Tron交易监控服务...");
//
//            // 关闭监控执行器
//            monitorExecutor.shutdown();
//
//            // 关闭交易处理器
//            transactionProcessor.shutdown();
//
//            try {
//                if (!monitorExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
//                    monitorExecutor.shutdownNow();
//                }
//                if (!transactionProcessor.awaitTermination(5, TimeUnit.SECONDS)) {
//                    transactionProcessor.shutdownNow();
//                }
//            } catch (InterruptedException e) {
//                monitorExecutor.shutdownNow();
//                transactionProcessor.shutdownNow();
//                Thread.currentThread().interrupt();
//            }
//
//            // 关闭HTTP客户端
//            httpClient.shutdown();
//
//            log.info("Tron交易监控服务已停止");
//        }
//    }
//
//    // ==================== 内部类 ====================
//
//    /**
//     * 交易事件
//     */
//    @Data
//    public static class TransactionEvent {
//        private String txId;
//        private String fromAddress;
//        private String toAddress;
//        private String tokenSymbol;
//        private BigDecimal amount;
//        private long blockNumber;
//        private EventType eventType;
//        private long timestamp;
//        private String contractAddress; // 合约地址（USDT等）
//        private Map<String, Object> metadata; // 扩展字段
//
//        public enum EventType {
//            TRX_TRANSFER,      // TRX转账
//            USDT_TRANSFER      // USDT转账
//        }
//    }
//
//    /**
//     * 交易事件处理器接口
//     */
//    public interface TransactionEventHandler {
//        void onTransactionEvent(TransactionEvent event);
//    }
//}
