//package com.xxx.chain.solana.config;
//
//import lombok.Data;
//
///**
// * Solana配置类
// */
//@Data
//public class SolanaConfig {
//
//    /**
//     * Solana网络配置
//     */
//    private Network network = new Network();
//
//    /**
//     * 代币配置
//     */
//    private Token token = new Token();
//
//    /**
//     * 监控配置
//     */
//    private Monitor monitor = new Monitor();
//
//    @Data
//    public static class Network {
//        /**
//         * Solana主网RPC URL
//         */
//        private String rpcUrl = "https://api.mainnet-beta.solana.com";
//
//        /**
//         * Solana测试网RPC URL
//         */
//        private String testnetRpcUrl = "https://api.testnet.solana.com";
//
//        /**
//         * Solana开发网RPC URL
//         */
//        private String devnetRpcUrl = "https://api.devnet.solana.com";
//
//        /**
//         * WebSocket URL（用于实时事件监听）
//         */
//        private String wsUrl = "wss://api.mainnet-beta.solana.com";
//
//        /**
//         * 网络类型（mainnet-beta, testnet, devnet）
//         */
//        private String networkType = "mainnet-beta";
//
//        /**
//         * 区块确认数
//         */
//        private Integer confirmations = 32;
//
//        /**
//         * 连接超时时间（毫秒）
//         */
//        private Integer connectionTimeout = 30000;
//
//        /**
//         * 读取超时时间（毫秒）
//         */
//        private Integer readTimeout = 60000;
//    }
//
//    @Data
//    public static class Token {
//        /**
//         * SOL代币配置
//         */
//        private Sol sol = new Sol();
//
//        /**
//         * USDT代币配置（Solana上的USDT）
//         */
//        private Usdt usdt = new Usdt();
//
//        @Data
//        public static class Sol {
//            /**
//             * SOL代币Mint地址（原生SOL）
//             */
//            private String mintAddress = "So11111111111111111111111111111111111111112";
//
//            /**
//             * SOL代币精度
//             */
//            private Integer decimals = 9;
//
//            /**
//             * SOL代币符号
//             */
//            private String symbol = "SOL";
//
//            /**
//             * 最小监控金额（Lamports）
//             */
//            private String minAmount = "1000000"; // 0.001 SOL
//        }
//
//        @Data
//        public static class Usdt {
//            /**
//             * USDT代币Mint地址（Solana上的USDT）
//             */
//            private String mintAddress = "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB";
//
//            /**
//             * USDT代币精度
//             */
//            private Integer decimals = 6;
//
//            /**
//             * USDT代币符号
//             */
//            private String symbol = "USDT";
//
//            /**
//             * 最小监控金额（以最小单位计）
//             */
//            private String minAmount = "1000000"; // 1 USDT
//        }
//    }
//
//    @Data
//    public static class Monitor {
//        /**
//         * 是否启用交易监控
//         */
//        private Boolean enabled = true;
//
//        /**
//         * 监控间隔（毫秒）
//         */
//        private Long interval = 15000L;
//
//        /**
//         * 是否监控SOL转账
//         */
//        private Boolean monitorSol = true;
//
//        /**
//         * 是否监控USDT转账
//         */
//        private Boolean monitorUsdt = true;
//
//        /**
//         * 是否启用实时事件监听
//         */
//        private Boolean enableRealTimeMonitoring = true;
//
//        /**
//         * 批量查询大小
//         */
//        private Integer batchSize = 100;
//
//        /**
//         * 最大重试次数
//         */
//        private Integer maxRetries = 3;
//
//        /**
//         * 重试间隔（毫秒）
//         */
//        private Long retryInterval = 5000L;
//    }
//}
