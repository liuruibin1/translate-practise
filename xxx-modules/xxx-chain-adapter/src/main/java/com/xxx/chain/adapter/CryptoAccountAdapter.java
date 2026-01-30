package com.xxx.chain.adapter;

import com.xxx.chain.adapter.bo.CryptoAccountBO;
import com.xxx.chain.enumerate.CryptoAccountAlgorithmEnum;
import com.xxx.chain.ethereum.utils.EthereumUtil;
import com.xxx.chain.ton.utils.TonUtil;
import com.xxx.chain.tron.utils.TronUtil;


public class CryptoAccountAdapter {

    public static Boolean isValidPrivateKey(CryptoAccountAlgorithmEnum cryptoAccountAlgorithmEnum, String privateKeyHex) {
        if (cryptoAccountAlgorithmEnum.equals(CryptoAccountAlgorithmEnum.ETH_ECDSA)) {
            return EthereumUtil.isValidPrivateKeyWithWeb3j(privateKeyHex);
        } else if (cryptoAccountAlgorithmEnum.equals(CryptoAccountAlgorithmEnum.TRON_ECDSA)) {
            return TronUtil.isValidPrivateKeyHex(privateKeyHex);
        } else if (cryptoAccountAlgorithmEnum.equals(CryptoAccountAlgorithmEnum.TON_CRC16_CCITT)) {
        }
        return false;
    }

    public static Boolean isValidAddress(CryptoAccountAlgorithmEnum cryptoAccountAlgorithm, String address) {
        if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.ETH_ECDSA)) {
            return EthereumUtil.isValidAddress(address);
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TRON_ECDSA)) {
            return TronUtil.isValidAddress(address);
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TON_CRC16_CCITT)) {
            return TonUtil.isValidAddress(address);
        }
        return false;
    }

    public static String toChecksumAddress(CryptoAccountAlgorithmEnum cryptoAccountAlgorithm, boolean isProduction, String address, boolean isContract) {
        if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.ETH_ECDSA)) {
            return EthereumUtil.toChecksumAddress(address);
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TRON_ECDSA)) {
            return address;
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TON_CRC16_CCITT)) {
            return TonUtil.toChecksumAddress(isProduction, address, isContract);
        } else {
            return null;
        }
    }

    public static CryptoAccountBO generate(CryptoAccountAlgorithmEnum cryptoAccountAlgorithm, boolean isProduction) throws Exception {
        if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.ETH_ECDSA)) {
            return EthereumUtil.generatePrivateKeyAndAddress();
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TRON_ECDSA)) {
            return TronUtil.generatePrivateKeyAndAddress().orElse(null);
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TON_CRC16_CCITT)) {
            return TonUtil.generateMnemonicAndV4R2Address(isProduction);
        }
        return null;
    }

    public static String getAddressByPrivateKey(CryptoAccountAlgorithmEnum cryptoAccountAlgorithm, boolean isProduction, String privateKey) {
        if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.ETH_ECDSA)) {
            return EthereumUtil.getAddressByPrivateKey(privateKey);
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TRON_ECDSA)) {
            return TronUtil.getAddressByPrivateKey(privateKey).orElse(null);
        } else if (cryptoAccountAlgorithm.equals(CryptoAccountAlgorithmEnum.TON_CRC16_CCITT)) {
            return TonUtil.getV4R2AddressByMnemonic(isProduction, privateKey);
        }
        return null;
    }

}