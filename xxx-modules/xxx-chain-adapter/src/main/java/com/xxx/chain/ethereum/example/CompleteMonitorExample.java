//package com.xxx.chain.ethereum.example;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import com.xxx.chain.ethereum.service.EthereumMonitorService;
//import com.xxx.chain.ethereum.service.HistoricalBlockSyncService;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.concurrent.CompletableFuture;
//
///**
// * 完整监控示例
// * 展示如何使用区块同步状态管理和历史区块同步功能
// * 解决Java重启后不丢失交易的问题
// */
//@Slf4j
//public class CompleteMonitorExample {
//
//    public static void main(String[] args) {
//        // 创建配置
//        EthereumConfig config = createConfig();
//
//        // 创建监控服务
//        EthereumMonitorService monitorService = new EthereumMonitorService(config);
//
//        try {
//            // 启动监控服务
//            log.info("启动完整监控服务...");
//            monitorService.start();
//
//            // 等待服务启动和初始化
//            Thread.sleep(10000);
//
//            // 显示同步进度
//            displaySyncProgress(monitorService);
//
//            // 模拟运行一段时间
//            log.info("监控服务运行中，按Ctrl+C停止...");
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
//            log.error("运行完整监控服务异常", e);
//        } finally {
//            // 优雅关闭服务
//            log.info("开始关闭监控服务...");
//            monitorService.shutdown();
//        }
//    }
//
//    /**
//     * 创建配置
//     */
//    private static EthereumConfig createConfig() {
//        EthereumConfig config = new EthereumConfig();
//
//        // 网络配置 - 使用测试网
//        EthereumConfig.Network network = new EthereumConfig.Network();
//        network.setRpcUrl("https://goerli.infura.io/v3/YOUR_PROJECT_ID");
//        network.setWsUrl("wss://goerli.infura.io/ws/v3/YOUR_PROJECT_ID");
//        network.setChainId(5L); // Goerli测试网
//        network.setConfirmations(6); // 6个区块确认
//        config.setNetwork(network);
//
//        // 合约配置
//        EthereumConfig.Contract contract = new EthereumConfig.Contract();
//        contract.setUsdtAddress("0x110a13FC3efE6A245B50102cf2CD8021EBeBDee4"); // Goerli USDT
//        contract.setTargetAddress("0x14F2D636e06893dcE422c96aC3B9370dc8bd500d");
//        config.setContract(contract);
//
//        // 监控配置
//        EthereumConfig.Monitor monitor = new EthereumConfig.Monitor();
//        monitor.setEnabled(true);
//        monitor.setInterval(15000L); // 15秒检查一次
//        monitor.setMonitorEth(true);
//        monitor.setMonitorUsdt(true);
//        monitor.setMinEthAmount("1000000000000000"); // 0.001 ETH
//        monitor.setMinUsdtAmount("1000000"); // 1 USDT
//        config.setMonitor(monitor);
//
//        return config;
//    }
//
//    /**
//     * 显示同步进度
//     */
//    private static void displaySyncProgress(EthereumMonitorService monitorService) {
//        try {
//            var progress = monitorService.getSyncProgress();
//
//            log.info("=== 同步进度 ===");
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
//    private static void displayServiceStatus(EthereumMonitorService monitorService) {
//        try {
//            log.info("=== 服务状态检查 ===");
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
//     * 演示手动触发历史区块同步
//     */
//    public static void demonstrateManualSync(EthereumMonitorService monitorService) {
//        log.info("演示手动触发历史区块同步...");
//
//        try {
//            CompletableFuture<HistoricalBlockSyncService.SyncResult> syncFuture =
//                monitorService.triggerHistoricalSync();
//
//            syncFuture.thenAccept(result -> {
//                if (result.isSuccess()) {
//                    log.info("手动同步完成: {}", result.getMessage());
//                    log.info("处理区块: {}, 处理交易: {}, 耗时: {}ms",
//                        result.getProcessedBlocks(),
//                        result.getProcessedTransactions(),
//                        result.getDuration());
//                } else {
//                    log.error("手动同步失败: {}", result.getMessage());
//                }
//            }).exceptionally(throwable -> {
//                log.error("手动同步异常", throwable);
//                return null;
//            });
//
//        } catch (Exception e) {
//            log.error("触发手动同步失败", e);
//        }
//    }
//
//    /**
//     * 演示强制同步到指定区块
//     */
//    public static void demonstrateForceSync(EthereumMonitorService monitorService) {
//        log.info("演示强制同步到指定区块...");
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
//    public static void demonstrateResetSync(EthereumMonitorService monitorService) {
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
//    public static void demonstrateRestartRecovery(EthereumMonitorService monitorService) {
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
//
//    /**
//     * 演示添加新用户地址
//     */
//    public static void demonstrateAddNewUser(EthereumMonitorService monitorService) {
//        log.info("演示添加新用户地址...");
//
//        try {
//            // 注意：这里需要访问UserAddressService，但在当前设计中无法直接访问
//            // 在实际使用中，应该通过适当的接口来管理用户地址
//
//            log.info("新用户地址管理功能需要通过UserAddressService接口实现");
//            log.info("演示完成");
//
//        } catch (Exception e) {
//            log.error("添加新用户演示失败", e);
//        }
//    }
//
//    /**
//     * 演示监控配置更新
//     */
//    public static void demonstrateConfigUpdate(EthereumMonitorService monitorService) {
//        log.info("演示监控配置更新...");
//
//        try {
//            // 在实际使用中，可以通过配置热更新来调整监控参数
//            log.info("监控配置更新功能需要实现配置热更新机制");
//            log.info("演示完成");
//
//        } catch (Exception e) {
//            log.error("配置更新演示失败", e);
//        }
//    }
//}
