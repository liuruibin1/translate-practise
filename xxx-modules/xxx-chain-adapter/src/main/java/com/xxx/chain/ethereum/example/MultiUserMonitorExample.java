//package com.xxx.chain.ethereum.example;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import com.xxx.chain.ethereum.service.MultiUserTransactionMonitor;
//import com.xxx.chain.ethereum.service.UserCryptoAccountService;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.http.HttpService;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 多用户监控示例
// * 展示如何监控多个用户地址和多种ERC20代币
// */
//@Slf4j
//public class MultiUserMonitorExample {
//
//    public static void main(String[] args) {
//        // 创建配置
//        EthereumConfig config = createConfig();
//
//        // 初始化Web3j客户端
//        Web3j web3j = Web3j.build(new HttpService(config.getNetwork().getRpcUrl()));
//
//        try {
//            // 创建用户地址管理服务
//            UserCryptoAccountService userCryptoAccountService = new UserCryptoAccountService(config);
//
//            // 创建多用户交易监控服务
//            MultiUserTransactionMonitor monitor = new MultiUserTransactionMonitor(config, web3j, userCryptoAccountService);
//
//            // 添加示例用户地址
//            addSampleUserAddresses(userCryptoAccountService);
//
//            // 添加示例代币合约
//            addSampleTokenContracts(userCryptoAccountService);
//
//            // 启动监控服务
//            log.info("启动多用户监控服务...");
//            monitor.startMonitoring();
//
//            // 等待服务启动
//            Thread.sleep(10000);
//
//            // 显示服务统计信息
//            displayServiceStats(userCryptoAccountService, monitor);
//
//            // 模拟运行一段时间
//            log.info("监控服务运行中，按Ctrl+C停止...");
//            Thread.sleep(300000); // 运行5分钟
//
//        } catch (Exception e) {
//            log.error("运行多用户监控服务异常", e);
//        } finally {
//            // 停止服务
//            log.info("停止监控服务...");
//            try {
//                // 这里应该调用monitor.stopMonitoring()，但monitor变量在try块中
//                web3j.shutdown();
//            } catch (Exception e) {
//                log.error("停止服务异常", e);
//            }
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
//        network.setConfirmations(6);
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
//        monitor.setInterval(30000L); // 30秒
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
//     * 添加示例用户地址
//     */
//    private static void addSampleUserAddresses(UserCryptoAccountService userCryptoAccountService) {
//        log.info("添加示例用户地址...");
//
//        // 用户1: 0x14F2D636e06893dcE422c96aC3B9370dc8bd500d
//        Map<String, BigDecimal> user1MinAmounts = new HashMap<>();
//        user1MinAmounts.put("ETH", new BigDecimal("0.001"));
//        user1MinAmounts.put("USDT", new BigDecimal("1.0"));
//        user1MinAmounts.put("USDC", new BigDecimal("1.0"));
//        user1MinAmounts.put("DAI", new BigDecimal("1.0"));
//
//        userCryptoAccountService.addUserCryptoAccount(
//            "user_001",
//            "0x14F2D636e06893dcE422c96aC3B9370dc8bd500d",
//            "ETH",
//            true,
//            user1MinAmounts
//        );
//
//        // 用户2: 0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6
//        Map<String, BigDecimal> user2MinAmounts = new HashMap<>();
//        user2MinAmounts.put("ETH", new BigDecimal("0.01"));
//        user2MinAmounts.put("USDT", new BigDecimal("10.0"));
//        user2MinAmounts.put("WETH", new BigDecimal("0.01"));
//
//        userCryptoAccountService.addUserCryptoAccount(
//            "user_002",
//            "0x742d35Cc6634C0532925a3b8D4C9db96C4b4d8b6",
//            "ETH",
//            true,
//            user2MinAmounts
//        );
//
//        // 用户3: 0x8ba1f109551bD432803012645Hac136c22C177e9
//        Map<String, BigDecimal> user3MinAmounts = new HashMap<>();
//        user3MinAmounts.put("ETH", new BigDecimal("0.005"));
//        user3MinAmounts.put("USDT", new BigDecimal("5.0"));
//        user3MinAmounts.put("USDC", new BigDecimal("5.0"));
//
//        userCryptoAccountService.addUserCryptoAccount(
//            "user_003",
//            "0x8ba1f109551bD432803012645Hac136c22C177e9",
//            "ETH",
//            true,
//            user3MinAmounts
//        );
//
//        // 批量添加更多用户地址（模拟生产环境）
//        addBatchUserAddresses(userCryptoAccountService);
//
//        log.info("示例用户地址添加完成");
//    }
//
//    /**
//     * 批量添加用户地址（模拟生产环境）
//     */
//    private static void addBatchUserAddresses(UserCryptoAccountService userCryptoAccountService) {
//        log.info("批量添加用户地址（模拟生产环境）...");
//
//        // 模拟添加1000个用户地址
//        for (int i = 4; i <= 1000; i++) {
//            String userId = String.format("user_%03d", i);
//            String address = generateMockAddress(i);
//
//            Map<String, BigDecimal> minAmounts = new HashMap<>();
//            minAmounts.put("ETH", new BigDecimal("0.001"));
//            minAmounts.put("USDT", new BigDecimal("1.0"));
//
//            userCryptoAccountService.addUserCryptoAccount(
//                userId,
//                address,
//                "ETH",
//                true,
//                minAmounts
//            );
//
//            // 每100个地址输出一次进度
//            if (i % 100 == 0) {
//                log.info("已添加 {} 个用户地址", i);
//            }
//        }
//
//        log.info("批量添加用户地址完成，共 {} 个用户", 1000);
//    }
//
//    /**
//     * 生成模拟地址
//     */
//    private static String generateMockAddress(int index) {
//        // 生成一个模拟的Ethereum地址
//        String baseAddress = "0x" + String.format("%040x", index);
//        return baseAddress.substring(0, 42); // 确保地址长度为42
//    }
//
//    /**
//     * 添加示例代币合约
//     */
//    private static void addSampleTokenContracts(UserCryptoAccountService userCryptoAccountService) {
//        log.info("添加示例代币合约...");
//
//        // USDT (Goerli测试网)
//        userCryptoAccountService.addToken(
//            "0x110a13FC3efE6A245B50102cf2CD8021EBeBDee4",
//            "USDT",
//            "Tether USD",
//            6,
//            true
//        );
//
//        // USDC (Goerli测试网)
//        userCryptoAccountService.addToken(
//            "0x07865c6E87B9F70255377e024ace6630C1Eaa37F",
//            "USDC",
//            "USD Coin",
//            6,
//            true
//        );
//
//        // DAI (Goerli测试网)
//        userCryptoAccountService.addToken(
//            "0x11fE4B6AE13d2a6055C8D9cF65c62bac00431455",
//            "DAI",
//            "Dai Stablecoin",
//            18,
//            true
//        );
//
//        // WETH (Goerli测试网)
//        userCryptoAccountService.addToken(
//            "0xB4FBF271143F4FBf7B91A5ded31805e42b2208d6",
//            "WETH",
//            "Wrapped Ether",
//            18,
//            true
//        );
//
//        // 添加更多常见代币
//        addMoreTokenContracts(userCryptoAccountService);
//
//        log.info("示例代币合约添加完成");
//    }
//
//    /**
//     * 添加更多代币合约
//     */
//    private static void addMoreTokenContracts(UserCryptoAccountService userCryptoAccountService) {
//        // 这里可以添加更多代币合约
//        // 在实际生产环境中，这些信息通常从数据库或配置文件中读取
//
//        log.info("添加更多代币合约...");
//
//        // 可以继续添加更多代币...
//    }
//
//    /**
//     * 显示服务统计信息
//     */
//    private static void displayServiceStats(UserCryptoAccountService userCryptoAccountService,
//                                         MultiUserTransactionMonitor monitor) {
//        try {
//            // 等待一段时间让服务稳定
//            Thread.sleep(5000);
//
//            // 获取用户地址服务统计
//            UserCryptoAccountService.ServiceStats userStats = userCryptoAccountService.getServiceStats();
//            log.info("=== 用户地址服务统计 ===");
//            log.info("总用户数: {}", userStats.getTotalUsers());
//            log.info("活跃用户数: {}", userStats.getActiveUsers());
//            log.info("总代币数: {}", userStats.getTotalTokens());
//            log.info("活跃代币数: {}", userStats.getActiveTokens());
//            log.info("最后更新时间: {}", userStats.getLastUpdatedTime());
//
//            // 获取监控服务统计
//            Map<String, Object> monitorStats = monitor.getMonitoringStats();
//            log.info("=== 监控服务统计 ===");
//            log.info("活跃订阅数: {}", monitorStats.get("activeSubscriptions"));
//            log.info("队列大小: {}", monitorStats.get("queueSize"));
//            log.info("最后处理区块: {}", monitorStats.get("lastProcessedBlock"));
//
//            // 显示交易统计
//            @SuppressWarnings("unchecked")
//            Map<String, Long> transactionCounts = (Map<String, Long>) monitorStats.get("transactionCounts");
//            if (transactionCounts != null && !transactionCounts.isEmpty()) {
//                log.info("=== 交易统计 ===");
//                for (Map.Entry<String, Long> entry : transactionCounts.entrySet()) {
//                    String key = entry.getKey();
//                    Long count = entry.getValue();
//                    String[] parts = key.split("_");
//                    if (parts.length == 2) {
//                        String userId = parts[0];
//                        String tokenSymbol = parts[1];
//                        log.info("用户 {} {}: {} 笔交易", userId, tokenSymbol, count);
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("获取服务统计信息异常", e);
//        }
//    }
//
//    /**
//     * 演示动态添加用户地址
//     */
//    public static void demonstrateDynamicUserManagement(UserCryptoAccountService userCryptoAccountService) {
//        log.info("演示动态用户管理...");
//
//        // 添加新用户
//        Map<String, BigDecimal> newUserMinAmounts = new HashMap<>();
//        newUserMinAmounts.put("ETH", new BigDecimal("0.002"));
//        newUserMinAmounts.put("USDT", new BigDecimal("2.0"));
//
//        userCryptoAccountService.addUserCryptoAccount(
//            "new_user_001",
//            "0x1234567890123456789012345678901234567890",
//            "ETH",
//            true,
//            newUserMinAmounts
//        );
//
//        // 更新现有用户
//        userCryptoAccountService.updateUserCryptoAccount(
//            "user_001",
//            "0x14F2D636e06893dcE422c96aC3B9370dc8bd500d",
//            false, // 暂时禁用
//            null
//        );
//
//        // 重新启用用户
//        userCryptoAccountService.updateUserCryptoAccount(
//            "user_001",
//            "0x14F2D636e06893dcE422c96aC3B9370dc8bd500d",
//            true, // 重新启用
//            null
//        );
//
//        log.info("动态用户管理演示完成");
//    }
//
//    /**
//     * 演示动态添加代币合约
//     */
//    public static void demonstrateDynamicTokenManagement(UserCryptoAccountService userCryptoAccountService) {
//        log.info("演示动态代币管理...");
//
//        // 添加新代币
//        userCryptoAccountService.addToken(
//            "0x9876543210987654321098765432109876543210",
//            "NEW",
//            "New Token",
//            18,
//            true
//        );
//
//        // 批量添加代币
//        // 这里可以添加更多代币...
//
//        log.info("动态代币管理演示完成");
//    }
//}
