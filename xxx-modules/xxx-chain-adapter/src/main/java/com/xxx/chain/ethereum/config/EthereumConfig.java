//package com.xxx.chain.ethereum.config;
//
//import lombok.Data;
//
///**
// * Ethereum配置类
// */
//@Data
//public class EthereumConfig {
//
//    /**
//     * Ethereum网络配置
//     */
//    private Network network = new Network();
//
//    /**
//     * 合约配置
//     */
//    private Contract contract = new Contract();
//
//    /**
//     * 监控配置
//     */
//    private Monitor monitor = new Monitor();
//
//    @Data
//    public static class Network {
//        /**
//         * Ethereum节点RPC URL
//         */
//        private String rpcUrl = "https://mainnet.infura.io/v3/YOUR_PROJECT_ID";
//
//        /**
//         * WebSocket URL（用于事件监听）
//         */
//        private String wsUrl = "wss://mainnet.infura.io/ws/v3/YOUR_PROJECT_ID";
//
//        /**
//         * 网络ID（1=主网，3=Ropsten测试网，4=Rinkeby测试网，5=Goerli测试网）
//         */
//        private Long chainId = 1L;
//
//        /**
//         * 区块确认数
//         */
//        private Integer confirmations = 12;
//    }
//
//    @Data
//    public static class Contract {
//        /**
//         * USDT合约地址（主网）
//         */
//        private String usdtAddress = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
//
//        /**
//         * 目标监控地址
//         */
//        private String targetAddress = "0x14F2D636e06893dcE422c96aC3B9370dc8bd500d";
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
//         * 是否监控ETH转账
//         */
//        private Boolean monitorEth = true;
//
//        /**
//         * 是否监控USDT转账
//         */
//        private Boolean monitorUsdt = true;
//
//        /**
//         * 最小监控金额（Wei）
//         */
//        private String minEthAmount = "1000000000000000"; // 0.001 ETH
//
//        /**
//         * 最小监控USDT金额（以最小单位计）
//         */
//        private String minUsdtAmount = "1000000"; // 1 USDT (6位小数)
//    }
//}
