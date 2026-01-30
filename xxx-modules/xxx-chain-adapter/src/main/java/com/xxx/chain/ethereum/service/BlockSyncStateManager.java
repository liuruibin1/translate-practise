//package com.xxx.chain.ethereum.service;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.response.EthBlock;
//
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * 区块同步状态管理器
// * 用于记录和管理区块同步进度，确保重启后不丢失交易
// */
//@Slf4j
//public class BlockSyncStateManager {
//
//    private final Web3j web3j;
//    private final String chainId;
//
//    // 当前同步状态
//    private final AtomicLong lastSyncedBlock;
//    private final AtomicLong lastProcessedBlock;
//
//    // 各代币的同步状态
//    private final ConcurrentHashMap<String, TokenSyncState> tokenSyncStates;
//
//    // 同步锁，防止并发同步
//    private final ReentrantLock syncLock;
//
//    // 配置
//    private final int maxBlockRange; // 单次同步的最大区块范围
//    private final int confirmations; // 区块确认数
//
//    public BlockSyncStateManager(Web3j web3j, String chainId, int maxBlockRange, int confirmations) {
//        this.web3j = web3j;
//        this.chainId = chainId;
//        this.maxBlockRange = maxBlockRange;
//        this.confirmations = confirmations;
//
//        this.lastSyncedBlock = new AtomicLong(0);
//        this.lastProcessedBlock = new AtomicLong(0);
//        this.tokenSyncStates = new ConcurrentHashMap<>();
//        this.syncLock = new ReentrantLock();
//
//        log.info("区块同步状态管理器初始化完成，链ID: {}, 最大区块范围: {}, 确认数: {}",
//            chainId, maxBlockRange, confirmations);
//    }
//
//    /**
//     * 初始化同步状态
//     * 从数据库或配置文件加载上次同步的区块号
//     */
//    public void initializeSyncState() {
//        try {
//            // TODO: 从数据库加载上次同步状态
//            // 这里应该从数据库读取上次同步的区块号
//            long savedLastSyncedBlock = loadLastSyncedBlockFromDatabase();
//
//            if (savedLastSyncedBlock > 0) {
//                lastSyncedBlock.set(savedLastSyncedBlock);
//                lastProcessedBlock.set(savedLastSyncedBlock);
//                log.info("从数据库加载同步状态，上次同步区块: {}", savedLastSyncedBlock);
//            } else {
//                // 如果没有保存的状态，从当前区块开始
//                initializeFromCurrentBlock();
//            }
//
//        } catch (Exception e) {
//            log.error("初始化同步状态失败，从当前区块开始", e);
//            initializeFromCurrentBlock();
//        }
//    }
//
//    /**
//     * 从当前区块初始化
//     */
//    private void initializeFromCurrentBlock() {
//        try {
//            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(
//                DefaultBlockParameterName.LATEST, false
//            ).send().getBlock();
//
//            if (latestBlock != null) {
//                long currentBlock = latestBlock.getNumber().longValue();
//                // 从确认数之前的区块开始，确保不会丢失交易
//                long startBlock = Math.max(0, currentBlock - confirmations);
//
//                lastSyncedBlock.set(startBlock);
//                lastProcessedBlock.set(startBlock);
//
//                log.info("从当前区块初始化同步状态，当前区块: {}, 开始同步区块: {}",
//                    currentBlock, startBlock);
//            }
//        } catch (Exception e) {
//            log.error("获取当前区块失败", e);
//            // 设置一个安全的起始区块
//            lastSyncedBlock.set(0);
//            lastProcessedBlock.set(0);
//        }
//    }
//
//    /**
//     * 获取需要同步的区块范围
//     */
//    public BlockRange getNextBlockRange() {
//        try {
//            // 获取当前最新区块
//            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(
//                DefaultBlockParameterName.LATEST, false
//            ).send().getBlock();
//
//            if (latestBlock == null) {
//                return null;
//            }
//
//            long currentBlock = latestBlock.getNumber().longValue();
//            long lastSynced = lastSyncedBlock.get();
//
//            // 计算需要同步的区块范围
//            long fromBlock = lastSynced + 1;
//            long toBlock = Math.min(fromBlock + maxBlockRange - 1, currentBlock - confirmations);
//
//            // 如果还有区块需要同步
//            if (fromBlock <= toBlock) {
//                return new BlockRange(fromBlock, toBlock);
//            }
//
//            return null;
//
//        } catch (Exception e) {
//            log.error("获取区块范围失败", e);
//            return null;
//        }
//    }
//
//    /**
//     * 更新同步状态
//     */
//    public void updateSyncState(long blockNumber) {
//        lastSyncedBlock.set(blockNumber);
//        lastProcessedBlock.set(blockNumber);
//
//        // TODO: 保存到数据库
//        saveSyncStateToDatabase(blockNumber);
//
//        log.debug("更新同步状态，区块: {}", blockNumber);
//    }
//
//    /**
//     * 获取代币同步状态
//     */
//    public TokenSyncState getTokenSyncState(String contractAddress) {
//        return tokenSyncStates.computeIfAbsent(contractAddress,
//            k -> new TokenSyncState(contractAddress));
//    }
//
//    /**
//     * 更新代币同步状态
//     */
//    public void updateTokenSyncState(String contractAddress, long lastLogBlock) {
//        TokenSyncState state = getTokenSyncState(contractAddress);
//        state.setLastLogBlock(lastLogBlock);
//        state.setLastUpdateTime(System.currentTimeMillis());
//
//        // TODO: 保存到数据库
//        saveTokenSyncStateToDatabase(contractAddress, lastLogBlock);
//    }
//
//    /**
//     * 检查是否需要历史区块同步
//     */
//    public boolean needsHistoricalSync() {
//        return lastSyncedBlock.get() < getCurrentBlockNumber() - confirmations;
//    }
//
//    /**
//     * 获取当前区块号
//     */
//    public long getCurrentBlockNumber() {
//        try {
//            EthBlock.Block latestBlock = web3j.ethGetBlockByNumber(
//                DefaultBlockParameterName.LATEST, false
//            ).send().getBlock();
//
//            return latestBlock != null ? latestBlock.getNumber().longValue() : 0;
//        } catch (Exception e) {
//            log.error("获取当前区块号失败", e);
//            return 0;
//        }
//    }
//
//    /**
//     * 获取同步进度
//     */
//    public SyncProgress getSyncProgress() {
//        long currentBlock = getCurrentBlockNumber();
//        long lastSynced = lastSyncedBlock.get();
//        long pendingBlocks = Math.max(0, currentBlock - confirmations - lastSynced);
//
//        SyncProgress progress = new SyncProgress();
//        progress.setCurrentBlock(currentBlock);
//        progress.setLastSyncedBlock(lastSynced);
//        progress.setPendingBlocks(pendingBlocks);
//        progress.setConfirmations(confirmations);
//        progress.setProgressPercentage(calculateProgressPercentage(currentBlock, lastSynced));
//
//        return progress;
//    }
//
//    /**
//     * 计算同步进度百分比
//     */
//    private double calculateProgressPercentage(long currentBlock, long lastSynced) {
//        if (currentBlock <= 0) return 0.0;
//
//        long totalBlocks = currentBlock - confirmations;
//        if (totalBlocks <= 0) return 100.0;
//
//        double percentage = (double) lastSynced / totalBlocks * 100;
//        return Math.min(100.0, Math.max(0.0, percentage));
//    }
//
//    /**
//     * 强制同步到指定区块
//     */
//    public void forceSyncToBlock(long blockNumber) {
//        log.warn("强制同步到区块: {}", blockNumber);
//        lastSyncedBlock.set(blockNumber);
//        lastProcessedBlock.set(blockNumber);
//
//        // TODO: 保存到数据库
//        saveSyncStateToDatabase(blockNumber);
//    }
//
//    /**
//     * 重置同步状态
//     */
//    public void resetSyncState() {
//        log.warn("重置同步状态");
//        lastSyncedBlock.set(0);
//        lastProcessedBlock.set(0);
//        tokenSyncStates.clear();
//
//        // TODO: 清除数据库中的同步状态
//        clearSyncStateFromDatabase();
//    }
//
//    /**
//     * 获取同步锁
//     */
//    public ReentrantLock getSyncLock() {
//        return syncLock;
//    }
//
//    // ==================== 数据库操作 ====================
//
//    /**
//     * 从数据库加载上次同步的区块号
//     */
//    private long loadLastSyncedBlockFromDatabase() {
//        // TODO: 实现从数据库加载同步状态
//        // 这里应该查询数据库中的同步状态表
//        return 0;
//    }
//
//    /**
//     * 保存同步状态到数据库
//     */
//    private void saveSyncStateToDatabase(long blockNumber) {
//        // TODO: 实现保存同步状态到数据库
//        // 这里应该更新数据库中的同步状态表
//    }
//
//    /**
//     * 保存代币同步状态到数据库
//     */
//    private void saveTokenSyncStateToDatabase(String contractAddress, long lastLogBlock) {
//        // TODO: 实现保存代币同步状态到数据库
//        // 这里应该更新数据库中的代币同步状态表
//    }
//
//    /**
//     * 清除数据库中的同步状态
//     */
//    private void clearSyncStateFromDatabase() {
//        // TODO: 实现清除数据库中的同步状态
//        // 这里应该清除数据库中的同步状态表
//    }
//
//    // ==================== 内部类 ====================
//
//    /**
//     * 区块范围
//     */
//    @Data
//    public static class BlockRange {
//        private final long fromBlock;
//        private final long toBlock;
//
//        public BlockRange(long fromBlock, long toBlock) {
//            this.fromBlock = fromBlock;
//            this.toBlock = toBlock;
//        }
//
//        public long getBlockCount() {
//            return toBlock - fromBlock + 1;
//        }
//
//        @Override
//        public String toString() {
//            return String.format("BlockRange[%d, %d] (%d blocks)", fromBlock, toBlock, getBlockCount());
//        }
//    }
//
//    /**
//     * 代币同步状态
//     */
//    @Data
//    public static class TokenSyncState {
//        private String contractAddress;
//        private long lastLogBlock;
//        private long lastUpdateTime;
//        private boolean isActive;
//
//        public TokenSyncState(String contractAddress) {
//            this.contractAddress = contractAddress;
//            this.lastLogBlock = 0;
//            this.lastUpdateTime = System.currentTimeMillis();
//            this.isActive = true;
//        }
//    }
//
//    /**
//     * 同步进度
//     */
//    @Data
//    public static class SyncProgress {
//        private long currentBlock;
//        private long lastSyncedBlock;
//        private long pendingBlocks;
//        private int confirmations;
//        private double progressPercentage;
//        private long lastUpdateTime = System.currentTimeMillis();
//    }
//}
