//package com.xxx.chain.ethereum.example;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import com.xxx.chain.ethereum.service.BlockchainMonitorService;
//import com.xxx.chain.ethereum.service.EthereumTransactionMonitor;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.http.HttpService;
//
//import java.math.BigDecimal;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
///**
// * Ethereum监控服务使用示例
// */
//@Slf4j
//public class EthereumMonitorExample {
//
//    public static void main(String[] args) {
//        // 创建配置
//        EthereumConfig config = createConfig();
//
//        // 初始化Web3j客户端
//        Web3j web3j = Web3j.build(new HttpService(config.getNetwork().getRpcUrl()));
//
//        // 创建交易监控服务
//        EthereumTransactionMonitor transactionMonitor = new EthereumTransactionMonitor(config);
//
//        // 创建区块监控服务
//        BlockchainMonitorService blockchainMonitor = new BlockchainMonitorService(config, web3j);
//
//        try {
//            // 启动监控服务
//            log.info("启动Ethereum监控服务...");
//            transactionMonitor.startMonitoring();
//            blockchainMonitor.startBlockMonitoring();
//
//            // 等待一段时间让服务运行
//            Thread.sleep(10000);
//
//            // 示例：检查特定交易
//            String exampleTxHash = "0x1234567890abcdef1234567890abcdef1234567890abcdef1234567890abcdef";
//            log.info("检查交易: {}", exampleTxHash);
//
//            CompletableFuture<EthereumTransactionMonitor.TransactionInfo> txFuture =
//                transactionMonitor.checkTransaction(exampleTxHash);
//
//            txFuture.thenAccept(txInfo -> {
//                if (txInfo != null) {
//                    log.info("交易信息: {}", txInfo);
//                } else {
//                    log.info("交易不存在或不是转入目标地址的交易");
//                }
//            });
//
//            // 示例：检查特定区块
//            long exampleBlockNumber = 19000000;
//            log.info("检查区块: {}", exampleBlockNumber);
//
//            CompletableFuture<BlockchainMonitorService.BlockInfo> blockFuture =
//                blockchainMonitor.checkBlock(exampleBlockNumber);
//
//            blockFuture.thenAccept(blockInfo -> {
//                if (blockInfo != null) {
//                    log.info("区块信息: {}", blockInfo);
//                } else {
//                    log.info("区块不存在");
//                }
//            });
//
//            // 示例：获取目标地址余额
//            String targetAddress = config.getContract().getTargetAddress();
//            log.info("获取目标地址余额: {}", targetAddress);
//
//            CompletableFuture<BigDecimal> ethBalanceFuture = blockchainMonitor.getEthBalance(targetAddress);
//            CompletableFuture<BigDecimal> usdtBalanceFuture = blockchainMonitor.getUsdtBalance(targetAddress);
//
//            ethBalanceFuture.thenAccept(balance ->
//                log.info("目标地址ETH余额: {} ETH", balance)
//            );
//
//            usdtBalanceFuture.thenAccept(balance ->
//                log.info("目标地址USDT余额: {} USDT", balance)
//            );
//
//            // 等待所有异步操作完成
//            CompletableFuture.allOf(txFuture, blockFuture, ethBalanceFuture, usdtBalanceFuture)
//                .thenRun(() -> log.info("所有操作完成"))
//                .get(30, TimeUnit.SECONDS);
//
//            // 保持服务运行一段时间
//            log.info("监控服务运行中，按Ctrl+C停止...");
//            Thread.sleep(60000); // 运行1分钟
//
//        } catch (Exception e) {
//            log.error("运行监控服务异常", e);
//        } finally {
//            // 停止监控服务
//            log.info("停止监控服务...");
//            try {
//                transactionMonitor.stopMonitoring();
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
//        // 网络配置
//        EthereumConfig.Network network = new EthereumConfig.Network();
//        network.setRpcUrl("https://mainnet.infura.io/v3/YOUR_PROJECT_ID");
//        network.setWsUrl("wss://mainnet.infura.io/ws/v3/YOUR_PROJECT_ID");
//        network.setChainId(1L);
//        network.setConfirmations(12);
//        config.setNetwork(network);
//
//        // 合约配置
//        EthereumConfig.Contract contract = new EthereumConfig.Contract();
//        contract.setUsdtAddress("0xdAC17F958D2ee523a2206206994597C13D831ec7");
//        contract.setTargetAddress("0x14F2D636e06893dcE422c96aC3B9370dc8bd500d");
//        config.setContract(contract);
//
//        // 监控配置
//        EthereumConfig.Monitor monitor = new EthereumConfig.Monitor();
//        monitor.setEnabled(true);
//        monitor.setInterval(15000L);
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
//     * 演示如何监控特定地址的交易
//     */
//    public static void monitorSpecificAddress() {
//        log.info("开始监控特定地址的交易...");
//
//        // 这里可以实现更具体的监控逻辑
//        // 比如监控特定时间范围内的交易
//        // 或者监控特定金额范围的交易
//    }
//
//    /**
//     * 演示如何处理USDT转账事件
//     */
//    public static void handleUsdtTransfer(String from, String to, BigDecimal amount, String txHash) {
//        log.info("处理USDT转账: 从 {} 到 {}，金额: {} USDT，交易哈希: {}",
//            from, to, amount, txHash);
//
//        // 这里可以实现具体的业务逻辑：
//
//        // 1. 验证交易
//        // - 检查交易是否已确认
//        // - 验证交易签名
//        // - 检查交易状态
//
//        // 2. 更新数据库
//        // - 记录交易历史
//        // - 更新用户余额
//        // - 记录转账记录
//
//        // 3. 发送通知
//        // - 发送邮件通知
//        // - 发送短信通知
//        // - 推送应用内通知
//
//        // 4. 触发业务流程
//        // - 自动充值
//        // - 触发奖励发放
//        // - 更新用户等级
//
//        // 5. 风控检查
//        // - 检查转账频率
//        // - 验证资金来源
//        // - 风险评估
//    }
//
//    /**
//     * 演示如何处理ETH转账事件
//     */
//    public static void handleEthTransfer(String from, String to, BigDecimal amount, String txHash) {
//        log.info("处理ETH转账: 从 {} 到 {}，金额: {} ETH，交易哈希: {}",
//            from, to, amount, txHash);
//
//        // 这里可以实现具体的业务逻辑：
//
//        // 1. 交易验证
//        // - 检查区块确认数
//        // - 验证交易有效性
//        // - 检查gas费用合理性
//
//        // 2. 业务处理
//        // - 更新用户ETH余额
//        // - 记录转账历史
//        // - 触发相关业务流程
//
//        // 3. 安全措施
//        // - 异常交易检测
//        // - 大额转账监控
//        // - 地址黑名单检查
//    }
//}
