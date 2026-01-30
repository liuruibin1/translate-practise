//package com.xxx.chain.ethereum.entity;
//
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 代币合约实体类
// */
//@Data
//@EqualsAndHashCode(callSuper = false)
//public class TokenContract {
//
//    private Long id;
//
//    /**
//     * 合约地址
//     */
//    private String contractAddress;
//
//    /**
//     * 代币符号
//     */
//    private String symbol;
//
//    /**
//     * 代币名称
//     */
//    private String name;
//
//    /**
//     * 小数位数
//     */
//    private Integer decimals;
//
//    /**
//     * 是否激活
//     */
//    private Boolean isActive = true;
//
//    /**
//     * 链ID
//     */
//    private String chainId;
//
//    /**
//     * 合约ABI
//     */
//    private String abi;
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
