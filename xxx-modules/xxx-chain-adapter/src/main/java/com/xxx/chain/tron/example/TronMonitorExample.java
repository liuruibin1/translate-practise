//package com.xxx.chain.tron.example;
//
//import com.xxx.chain.tron.config.TronConfig;
//import com.xxx.chain.tron.service.TronMonitorService;
//import com.xxx.chain.tron.service.TronTransactionMonitor;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Tron监控示例
// * 展示如何使用Tron监控服务监控TRX和USDT转账
// */
//@Slf4j
//public class TronMonitorExample {
//
//    public static void main(String[] args) {
//        // 创建配置
//        TronConfig config = createConfig();
//
//        // 创建监控服务
//        TronMonitorService monitorService = new TronMonitorService(config);
//
//        // 添加交易事件处理器
//        monitorService.addTransactionEventHandler(new TronTransactionMonitor.TransactionEventHandler() {
//            @Override
//            public void onTransactionEvent(TronTransactionMonitor.TransactionEvent event) {
//                handleTransactionEvent(event);
//            }
//        });
//
//        try {
//            // 启动监控服务
//            log.info("启动Tron监控服务...");
//            monitorService.start();
//
//            // 等待服务启动和初始化
//            Thread.sleep(10000);
//
//            // 显示同步进度
//            displaySyncProgress(monitorService);
//
//            // 模拟运行一段时间
//            log.info("Tron监控服务运行中，按Ctrl+C停止...");
//
//            // 模拟运行5分钟
//            for (int i = 0; i < 60; i++) {
//                Thread.sleep(5000); // 每5秒检查一次
//
//                // 每30秒显示一次状态
//                if (i % 6 == 0) {
//                    displayServiceStatus(monitorService);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("运行Tron监控服务异常", e);
//        } finally {
//            // 优雅关闭服务
//            log.info("开始关闭Tron监控服务...");
//            monitorService.shutdown();
//        }
//    }
//
//    /**
//     * 创建配置
//     */
//    private static TronConfig createConfig() {
//        TronConfig config = new TronConfig();
//
//        // 网络配置 - 使用主网
//        TronConfig.Network network = new TronConfig.Network();
//        network.setNetworkType("mainnet");
//        network.setApiKey("YOUR_TRON_API_KEY"); // 替换为你的API Key
//        network.setConfirmations(20); // 20个区块确认
//        network.setTimeout(30000); // 30秒超时
//        config.setNetwork(network);
//
//        // 合约配置
//        TronConfig.Contract contract = new TronConfig.Contract();
//        contract.setUsdtAddress("TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t"); // 主网USDT
//        contract.setTronTargetAddress("TQn9Y2khDD95J42FQtQTdwVVRZQJqJqJqJ"); // 目标地址
//        config.setContract(contract);
//
//        // 监控配置
//        TronConfig.Monitor monitor = new TronConfig.Monitor();
//        monitor.setEnabled(true);
//        monitor.setInterval(15000L); // 15秒检查一次
//        monitor.setMonitorTrx(true);
//        monitor.setMonitorUsdt(true);
//        monitor.setMinTrxAmount("1000000"); // 1 TRX (1,000,000 Sun)
//        monitor.setMinUsdtAmount("1000000"); // 1 USDT (6位小数)
//        monitor.setBlockRange(100);
//        monitor.setMaxConcurrentRequests(10);
//        config.setMonitor(monitor);
//
//        return config;
//    }
//
//    /**
//     * 处理交易事件
//     */
//    private static void handleTransactionEvent(TronTransactionMonitor.TransactionEvent event) {
//        try {
//            log.info("=== 检测到Tron交易事件 ===");
//            log.info("交易ID: {}", event.getTxId());
//            log.info("代币: {}", event.getTokenSymbol());
//            log.info("金额: {} {}", event.getAmount(), event.getTokenSymbol());
//            log.info("从地址: {}", event.getFromAddress());
//            log.info("到地址: {}", event.getToAddress());
//            log.info("区块号: {}", event.getBlockNumber());
//            log.info("事件类型: {}", event.getEventType());
//            log.info("时间戳: {}", event.getTimestamp());
//
//            if (event.getContractAddress() != null) {
//                log.info("合约地址: {}", event.getContractAddress());
//            }
//
//            // 在这里可以添加业务逻辑，比如：
//            // - 保存到数据库
//            // - 发送通知
//            // - 更新用户余额
//            // - 触发其他业务流程
//
//            log.info("交易事件处理完成");
//
//        } catch (Exception e) {
//            log.error("处理交易事件失败", e);
//        }
//    }
//
//    /**
//     * 显示同步进度
//     */
//    private static void displaySyncProgress(TronMonitorService monitorService) {
//        try {
//            var progress = monitorService.getSyncProgress();
//
//            log.info("=== Tron同步进度 ===");
//            log.info("当前区块: {}", progress.getCurrentBlock());
//            log.info("已同步区块: {}", progress.getLastSyncedBlock());
//            log.info("待同步区块: {}", progress.getPendingBlocks());
//            log.info("区块确认数: {}", progress.getConfirmations());
//            log.info("同步进度: {:.2f}%", progress.getProgressPercentage());
//
//            if (progress.getPendingBlocks() > 0) {
//                log.info("需要同步 {} 个区块", progress.getPendingBlocks());
//            } else {
//                log.info("同步状态正常，无需同步");
//            }
//
//        } catch (Exception e) {
//            log.error("获取同步进度失败", e);
//        }
//    }
//
//    /**
//     * 显示服务状态
//     */
//    private static void displayServiceStatus(TronMonitorService monitorService) {
//        try {
//            log.info("=== Tron服务状态检查 ===");
//
//            // 检查服务运行状态
//            boolean isRunning = monitorService.isRunning();
//            log.info("服务运行状态: {}", isRunning ? "运行中" : "已停止");
//
//            // 获取同步进度
//            var progress = monitorService.getSyncProgress();
//            log.info("同步状态: {:.2f}% - 待同步: {} 区块",
//                progress.getProgressPercentage(), progress.getPendingBlocks());
//
//            // 获取监控统计
//            var stats = monitorService.getMonitoringStats();
//            log.info("监控状态: {}", stats.get("isMonitoring"));
//            log.info("交易统计: {}", stats.get("transactionCounts"));
//            log.info("金额统计: {}", stats.get("totalAmounts"));
//
//            // 检查是否需要手动同步
//            if (progress.getPendingBlocks() > 100) {
//                log.warn("待同步区块过多，考虑手动触发同步");
//            }
//
//        } catch (Exception e) {
//            log.error("获取服务状态失败", e);
//        }
//    }
//
//    /**
//     * 演示手动处理区块
//     */
//    public static void demonstrateManualBlockProcessing(TronMonitorService monitorService) {
//        log.info("演示手动处理区块...");
//
//        try {
//            // 获取当前同步状态
//            var progress = monitorService.getSyncProgress();
//            long currentBlock = progress.getCurrentBlock();
//
//            // 手动处理当前区块
//            if (currentBlock > 0) {
//                log.info("手动处理区块: {}", currentBlock);
//                monitorService.processBlockManually(currentBlock);
//            }
//
//        } catch (Exception e) {
//            log.error("手动处理区块失败", e);
//        }
//    }
//
//    /**
//     * 演示强制同步
//     */
//    public static void demonstrateForceSync(TronMonitorService monitorService) {
//        log.info("演示强制同步...");
//
//        try {
//            // 获取当前同步状态
//            var progress = monitorService.getSyncProgress();
//            long currentBlock = progress.getCurrentBlock();
//
//            // 强制同步到当前区块前100个区块
//            long targetBlock = Math.max(0, currentBlock - 100);
//
//            log.info("强制同步到区块: {} (当前: {})", targetBlock, currentBlock);
//            monitorService.forceSyncToBlock(targetBlock);
//
//            // 等待同步完成
//            Thread.sleep(5000);
//
//            // 检查同步状态
//            var newProgress = monitorService.getSyncProgress();
//            log.info("强制同步后状态: 已同步区块: {}", newProgress.getLastSyncedBlock());
//
//        } catch (Exception e) {
//            log.error("强制同步失败", e);
//        }
//    }
//
//    /**
//     * 演示重置同步状态
//     */
//    public static void demonstrateResetSync(TronMonitorService monitorService) {
//        log.info("演示重置同步状态...");
//
//        try {
//            // 警告：这会重置所有同步进度
//            log.warn("即将重置同步状态，这将清除所有同步进度！");
//
//            // 在实际使用中，应该添加确认机制
//            // 这里仅作演示
//            // monitorService.resetSyncState();
//
//            log.info("重置同步状态演示完成（未实际执行）");
//
//        } catch (Exception e) {
//            log.error("重置同步状态失败", e);
//        }
//    }
//
//    /**
//     * 演示重启后的恢复流程
//     */
//    public static void demonstrateRestartRecovery(TronMonitorService monitorService) {
//        log.info("演示重启后的恢复流程...");
//
//        try {
//            // 1. 停止服务
//            log.info("步骤1: 停止监控服务");
//            monitorService.stop();
//
//            // 2. 等待一段时间（模拟重启）
//            log.info("步骤2: 等待5秒（模拟重启）");
//            Thread.sleep(5000);
//
//            // 3. 重新启动服务
//            log.info("步骤3: 重新启动监控服务");
//            monitorService.start();
//
//            // 4. 等待服务初始化
//            log.info("步骤4: 等待服务初始化");
//            Thread.sleep(10000);
//
//            // 5. 检查同步状态
//            log.info("步骤5: 检查同步状态");
//            displaySyncProgress(monitorService);
//
//            log.info("重启恢复流程演示完成");
//
//        } catch (Exception e) {
//            log.error("重启恢复流程演示失败", e);
//        }
//    }
//}
