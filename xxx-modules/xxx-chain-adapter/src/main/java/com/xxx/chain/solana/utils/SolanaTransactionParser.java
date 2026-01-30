//package com.xxx.chain.solana.utils;
//
//import com.alibaba.fastjson2.JSONArray;
//import com.alibaba.fastjson2.JSONObject;
//import com.xxx.chain.solana.config.SolanaConfig;
//import com.xxx.chain.solana.entity.SolanaTransactionEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Solana交易解析器
// */
//@Slf4j
//public class SolanaTransactionParser {
//
//    private final SolanaConfig config;
//
//    public SolanaTransactionParser(SolanaConfig config) {
//        this.config = config;
//    }
//
//    /**
//     * 解析交易信息
//     */
//    public List<SolanaTransactionEvent> parseTransaction(JSONObject transaction, Long slot, Long blockTime) {
//        List<SolanaTransactionEvent> events = new ArrayList<>();
//
//        try {
//            if (transaction == null) {
//                return events;
//            }
//
//            // 检查交易是否成功
//            JSONObject meta = transaction.getJSONObject("meta");
//            if (meta != null && meta.getInteger("err") != null) {
//                log.debug("交易失败，跳过解析: {}", transaction.getString("transaction"));
//                return events;
//            }
//
//            // 获取交易签名
//            String signature = transaction.getString("transaction");
//            if (StringUtils.isBlank(signature)) {
//                return events;
//            }
//
//            // 解析交易详情
//            JSONObject transactionDetails = transaction.getJSONObject("transaction");
//            if (transactionDetails == null) {
//                return events;
//            }
//
//            // 获取交易指令
//            JSONArray instructions = transactionDetails.getJSONArray("instructions");
//            if (instructions == null || instructions.isEmpty()) {
//                return events;
//            }
//
//            // 获取账户列表
//            JSONArray accountKeys = transactionDetails.getJSONArray("accountKeys");
//            if (accountKeys == null || accountKeys.isEmpty()) {
//                return events;
//            }
//
//            // 获取手续费
//            Long fee = meta != null ? meta.getLong("fee") : 0L;
//
//            // 解析每个指令
//            for (int i = 0; i < instructions.size(); i++) {
//                JSONObject instruction = instructions.getJSONObject(i);
//                if (instruction == null) {
//                    continue;
//                }
//
//                // 解析指令
//                List<SolanaTransactionEvent> instructionEvents = parseInstruction(
//                    instruction, accountKeys, signature, slot, blockTime, fee
//                );
//                events.addAll(instructionEvents);
//            }
//
//        } catch (Exception e) {
//            log.error("解析交易失败: {}", transaction, e);
//        }
//
//        return events;
//    }
//
//    /**
//     * 解析交易指令
//     */
//    private List<SolanaTransactionEvent> parseInstruction(JSONObject instruction, JSONArray accountKeys,
//                                                         String signature, Long slot, Long blockTime, Long fee) {
//        List<SolanaTransactionEvent> events = new ArrayList<>();
//
//        try {
//            // 获取程序ID索引
//            Integer programIdIndex = instruction.getInteger("programIdIndex");
//            if (programIdIndex == null || programIdIndex >= accountKeys.size()) {
//                return events;
//            }
//
//            // 获取程序ID
//            String programId = accountKeys.getString(programIdIndex);
//            if (StringUtils.isBlank(programId)) {
//                return events;
//            }
//
//            // 获取账户索引
//            JSONArray accounts = instruction.getJSONArray("accounts");
//            if (accounts == null || accounts.isEmpty()) {
//                return events;
//            }
//
//            // 获取数据
//            String data = instruction.getString("data");
//
//            // 根据程序ID解析不同类型的指令
//            if (isSystemProgram(programId)) {
//                // 系统程序 - SOL转账
//                events.addAll(parseSystemTransfer(instruction, accountKeys, signature, slot, blockTime, fee));
//            } else if (isTokenProgram(programId)) {
//                // SPL代币程序 - 代币转账
//                events.addAll(parseTokenTransfer(instruction, accountKeys, signature, slot, blockTime, fee));
//            }
//
//        } catch (Exception e) {
//            log.error("解析指令失败: {}", instruction, e);
//        }
//
//        return events;
//    }
//
//    /**
//     * 解析系统程序转账（SOL）
//     */
//    private List<SolanaTransactionEvent> parseSystemTransfer(JSONObject instruction, JSONArray accountKeys,
//                                                            String signature, Long slot, Long blockTime, Long fee) {
//        List<SolanaTransactionEvent> events = new ArrayList<>();
//
//        try {
//            // 系统转账指令ID: 2
//            String data = instruction.getString("data");
//            if (StringUtils.isBlank(data) || !data.startsWith("2")) {
//                return events;
//            }
//
//            JSONArray accounts = instruction.getJSONArray("accounts");
//            if (accounts == null || accounts.size() < 2) {
//                return events;
//            }
//
//            // 获取发送方和接收方账户索引
//            Integer fromIndex = accounts.getInteger(0);
//            Integer toIndex = accounts.getInteger(1);
//
//            if (fromIndex == null || toIndex == null ||
//                fromIndex >= accountKeys.size() || toIndex >= accountKeys.size()) {
//                return events;
//            }
//
//            String from = accountKeys.getString(fromIndex);
//            String to = accountKeys.getString(toIndex);
//
//            if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
//                return events;
//            }
//
//            // 解析转账金额（Lamports）
//            BigInteger amount = parseSystemTransferAmount(data);
//            if (amount == null || amount.compareTo(BigInteger.ZERO) <= 0) {
//                return events;
//            }
//
//            // 转换为SOL
//            BigDecimal solAmount = new BigDecimal(amount).divide(BigDecimal.valueOf(Math.pow(10, 9)));
//
//            // 检查是否达到最小监控金额
//            BigDecimal minAmount = new BigDecimal(config.getToken().getSol().getMinAmount())
//                .divide(BigDecimal.valueOf(Math.pow(10, 9)));
//
//            if (solAmount.compareTo(minAmount) >= 0) {
//                SolanaTransactionEvent event = new SolanaTransactionEvent();
//                event.setSignature(signature);
//                event.setFrom(from);
//                event.setTo(to);
//                event.setTokenSymbol(config.getToken().getSol().getSymbol());
//                event.setMintAddress(config.getToken().getSol().getMintAddress());
//                event.setAmount(solAmount);
//                event.setDecimals(config.getToken().getSol().getDecimals());
//                event.setSlot(slot);
//                event.setBlockTime(blockTime);
//                event.setEventType(SolanaTransactionEvent.EventType.SOL_TRANSFER);
//                event.setStatus(SolanaTransactionEvent.TransactionStatus.CONFIRMED);
//                event.setFee(fee);
//                event.setTimestamp(System.currentTimeMillis());
//
//                events.add(event);
//
//                log.debug("解析到SOL转账: 从={}, 到={}, 金额={} SOL, 签名={}",
//                    from, to, solAmount, signature);
//            }
//
//        } catch (Exception e) {
//            log.error("解析系统转账失败: {}", instruction, e);
//        }
//
//        return events;
//    }
//
//    /**
//     * 解析代币转账（USDT等SPL代币）
//     */
//    private List<SolanaTransactionEvent> parseTokenTransfer(JSONObject instruction, JSONArray accountKeys,
//                                                           String signature, Long slot, Long blockTime, Long fee) {
//        List<SolanaTransactionEvent> events = new ArrayList<>();
//
//        try {
//            // 代币转账指令ID: 3
//            String data = instruction.getString("data");
//            if (StringUtils.isBlank(data) || !data.startsWith("3")) {
//                return events;
//            }
//
//            JSONArray accounts = instruction.getJSONArray("accounts");
//            if (accounts == null || accounts.size() < 3) {
//                return events;
//            }
//
//            // 获取账户索引
//            Integer sourceIndex = accounts.getInteger(0);  // 源代币账户
//            Integer destIndex = accounts.getInteger(1);    // 目标代币账户
//            Integer ownerIndex = accounts.getInteger(2);   // 所有者账户
//
//            if (sourceIndex == null || destIndex == null || ownerIndex == null ||
//                sourceIndex >= accountKeys.size() || destIndex >= accountKeys.size() || ownerIndex >= accountKeys.size()) {
//                return events;
//            }
//
//            String source = accountKeys.getString(sourceIndex);
//            String dest = accountKeys.getString(destIndex);
//            String owner = accountKeys.getString(ownerIndex);
//
//            if (StringUtils.isBlank(source) || StringUtils.isBlank(dest) || StringUtils.isBlank(owner)) {
//                return events;
//            }
//
//            // 解析转账金额
//            BigInteger amount = parseTokenTransferAmount(data);
//            if (amount == null || amount.compareTo(BigInteger.ZERO) <= 0) {
//                return events;
//            }
//
//            // 需要确定代币类型和精度
//            // 这里简化处理，实际应该查询代币账户信息
//            String mintAddress = determineMintAddress(source, dest);
//            if (StringUtils.isBlank(mintAddress)) {
//                return events;
//            }
//
//            // 根据Mint地址确定代币信息
//            TokenInfo tokenInfo = getTokenInfo(mintAddress);
//            if (tokenInfo == null) {
//                return events;
//            }
//
//            // 转换为代币金额
//            BigDecimal tokenAmount = new BigDecimal(amount).divide(BigDecimal.valueOf(Math.pow(10, tokenInfo.decimals)));
//
//            // 检查是否达到最小监控金额
//            BigDecimal minAmount = new BigDecimal(tokenInfo.minAmount)
//                .divide(BigDecimal.valueOf(Math.pow(10, tokenInfo.decimals)));
//
//            if (tokenAmount.compareTo(minAmount) >= 0) {
//                SolanaTransactionEvent event = new SolanaTransactionEvent();
//                event.setSignature(signature);
//                event.setFrom(owner);  // 使用所有者账户作为发送方
//                event.setTo(dest);     // 使用目标代币账户作为接收方
//                event.setTokenSymbol(tokenInfo.symbol);
//                event.setMintAddress(mintAddress);
//                event.setAmount(tokenAmount);
//                event.setDecimals(tokenInfo.decimals);
//                event.setSlot(slot);
//                event.setBlockTime(blockTime);
//                event.setEventType(SolanaTransactionEvent.EventType.TOKEN_TRANSFER);
//                event.setStatus(SolanaTransactionEvent.TransactionStatus.CONFIRMED);
//                event.setFee(fee);
//                event.setTimestamp(System.currentTimeMillis());
//
//                events.add(event);
//
//                log.debug("解析到代币转账: 从={}, 到={}, 代币={}, 金额={}, 签名={}",
//                    owner, dest, tokenInfo.symbol, tokenAmount, signature);
//            }
//
//        } catch (Exception e) {
//            log.error("解析代币转账失败: {}", instruction, e);
//        }
//
//        return events;
//    }
//
//    /**
//     * 解析系统转账金额
//     */
//    private BigInteger parseSystemTransferAmount(String data) {
//        try {
//            if (StringUtils.isBlank(data) || data.length() < 9) {
//                return null;
//            }
//
//            // 跳过指令ID，读取8字节的金额
//            String amountHex = data.substring(2, 18);
//            return new BigInteger(amountHex, 16);
//
//        } catch (Exception e) {
//            log.error("解析系统转账金额失败: {}", data, e);
//            return null;
//        }
//    }
//
//    /**
//     * 解析代币转账金额
//     */
//    private BigInteger parseTokenTransferAmount(String data) {
//        try {
//            if (StringUtils.isBlank(data) || data.length() < 9) {
//                return null;
//            }
//
//            // 跳过指令ID，读取8字节的金额
//            String amountHex = data.substring(2, 18);
//            return new BigInteger(amountHex, 16);
//
//        } catch (Exception e) {
//            log.error("解析代币转账金额失败: {}", data, e);
//            return null;
//        }
//    }
//
//    /**
//     * 确定代币Mint地址
//     * 实际实现中应该查询代币账户信息
//     */
//    private String determineMintAddress(String source, String dest) {
//        // 这里简化处理，实际应该查询代币账户的Mint地址
//        // 可以根据配置的代币地址进行匹配
//        if (StringUtils.isNotBlank(source) && StringUtils.isNotBlank(dest)) {
//            // 检查是否是配置的代币
//            if (config.getToken().getUsdt().getMintAddress() != null) {
//                return config.getToken().getUsdt().getMintAddress();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 获取代币信息
//     */
//    private TokenInfo getTokenInfo(String mintAddress) {
//        if (StringUtils.isBlank(mintAddress)) {
//            return null;
//        }
//
//        // 检查是否是USDT
//        if (mintAddress.equals(config.getToken().getUsdt().getMintAddress())) {
//            return new TokenInfo(
//                config.getToken().getUsdt().getSymbol(),
//                config.getToken().getUsdt().getDecimals(),
//                config.getToken().getUsdt().getMinAmount()
//            );
//        }
//
//        return null;
//    }
//
//    /**
//     * 检查是否是系统程序
//     */
//    private boolean isSystemProgram(String programId) {
//        return "11111111111111111111111111111111".equals(programId);
//    }
//
//    /**
//     * 检查是否是SPL代币程序
//     */
//    private boolean isTokenProgram(String programId) {
//        return "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".equals(programId);
//    }
//
//    /**
//     * 代币信息类
//     */
//    private static class TokenInfo {
//        private final String symbol;
//        private final Integer decimals;
//        private final String minAmount;
//
//        public TokenInfo(String symbol, Integer decimals, String minAmount) {
//            this.symbol = symbol;
//            this.decimals = decimals;
//            this.minAmount = minAmount;
//        }
//    }
//}
