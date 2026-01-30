//package com.xxx.chain.solana.test;
//
//import com.xxx.chain.solana.config.SolanaConfig;
//import com.xxx.chain.solana.service.SolanaMultiUserTransactionMonitor;
//import com.xxx.chain.solana.utils.SolanaRpcClient;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Solana监控服务测试类
// * 注意：这些测试需要连接到Solana网络，建议在测试环境中运行
// */
//@Slf4j
//public class SolanaMonitorTest {
//
//    private SolanaConfig config;
//    private SolanaRpcClient rpcClient;
//    private SolanaMultiUserTransactionMonitor monitor;
//
//    public static void main(String[] args) {
//        SolanaMonitorTest test = new SolanaMonitorTest();
//        test.runTests();
//    }
//
//    public void runTests() {
//        try {
//            log.info("开始运行Solana监控服务测试...");
//
//            // 初始化
//            setUp();
//
//            // 运行测试
//            testSolanaConfig();
//            testRpcClientConnection();
//            testBlockRetrieval();
//            testTransactionRetrieval();
//            testMonitorService();
//            testMonitorStartStop();
//            testManualSync();
//
//            log.info("所有测试完成！");
//
//        } catch (Exception e) {
//            log.error("测试运行失败", e);
//        }
//    }
//
//    void setUp() {
//        // 创建测试配置
//        config = createTestConfig();
//        rpcClient = new SolanaRpcClient(config);
//
//        // 创建测试用户服务
//        SolanaMultiUserTransactionMonitor.UserCryptoAccountService userService = createTestUserService();
//        monitor = new SolanaMultiUserTransactionMonitor(config, userService);
//    }
//
//    void testSolanaConfig() {
//        if (config == null) throw new RuntimeException("配置为空");
//        if (config.getNetwork() == null) throw new RuntimeException("网络配置为空");
//        if (config.getToken() == null) throw new RuntimeException("代币配置为空");
//        if (config.getMonitor() == null) throw new RuntimeException("监控配置为空");
//
//        if (!"testnet".equals(config.getNetwork().getNetworkType()))
//            throw new RuntimeException("网络类型配置错误");
//        if (!"SOL".equals(config.getToken().getSol().getSymbol()))
//            throw new RuntimeException("SOL代币配置错误");
//        if (!"USDT".equals(config.getToken().getUsdt().getSymbol()))
//            throw new RuntimeException("USDT代币配置错误");
//        if (!config.getMonitor().getEnabled())
//            throw new RuntimeException("监控配置错误");
//
//        log.info("配置测试通过");
//    }
//
//    void testRpcClientConnection() {
//        try {
//            Long latestSlot = rpcClient.getLatestSlot();
//            if (latestSlot == null) throw new RuntimeException("无法获取最新区块高度");
//            if (latestSlot <= 0) throw new RuntimeException("区块高度无效");
//
//            log.info("成功连接到Solana网络，最新区块高度: {}", latestSlot);
//        } catch (Exception e) {
//            log.error("连接Solana网络失败", e);
//            throw new RuntimeException("无法连接到Solana网络: " + e.getMessage());
//        }
//    }
//
//    void testBlockRetrieval() {
//        try {
//            Long latestSlot = rpcClient.getLatestSlot();
//            if (latestSlot == null) throw new RuntimeException("无法获取最新区块高度");
//
//            // 获取最新区块
//            var block = rpcClient.getBlock(latestSlot);
//            if (block == null) throw new RuntimeException("无法获取区块");
//
//            log.info("成功获取区块: {}", latestSlot);
//            log.info("区块包含交易数量: {}",
//                block.getJSONArray("transactions") != null ?
//                block.getJSONArray("transactions").size() : 0);
//
//        } catch (Exception e) {
//            log.error("获取区块失败", e);
//            throw new RuntimeException("获取区块失败: " + e.getMessage());
//        }
//    }
//
//    void testTransactionRetrieval() {
//        try {
//            Long latestSlot = rpcClient.getLatestSlot();
//            if (latestSlot == null) throw new RuntimeException("无法获取最新区块高度");
//
//            // 获取最新区块
//            var block = rpcClient.getBlock(latestSlot);
//            if (block == null) throw new RuntimeException("无法获取区块");
//
//            var transactions = block.getJSONArray("transactions");
//            if (transactions != null && !transactions.isEmpty()) {
//                // 获取第一笔交易
//                var firstTx = transactions.getJSONObject(0);
//                if (firstTx == null) throw new RuntimeException("无法获取交易");
//
//                String signature = firstTx.getString("transaction");
//                if (signature == null) throw new RuntimeException("无法获取交易签名");
//
//                // 获取交易详情
//                var txDetail = rpcClient.getTransaction(signature);
//                if (txDetail == null) throw new RuntimeException("无法获取交易详情");
//
//                log.info("成功获取交易详情: {}", signature);
//            } else {
//                log.info("区块 {} 没有交易", latestSlot);
//            }
//
//        } catch (Exception e) {
//            log.error("获取交易失败", e);
//            throw new RuntimeException("获取交易失败: " + e.getMessage());
//        }
//    }
//
//    void testMonitorService() {
//        if (monitor == null) throw new RuntimeException("监控服务为空");
//
//        // 测试监控统计
//        var stats = monitor.getMonitoringStats();
//        if (stats == null) throw new RuntimeException("监控统计为空");
//        if ((Boolean) stats.get("isMonitoring")) throw new RuntimeException("监控状态错误");
//        if (!stats.get("queueSize").equals(0)) throw new RuntimeException("队列大小错误");
//
//        log.info("监控服务初始化成功");
//    }
//
//    void testMonitorStartStop() {
//        try {
//            // 启动监控
//            monitor.startMonitoring();
//
//            // 等待一段时间
//            Thread.sleep(5000);
//
//            // 检查状态
//            var stats = monitor.getMonitoringStats();
//            if (!(Boolean) stats.get("isMonitoring")) throw new RuntimeException("监控启动失败");
//
//            // 停止监控
//            monitor.stopMonitoring();
//
//            // 再次检查状态
//            stats = monitor.getMonitoringStats();
//            if ((Boolean) stats.get("isMonitoring")) throw new RuntimeException("监控停止失败");
//
//            log.info("监控服务启动停止测试成功");
//
//        } catch (Exception e) {
//            log.error("监控服务启动停止测试失败", e);
//            throw new RuntimeException("监控服务测试失败: " + e.getMessage());
//        }
//    }
//
//    void testManualSync() {
//        try {
//            Long latestSlot = rpcClient.getLatestSlot();
//            if (latestSlot == null) throw new RuntimeException("无法获取最新区块高度");
//
//            // 手动同步最近的几个区块
//            long startSlot = Math.max(0, latestSlot - 5);
//            long endSlot = latestSlot;
//
//            monitor.syncSlotRange(startSlot, endSlot);
//
//            log.info("手动同步测试成功: {} - {}", startSlot, endSlot);
//
//        } catch (Exception e) {
//            log.error("手动同步测试失败", e);
//            throw new RuntimeException("手动同步测试失败: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 创建测试配置
//     */
//    private SolanaConfig createTestConfig() {
//        SolanaConfig config = new SolanaConfig();
//
//        // 使用测试网
//        SolanaConfig.Network network = config.getNetwork();
//        network.setNetworkType("testnet");
//        network.setRpcUrl("https://api.testnet.solana.com");
//        network.setWsUrl("wss://api.testnet.solana.com");
//        network.setConfirmations(1); // 测试网使用较少的确认数
//        network.setConnectionTimeout(10000);
//        network.setReadTimeout(30000);
//
//        // 配置代币
//        SolanaConfig.Token.Sol sol = config.getToken().getSol();
//        sol.setMintAddress("So11111111111111111111111111111111111111112");
//        sol.setDecimals(9);
//        sol.setSymbol("SOL");
//        sol.setMinAmount("100000"); // 0.0001 SOL (测试网)
//
//        SolanaConfig.Token.Usdt usdt = config.getToken().getUsdt();
//        usdt.setMintAddress("Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB");
//        usdt.setDecimals(6);
//        usdt.setSymbol("USDT");
//        usdt.setMinAmount("100000"); // 0.1 USDT (测试网)
//
//        // 配置监控
//        SolanaConfig.Monitor monitor = config.getMonitor();
//        monitor.setEnabled(true);
//        monitor.setInterval(5000L); // 5秒间隔，测试用
//        monitor.setMonitorSol(true);
//        monitor.setMonitorUsdt(true);
//        monitor.setEnableRealTimeMonitoring(false); // 测试时关闭实时监控
//        monitor.setBatchSize(10); // 较小的批量大小
//        monitor.setMaxRetries(2);
//        monitor.setRetryInterval(1000L);
//
//        return config;
//    }
//
//    /**
//     * 创建测试用户服务
//     */
//    private SolanaMultiUserTransactionMonitor.UserCryptoAccountService createTestUserService() {
//        return new SolanaMultiUserTransactionMonitor.UserCryptoAccountService() {
//
//            @Override
//            public boolean isAddressMonitored(String address) {
//                // 测试时监控所有地址
//                log.debug("测试模式：监控地址: {}", address);
//                return true;
//            }
//
//            @Override
//            public String getUserIdByAddress(String address) {
//                // 测试时生成测试用户ID
//                String userId = "test_user_" + address.substring(0, 8);
//                log.debug("测试模式：地址 {} 映射到用户ID: {}", address, userId);
//                return userId;
//            }
//
//            @Override
//            public ServiceStats getServiceStats() {
//                ServiceStats stats = new ServiceStats();
//                stats.setActiveUsers(5);
//                stats.setTotalUsers(10);
//                stats.setActiveTokens(2);
//                stats.setTotalTokens(2);
//                return stats;
//            }
//        };
//    }
//}
