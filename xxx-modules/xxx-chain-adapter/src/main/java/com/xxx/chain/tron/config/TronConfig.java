//package com.xxx.chain.tron.config;
//
//import lombok.Data;
//
///**
// * Tron波场链配置类
// */
//@Data
//public class TronConfig {
//
//    /**
//     * Tron网络配置
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
//         * Tron主网API URL
//         */
//        private String mainnetApiUrl = "https://api.trongrid.io";
//
//        /**
//         * Tron测试网API URL
//         */
//        private String testnetApiUrl = "https://api.shasta.trongrid.io";
//
//        /**
//         * Tron主网API Key（用于提高请求限制）
//         */
//        private String apiKey = "YOUR_TRON_API_KEY";
//
//        /**
//         * 网络类型（mainnet/testnet）
//         */
//        private String networkType = "mainnet";
//
//        /**
//         * 区块确认数
//         */
//        private Integer confirmations = 20;
//
//        /**
//         * 请求超时时间（毫秒）
//         */
//        private Integer timeout = 30000;
//    }
//
//    @Data
//    public static class Contract {
//        /**
//         * USDT合约地址（主网）
//         * TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t
//         */
//        private String usdtAddress = "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t";
//
//        /**
//         * USDT合约地址（测试网）
//         */
//        private String testnetUsdtAddress = "TG3XXyExYk2YiRz6ztzL5qsymR7AgGN3D6";
//
//        /**
//         * 目标监控地址
//         */
//        private String targetAddress = "0x14F2D636e06893dcE422c96aC3B9370dc8bd500d";
//
//        /**
//         * 目标监控地址（Tron格式）
//         */
//        private String tronTargetAddress = "TQn9Y2khDD95J42FQtQTdwVVRZQJqJqJqJ";
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
//         * 是否监控TRX转账
//         */
//        private Boolean monitorTrx = true;
//
//        /**
//         * 是否监控USDT转账
//         */
//        private Boolean monitorUsdt = true;
//
//        /**
//         * 最小监控TRX金额（Sun，1 TRX = 1,000,000 Sun）
//         */
//        private String minTrxAmount = "1000000"; // 1 TRX
//
//        /**
//         * 最小监控USDT金额（以最小单位计，6位小数）
//         */
//        private String minUsdtAmount = "1000000"; // 1 USDT
//
//        /**
//         * 区块同步范围
//         */
//        private Integer blockRange = 100;
//
//        /**
//         * 最大并发请求数
//         */
//        private Integer maxConcurrentRequests = 10;
//    }
//
//    /**
//     * 获取当前网络的API URL
//     */
//    public String getCurrentApiUrl() {
//        return "mainnet".equals(network.getNetworkType())
//            ? network.getMainnetApiUrl()
//            : network.getTestnetApiUrl();
//    }
//
//    /**
//     * 获取当前网络的USDT合约地址
//     */
//    public String getCurrentUsdtAddress() {
//        return "mainnet".equals(network.getNetworkType())
//            ? contract.getUsdtAddress()
//            : contract.getTestnetUsdtAddress();
//    }
//
//    /**
//     * 获取当前网络的目标地址
//     */
//    public String getCurrentTargetAddress() {
//        return "mainnet".equals(network.getNetworkType())
//            ? contract.getTronTargetAddress()
//            : contract.getTargetAddress();
//    }
//}
