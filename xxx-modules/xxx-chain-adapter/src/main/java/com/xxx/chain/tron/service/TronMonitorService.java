//package com.xxx.chain.tron.service;
//
//import com.xxx.chain.tron.config.TronConfig;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Tron完整监控服务启动器
// * 集成区块同步状态管理、历史区块同步和交易监控功能
// */
//@Slf4j
//public class TronMonitorService {
//
//    private final TronConfig config;
//
//    // 核心服务
//    private final TronBlockSyncStateManager syncStateManager;
//    private final TronTransactionMonitor transactionMonitor;
//
//    // 控制服务
//    private final ScheduledExecutorService controlExecutor;
//    private volatile boolean isRunning = false;
//
//    public TronMonitorService(TronConfig config) {
//        this.config = config;
//
//        // 初始化服务
//        this.syncStateManager = new TronBlockSyncStateManager(config);
//        this.transactionMonitor = new TronTransactionMonitor(config);
//
//        this.controlExecutor = Executors.newSingleThreadScheduledExecutor();
//
//        log.info("Tron监控服务启动器初始化完成");
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
//        log.info("启动Tron监控服务...");
//
//        try {
//            // 1. 初始化同步状态
//            log.info("步骤1: 初始化区块同步状态...");
//            syncStateManager.initializeSyncState();
//
//            // 2. 启动交易监控
//            log.info("步骤2: 启动交易监控...");
//            transactionMonitor.startMonitoring();
//
//            // 3. 启动控制循环
//            startControlLoop();
//
//            isRunning = true;
//            log.info("Tron监控服务启动完成");
//
//        } catch (Exception e) {
//            log.error("启动监控服务失败", e);
//            throw new RuntimeException("启动监控服务失败", e);
//        }
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
//            TronBlockSyncStateManager.SyncProgress progress = syncStateManager.getSyncProgress();
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
//            // 检查网络连接
//            boolean networkHealthy = checkNetworkHealth();
//
//            // 检查监控服务
//            Map<String, Object> monitorStats = transactionMonitor.getMonitoringStats();
//
//            if (!networkHealthy) {
//                log.warn("Tron网络连接异常");
//            }
//
//            log.debug("服务健康检查 - 网络: {}, 监控: {}, 事件处理器: {}",
//                networkHealthy ? "正常" : "异常",
//                monitorStats.get("isMonitoring"),
//                monitorStats.get("eventHandlers"));
//
//        } catch (Exception e) {
//            log.error("检查服务健康状态失败", e);
//        }
//    }
//
//    /**
//     * 检查网络健康状态
//     */
//    private boolean checkNetworkHealth() {
//        try {
//            // 通过同步状态管理器检查网络连接
//            long currentBlock = syncStateManager.getCurrentBlockNumber();
//            return currentBlock > 0;
//        } catch (Exception e) {
//            log.error("检查网络健康状态失败", e);
//            return false;
//        }
//    }
//
//    /**
//     * 输出统计信息
//     */
//    private void outputStatistics() {
//        try {
//            // 获取同步进度
//            TronBlockSyncStateManager.SyncProgress progress = syncStateManager.getSyncProgress();
//
//            // 获取监控统计
//            Map<String, Object> monitorStats = transactionMonitor.getMonitoringStats();
//
//            log.info("=== Tron监控服务统计 ===");
//            log.info("当前区块: {}", progress.getCurrentBlock());
//            log.info("已同步区块: {}", progress.getLastSyncedBlock());
//            log.info("待同步区块: {}", progress.getPendingBlocks());
//            log.info("同步进度: {:.2f}%", progress.getProgressPercentage());
//            log.info("监控状态: {}", monitorStats.get("isMonitoring"));
//            log.info("交易统计: {}", monitorStats.get("transactionCounts"));
//            log.info("金额统计: {}", monitorStats.get("totalAmounts"));
//
//        } catch (Exception e) {
//            log.error("输出统计信息失败", e);
//        }
//    }
//
//    /**
//     * 获取同步进度
//     */
//    public TronBlockSyncStateManager.SyncProgress getSyncProgress() {
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
//     * 手动处理指定区块
//     */
//    public void processBlockManually(long blockNumber) {
//        log.info("手动处理区块: {}", blockNumber);
//        transactionMonitor.processBlockManually(blockNumber);
//    }
//
//    /**
//     * 添加交易事件处理器
//     */
//    public void addTransactionEventHandler(TronTransactionMonitor.TransactionEventHandler handler) {
//        transactionMonitor.addEventHandler(handler);
//    }
//
//    /**
//     * 移除交易事件处理器
//     */
//    public void removeTransactionEventHandler(TronTransactionMonitor.TransactionEventHandler handler) {
//        transactionMonitor.removeEventHandler(handler);
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
//        log.info("停止Tron监控服务...");
//
//        try {
//            // 停止控制循环
//            controlExecutor.shutdown();
//
//            // 停止交易监控
//            transactionMonitor.stopMonitoring();
//
//            isRunning = false;
//            log.info("Tron监控服务已停止");
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
//        log.info("开始优雅关闭Tron监控服务...");
//
//        stop();
//        log.info("Tron监控服务优雅关闭完成");
//    }
//
//    /**
//     * 获取监控统计信息
//     */
//    public Map<String, Object> getMonitoringStats() {
//        Map<String, Object> stats = new HashMap<>();
//
//        try {
//            // 同步状态统计
//            TronBlockSyncStateManager.SyncProgress progress = syncStateManager.getSyncProgress();
//            stats.put("currentBlock", progress.getCurrentBlock());
//            stats.put("lastSyncedBlock", progress.getLastSyncedBlock());
//            stats.put("pendingBlocks", progress.getPendingBlocks());
//            stats.put("progressPercentage", progress.getProgressPercentage());
//
//            // 交易监控统计
//            Map<String, Object> monitorStats = transactionMonitor.getMonitoringStats();
//            stats.putAll(monitorStats);
//
//            // 服务状态
//            stats.put("isRunning", isRunning);
//
//        } catch (Exception e) {
//            log.error("获取监控统计信息失败", e);
//        }
//
//        return stats;
//    }
//}
