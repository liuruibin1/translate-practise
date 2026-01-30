//package com.xxx.chain.solana.example;
//
//import com.xxx.chain.solana.config.SolanaConfig;
//import com.xxx.chain.solana.service.SolanaMultiUserTransactionMonitor;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * Solana监控服务使用示例
// */
//@Slf4j
//public class SolanaMonitorExample {
//
//    public static void main(String[] args) {
//        // 创建配置
//        SolanaConfig config = createSolanaConfig();
//
//        // 创建用户账户服务（需要实现具体的业务逻辑）
//        SolanaMultiUserTransactionMonitor.UserCryptoAccountService userService = createUserService();
//
//        // 创建监控服务
//        SolanaMultiUserTransactionMonitor monitor = new SolanaMultiUserTransactionMonitor(config, userService);
//
//        try {
//            // 启动监控
//            monitor.startMonitoring();
//
//            // 等待一段时间
//            Thread.sleep(30000);
//
//            // 获取监控统计
//            log.info("监控统计: {}", monitor.getMonitoringStats());
//
//            // 手动同步指定区块范围（可选）
//            // monitor.syncSlotRange(1000000, 1000100);
//
//            // 停止监控
//            monitor.stopMonitoring();
//
//        } catch (Exception e) {
//            log.error("监控服务运行异常", e);
//        }
//    }
//
//    /**
//     * 创建Solana配置
//     */
//    private static SolanaConfig createSolanaConfig() {
//        SolanaConfig config = new SolanaConfig();
//
//        // 配置网络
//        SolanaConfig.Network network = config.getNetwork();
//        network.setNetworkType("mainnet-beta");
//        network.setRpcUrl("https://api.mainnet-beta.solana.com");
//        network.setWsUrl("wss://api.mainnet-beta.solana.com");
//        network.setConfirmations(32);
//        network.setConnectionTimeout(30000);
//        network.setReadTimeout(60000);
//
//        // 配置代币
//        SolanaConfig.Token.Sol sol = config.getToken().getSol();
//        sol.setMintAddress("So11111111111111111111111111111111111111112");
//        sol.setDecimals(9);
//        sol.setSymbol("SOL");
//        sol.setMinAmount("1000000"); // 0.001 SOL
//
//        SolanaConfig.Token.Usdt usdt = config.getToken().getUsdt();
//        usdt.setMintAddress("Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB");
//        usdt.setDecimals(6);
//        usdt.setSymbol("USDT");
//        usdt.setMinAmount("1000000"); // 1 USDT
//
//        // 配置监控
//        SolanaConfig.Monitor monitor = config.getMonitor();
//        monitor.setEnabled(true);
//        monitor.setInterval(15000L);
//        monitor.setMonitorSol(true);
//        monitor.setMonitorUsdt(true);
//        monitor.setEnableRealTimeMonitoring(true);
//        monitor.setBatchSize(100);
//        monitor.setMaxRetries(3);
//        monitor.setRetryInterval(5000L);
//
//        return config;
//    }
//
//    /**
//     * 创建用户服务（示例实现）
//     */
//    private static SolanaMultiUserTransactionMonitor.UserCryptoAccountService createUserService() {
//        return new SolanaMultiUserTransactionMonitor.UserCryptoAccountService() {
//
//            @Override
//            public boolean isAddressMonitored(String address) {
//                // 这里实现具体的地址监控逻辑
//                // 例如：检查地址是否在数据库中，是否处于活跃状态等
//                log.debug("检查地址是否被监控: {}", address);
//                return true; // 示例：所有地址都被监控
//            }
//
//            @Override
//            public String getUserIdByAddress(String address) {
//                // 这里实现根据地址获取用户ID的逻辑
//                // 例如：查询数据库，返回对应的用户ID
//                log.debug("根据地址获取用户ID: {}", address);
//                return "user_" + address.substring(0, 8); // 示例：生成用户ID
//            }
//
//            @Override
//            public ServiceStats getServiceStats() {
//                // 这里实现获取服务统计信息的逻辑
//                ServiceStats stats = new ServiceStats();
//                stats.setActiveUsers(100);
//                stats.setTotalUsers(150);
//                stats.setActiveTokens(2); // SOL和USDT
//                stats.setTotalTokens(2);
//                return stats;
//            }
//        };
//    }
//}
