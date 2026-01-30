//package com.xxx.chain.ethereum.service;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.http.HttpService;
//
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Ethereum监控服务启动器
// * 集成区块同步状态管理、历史区块同步和多用户监控功能
// */
//@Slf4j
//public class EthereumMonitorService {
//
//    private final EthereumConfig config;
//    private final Web3j web3j;
//
//    // 核心服务
//    private final UserCryptoAccountService userCryptoAccountService;
//    private final BlockSyncStateManager syncStateManager;
//    private final MultiUserTransactionMonitor transactionMonitor;
//    private final HistoricalBlockSyncService historicalSyncService;
//
//    // 控制服务
//    private final ScheduledExecutorService controlExecutor;
//    private volatile boolean isRunning = false;
//
//    public EthereumMonitorService(EthereumConfig config) {
//        this.config = config;
//        this.web3j = Web3j.build(new HttpService(config.getNetwork().getRpcUrl()));
//
//        // 初始化服务
//        this.userCryptoAccountService = new UserCryptoAccountService(config);
//        this.syncStateManager = new BlockSyncStateManager(
//            web3j,
//            String.valueOf(config.getNetwork().getChainId()),
//            1000, // 最大区块范围
//            config.getNetwork().getConfirmations()
//        );
//        this.transactionMonitor = new MultiUserTransactionMonitor(config, web3j, userCryptoAccountService);
//        this.historicalSyncService = new HistoricalBlockSyncService(
//            web3j, userCryptoAccountService, syncStateManager, transactionMonitor
//        );
//
//        this.controlExecutor = Executors.newSingleThreadScheduledExecutor();
//
//        log.info("Ethereum监控服务启动器初始化完成");
//    }
//
//    /**
//     * 启动监控服务
//     */
//    public void start() {
//        if (isRunning) {
//            log.warn("监控服务已在运行中");
//            return;
//        }
//
//        log.info("启动Ethereum监控服务...");
//
//        try {
//            // 1. 初始化同步状态
//            log.info("步骤1: 初始化区块同步状态...");
//            syncStateManager.initializeSyncState();
//
//            // 2. 启动历史区块同步（如果需要）
//            if (syncStateManager.needsHistoricalSync()) {
//                log.info("步骤2: 启动历史区块同步...");
//                startHistoricalSync();
//            } else {
//                log.info("步骤2: 无需历史区块同步");
//            }
//
//            // 3. 启动实时监控
//            log.info("步骤3: 启动实时交易监控...");
//            transactionMonitor.startMonitoring();
//
//            // 4. 启动控制循环
//            startControlLoop();
//
//            isRunning = true;
//            log.info("Ethereum监控服务启动完成");
//
//        } catch (Exception e) {
//            log.error("启动监控服务失败", e);
//            throw new RuntimeException("启动监控服务失败", e);
//        }
//    }
//
//    /**
//     * 启动历史区块同步
//     */
//    private void startHistoricalSync() {
//        CompletableFuture<HistoricalBlockSyncService.SyncResult> syncFuture =
//            historicalSyncService.startHistoricalSync();
//
//        syncFuture.thenAccept(result -> {
//            if (result.isSuccess()) {
//                log.info("历史区块同步完成: {}", result.getMessage());
//                log.info("处理区块: {}, 处理交易: {}, 耗时: {}ms",
//                    result.getProcessedBlocks(),
//                    result.getProcessedTransactions(),
//                    result.getDuration());
//            } else {
//                log.error("历史区块同步失败: {}", result.getMessage());
//            }
//        }).exceptionally(throwable -> {
//            log.error("历史区块同步异常", throwable);
//            return null;
//        });
//    }
//
//    /**
//     * 启动控制循环
//     */
//    private void startControlLoop() {
//        controlExecutor.scheduleAtFixedRate(() -> {
//            try {
//                // 检查同步状态
//                checkSyncStatus();
//
//                // 检查服务健康状态
//                checkServiceHealth();
//
//                // 输出统计信息
//                outputStatistics();
//
//            } catch (Exception e) {
//                log.error("控制循环异常", e);
//            }
//        }, 30, 30, TimeUnit.SECONDS);
//    }
//
//    /**
//     * 检查同步状态
//     */
//    private void checkSyncStatus() {
//        try {
//            BlockSyncStateManager.SyncProgress progress = syncStateManager.getSyncProgress();
//
//            if (progress.getPendingBlocks() > 0) {
//                log.info("同步进度: {:.2f}% - 待同步区块: {}",
//                    progress.getProgressPercentage(), progress.getPendingBlocks());
//
//                // 如果待同步区块过多，可能需要重新启动历史同步
//                if (progress.getPendingBlocks() > 1000) {
//                    log.warn("待同步区块过多 ({}), 考虑重新启动历史同步", progress.getPendingBlocks());
//                }
//            } else {
//                log.debug("同步状态正常，无需同步");
//            }
//
//        } catch (Exception e) {
//            log.error("检查同步状态失败", e);
//        }
//    }
//
//    /**
//     * 检查服务健康状态
//     */
//    private void checkServiceHealth() {
//        try {
//            // 检查Web3j连接
//            boolean web3jHealthy = checkWeb3jHealth();
//
//            // 检查用户地址服务
//            UserCryptoAccountService.ServiceStats userStats = userCryptoAccountService.getServiceStats();
//
//            // 检查监控服务
//            Map<String, Object> monitorStats = transactionMonitor.getMonitoringStats();
//
//            if (!web3jHealthy) {
//                log.warn("Web3j连接异常");
//            }
//
//            log.debug("服务健康检查 - Web3j: {}, 用户: {}/{}, 代币: {}/{}, 订阅: {}",
//                web3jHealthy ? "正常" : "异常",
//                userStats.getActiveUsers(), userStats.getTotalUsers(),
//                userStats.getActiveTokens(), userStats.getTotalTokens(),
//                monitorStats.get("activeSubscriptions"));
//
//        } catch (Exception e) {
//            log.error("检查服务健康状态失败", e);
//        }
//    }
//
//    /**
//     * 检查Web3j连接健康状态
//     */
//    private boolean checkWeb3jHealth() {
//        try {
//            // 尝试获取最新区块号
//            var response = web3j.ethBlockNumber().send();
//            return !response.hasError();
//        } catch (Exception e) {
//            log.error("Web3j健康检查失败", e);
//            return false;
//        }
//    }
//
//    /**
//     * 输出统计信息
//     */
//    private void outputStatistics() {
//        try {
//            // 获取用户地址统计
//            UserCryptoAccountService.ServiceStats userStats = userCryptoAccountService.getServiceStats();
//
//            // 获取监控统计
//            Map<String, Object> monitorStats = transactionMonitor.getMonitoringStats();
//
//            // 获取历史同步统计
//            HistoricalBlockSyncService.SyncStatistics syncStats = historicalSyncService.getSyncStatistics();
//
//            log.info("=== 监控服务统计 ===");
//            log.info("用户地址: {}/{} (活跃/总数)", userStats.getActiveUsers(), userStats.getTotalUsers());
//            log.info("代币合约: {}/{} (活跃/总数)", userStats.getActiveTokens(), userStats.getTotalTokens());
//            log.info("活跃订阅: {}", monitorStats.get("activeSubscriptions"));
//            log.info("交易队列: {}", monitorStats.get("queueSize"));
//            log.info("历史同步: {} 区块, {} 交易",
//                syncStats.getTotalProcessedBlocks(), syncStats.getTotalProcessedTransactions());
//
//        } catch (Exception e) {
//            log.error("输出统计信息失败", e);
//        }
//    }
//
//    /**
//     * 手动触发历史区块同步
//     */
//    public CompletableFuture<HistoricalBlockSyncService.SyncResult> triggerHistoricalSync() {
//        if (historicalSyncService.isSyncing()) {
//            log.warn("历史区块同步已在运行中");
//            return CompletableFuture.completedFuture(
//                new HistoricalBlockSyncService.SyncResult(false, "同步已在运行中")
//            );
//        }
//
//        log.info("手动触发历史区块同步...");
//        return historicalSyncService.startHistoricalSync();
//    }
//
//    /**
//     * 获取同步进度
//     */
//    public BlockSyncStateManager.SyncProgress getSyncProgress() {
//        return syncStateManager.getSyncProgress();
//    }
//
//    /**
//     * 强制同步到指定区块
//     */
//    public void forceSyncToBlock(long blockNumber) {
//        log.warn("强制同步到区块: {}", blockNumber);
//        syncStateManager.forceSyncToBlock(blockNumber);
//    }
//
//    /**
//     * 重置同步状态
//     */
//    public void resetSyncState() {
//        log.warn("重置同步状态");
//        syncStateManager.resetSyncState();
//    }
//
//    /**
//     * 检查服务是否正在运行
//     */
//    public boolean isRunning() {
//        return isRunning;
//    }
//
//    /**
//     * 停止监控服务
//     */
//    public void stop() {
//        if (!isRunning) {
//            log.warn("监控服务未在运行");
//            return;
//        }
//
//        log.info("停止Ethereum监控服务...");
//
//        try {
//            // 停止控制循环
//            controlExecutor.shutdown();
//
//            // 停止历史区块同步
//            historicalSyncService.shutdown();
//
//            // 停止交易监控
//            transactionMonitor.stopMonitoring();
//
//            // 停止用户地址服务
//            userCryptoAccountService.shutdown();
//
//            // 关闭Web3j
//            web3j.shutdown();
//
//            isRunning = false;
//            log.info("Ethereum监控服务已停止");
//
//        } catch (Exception e) {
//            log.error("停止监控服务失败", e);
//        }
//    }
//
//    /**
//     * 优雅关闭
//     */
//    public void shutdown() {
//        log.info("开始优雅关闭Ethereum监控服务...");
//
//        // 等待当前同步完成
//        if (historicalSyncService.isSyncing()) {
//            log.info("等待历史区块同步完成...");
//            try {
//                Thread.sleep(5000); // 等待5秒
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//
//        stop();
//        log.info("Ethereum监控服务优雅关闭完成");
//    }
//}
