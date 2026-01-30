//package com.xxx.chain.ethereum.service;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import io.reactivex.disposables.Disposable;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.request.EthFilter;
//import org.web3j.protocol.core.methods.response.EthBlock;
//import org.web3j.protocol.core.methods.response.Transaction;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * 多用户交易监控服务
// * 支持监控多个用户地址和多种ERC20代币
// */
//@Slf4j
//public class MultiUserTransactionMonitor implements TransactionEventProcessor {
//
//    private final EthereumConfig config;
//    private final Web3j web3j;
//    private final UserCryptoAccountService userCryptoAccountService;
//    private final ScheduledExecutorService scheduler;
//    private final ExecutorService transactionProcessor;
//    private final AtomicLong lastProcessedBlock;
//
//    // 事件监听器映射
//    private final Map<String, Disposable> eventSubscriptions = new ConcurrentHashMap<>();
//
//    // 交易处理队列
//    private final BlockingQueue<TransactionEvent> transactionQueue = new LinkedBlockingQueue<>(10000);
//
//    // 统计信息
//    private final Map<String, Long> transactionCounts = new ConcurrentHashMap<>();
//    private final Map<String, BigDecimal> totalAmounts = new ConcurrentHashMap<>();
//
//    public MultiUserTransactionMonitor(EthereumConfig config, Web3j web3j, UserCryptoAccountService userCryptoAccountService) {
//        this.config = config;
//        this.web3j = web3j;
//        this.userCryptoAccountService = userCryptoAccountService;
//        this.lastProcessedBlock = new AtomicLong(0);
//
//        // 初始化调度器和处理器
//        this.scheduler = Executors.newScheduledThreadPool(3);
//        this.transactionProcessor = Executors.newFixedThreadPool(5);
//
//        log.info("多用户交易监控服务初始化完成");
//    }
//
//    /**
//     * 启动监控服务
//     */
//    public void startMonitoring() {
//        log.info("启动多用户交易监控服务...");
//
//        // 启动区块监控
//        startBlockMonitoring();
//
//        // 启动事件监听
//        startEventListening();
//
//        // 启动交易处理器
//        startTransactionProcessor();
//
//        // 启动统计信息收集
//        startStatisticsCollection();
//
//        log.info("多用户交易监控服务启动完成");
//    }
//
//    /**
//     * 启动区块监控
//     */
//    private void startBlockMonitoring() {
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                monitorNewBlocks();
//            } catch (Exception e) {
//                log.error("区块监控异常", e);
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 启动事件监听
//     */
//    private void startEventListening() {
//        // 为每个活跃的代币启动Transfer事件监听
//        List<UserCryptoAccountService.TokenInfo> activeTokens = userCryptoAccountService.getActiveTokens();
//
//        for (UserCryptoAccountService.TokenInfo token : activeTokens) {
//            startTokenEventListening(token);
//        }
//
//        log.info("启动事件监听完成，共 {} 个代币", activeTokens.size());
//    }
//
//    /**
//     * 启动代币事件监听
//     */
//    private void startTokenEventListening(UserCryptoAccountService.TokenInfo token) {
//        try {
//            // 创建Transfer事件过滤器
//            EthFilter filter = new EthFilter(
//                DefaultBlockParameterName.LATEST,
//                DefaultBlockParameterName.LATEST,
//                token.getContractAddress()
//            );
//
//            // Transfer事件签名: 0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef
//            filter.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef");
//
//            // 订阅事件
//            Disposable subscription = web3j.ethLogFlowable(filter).subscribe(
//                logResult -> {
//                    try {
//                        processTransferEvent(logResult, token);
//                    } catch (Exception e) {
//                        log.error("处理Transfer事件异常", e);
//                    }
//                },
//                error -> log.error("代币 {} 事件监听异常", token.getSymbol(), error)
//            );
//
//            eventSubscriptions.put(token.getContractAddress(), subscription);
//            log.info("启动代币 {} 事件监听", token.getSymbol());
//
//        } catch (Exception e) {
//            log.error("启动代币 {} 事件监听失败", token.getSymbol(), e);
//        }
//    }
//
//    /**
//     * 启动交易处理器
//     */
//    private void startTransactionProcessor() {
//        for (int i = 0; i < 5; i++) {
//            transactionProcessor.submit(() -> {
//                while (!Thread.currentThread().isInterrupted()) {
//                    try {
//                        TransactionEvent event = transactionQueue.poll(1, TimeUnit.SECONDS);
//                        if (event != null) {
//                            processTransactionEvent(event);
//                        }
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                        break;
//                    } catch (Exception e) {
//                        log.error("处理交易事件异常", e);
//                    }
//                }
//            });
//        }
//        log.info("交易处理器启动完成，共 {} 个线程", 5);
//    }
//
//    /**
//     * 启动统计信息收集
//     */
//    private void startStatisticsCollection() {
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                collectStatistics();
//            } catch (Exception e) {
//                log.error("收集统计信息异常", e);
//            }
//        }, 1, 1, TimeUnit.MINUTES);
//    }
//
//    /**
//     * 监控新区块
//     */
//    private void monitorNewBlocks() {
//        try {
//            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(
//                DefaultBlockParameterName.LATEST, false
//            ).send().getBlock();
//
//            if (latestBlock != null) {
//                long blockNumber = latestBlock.getNumber().longValue();
//                long lastProcessed = lastProcessedBlock.get();
//
//                if (blockNumber > lastProcessed) {
//                    processNewBlock(latestBlock);
//                    lastProcessedBlock.set(blockNumber);
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取最新区块失败", e);
//        }
//    }
//
//    /**
//     * 处理新区块
//     */
//    private void processNewBlock(EthBlock.Block block) {
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
//                        processIncomingTransaction(tx, block);
//                    }
//                }
//            } catch (Exception e) {
//                log.error("处理区块交易异常", e);
//            }
//        }
//    }
//
//    /**
//     * 处理转入交易
//     */
//    private void processIncomingTransaction(Transaction tx, EthBlock.Block block) {
//        try {
//            String txHash = tx.getHash();
//            String from = tx.getFrom();
//            String to = tx.getTo();
//            BigInteger value = tx.getValue();
//            BigInteger blockNumber = block.getNumber();
//
//            // 判断交易类型
//            if (userCryptoAccountService.isTokenMonitored(to)) {
//                // ERC20代币转账，通过事件日志处理
//                log.debug("检测到ERC20合约调用: 交易哈希: {}, 从: {}, 到: {}", txHash, from, to);
//            } else {
//                // ETH转账
//                BigDecimal ethAmount = Convert.fromWei(value.toString(), Convert.Unit.ETHER);
//                String userId = userCryptoAccountService.getUserIdByAddress(to);
//
//                if (userId != null) {
//                    // 检查是否达到最小监控金额
//                    BigDecimal minAmount = userCryptoAccountService.getMinAmount(userId, "ETH");
//                    if (ethAmount.compareTo(minAmount) >= 0) {
//                        TransactionEvent event = new TransactionEvent();
//                        event.setTxHash(txHash);
//                        event.setFrom(from);
//                        event.setTo(to);
//                        event.setUserId(userId);
//                        event.setTokenSymbol("ETH");
//                        event.setAmount(ethAmount);
//                        event.setBlockNumber(blockNumber);
//                        event.setEventType(TransactionEvent.EventType.ETH_TRANSFER);
//                        event.setTimestamp(System.currentTimeMillis());
//
//                        // 添加到处理队列
//                        transactionQueue.offer(event);
//
//                        log.info("检测到ETH转入: 用户ID={}, 交易哈希={}, 金额={} ETH",
//                            userId, txHash, ethAmount);
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理转入交易异常", e);
//        }
//    }
//
//    /**
//     * 处理Transfer事件
//     */
//        private void processTransferEvent(org.web3j.protocol.core.methods.response.Log logResult,
//                                    UserCryptoAccountService.TokenInfo token) {
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
//                        .divide(BigDecimal.valueOf(Math.pow(10, token.getDecimals())));
//
//                    // 检查是否达到最小监控金额
//                    BigDecimal minAmount = userCryptoAccountService.getMinAmount(userId, token.getSymbol());
//                    if (tokenAmount.compareTo(minAmount) >= 0) {
//                        TransactionEvent event = new TransactionEvent();
//                        event.setTxHash(logResult.getTransactionHash());
//                        event.setFrom(from);
//                        event.setTo(to);
//                        event.setUserId(userId);
//                        event.setTokenSymbol(token.getSymbol());
//                        event.setAmount(tokenAmount);
//                        event.setContractAddress(token.getContractAddress());
//                        event.setEventType(TransactionEvent.EventType.TOKEN_TRANSFER);
//                        event.setTimestamp(System.currentTimeMillis());
//
//                        // 添加到处理队列
//                        transactionQueue.offer(event);
//
//                        log.info("检测到{}转入: 用户ID={}, 交易哈希={}, 金额={} {}",
//                            token.getSymbol(), userId, logResult.getTransactionHash(), tokenAmount, token.getSymbol());
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理Transfer事件异常", e);
//        }
//    }
//
//    /**
//     * 处理交易事件
//     */
//    @Override
//    public void processTransactionEvent(TransactionEvent event) {
//        try {
//            // 更新统计信息
//            updateStatistics(event);
//
//            // 处理业务逻辑
//            handleTransactionEvent(event);
//
//        } catch (Exception e) {
//            log.error("处理交易事件异常: {}", event.getTxHash(), e);
//        }
//    }
//
//    /**
//     * 更新统计信息
//     */
//    private void updateStatistics(TransactionEvent event) {
//        String key = event.getUserId() + "_" + event.getTokenSymbol();
//
//        // 更新交易次数
//        transactionCounts.merge(key, 1L, Long::sum);
//
//        // 更新总金额
//        totalAmounts.merge(key, event.getAmount(), BigDecimal::add);
//    }
//
//    /**
//     * 处理交易事件业务逻辑
//     */
//    private void handleTransactionEvent(TransactionEvent event) {
//        // TODO: 实现具体的业务逻辑
//        log.info("处理交易事件: 用户ID={}, 代币={}, 金额={}, 交易哈希={}",
//            event.getUserId(), event.getTokenSymbol(), event.getAmount(), event.getTxHash());
//
//        // 这里可以添加：
//        // 1. 保存到数据库
//        // 2. 发送通知
//        // 3. 触发业务流程
//        // 4. 更新用户余额
//        // 5. 风控检查
//    }
//
//    /**
//     * 收集统计信息
//     */
//    private void collectStatistics() {
//        UserCryptoAccountService.ServiceStats userStats = userCryptoAccountService.getServiceStats();
//
//        log.info("监控统计信息 - 用户: {}/{}, 代币: {}/{}, 交易队列: {}",
//            userStats.getActiveUsers(), userStats.getTotalUsers(),
//            userStats.getActiveTokens(), userStats.getTotalTokens(),
//            transactionQueue.size());
//
//        // 输出各用户的交易统计
//        for (Map.Entry<String, Long> entry : transactionCounts.entrySet()) {
//            String key = entry.getKey();
//            Long count = entry.getValue();
//            BigDecimal totalAmount = totalAmounts.getOrDefault(key, BigDecimal.ZERO);
//
//            String[] parts = key.split("_");
//            if (parts.length == 2) {
//                String userId = parts[0];
//                String tokenSymbol = parts[1];
//                log.info("用户 {} {} 交易统计: 次数={}, 总金额={}", userId, tokenSymbol, count, totalAmount);
//            }
//        }
//    }
//
//    /**
//     * 添加新的代币监控
//     */
//    public void addTokenMonitoring(UserCryptoAccountService.TokenInfo token) {
//        if (!eventSubscriptions.containsKey(token.getContractAddress())) {
//            startTokenEventListening(token);
//        }
//    }
//
//    /**
//     * 移除代币监控
//     */
//    public void removeTokenMonitoring(String contractAddress) {
//        Disposable subscription = eventSubscriptions.remove(contractAddress);
//        if (subscription != null) {
//            subscription.dispose();
//            log.info("移除代币监控: {}", contractAddress);
//        }
//    }
//
//    /**
//     * 获取监控统计信息
//     */
//    public Map<String, Object> getMonitoringStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("activeSubscriptions", eventSubscriptions.size());
//        stats.put("queueSize", transactionQueue.size());
//        stats.put("lastProcessedBlock", lastProcessedBlock.get());
//        stats.put("transactionCounts", new HashMap<>(transactionCounts));
//        stats.put("totalAmounts", new HashMap<>(totalAmounts));
//        return stats;
//    }
//
//    /**
//     * 停止监控服务
//     */
//    public void stopMonitoring() {
//        log.info("停止多用户交易监控服务...");
//
//        // 停止所有事件订阅
//        for (Map.Entry<String, Disposable> entry : eventSubscriptions.entrySet()) {
//            entry.getValue().dispose();
//        }
//        eventSubscriptions.clear();
//
//        // 关闭调度器
//        scheduler.shutdown();
//
//        // 关闭交易处理器
//        transactionProcessor.shutdown();
//
//        try {
//            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
//                scheduler.shutdownNow();
//            }
//            if (!transactionProcessor.awaitTermination(5, TimeUnit.SECONDS)) {
//                transactionProcessor.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            scheduler.shutdownNow();
//            transactionProcessor.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//
//        log.info("多用户交易监控服务已停止");
//    }
//
//    /**
//     * 交易事件处理器接口
//     */
//    public interface TransactionEventProcessor {
//        void processTransactionEvent(TransactionEvent event);
//    }
//
//    /**
//     * 交易事件类
//     */
//    @Data
//    public static class TransactionEvent {
//        private String txHash;
//        private String from;
//        private String to;
//        private String userId;
//        private String tokenSymbol;
//        private BigDecimal amount;
//        private String contractAddress; // 代币合约地址（ERC20）
//        private BigInteger blockNumber;
//        private EventType eventType;
//        private long timestamp;
//        private Map<String, Object> metadata; // 扩展字段
//
//        public enum EventType {
//            ETH_TRANSFER,      // ETH转账
//            TOKEN_TRANSFER     // 代币转账
//        }
//    }
//}
