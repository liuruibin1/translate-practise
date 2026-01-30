//package com.xxx.chain.ethereum.service;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.response.EthBlock;
//import org.web3j.protocol.core.methods.response.Transaction;
//import org.web3j.utils.Convert;
//import org.web3j.utils.Numeric;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * 区块链监控服务
// * 实时监控新区块中的交易
// */
//@Slf4j
//public class BlockchainMonitorService {
//
//    private final EthereumConfig config;
//    private final Web3j web3j;
//    private final String targetAddress;
//    private final String usdtContractAddress;
//    private final AtomicLong lastProcessedBlock;
//
//    public BlockchainMonitorService(EthereumConfig config, Web3j web3j) {
//        this.config = config;
//        this.web3j = web3j;
//        this.targetAddress = config.getContract().getTargetAddress();
//        this.usdtContractAddress = config.getContract().getUsdtAddress();
//        this.lastProcessedBlock = new AtomicLong(0);
//
//        log.info("区块链监控服务初始化完成，目标地址: {}", targetAddress);
//    }
//
//    /**
//     * 启动区块监控
//     */
//    public void startBlockMonitoring() {
//        log.info("启动区块监控服务...");
//
//        // 获取当前最新区块号
//        try {
//            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(
//                DefaultBlockParameterName.LATEST, false
//            ).send().getBlock();
//
//            if (latestBlock != null) {
//                lastProcessedBlock.set(latestBlock.getNumber().longValue());
//                log.info("当前最新区块号: {}", latestBlock.getNumber());
//            }
//        } catch (Exception e) {
//            log.error("获取最新区块失败", e);
//        }
//
//        // 开始监控新区块
//        monitorNewBlocks();
//    }
//
//    /**
//     * 监控新区块
//     */
//    private void monitorNewBlocks() {
//        web3j.blockFlowable(false).subscribe(
//            block -> {
//                try {
//                    processNewBlock(block.getBlock());
//                } catch (Exception e) {
//                    log.error("处理新区块异常", e);
//                }
//            },
//            error -> log.error("区块监控异常", error)
//        );
//    }
//
//    /**
//     * 处理新区块
//     */
//    private void processNewBlock(EthBlock.Block block) {
//        long blockNumber = block.getNumber().longValue();
//        long lastProcessed = lastProcessedBlock.get();
//
//        // 检查是否是新区块
//        if (blockNumber > lastProcessed) {
//            log.info("检测到新区块: {}，包含 {} 笔交易", blockNumber, block.getTransactions().size());
//
//            // 处理区块中的交易
//            processBlockTransactions(block);
//
//            // 更新最后处理的区块号
//            lastProcessedBlock.set(blockNumber);
//        }
//    }
//
//    /**
//     * 处理区块中的交易
//     */
//    private void processBlockTransactions(EthBlock.Block block) {
//        List<EthBlock.TransactionResult> transactions = block.getTransactions();
//
//        for (EthBlock.TransactionResult txResult : transactions) {
//            try {
//                if (txResult instanceof EthBlock.TransactionObject) {
//                    EthBlock.TransactionObject txObj = (EthBlock.TransactionObject) txResult;
//                    Transaction tx = txObj.get();
//
//                    // 检查是否是转入目标地址的交易
//                    if (targetAddress.equalsIgnoreCase(tx.getTo())) {
//                        processIncomingTransaction(tx, block);
//                    }
//                }
//            } catch (Exception e) {
//                log.error("处理交易异常", e);
//            }
//        }
//    }
//
//    /**
//     * 处理转入交易
//     */
//    private void processIncomingTransaction(Transaction tx, EthBlock.Block block) {
//        try {
//            String txHash = tx.getHash();
//            String from = tx.getFrom();
//            String to = tx.getTo();
//            BigInteger value = tx.getValue();
//            BigInteger blockNumber = block.getNumber();
//            BigInteger gasPrice = tx.getGasPrice();
//            BigInteger nonce = tx.getNonce();
//
//            // 判断交易类型
//            if (usdtContractAddress.equalsIgnoreCase(to)) {
//                // USDT合约调用，需要进一步解析
//                log.info("检测到USDT合约调用: 交易哈希: {}，从: {}，到: {}", txHash, from, to);
//                // USDT转账的具体金额需要通过事件日志获取
//            } else {
//                // ETH转账
//                BigDecimal ethAmount = Convert.fromWei(value.toString(), Convert.Unit.ETHER);
//
//                // 检查是否达到最小监控金额
//                BigInteger minAmount = new BigInteger(config.getMonitor().getMinEthAmount());
//                if (value.compareTo(minAmount) >= 0) {
//                    log.info("检测到ETH转入: 交易哈希: {}，从: {}，到: {}，金额: {} ETH，区块: {}",
//                        txHash, from, to, ethAmount, blockNumber);
//
//                    // 处理ETH转入业务逻辑
//                    handleEthTransfer(from, to, ethAmount, txHash, blockNumber, gasPrice, nonce);
//                }
//            }
//
//        } catch (Exception e) {
//            log.error("处理转入交易异常", e);
//        }
//    }
//
//    /**
//     * 处理ETH转入业务逻辑
//     */
//    private void handleEthTransfer(String from, String to, BigDecimal amount, String txHash,
//                                 BigInteger blockNumber, BigInteger gasPrice, BigInteger nonce) {
//        // TODO: 实现具体的业务逻辑
//        log.info("处理ETH转入业务逻辑: 从 {} 到 {}，金额: {} ETH，交易哈希: {}，区块: {}",
//            from, to, amount, txHash, blockNumber);
//
//        // 这里可以添加：
//        // 1. 保存到数据库
//        // 2. 发送通知
//        // 3. 触发其他业务流程
//        // 4. 更新用户余额
//        // 5. 记录交易历史
//    }
//
//    /**
//     * 检查特定区块
//     */
//    public CompletableFuture<BlockInfo> checkBlock(long blockNumber) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                EthBlock.Block block = web3j.ethGetBlockByNumber(
//                    org.web3j.protocol.core.DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)),
//                    true
//                ).send().getBlock();
//
//                if (block == null) {
//                    log.warn("区块不存在: {}", blockNumber);
//                    return null;
//                }
//
//                BlockInfo info = new BlockInfo();
//                info.setBlockNumber(block.getNumber().longValue());
//                info.setBlockHash(block.getHash());
//                info.setParentHash(block.getParentHash());
//                info.setTimestamp(block.getTimestamp().longValue());
//                info.setTransactionCount(block.getTransactions().size());
//                info.setGasLimit(block.getGasLimit());
//                info.setGasUsed(block.getGasUsed());
//                info.setMiner(block.getMiner());
//
//                return info;
//
//            } catch (Exception e) {
//                log.error("检查区块异常: {}", blockNumber, e);
//                return null;
//            }
//        });
//    }
//
//    /**
//     * 获取目标地址的ETH余额
//     */
//    public CompletableFuture<BigDecimal> getEthBalance(String address) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                var response = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
//                if (response.hasError()) {
//                    log.error("获取ETH余额失败: {}", response.getError().getMessage());
//                    return BigDecimal.ZERO;
//                }
//
//                BigInteger balance = response.getBalance();
//                return Convert.fromWei(balance.toString(), Convert.Unit.ETHER);
//
//            } catch (Exception e) {
//                log.error("获取ETH余额异常", e);
//                return BigDecimal.ZERO;
//            }
//        });
//    }
//
//    /**
//     * 获取目标地址的USDT余额
//     */
//    public CompletableFuture<BigDecimal> getUsdtBalance(String address) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                // 构建USDT balanceOf方法的调用数据
//                String methodId = "0x70a08231"; // balanceOf(address)的方法ID
//                String paddedAddress = "000000000000000000000000" + address.substring(2);
//                String data = methodId + paddedAddress;
//
//                var response = web3j.ethCall(
//                    org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
//                        null, usdtContractAddress, data
//                    ),
//                    DefaultBlockParameterName.LATEST
//                ).send();
//
//                if (response.hasError()) {
//                    log.error("获取USDT余额失败: {}", response.getError().getMessage());
//                    return BigDecimal.ZERO;
//                }
//
//                String result = response.getValue();
//                if (result.equals("0x")) {
//                    return BigDecimal.ZERO;
//                }
//
//                BigInteger balance = Numeric.toBigInt(result);
//                // USDT有6位小数
//                return new BigDecimal(balance).divide(BigDecimal.valueOf(1000000));
//
//            } catch (Exception e) {
//                log.error("获取USDT余额异常", e);
//                return BigDecimal.ZERO;
//            }
//        });
//    }
//
//    /**
//     * 区块信息类
//     */
//    public static class BlockInfo {
//        private long blockNumber;
//        private String blockHash;
//        private String parentHash;
//        private long timestamp;
//        private int transactionCount;
//        private BigInteger gasLimit;
//        private BigInteger gasUsed;
//        private String miner;
//
//        // Getters and Setters
//        public long getBlockNumber() { return blockNumber; }
//        public void setBlockNumber(long blockNumber) { this.blockNumber = blockNumber; }
//
//        public String getBlockHash() { return blockHash; }
//        public void setBlockHash(String blockHash) { this.blockHash = blockHash; }
//
//        public String getParentHash() { return parentHash; }
//        public void setParentHash(String parentHash) { this.parentHash = parentHash; }
//
//        public long getTimestamp() { return timestamp; }
//        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
//
//        public int getTransactionCount() { return transactionCount; }
//        public void setTransactionCount(int transactionCount) { this.transactionCount = transactionCount; }
//
//        public BigInteger getGasLimit() { return gasLimit; }
//        public void setGasLimit(BigInteger gasLimit) { this.gasLimit = gasLimit; }
//
//        public BigInteger getGasUsed() { return gasUsed; }
//        public void setGasUsed(BigInteger gasUsed) { this.gasUsed = gasUsed; }
//
//        public String getMiner() { return miner; }
//        public void setMiner(String miner) { this.miner = miner; }
//
//        @Override
//        public String toString() {
//            return "BlockInfo{" +
//                "blockNumber=" + blockNumber +
//                ", blockHash='" + blockHash + '\'' +
//                ", timestamp=" + timestamp +
//                ", transactionCount=" + transactionCount +
//                ", gasUsed=" + gasUsed +
//                '}';
//        }
//    }
//}
