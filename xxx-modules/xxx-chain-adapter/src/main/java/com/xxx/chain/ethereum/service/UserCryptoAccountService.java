//package com.xxx.chain.ethereum.service;
//
//import com.xxx.chain.ethereum.config.EthereumConfig;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
///**
// * 用户加密账户管理服务
// * 支持多用户加密账户和多ERC20代币的监控
// */
//@Slf4j
//public class UserCryptoAccountService {
//
//    // 用户加密账户缓存 - 用户ID -> 账户信息
//    private final Map<String, UserCryptoAccountInfo> userCryptoAccounts = new ConcurrentHashMap<>();
//
//    // 代币合约缓存 - 代币地址 -> 代币信息
//    private final Map<String, TokenInfo> tokenContracts = new ConcurrentHashMap<>();
//
//    // 地址到用户的映射 - 地址 -> 用户ID
//    private final Map<String, String> addressToUser = new ConcurrentHashMap<>();
//
//    // 调度器
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
//
//    // 配置
//    private final EthereumConfig config;
//
//    public UserCryptoAccountService(EthereumConfig config) {
//        this.config = config;
//        initializeDefaultTokens();
//        startAccountSync();
//    }
//
//    /**
//     * 初始化默认代币
//     */
//    private void initializeDefaultTokens() {
//        // USDT主网
//        addToken("0xdAC17F958D2ee523a2206206994597C13D831ec7",
//                "USDT", "Tether USD", 6, true);
//
//        // USDC主网
//        addToken("0xA0b86a33E6441b8B4b8C8C8C8C8C8C8C8C8C8C8",
//                "USDC", "USD Coin", 6, true);
//
//        // DAI主网
//        addToken("0x6B175474E89094C44Da98b954EedeAC495271d0F",
//                "DAI", "Dai Stablecoin", 18, true);
//
//        // WETH主网
//        addToken("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2",
//                "WETH", "Wrapped Ether", 18, true);
//
//        log.info("初始化默认代币完成，共 {} 个代币", tokenContracts.size());
//    }
//
//    /**
//     * 添加用户加密账户
//     */
//    public void addUserCryptoAccount(String userId, String address, String addressType,
//                                     boolean isActive, Map<String, BigDecimal> minAmounts) {
//        UserCryptoAccountInfo accountInfo = new UserCryptoAccountInfo();
//        accountInfo.setUserId(userId);
//        accountInfo.setAddress(address);
//        accountInfo.setAddressType(addressType);
//        accountInfo.setActive(isActive);
//        accountInfo.setMinAmounts(minAmounts != null ? minAmounts : new HashMap<>());
//        accountInfo.setCreatedTime(System.currentTimeMillis());
//        accountInfo.setLastUpdatedTime(System.currentTimeMillis());
//
//        userCryptoAccounts.put(userId, accountInfo);
//        addressToUser.put(address.toLowerCase(), userId);
//
//        log.info("添加用户加密账户: 用户ID={}, 地址={}, 类型={}", userId, address, addressType);
//    }
//
//    /**
//     * 批量添加用户加密账户
//     */
//    public void addUserCryptoAccounts(List<UserCryptoAccountInfo> accountList) {
//        for (UserCryptoAccountInfo accountInfo : accountList) {
//            userCryptoAccounts.put(accountInfo.getUserId(), accountInfo);
//            addressToUser.put(accountInfo.getAddress().toLowerCase(), accountInfo.getUserId());
//        }
//        log.info("批量添加用户加密账户完成，共 {} 个账户", accountList.size());
//    }
//
//    /**
//     * 更新用户加密账户
//     */
//    public void updateUserCryptoAccount(String userId, String address, boolean isActive,
//                                        Map<String, BigDecimal> minAmounts) {
//        UserCryptoAccountInfo accountInfo = userCryptoAccounts.get(userId);
//        if (accountInfo != null) {
//            accountInfo.setActive(isActive);
//            if (minAmounts != null) {
//                accountInfo.setMinAmounts(minAmounts);
//            }
//            accountInfo.setLastUpdatedTime(System.currentTimeMillis());
//            log.info("更新用户加密账户: 用户ID={}, 地址={}, 激活状态={}", userId, address, isActive);
//        }
//    }
//
//    /**
//     * 删除用户加密账户
//     */
//    public void removeUserCryptoAccount(String userId) {
//        UserCryptoAccountInfo accountInfo = userCryptoAccounts.remove(userId);
//        if (accountInfo != null) {
//            addressToUser.remove(accountInfo.getAddress().toLowerCase());
//            log.info("删除用户加密账户: 用户ID={}, 地址={}", userId, accountInfo.getAddress());
//        }
//    }
//
//    /**
//     * 添加代币合约
//     */
//    public void addToken(
//            String contractAddress,
//            String symbol,
//            String name,
//            int decimals,
//            boolean isActive) {
//        TokenInfo tokenInfo = new TokenInfo();
//        tokenInfo.setContractAddress(contractAddress);
//        tokenInfo.setSymbol(symbol);
//        tokenInfo.setName(name);
//        tokenInfo.setDecimals(decimals);
//        tokenInfo.setActive(isActive);
//        tokenInfo.setCreatedTime(System.currentTimeMillis());
//
//        tokenContracts.put(contractAddress.toLowerCase(), tokenInfo);
//        log.info("添加代币合约: 地址={}, 符号={}, 名称={}, 小数位={}",
//                contractAddress, symbol, name, decimals);
//    }
//
//    /**
//     * 批量添加代币合约
//     */
//    public void addTokens(List<TokenInfo> tokenList) {
//        for (TokenInfo tokenInfo : tokenList) {
//            tokenContracts.put(tokenInfo.getContractAddress().toLowerCase(), tokenInfo);
//        }
//        log.info("批量添加代币合约完成，共 {} 个代币", tokenList.size());
//    }
//
//    /**
//     * 获取所有活跃的用户加密账户
//     */
//    public List<UserCryptoAccountInfo> getActiveUserCryptoAccounts() {
//        return userCryptoAccounts.values().stream()
//                .filter(UserCryptoAccountInfo::isActive)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 获取所有活跃的代币合约
//     */
//    public List<TokenInfo> getActiveTokens() {
//        return tokenContracts.values().stream()
//                .filter(TokenInfo::isActive)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 根据地址获取用户ID
//     */
//    public String getUserIdByAddress(String address) {
//        return addressToUser.get(address.toLowerCase());
//    }
//
//    /**
//     * 根据用户ID获取账户信息
//     */
//    public UserCryptoAccountInfo getUserCryptoAccountInfo(String userId) {
//        return userCryptoAccounts.get(userId);
//    }
//
//    /**
//     * 根据合约地址获取代币信息
//     */
//    public TokenInfo getTokenInfo(String contractAddress) {
//        return tokenContracts.get(contractAddress.toLowerCase());
//    }
//
//    /**
//     * 检查地址是否被监控
//     */
//    public boolean isAddressMonitored(String address) {
//        return addressToUser.containsKey(address.toLowerCase());
//    }
//
//    /**
//     * 检查代币是否被监控
//     */
//    public boolean isTokenMonitored(String contractAddress) {
//        TokenInfo tokenInfo = tokenContracts.get(contractAddress.toLowerCase());
//        return tokenInfo != null && tokenInfo.isActive();
//    }
//
//    /**
//     * 获取用户的最小监控金额
//     */
//    public BigDecimal getMinAmount(String userId, String tokenSymbol) {
//        UserCryptoAccountInfo accountInfo = userCryptoAccounts.get(userId);
//        if (accountInfo != null && accountInfo.getMinAmounts() != null) {
//            return accountInfo.getMinAmounts().getOrDefault(tokenSymbol, BigDecimal.ZERO);
//        }
//        return BigDecimal.ZERO;
//    }
//
//    /**
//     * 获取统计信息
//     */
//    public ServiceStats getServiceStats() {
//        ServiceStats stats = new ServiceStats();
//        stats.setTotalUsers(userCryptoAccounts.size());
//        stats.setActiveUsers((int) userCryptoAccounts.values().stream().filter(UserCryptoAccountInfo::isActive).count());
//        stats.setTotalTokens(tokenContracts.size());
//        stats.setActiveTokens((int) tokenContracts.values().stream().filter(TokenInfo::isActive).count());
//        stats.setLastUpdatedTime(System.currentTimeMillis());
//        return stats;
//    }
//
//    /**
//     * 启动账户同步
//     */
//    private void startAccountSync() {
//        // 定期同步用户加密账户（从数据库或其他服务）
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                syncUserCryptoAccounts();
//            } catch (Exception e) {
//                log.error("同步用户加密账户异常", e);
//            }
//        }, 0, 5, TimeUnit.MINUTES);
//
//        // 定期同步代币合约
//        scheduler.scheduleAtFixedRate(() -> {
//            try {
//                syncTokenContracts();
//            } catch (Exception e) {
//                log.error("同步代币合约异常", e);
//            }
//        }, 0, 10, TimeUnit.MINUTES);
//
//        log.info("用户加密账户同步服务已启动");
//    }
//
//    /**
//     * 同步用户加密账户
//     */
//    private void syncUserCryptoAccounts() {
//        // TODO: 从数据库或其他服务同步用户加密账户
//        // 这里可以实现具体的同步逻辑
//        log.debug("同步用户加密账户...");
//    }
//
//    /**
//     * 同步代币合约
//     */
//    private void syncTokenContracts() {
//        // TODO: 从数据库或其他服务同步代币合约
//        // 这里可以实现具体的同步逻辑
//        log.debug("同步代币合约...");
//    }
//
//    /**
//     * 关闭服务
//     */
//    public void shutdown() {
//        log.info("关闭用户加密账户管理服务...");
//        scheduler.shutdown();
//        try {
//            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
//                scheduler.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            scheduler.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
//        log.info("用户加密账户管理服务已关闭");
//    }
//
//    /**
//     * 用户加密账户信息
//     */
//    @Data
//    public static class UserCryptoAccountInfo {
//        private String userId;
//        private String address;
//        private String addressType; // ETH, BTC, BSC等
//        private boolean active;
//        private Map<String, BigDecimal> minAmounts; // 代币符号 -> 最小监控金额
//        private long createdTime;
//        private long lastUpdatedTime;
//        private String description;
//        private Map<String, Object> metadata; // 扩展字段
//    }
//
//    /**
//     * 代币信息
//     */
//    @Data
//    public static class TokenInfo {
//        private String contractAddress;
//        private String symbol;
//        private String name;
//        private int decimals;
//        private boolean active;
//        private long createdTime;
//        private String chainId;
//        private String abi; // 合约ABI
//        private Map<String, Object> metadata; // 扩展字段
//    }
//
//    /**
//     * 服务统计信息
//     */
//    @Data
//    public static class ServiceStats {
//        private int totalUsers;
//        private int activeUsers;
//        private int totalTokens;
//        private int activeTokens;
//        private long lastUpdatedTime;
//    }
//}
