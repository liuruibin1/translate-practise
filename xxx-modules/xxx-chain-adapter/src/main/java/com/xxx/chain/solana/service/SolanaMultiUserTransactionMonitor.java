//package com.xxx.chain.solana.service;
//
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.xxx.chain.solana.config.SolanaConfig;
//import com.xxx.chain.solana.entity.SolanaTransactionEvent;
//import com.xxx.chain.solana.utils.SolanaRpcClient;
//import com.xxx.chain.solana.utils.SolanaTransactionParser;
//import lombok.extern.slf4j.Slf4j;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * Solana多用户交易监控服务
// * 支持监控多个用户地址和SOL、USDT代币
// */
//@Slf4j
//public class SolanaMultiUserTransactionMonitor {
//
//    private final SolanaConfig config;
//    private final SolanaRpcClient rpcClient;
//    private final SolanaTransactionParser transactionParser;
//    private final UserCryptoAccountService userCryptoAccountService;
//    private final ScheduledExecutorService scheduler;
//    private final ExecutorService transactionProcessor;
//    private final AtomicLong lastProcessedSlot;
//
//    // 交易处理队列
//    private final BlockingQueue<SolanaTransactionEvent> transactionQueue = new LinkedBlockingQueue<>(10000);
//
//    // 统计信息
//    private final Map<String, Long> transactionCounts = new ConcurrentHashMap<>();
//    private final Map<String, BigDecimal> totalAmounts = new ConcurrentHashMap<>();
//
//    // 监控状态
//    private volatile boolean isMonitoring = false;
//
//    public SolanaMultiUserTransactionMonitor(SolanaConfig config, UserCryptoAccountService userCryptoAccountService) {
//        this.config = config;
//        this.userCryptoAccountService = userCryptoAccountService;
//        this.rpcClient = new SolanaRpcClient(config);
//        this.transactionParser = new SolanaTransactionParser(config);
//        this.lastProcessedSlot = new AtomicLong(0);
//
//        // 初始化调度器和处理器
//        this.scheduler = Executors.newScheduledThreadPool(3);
//        this.transactionProcessor = Executors.newFixedThreadPool(5);
//
//        log.info("Solana多用户交易监控服务初始化完成");
//    }
//
//    /**
//     * 启动监控服务
//     */
//    public void startMonitoring() {
//        if (isMonitoring) {
//            log.warn("监控服务已在运行中");
//            return;
//        }
//
//        log.info("启动Solana多用户交易监控服务...");
//        isMonitoring = true;
//
//        // 启动区块监控
//        startSlotMonitoring();
//
//        // 启动交易处理器
//        startTransactionProcessor();
//
//        // 启动统计信息收集
//        startStatisticsCollection();
//
//        log.info("Solana多用户交易监控服务启动完成");
//    }
//
//    /**
//     * 启动区块监控
//     */
//    private void startSlotMonitoring() {
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                if (isMonitoring) {
//                    monitorNewSlots();
//                }
//            } catch (Exception e) {
//                log.error("区块监控异常", e);
//            }
//        }, 0, config.getMonitor().getInterval(), TimeUnit.MILLISECONDS);
//    }
//
//    /**
//     * 启动交易处理器
//     */
//    private void startTransactionProcessor() {
//        for (int i = 0; i < 5; i++) {
//            transactionProcessor.submit(() -> {
//                while (!Thread.currentThread().isInterrupted() && isMonitoring) {
//                    try {
//                        SolanaTransactionEvent event = transactionQueue.poll(1, TimeUnit.SECONDS);
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
//                if (isMonitoring) {
//                    collectStatistics();
//                }
//            } catch (Exception e) {
//                log.error("收集统计信息异常", e);
//            }
//        }, 1, 1, TimeUnit.MINUTES);
//    }
//
//    /**
//     * 监控新区块
//     */
//    private void monitorNewSlots() {
//        try {
//            Long latestSlot = rpcClient.getLatestSlot();
//            if (latestSlot != null) {
//                Long lastProcessed = lastProcessedSlot.get();
//
//                if (latestSlot > lastProcessed) {
//                    // 处理新区块范围
//                    long startSlot = lastProcessed == 0 ? latestSlot - 1 : lastProcessed + 1;
//                    processNewSlots(startSlot, latestSlot);
//                    lastProcessedSlot.set(latestSlot);
//                }
//            }
//        } catch (Exception e) {
//            log.error("获取最新区块高度失败", e);
//        }
//    }
//
//    /**
//     * 处理新区块范围
//     */
//    private void processNewSlots(long startSlot, long endSlot) {
//        log.debug("处理新区块范围: {} - {}", startSlot, endSlot);
//
//        for (long slot = startSlot; slot <= endSlot; slot++) {
//            try {
//                processNewSlot(slot);
//
//                // 避免请求过于频繁
//                if (slot < endSlot) {
//                    Thread.sleep(100);
//                }
//            } catch (Exception e) {
//                log.error("处理区块失败, slot: {}", slot, e);
//            }
//        }
//    }
//
//    /**
//     * 处理新区块
//     */
//    private void processNewSlot(long slot) {
//        try {
//            JSONObject block = rpcClient.getBlock(slot);
//            if (block == null) {
//                return;
//            }
//
//            // 获取区块时间戳
//            Long blockTime = block.getLong("blockTime");
//
//            // 获取交易列表
//            JSONArray transactions = block.getJSONArray("transactions");
//            if (transactions == null || transactions.isEmpty()) {
//                return;
//            }
//
//            log.debug("区块 {} 包含 {} 笔交易", slot, transactions.size());
//
//            // 处理每笔交易
//            for (int i = 0; i < transactions.size(); i++) {
//                JSONObject transaction = transactions.getJSONObject(i);
//                if (transaction != null) {
//                    processTransaction(transaction, slot, blockTime);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理区块异常, slot: {}", slot, e);
//        }
//    }
//
//    /**
//     * 处理交易
//     */
//    private void processTransaction(JSONObject transaction, Long slot, Long blockTime) {
//        try {
//            // 解析交易
//            List<SolanaTransactionEvent> events = transactionParser.parseTransaction(transaction, slot, blockTime);
//
//            // 过滤相关交易
//            for (SolanaTransactionEvent event : events) {
//                if (isRelevantTransaction(event)) {
//                    // 添加到处理队列
//                    transactionQueue.offer(event);
//
//                    log.info("检测到相关交易: 代币={}, 从={}, 到={}, 金额={}, 签名={}",
//                        event.getTokenSymbol(), event.getFrom(), event.getTo(),
//                        event.getAmount(), event.getSignature());
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理交易异常: {}", transaction, e);
//        }
//    }
//
//    /**
//     * 判断交易是否相关
//     */
//    private boolean isRelevantTransaction(SolanaTransactionEvent event) {
//        try {
//            // 检查是否监控SOL转账
//            if (config.getMonitor().getMonitorSol() &&
//                event.getEventType() == SolanaTransactionEvent.EventType.SOL_TRANSFER) {
//
//                // 检查接收方是否是被监控的地址
//                if (userCryptoAccountService.isAddressMonitored(event.getTo())) {
//                    String userId = userCryptoAccountService.getUserIdByAddress(event.getTo());
//                    if (userId != null) {
//                        event.setUserId(userId);
//                        return true;
//                    }
//                }
//            }
//
//            // 检查是否监控USDT转账
//            if (config.getMonitor().getMonitorUsdt() &&
//                event.getEventType() == SolanaTransactionEvent.EventType.TOKEN_TRANSFER) {
//
//                // 检查是否是USDT
//                if (config.getToken().getUsdt().getMintAddress().equals(event.getMintAddress())) {
//
//                    // 检查接收方是否是被监控的地址
//                    if (userCryptoAccountService.isAddressMonitored(event.getTo())) {
//                        String userId = userCryptoAccountService.getUserIdByAddress(event.getTo());
//                        if (userId != null) {
//                            event.setUserId(userId);
//                            return true;
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("判断交易相关性异常", e);
//        }
//
//        return false;
//    }
//
//    /**
//     * 处理交易事件
//     */
//    public void processTransactionEvent(SolanaTransactionEvent event) {
//        try {
//            // 更新统计信息
//            updateStatistics(event);
//
//            // 处理业务逻辑
//            handleTransactionEvent(event);
//
//        } catch (Exception e) {
//            log.error("处理交易事件异常: {}", event.getSignature(), e);
//        }
//    }
//
//    /**
//     * 更新统计信息
//     */
//    private void updateStatistics(SolanaTransactionEvent event) {
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
//    private void handleTransactionEvent(SolanaTransactionEvent event) {
//        // TODO: 实现具体的业务逻辑
//        log.info("处理交易事件: 用户ID={}, 代币={}, 金额={}, 交易签名={}",
//            event.getUserId(), event.getTokenSymbol(), event.getAmount(), event.getSignature());
//
//        // 这里可以添加：
//        // 1. 保存到数据库
//        // 2. 发送通知
//        // 3. 触发业务流程
//        // 4. 更新用户余额
//        // 5. 风控检查
//        // 6. 记录审计日志
//    }
//
//    /**
//     * 收集统计信息
//     */
//    private void collectStatistics() {
//        try {
//            UserCryptoAccountService.ServiceStats userStats = userCryptoAccountService.getServiceStats();
//
//            log.info("Solana监控统计信息 - 用户: {}/{}, 代币: {}/{}, 交易队列: {}, 最后处理区块: {}",
//                userStats.getActiveUsers(), userStats.getTotalUsers(),
//                userStats.getActiveTokens(), userStats.getTotalTokens(),
//                transactionQueue.size(), lastProcessedSlot.get());
//
//            // 输出各用户的交易统计
//            for (Map.Entry<String, Long> entry : transactionCounts.entrySet()) {
//                String key = entry.getKey();
//                Long count = entry.getValue();
//                BigDecimal totalAmount = totalAmounts.getOrDefault(key, BigDecimal.ZERO);
//
//                String[] parts = key.split("_");
//                if (parts.length == 2) {
//                    String userId = parts[0];
//                    String tokenSymbol = parts[1];
//                    log.info("用户 {} {} 交易统计: 次数={}, 总金额={}", userId, tokenSymbol, count, totalAmount);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("收集统计信息异常", e);
//        }
//    }
//
//    /**
//     * 手动同步指定区块范围
//     */
//    public void syncSlotRange(long startSlot, long endSlot) {
//        log.info("开始手动同步区块范围: {} - {}", startSlot, endSlot);
//
//        try {
//            processNewSlots(startSlot, endSlot);
//            lastProcessedSlot.set(endSlot);
//            log.info("手动同步完成: {} - {}", startSlot, endSlot);
//        } catch (Exception e) {
//            log.error("手动同步失败: {} - {}", startSlot, endSlot, e);
//        }
//    }
//
//    /**
//     * 获取监控统计信息
//     */
//    public Map<String, Object> getMonitoringStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("isMonitoring", isMonitoring);
//        stats.put("queueSize", transactionQueue.size());
//        stats.put("lastProcessedSlot", lastProcessedSlot.get());
//        stats.put("transactionCounts", new HashMap<>(transactionCounts));
//        stats.put("totalAmounts", new HashMap<>(totalAmounts));
//        return stats;
//    }
//
//    /**
//     * 停止监控服务
//     */
//    public void stopMonitoring() {
//        if (!isMonitoring) {
//            log.warn("监控服务未在运行");
//            return;
//        }
//
//        log.info("停止Solana多用户交易监控服务...");
//        isMonitoring = false;
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
//        // 关闭RPC客户端
//        rpcClient.close();
//
//        log.info("Solana多用户交易监控服务已停止");
//    }
//
//    /**
//     * 交易事件处理器接口
//     */
//    public interface TransactionEventProcessor {
//        void processTransactionEvent(SolanaTransactionEvent event);
//    }
//
//    /**
//     * 用户加密账户服务接口
//     */
//    public interface UserCryptoAccountService {
//
//        /**
//         * 检查地址是否被监控
//         */
//        boolean isAddressMonitored(String address);
//
//        /**
//         * 根据地址获取用户ID
//         */
//        String getUserIdByAddress(String address);
//
//        /**
//         * 获取服务统计信息
//         */
//        ServiceStats getServiceStats();
//
//        /**
//         * 服务统计信息
//         */
//        class ServiceStats {
//            private int activeUsers;
//            private int totalUsers;
//            private int activeTokens;
//            private int totalTokens;
//
//            // getters and setters
//            public int getActiveUsers() { return activeUsers; }
//            public void setActiveUsers(int activeUsers) { this.activeUsers = activeUsers; }
//            public int getTotalUsers() { return totalUsers; }
//            public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }
//            public int getActiveTokens() { return activeTokens; }
//            public void setActiveTokens(int activeTokens) { this.activeTokens = activeTokens; }
//            public int getTotalTokens() { return totalTokens; }
//            public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }
//        }
//    }
//}
