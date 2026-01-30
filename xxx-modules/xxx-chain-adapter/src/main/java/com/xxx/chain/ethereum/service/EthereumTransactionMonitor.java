//package com.xxx.chain.ethereum.service;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.request.EthFilter;
//import org.web3j.protocol.core.methods.response.EthGetBalance;
//import org.web3j.protocol.core.methods.response.EthTransaction;
//import org.web3j.protocol.http.HttpService;
//import org.web3j.protocol.websocket.WebSocketService;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * Ethereum交易监控服务
// * 监控ETH和USDT转账到目标地址
// */
//@Slf4j
//public class EthereumTransactionMonitor {
//
//    private final EthereumConfig config;
//    private final Web3j web3j;
//    private final WebSocketService webSocketService;
//    private final ScheduledExecutorService scheduler;
//    private final String targetAddress;
//    private final String usdtContractAddress;
//
//    public EthereumTransactionMonitor(EthereumConfig config) {
//        this.config = config;
//        this.targetAddress = config.getContract().getTargetAddress();
//        this.usdtContractAddress = config.getContract().getUsdtAddress();
//
//        // 初始化Web3j HTTP客户端
//        this.web3j = Web3j.build(new HttpService(config.getNetwork().getRpcUrl()));
//
//        // 初始化WebSocket服务（用于事件监听）
//        this.webSocketService = new WebSocketService(config.getNetwork().getWsUrl(), false);
//
//        // 初始化调度器
//        this.scheduler = Executors.newScheduledThreadPool(2);
//
//        log.info("Ethereum交易监控服务初始化完成，目标地址: {}", targetAddress);
//    }
//
//    /**
//     * 启动监控服务
//     */
//    public void startMonitoring() {
//        if (!config.getMonitor().getEnabled()) {
//            log.info("交易监控已禁用");
//            return;
//        }
//
//        log.info("启动Ethereum交易监控服务...");
//
//        // 启动ETH转账监控
//        if (config.getMonitor().getMonitorEth()) {
//            startEthMonitoring();
//        }
//
//        // 启动USDT转账监控
//        if (config.getMonitor().getMonitorUsdt()) {
//            startUsdtMonitoring();
//        }
//    }
//
//    /**
//     * 启动ETH转账监控
//     */
//    private void startEthMonitoring() {
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                monitorEthTransactions();
//            } catch (Exception e) {
//                log.error("ETH转账监控异常", e);
//            }
//        }, 0, config.getMonitor().getInterval(), TimeUnit.MILLISECONDS);
//
//        log.info("ETH转账监控已启动，监控间隔: {}ms", config.getMonitor().getInterval());
//    }
//
//    /**
//     * 启动USDT转账监控
//     */
//    private void startUsdtMonitoring() {
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                monitorUsdtTransactions();
//            } catch (Exception e) {
//                log.error("USDT转账监控异常", e);
//            }
//        }, 0, config.getMonitor().getInterval(), TimeUnit.MILLISECONDS);
//
//        log.info("USDT转账监控已启动，监控间隔: {}ms", config.getMonitor().getInterval());
//    }
//
//    /**
//     * 监控ETH转账交易
//     */
//    private void monitorEthTransactions() {
//        try {
//            // 获取目标地址的ETH余额
//            EthGetBalance balanceResponse = web3j.ethGetBalance(
//                targetAddress,
//                DefaultBlockParameterName.LATEST
//            ).send();
//
//            if (balanceResponse.hasError()) {
//                log.error("获取ETH余额失败: {}", balanceResponse.getError().getMessage());
//                return;
//            }
//
//            BigInteger balance = balanceResponse.getBalance();
//            BigDecimal ethBalance = Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
//
//            log.info("目标地址 {} 当前ETH余额: {} ETH", targetAddress, ethBalance);
//
//            // 检查是否有新的ETH转入
//            // 这里可以实现更复杂的逻辑，比如记录上次检查的区块号
//            // 然后只检查新区块中的交易
//
//        } catch (Exception e) {
//            log.error("监控ETH转账异常", e);
//        }
//    }
//
//    /**
//     * 监控USDT转账交易
//     */
//    private void monitorUsdtTransactions() {
//        try {
//            // 创建USDT Transfer事件的过滤器
//            EthFilter filter = new EthFilter(
//                DefaultBlockParameterName.LATEST,
//                DefaultBlockParameterName.LATEST,
//                usdtContractAddress
//            );
//
//            // 添加Transfer事件过滤器，只监听转入目标地址的交易
//            filter.addSingleTopic("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef"); // Transfer事件签名
//            filter.addSingleTopic("0x000000000000000000000000" + targetAddress.substring(2)); // 目标地址（to参数）
//
//            // 监听USDT Transfer事件
//            web3j.ethLogFlowable(filter).subscribe(
//                logResult -> {
//                    try {
//                        processUsdtTransferEvent(logResult);
//                    } catch (Exception e) {
//                        log.error("处理USDT Transfer事件异常", e);
//                    }
//                },
//                error -> log.error("USDT事件监听异常", error)
//            );
//
//        } catch (Exception e) {
//            log.error("监控USDT转账异常", e);
//        }
//    }
//
//    /**
//     * 处理USDT Transfer事件
//     */
//    private void processUsdtTransferEvent(org.web3j.protocol.core.methods.response.Log logResult) {
//        try {
//            // 解析事件数据
//            String from = "0x" + logResult.getTopics().get(1).substring(26);
//            String to = "0x" + logResult.getTopics().get(2).substring(26);
//            String value = Numeric.toBigInt(logResult.getData()).toString();
//
//            // 转换为USDT金额（6位小数）
//            BigDecimal usdtAmount = new BigDecimal(value).divide(BigDecimal.valueOf(1000000));
//
//            // 检查是否达到最小监控金额
//            BigInteger minAmount = new BigInteger(config.getMonitor().getMinUsdtAmount());
//            if (new BigInteger(value).compareTo(minAmount) >= 0) {
//                log.info("检测到USDT转账: 从 {} 到 {}，金额: {} USDT，交易哈希: {}",
//                    from, to, usdtAmount, logResult.getTransactionHash());
//
//                // 这里可以添加业务逻辑，比如：
//                // 1. 保存到数据库
//                // 2. 发送通知
//                // 3. 触发其他业务流程
//                handleUsdtTransfer(from, to, usdtAmount, logResult.getTransactionHash());
//            }
//
//        } catch (Exception e) {
//            log.error("解析USDT Transfer事件异常", e);
//        }
//    }
//
//    /**
//     * 处理USDT转账业务逻辑
//     */
//    private void handleUsdtTransfer(String from, String to, BigDecimal amount, String txHash) {
//        // TODO: 实现具体的业务逻辑
//        log.info("处理USDT转账业务逻辑: 从 {} 到 {}，金额: {} USDT，交易哈希: {}",
//            from, to, amount, txHash);
//    }
//
//    /**
//     * 检查特定交易
//     */
//    public CompletableFuture<TransactionInfo> checkTransaction(String txHash) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                EthTransaction txResponse = web3j.ethGetTransactionByHash(txHash).send();
//                if (txResponse.hasError()) {
//                    log.error("获取交易失败: {}", txResponse.getError().getMessage());
//                    return null;
//                }
//
//                org.web3j.protocol.core.methods.response.Transaction tx = txResponse.getTransaction().orElse(null);
//                if (tx == null) {
//                    log.warn("交易不存在: {}", txHash);
//                    return null;
//                }
//
//                // 检查是否是转入目标地址的交易
//                if (targetAddress.equalsIgnoreCase(tx.getTo())) {
//                    TransactionInfo info = new TransactionInfo();
//                    info.setTxHash(txHash);
//                    info.setFrom(tx.getFrom());
//                    info.setTo(tx.getTo());
//                    info.setValue(tx.getValue());
//                    info.setBlockNumber(tx.getBlockNumber());
//                    info.setGasPrice(tx.getGasPrice());
//                    info.setNonce(tx.getNonce());
//
//                    // 判断是ETH还是USDT转账
//                    if (usdtContractAddress.equalsIgnoreCase(tx.getTo())) {
//                        info.setTokenType("USDT");
//                    } else {
//                        info.setTokenType("ETH");
//                        BigDecimal ethAmount = Convert.fromWei(tx.getValue().toString(), Convert.Unit.ETHER);
//                        info.setEthAmount(ethAmount);
//                    }
//
//                    return info;
//                }
//
//            } catch (Exception e) {
//                log.error("检查交易异常: {}", txHash, e);
//            }
//
//            return null;
//        });
//    }
//
//    /**
//     * 停止监控服务
//     */
//    public void stopMonitoring() {
//        log.info("停止Ethereum交易监控服务...");
//        scheduler.shutdown();
//        try {
//            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
//                scheduler.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            scheduler.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//
//        web3j.shutdown();
//        webSocketService.close();
//
//        log.info("Ethereum交易监控服务已停止");
//    }
//
//    /**
//     * 交易信息类
//     */
//    public static class TransactionInfo {
//        private String txHash;
//        private String from;
//        private String to;
//        private BigInteger value;
//        private BigInteger blockNumber;
//        private BigInteger gasPrice;
//        private BigInteger nonce;
//        private String tokenType;
//        private BigDecimal ethAmount;
//
//        // Getters and Setters
//        public String getTxHash() { return txHash; }
//        public void setTxHash(String txHash) { this.txHash = txHash; }
//
//        public String getFrom() { return from; }
//        public void setFrom(String from) { this.from = from; }
//
//        public String getTo() { return to; }
//        public void setTo(String to) { this.to = to; }
//
//        public BigInteger getValue() { return value; }
//        public void setValue(BigInteger value) { this.value = value; }
//
//        public BigInteger getBlockNumber() { return blockNumber; }
//        public void setBlockNumber(BigInteger blockNumber) { this.blockNumber = blockNumber; }
//
//        public BigInteger getGasPrice() { return gasPrice; }
//        public void setGasPrice(BigInteger gasPrice) { this.gasPrice = gasPrice; }
//
//        public BigInteger getNonce() { return nonce; }
//        public void setNonce(BigInteger nonce) { this.nonce = nonce; }
//
//        public String getTokenType() { return tokenType; }
//        public void setTokenType(String tokenType) { this.tokenType = tokenType; }
//
//        public BigDecimal getEthAmount() { return ethAmount; }
//        public void setEthAmount(BigDecimal ethAmount) { this.ethAmount = ethAmount; }
//
//        @Override
//        public String toString() {
//            return "TransactionInfo{" +
//                "txHash='" + txHash + '\'' +
//                ", from='" + from + '\'' +
//                ", to='" + to + '\'' +
//                ", value=" + value +
//                ", blockNumber=" + blockNumber +
//                ", tokenType='" + tokenType + '\'' +
//                ", ethAmount=" + ethAmount +
//                '}';
//        }
//    }
//}
