//package com.xxx.chain.solana.entity;
//
//import lombok.Data;
//import java.math.BigDecimal;
//import java.util.Map;
//
///**
// * Solana交易事件类
// */
//@Data
//public class SolanaTransactionEvent {
//
//    /**
//     * 交易签名（交易哈希）
//     */
//    private String signature;
//
//    /**
//     * 发送方地址
//     */
//    private String from;
//
//    /**
//     * 接收方地址
//     */
//    private String to;
//
//    /**
//     * 用户ID
//     */
//    private String userId;
//
//    /**
//     * 代币符号
//     */
//    private String tokenSymbol;
//
//    /**
//     * 代币Mint地址
//     */
//    private String mintAddress;
//
//    /**
//     * 转账金额
//     */
//    private BigDecimal amount;
//
//    /**
//     * 代币精度
//     */
//    private Integer decimals;
//
//    /**
//     * 区块高度
//     */
//    private Long slot;
//
//    /**
//     * 区块时间戳
//     */
//    private Long blockTime;
//
//    /**
//     * 事件类型
//     */
//    private EventType eventType;
//
//    /**
//     * 交易状态
//     */
//    private TransactionStatus status;
//
//    /**
//     * 手续费（Lamports）
//     */
//    private Long fee;
//
//    /**
//     * 交易时间戳
//     */
//    private long timestamp;
//
//    /**
//     * 扩展字段
//     */
//    private Map<String, Object> metadata;
//
//    /**
//     * 事件类型枚举
//     */
//    public enum EventType {
//        SOL_TRANSFER,      // SOL转账
//        TOKEN_TRANSFER,    // SPL代币转账
//        TOKEN_MINT,        // 代币铸造
//        TOKEN_BURN         // 代币销毁
//    }
//
//    /**
//     * 交易状态枚举
//     */
//    public enum TransactionStatus {
//        PENDING,           // 待确认
//        CONFIRMED,         // 已确认
//        FAILED,            // 失败
//        EXPIRED            // 过期
//    }
//
//    /**
//     * 获取格式化后的金额
//     */
//    public BigDecimal getFormattedAmount() {
//        if (amount == null || decimals == null) {
//            return BigDecimal.ZERO;
//        }
//        return amount.divide(BigDecimal.valueOf(Math.pow(10, decimals)));
//    }
//
//    /**
//     * 获取交易状态描述
//     */
//    public String getStatusDescription() {
//        if (status == null) {
//            return "未知";
//        }
//        switch (status) {
//            case PENDING:
//                return "待确认";
//            case CONFIRMED:
//                return "已确认";
//            case FAILED:
//                return "失败";
//            case EXPIRED:
//                return "过期";
//            default:
//                return "未知";
//        }
//    }
//
//    /**
//     * 获取事件类型描述
//     */
//    public String getEventTypeDescription() {
//        if (eventType == null) {
//            return "未知";
//        }
//        switch (eventType) {
//            case SOL_TRANSFER:
//                return "SOL转账";
//            case TOKEN_TRANSFER:
//                return "代币转账";
//            case TOKEN_MINT:
//                return "代币铸造";
//            case TOKEN_BURN:
//                return "代币销毁";
//            default:
//                return "未知";
//        }
//    }
//}
