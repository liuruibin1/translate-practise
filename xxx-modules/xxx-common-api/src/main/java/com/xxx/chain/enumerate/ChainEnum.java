package com.xxx.chain.enumerate;

import lombok.Getter;

import static com.xxx.chain.enumerate.CryptoAccountAlgorithmEnum.*;
import static com.xxx.chain.enumerate.CryptoCurrencyProposalEnum.*;

@Getter
public enum ChainEnum {

    ETHEREUM(21,
            "Ethereum",
            "ETH",
            18,
            "https://etherscan.io",
            "1",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),
    SEPOLIA(22,
            "Sepolia",
            "ETH",
            18,
            "https://sepolia.etherscan.io",
            "11155111",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),

    POLYGON(31,
            "Polygon",
            "POL",
            18,
            "https://polygonscan.com/",
            "137",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            BEP20),
    POLYGON_AMOY(32,
            "PolygonAmoy",
            "POL",
            18,
            "https://amoy.polygonscan.com/",
            "80002",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            BEP20),

    BSC(51,
            "BSC",
            "BNB",
            18,
            "https://bscscan.com/",
            "56",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            BEP20),
    BSC_TESTNET(52,
            "BSCTestnet",
            "BNB",
            18,
            "https://testnet.bscscan.com/",
            "97",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            BEP20),

    OP(61,
            "OP",
            "ETH",
            18,
            "https://optimistic.etherscan.io/",
            "10",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),
    OP_SEPOLIA_TESTNET(62,
            "OpSepoliaTestnet",
            "ETH",
            18,
            "https://sepolia-optimism.etherscan.io/",
            "11155420",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),

    ARB(71,
            "ArbitrumOne",
            "ETH",
            18,
            "https://arbiscan.io/",
            "42161",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),
    ARB_SEPOLIA_TESTNET(72,
            "ArbSepoliaTestnet",
            "ETH",
            18,
            "https://sepolia.arbiscan.io/",
            "421614",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),

    BASE(81,
            "Base",
            "ETH",
            18,
            "https://basescan.org/",
            "8453",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),
    BASE_SEPOLIA_TESTNET(82,
            "BaseSepoliaTestnet",
            "ETH",
            18,
            "https://sepolia.basescan.org/",
            "84532",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),

    CELO(91,
            "Celo",
            "CELO",
            18,
            "https://celoscan.io/",
            "42220",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),
    CELO_ALFAJORES_TESTNET(92,
            "CeloAlfajoresTestnet",
            "CELO",
            18,
            "https://alfajores.celoscan.io/",
            "44787",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),

    AVALANCHE_C_CHAIN(101,
            "AvalancheCChain",
            "AVAX",
            18,
            "https://snowscan.xyz/",
            "43114",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),
    AVALANCHE_FUJI_TESTNET(102,
            "AvalancheFujiTestnet",
            "AVAX",
            18,
            "https://testnet.snowscan.xyz/",
            "43113",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            ERC20),

    OP_BNB(111,
            "opBNB",
            "BNB",
            18,
            "https://opbnbscan.com/",
            "204",
            true,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            BEP20),
    OP_BNB_TESTNET(112,
            "opBNBTestnet",
            "BNB",
            18,
            "https://testnet.opbnbscan.com/",
            "5611",
            false,
            ChainTypeEnum.ETHEREUM,
            ETH_ECDSA,
            BEP20),

    TRON(601,
            "Tron",
            "TRX",
            6,
            "https://tronscan.org/",
            "",
            true,
            ChainTypeEnum.TRON,
            TRON_ECDSA,
            TRC20),
    TRON_SHASTA(602,
            "TronShasta",
            "TRX",
            6,
            "https://shasta.tronscan.org/",
            "",
            false,
            ChainTypeEnum.TRON,
            TRON_ECDSA,
            TRC20),

    TON(701,
            "TON",

            "TON",
            9,
            "https://tonviewer.com/",
            "",
            true,
            ChainTypeEnum.TON,
            TON_CRC16_CCITT,
            JETTON),
    TON_TESTNET(702,
            "TONTestnet",
            "TON",
            9,
            "https://testnet.tonviewer.com/",
            "",
            false,
            ChainTypeEnum.TON,
            TON_CRC16_CCITT,
            JETTON),

    ;

    private final Integer id;
    private final String name;
    private final String nativeCryptoCurrencySymbol;
    private final Integer nativeCryptoCurrencyDecimals;
    private final String explorerUrl;
    private final String chainIdRaw;
    private final boolean isProduction;
    private final ChainTypeEnum type;
    private final CryptoAccountAlgorithmEnum cryptoAccountAlgorithm;
    private final CryptoCurrencyProposalEnum CryptoCurrencyProposal;

    ChainEnum(
            Integer id,
            String name,
            String nativeCryptoCurrencySymbol,
            Integer nativeCryptoCurrencyDecimals,
            String explorerUrl,
            String chainIdRaw,
            boolean isProduction,
            ChainTypeEnum type,
            CryptoAccountAlgorithmEnum cryptoAccountAlgorithm,
            CryptoCurrencyProposalEnum CryptoCurrencyProposal) {
        this.id = id;
        this.name = name;
        this.nativeCryptoCurrencySymbol = nativeCryptoCurrencySymbol;
        this.nativeCryptoCurrencyDecimals = nativeCryptoCurrencyDecimals;
        this.explorerUrl = explorerUrl;
        this.chainIdRaw = chainIdRaw;
        this.isProduction = isProduction;
        this.type = type;
        this.cryptoAccountAlgorithm = cryptoAccountAlgorithm;
        this.CryptoCurrencyProposal = CryptoCurrencyProposal;
    }

    public static ChainEnum getByName(String name) {
        for (ChainEnum enumerate : ChainEnum.values()) {
            if (enumerate.getName().equals(name)) {
                return enumerate;
            }
        }
        return null;
    }

    public static ChainEnum getById(Integer id) {
        for (ChainEnum enumerate : ChainEnum.values()) {
            if (enumerate.getId().equals(id)) {
                return enumerate;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        //String sql = "insert into chain (id, name, display_name, native_crypto_currency_symbol, native_crypto_currency_decimals, " +
        //        "crypto_account_algorithm, crypto_currency_proposal, type, is_production, explorer_url, " +
        //        "chain_id_raw, block_confirmations, transaction_fee, sort)\n" +
        //        "values";
        //System.out.println(sql);
        //for (ChainEnum chainEnum : ChainEnum.values()) {
        //    System.out.printf("(%d,'%s','%s'," +
        //                    "'%s',%d,%d," +
        //                    "%d,'%s',%s," +
        //                    "'%s','%s',%d,0,1),\n",
        //            chainEnum.getId(), chainEnum.getName(), chainEnum.name,
        //            chainEnum.getNativeCryptoCurrencySymbol(), chainEnum.getNativeCryptoCurrencyDecimals(), chainEnum.getCryptoAccountAlgorithm().getValue(),
        //            chainEnum.getCryptoCurrencyProposal().getValue(), chainEnum.getType(), chainEnum.isProduction(),
        //            chainEnum.getExplorerUrl(), chainEnum.getChainIdRaw(), 1
        //    );
        //}
        for (ChainEnum chainEnum : ChainEnum.values()) {
            if (chainEnum.isProduction) {
                System.out.printf("%s,", chainEnum.getName());
            }
        }
        System.out.print("\n");
        for (ChainEnum chainEnum : ChainEnum.values()) {
            if (!chainEnum.isProduction) {
                System.out.printf("%s,", chainEnum.getName());
            }
        }

        System.out.print("\n");
        for (ChainEnum chainEnum : ChainEnum.values()) {
            System.out.printf("%s = %d,", chainEnum.getName(), chainEnum.getId());
        }

        System.out.print("\n");
        for (ChainEnum chainEnum : ChainEnum.values()) {
            System.out.printf("[ChainTypeEnum.%s]: '%s',", chainEnum.getName(), chainEnum.getName());
        }
    }

}