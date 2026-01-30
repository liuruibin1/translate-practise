//package com.xxx.chain.ethereum.entity;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 用户地址实体类
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//public class UserAddress {
//
//    private Long id;
//
//    /**
//     * 用户ID
//     */
//    private String userId;
//
//    /**
//     * 区块链地址
//     */
//    private String address;
//
//    /**
//     * 地址类型（ETH, BTC, BSC等）
//     */
//    private String addressType;
//
//    /**
//     * 是否激活
//     */
//    private Boolean isActive = true;
//
//    /**
//     * 最小监控金额配置（JSON格式）
//     * 例如: {"ETH": "0.001", "USDT": "1.0", "USDC": "1.0"}
//     */
//    private String minAmountsJson;
//
//    /**
//     * 描述
//     */
//    private String description;
//
//    /**
//     * 创建时间
//     */
//    private LocalDateTime createdTime;
//
//    /**
//     * 更新时间
//     */
//    private LocalDateTime updatedTime;
//
//    /**
//     * 扩展字段（JSON格式）
//     */
//    private String metadataJson;
//
//    /**
//     * 获取最小监控金额
//     */
//    public Map<String, BigDecimal> getMinAmounts() {
//        // TODO: 实现JSON解析逻辑
//        return new HashMap<>();
//    }
//
//    /**
//     * 设置最小监控金额
//     */
//    public void setMinAmounts(Map<String, BigDecimal> minAmounts) {
//        // TODO: 实现JSON序列化逻辑
//    }
//
//    /**
//     * 获取扩展字段
//     */
//    public Map<String, Object> getMetadata() {
//        // TODO: 实现JSON解析逻辑
//        return new HashMap<>();
//    }
//
//    /**
//     * 设置扩展字段
//     */
//    public void setMetadata(Map<String, Object> metadata) {
//        // TODO: 实现JSON序列化逻辑
//    }
//}
