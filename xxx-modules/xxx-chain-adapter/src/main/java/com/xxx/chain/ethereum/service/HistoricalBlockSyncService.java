//package com.xxx.chain.ethereum.service;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameter;
//import org.web3j.protocol.core.methods.request.EthFilter;
//import org.web3j.protocol.core.methods.response.EthBlock;
//import org.web3j.protocol.core.methods.response.EthLog;
//import org.web3j.protocol.core.methods.response.Transaction;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.List;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * 历史区块同步服务
// * 用于处理重启后的历史区块同步，确保不丢失交易
// */
//@Slf4j
//public class HistoricalBlockSyncService {
//
//    private final Web3j web3j;
//    private final UserCryptoAccountService userCryptoAccountService;
//    private final BlockSyncStateManager syncStateManager;
//    private final TransactionEventProcessor eventProcessor;
//
//    // 同步控制
//    private final AtomicBoolean isSyncing;
//    private final AtomicLong totalProcessedBlocks;
//    private final AtomicLong totalProcessedTransactions;
//
//    // 线程池
//    private final ExecutorService syncExecutor;
//    private final ScheduledExecutorService progressExecutor;
//
//    // 配置
//    private final int maxConcurrentBlocks; // 并发处理的区块数
//    private final int batchSize; // 批处理大小
//    private final long syncInterval; // 同步间隔（毫秒）
//
//    public HistoricalBlockSyncService(
//            Web3j web3j,
//            UserCryptoAccountService userCryptoAccountService,
//            BlockSyncStateManager syncStateManager,
//            TransactionEventProcessor eventProcessor) {
//        this.web3j = web3j;
//        this.userCryptoAccountService = userCryptoAccountService;
//        this.syncStateManager = syncStateManager;
//        this.eventProcessor = eventProcessor;
//
//        this.isSyncing = new AtomicBoolean(false);
//        this.totalProcessedBlocks = new AtomicLong(0);
//        this.totalProcessedTransactions = new AtomicLong(0);
//
//        // 配置线程池
//        this.maxConcurrentBlocks = 5; // 最多并发处理5个区块
//        this.batchSize = 100; // 每次处理100个区块
//        this.syncInterval = 1000; // 1秒间隔
//
//        this.syncExecutor = Executors.newFixedThreadPool(maxConcurrentBlocks);
//        this.progressExecutor = Executors.newSingleThreadScheduledExecutor();
//
//        log.info("历史区块同步服务初始化完成");
//    }
//
//    /**
//     * 启动历史区块同步
//     */
//    public CompletableFuture<SyncResult> startHistoricalSync() {
//        if (isSyncing.compareAndSet(false, true)) {
//            log.info("开始历史区块同步...");
//
//            return CompletableFuture.supplyAsync(() -> {
//                try {
//                    return performHistoricalSync();
//                } finally {
//                    isSyncing.set(false);
//                }
//            }, syncExecutor);
//        } else {
//            log.warn("历史区块同步已在运行中");
//            return CompletableFuture.completedFuture(new SyncResult(false, "同步已在运行中"));
//        }
//    }
//
//    /**
//     * 执行历史区块同步
//     */
//    private SyncResult performHistoricalSync() {
//        long startTime = System.currentTimeMillis();
//        long totalBlocks = 0;
//        long totalTransactions = 0;
//
//        try {
//            // 检查是否需要历史同步
//            if (!syncStateManager.needsHistoricalSync()) {
//                log.info("无需历史区块同步，当前状态已是最新");
//                return new SyncResult(true, "无需同步", 0, 0, 0);
//            }
//
//            // 启动进度监控
//            startProgressMonitoring();
//
//            // 开始同步循环
//            while (syncStateManager.needsHistoricalSync()) {
//                BlockSyncStateManager.BlockRange blockRange = syncStateManager.getNextBlockRange();
//                if (blockRange == null) {
//                    break;
//                }
//
//                log.info("同步区块范围: {}", blockRange);
//
//                // 并发处理区块范围
//                List<CompletableFuture<BlockSyncResult>> futures = processBlockRange(blockRange);
//
//                // 等待所有区块处理完成
//                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
//
//                // 统计结果
//                for (CompletableFuture<BlockSyncResult> future : futures) {
//                    try {
//                        BlockSyncResult result = future.get();
//                        totalBlocks += result.getProcessedBlocks();
//                        totalTransactions += result.getProcessedTransactions();
//                    } catch (Exception e) {
//                        log.error("获取区块同步结果失败", e);
//                    }
//                }
//
//                // 更新同步状态
//                syncStateManager.updateSyncState(blockRange.getToBlock());
//
//                // 检查是否需要停止
//                if (Thread.currentThread().isInterrupted()) {
//                    log.info("历史区块同步被中断");
//                    break;
//                }
//
//                // 短暂休息，避免过度占用资源
//                Thread.sleep(syncInterval);
//            }
//
//            long duration = System.currentTimeMillis() - startTime;
//            log.info("历史区块同步完成，总区块: {}, 总交易: {}, 耗时: {}ms",
//                    totalBlocks, totalTransactions, duration);
//
//            return new SyncResult(true, "同步完成", totalBlocks, totalTransactions, duration);
//
//        } catch (Exception e) {
//            log.error("历史区块同步失败", e);
//            return new SyncResult(false, "同步失败: " + e.getMessage(), totalBlocks, totalTransactions, 0);
//        } finally {
//            stopProgressMonitoring();
//        }
//    }
//
//    /**
//     * 处理区块范围
//     */
//    private List<CompletableFuture<BlockSyncResult>> processBlockRange(BlockSyncStateManager.BlockRange blockRange) {
//        List<CompletableFuture<BlockSyncResult>> futures = new CopyOnWriteArrayList<>();
//
//        // 将区块范围分割成多个批次
//        long fromBlock = blockRange.getFromBlock();
//        long toBlock = blockRange.getToBlock();
//
//        for (long start = fromBlock; start <= toBlock; start += batchSize) {
//            final long batchStart = start;
//            long end = Math.min(start + batchSize - 1, toBlock);
//
//            CompletableFuture<BlockSyncResult> future = CompletableFuture.supplyAsync(() -> {
//                return processBlockBatch(batchStart, end);
//            }, syncExecutor);
//
//            futures.add(future);
//        }
//
//        return futures;
//    }
//
//    /**
//     * 处理区块批次
//     */
//    private BlockSyncResult processBlockBatch(long fromBlock, long toBlock) {
//        long processedBlocks = 0;
//        long processedTransactions = 0;
//
//        try {
//            for (long blockNumber = fromBlock; blockNumber <= toBlock; blockNumber++) {
//                if (Thread.currentThread().isInterrupted()) {
//                    break;
//                }
//
//                try {
//                    // 获取区块信息
//                    EthBlock.Block block = web3j.ethGetBlockByNumber(
//                            DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true
//                    ).send().getBlock();
//
//                    if (block != null) {
//                        // 处理区块中的交易
//                        long txCount = processBlockTransactions(block);
//                        processedTransactions += txCount;
//                        processedBlocks++;
//
//                        // 处理区块中的事件日志
//                        long logCount = processBlockLogs(block);
//                        processedTransactions += logCount;
//
//                        log.debug("处理区块 {} 完成，交易: {}, 事件: {}", blockNumber, txCount, logCount);
//                    }
//
//                } catch (Exception e) {
//                    log.error("处理区块 {} 失败", blockNumber, e);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理区块批次 [{}, {}] 失败", fromBlock, toBlock, e);
//        }
//
//        return new BlockSyncResult(processedBlocks, processedTransactions);
//    }
//
//    /**
//     * 处理区块中的交易
//     */
//    private long processBlockTransactions(EthBlock.Block block) {
//        long count = 0;
//        List<EthBlock.TransactionResult> transactions = block.getTransactions();
//
//        for (EthBlock.TransactionResult txResult : transactions) {
//            try {
//                if (txResult instanceof EthBlock.TransactionObject) {
//                    EthBlock.TransactionObject txObj = (EthBlock.TransactionObject) txResult;
//                    Transaction tx = txObj.get();
//
//                    // 检查是否是转入被监控地址的交易
//                    if (tx.getTo() != null && userCryptoAccountService.isAddressMonitored(tx.getTo())) {
//                        processHistoricalTransaction(tx, block);
//                        count++;
//                    }
//                }
//            } catch (Exception e) {
//                log.error("处理区块交易失败", e);
//            }
//        }
//
//        return count;
//    }
//
//    /**
//     * 处理区块中的事件日志
//     */
//    private long processBlockLogs(EthBlock.Block block) {
//        long count = 0;
//        List<UserCryptoAccountService.TokenInfo> activeTokens = userCryptoAccountService.getActiveTokens();
//
//        for (UserCryptoAccountService.TokenInfo token : activeTokens) {
//            try {
//                // 创建事件过滤器
//                EthFilter filter = new EthFilter(
//                        DefaultBlockParameter.valueOf(block.getNumber()),
//                        DefaultBlockParameter.valueOf(block.getNumber()),
//                        token.getContractAddress()
//                );
//
//                // Transfer事件签名
//                filter.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
//
//                // 获取事件日志
//                List<EthLog.LogResult> logs = web3j.ethGetLogs(filter).send().getLogs();
//
//                for (EthLog.LogResult logResult : logs) {
//                    try {
//                        if (logResult instanceof EthLog.LogObject) {
//                            EthLog.LogObject logObj = (EthLog.LogObject) logResult;
//                            processHistoricalTransferEvent(logObj, token);
//                            count++;
//                        }
//                    } catch (Exception e) {
//                        log.error("处理事件日志失败", e);
//                    }
//                }
//
//            } catch (Exception e) {
//                log.error("获取代币 {} 事件日志失败", token.getSymbol(), e);
//            }
//        }
//
//        return count;
//    }
//
//    /**
//     * 处理历史交易
//     */
//    private void processHistoricalTransaction(Transaction tx, EthBlock.Block block) {
//        try {
//            String txHash = tx.getHash();
//            String from = tx.getFrom();
//            String to = tx.getTo();
//            BigInteger value = tx.getValue();
//
//            // 判断交易类型
//            if (userCryptoAccountService.isTokenMonitored(to)) {
//                // ERC20代币转账，通过事件日志处理
//                log.debug("检测到历史ERC20合约调用: 交易哈希: {}, 区块: {}", txHash, block.getNumber());
//            } else {
//                // ETH转账
//                BigDecimal ethAmount = Convert.fromWei(value.toString(), Convert.Unit.ETHER);
//                String userId = userCryptoAccountService.getUserIdByAddress(to);
//
//                if (userId != null) {
//                    // 检查是否达到最小监控金额
//                    BigDecimal minAmount = userCryptoAccountService.getMinAmount(userId, "ETH");
//                    if (ethAmount.compareTo(minAmount) >= 0) {
//                        // 创建交易事件
//                        MultiUserTransactionMonitor.TransactionEvent event =
//                                createTransactionEvent(tx, block, userId, "ETH", ethAmount,
//                                        MultiUserTransactionMonitor.TransactionEvent.EventType.ETH_TRANSFER);
//
//                        // 处理事件
//                        eventProcessor.processTransactionEvent(event);
//
//                        log.info("处理历史ETH转入: 用户ID={}, 交易哈希={}, 金额={} ETH, 区块={}",
//                                userId, txHash, ethAmount, block.getNumber());
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理历史交易失败", e);
//        }
//    }
//
//    /**
//     * 处理历史Transfer事件
//     */
//    private void processHistoricalTransferEvent(EthLog.LogObject logResult, UserCryptoAccountService.TokenInfo token) {
//        try {
//            // 解析事件数据
//            String from = "0x" + logResult.getTopics().get(1).substring(26);
//            String to = "0x" + logResult.getTopics().get(2).substring(26);
//            String value = Numeric.toBigInt(logResult.getData()).toString();
//
//            // 检查是否是转入被监控地址
//            if (userCryptoAccountService.isAddressMonitored(to)) {
//                String userId = userCryptoAccountService.getUserIdByAddress(to);
//
//                if (userId != null) {
//                    // 转换为代币金额
//                    BigDecimal tokenAmount = new BigDecimal(value)
//                            .divide(BigDecimal.valueOf(Math.pow(10, token.getDecimals())));
//
//                    // 检查是否达到最小监控金额
//                    BigDecimal minAmount = userCryptoAccountService.getMinAmount(userId, token.getSymbol());
//                    if (tokenAmount.compareTo(minAmount) >= 0) {
//                        // 创建交易事件
//                        MultiUserTransactionMonitor.TransactionEvent event =
//                                createTransactionEvent(null, null, userId, token.getSymbol(), tokenAmount,
//                                        MultiUserTransactionMonitor.TransactionEvent.EventType.TOKEN_TRANSFER);
//
//                        event.setFrom(from);
//                        event.setTo(to);
//                        event.setContractAddress(token.getContractAddress());
//                        event.setTxHash(logResult.getTransactionHash());
//
//                        // 处理事件
//                        eventProcessor.processTransactionEvent(event);
//
//                        log.info("处理历史{}转入: 用户ID={}, 交易哈希={}, 金额={} {}, 区块={}",
//                                token.getSymbol(), userId, logResult.getTransactionHash(),
//                                tokenAmount, token.getSymbol(), logResult.getBlockNumber());
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理历史Transfer事件失败", e);
//        }
//    }
//
//    /**
//     * 创建交易事件
//     */
//    private MultiUserTransactionMonitor.TransactionEvent createTransactionEvent(Transaction tx,
//                                                                                EthBlock.Block block, String userId, String tokenSymbol, BigDecimal amount,
//                                                                                MultiUserTransactionMonitor.TransactionEvent.EventType eventType) {
//
//        MultiUserTransactionMonitor.TransactionEvent event = new MultiUserTransactionMonitor.TransactionEvent();
//        event.setUserId(userId);
//        event.setTokenSymbol(tokenSymbol);
//        event.setAmount(amount);
//        event.setEventType(eventType);
//        event.setTimestamp(System.currentTimeMillis());
//
//        if (tx != null) {
//            event.setTxHash(tx.getHash());
//            event.setFrom(tx.getFrom());
//            event.setTo(tx.getTo());
//        }
//
//        if (block != null) {
//            event.setBlockNumber(block.getNumber());
//        }
//
//        return event;
//    }
//
//    /**
//     * 启动进度监控
//     */
//    private void startProgressMonitoring() {
//        progressExecutor.scheduleAtFixedRate(() -> {
//            try {
//                BlockSyncStateManager.SyncProgress progress = syncStateManager.getSyncProgress();
//                log.info("同步进度: {:.2f}% - 当前区块: {}, 已同步: {}, 待同步: {}",
//                        progress.getProgressPercentage(),
//                        progress.getCurrentBlock(),
//                        progress.getLastSyncedBlock(),
//                        progress.getPendingBlocks());
//            } catch (Exception e) {
//                log.error("获取同步进度失败", e);
//            }
//        }, 10, 10, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 停止进度监控
//     */
//    private void stopProgressMonitoring() {
//        progressExecutor.shutdown();
//    }
//
//    /**
//     * 检查是否正在同步
//     */
//    public boolean isSyncing() {
//        return isSyncing.get();
//    }
//
//    /**
//     * 获取同步统计
//     */
//    public SyncStatistics getSyncStatistics() {
//        return new SyncStatistics(
//                totalProcessedBlocks.get(),
//                totalProcessedTransactions.get(),
//                isSyncing.get()
//        );
//    }
//
//    /**
//     * 停止同步服务
//     */
//    public void shutdown() {
//        log.info("停止历史区块同步服务...");
//
//        isSyncing.set(false);
//        syncExecutor.shutdown();
//        progressExecutor.shutdown();
//
//        try {
//            if (!syncExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
//                syncExecutor.shutdownNow();
//            }
//            if (!progressExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
//                progressExecutor.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            syncExecutor.shutdownNow();
//            progressExecutor.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//
//        log.info("历史区块同步服务已停止");
//    }
//
//    // ==================== 内部类 ====================
//
//    /**
//     * 同步结果
//     */
//    @Data
//    public static class SyncResult {
//        private final boolean success;
//        private final String message;
//        private final long processedBlocks;
//        private final long processedTransactions;
//        private final long duration;
//
//        public SyncResult(boolean success, String message) {
//            this(success, message, 0, 0, 0);
//        }
//
//        public SyncResult(boolean success, String message, long processedBlocks,
//                          long processedTransactions, long duration) {
//            this.success = success;
//            this.message = message;
//            this.processedBlocks = processedBlocks;
//            this.processedTransactions = processedTransactions;
//            this.duration = duration;
//        }
//    }
//
//    /**
//     * 区块同步结果
//     */
//    @Data
//    public static class BlockSyncResult {
//        private final long processedBlocks;
//        private final long processedTransactions;
//
//        public BlockSyncResult(long processedBlocks, long processedTransactions) {
//            this.processedBlocks = processedBlocks;
//            this.processedTransactions = processedTransactions;
//        }
//    }
//
//    /**
//     * 同步统计
//     */
//    @Data
//    public static class SyncStatistics {
//        private final long totalProcessedBlocks;
//        private final long totalProcessedTransactions;
//        private final boolean isCurrentlySyncing;
//
//        public SyncStatistics(long totalProcessedBlocks, long totalProcessedTransactions, boolean isCurrentlySyncing) {
//            this.totalProcessedBlocks = totalProcessedBlocks;
//            this.totalProcessedTransactions = totalProcessedTransactions;
//            this.isCurrentlySyncing = isCurrentlySyncing;
//        }
//    }
//}
