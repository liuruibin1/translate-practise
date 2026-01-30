package com.xxx.chain.ton.utils;

import com.iwebpp.crypto.TweetNaclFast;
import com.xxx.chain.adapter.bo.ChainBlockBO;
import com.xxx.chain.adapter.bo.CryptoAccountBO;
import com.xxx.chain.adapter.bo.RpcConfigBO;
import com.xxx.chain.adapter.bo.TransferTransactionBO;
import com.xxx.chain.enumerate.ChainSiteEnum;
import com.xxx.chain.enumerate.ChainTransactionStatusEnum;
import com.xxx.common.core.utils.StringUtils;
import org.ton.ton4j.address.Address;
import org.ton.ton4j.cell.Cell;
import org.ton.ton4j.cell.CellBuilder;
import org.ton.ton4j.mnemonic.Ed25519;
import org.ton.ton4j.mnemonic.Mnemonic;
import org.ton.ton4j.mnemonic.Pair;
import org.ton.ton4j.smartcontract.wallet.v4.WalletV4R2;
import org.ton.ton4j.utils.Utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class TonUtil {

    public static CryptoAccountBO generateMnemonicAndV4R2Address(boolean isProduction) throws NoSuchAlgorithmException, InvalidKeyException {
        List<String> mnemonic = Mnemonic.generate(24);
        String mnemonicJoin = String.join(",", mnemonic);
        CryptoAccountBO cryptoAccountBO = new CryptoAccountBO();
        cryptoAccountBO.setPrivateKey(mnemonicJoin);
        Address address = getV4R2AddressByMnemonic(mnemonic);
        String addressStr = toChecksumAddress(isProduction, address.toString(true), false);
        cryptoAccountBO.setAddress(addressStr);
        return cryptoAccountBO;
    }

    public static String getV4R2AddressByMnemonic(boolean isProduction, String mnemonicString) {
        String[] split = mnemonicString.split(",");
        List<String> mnemonic = List.of(split);
        try {
            Address address = getV4R2AddressByMnemonic(mnemonic);
            return toChecksumAddress(isProduction, address.toString(true), false);
        } catch (Exception ig) {
            return null;
        }
    }

    public static Boolean isValidAddress(String address) {
        return Address.isValid(address);
    }

    public static String toChecksumAddress(boolean isProduction, String address, boolean isContract) {
        if (isValidAddress(address)) {
            return toChecksumAddress(isProduction, Address.of(address), isContract);
        } else {
            return null;
        }
    }

    public static String toChecksumAddress(boolean isProduction, Address address, boolean isContract) {
        return address.toString(true, true, isContract, !isProduction);
    }

    public static String getAccountId(String address) {
        String addressHex = Address.of(address).toHex();
        if (!addressHex.contains("0:")) {
            return "0:" + addressHex;
        }
        return addressHex;
    }

    public static Address getV4R2AddressByMnemonic(List<String> mnemonicList) throws NoSuchAlgorithmException, InvalidKeyException {
        /*Pair key = Mnemonic.toKeyPair(mnemonic);
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(key.getSecretKey());
        WalletV4R2 contract = WalletV4R2.builder()
                .keyPair(keyPair)
                .build();
        Message msg = MsgUtils.createExternalMessageWithSignedBody(
                contract.getKeyPair(),
                contract.getAddress(),
                contract.getStateInit(),
                null);
        return msg.getInit().getAddress();*/

        /*
        ton4j 3.2.0
        Pair key = Mnemonic.toKeyPair(mnemonic);
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(key.getSecretKey());
        Options options = Options.builder().publicKey(keyPair.getPublicKey()).wc(0L).build();
        Wallet wallet = new Wallet(WalletVersion.V4R2, options);
        WalletV4ContractR2 contract = wallet.create();
        InitExternalMessage initExternalMessage = contract.createInitExternalMessage(keyPair.getSecretKey());
        return initExternalMessage.address;
        */

        // ton4j 4.7.0
        Pair pair = Mnemonic.toKeyPair(mnemonicList);
        TweetNaclFast.Signature.KeyPair keyPair = Utils.generateSignatureKeyPairFromSeed(pair.getSecretKey());

        WalletV4R2 contract = WalletV4R2.builder()
                .wc(0)
                .walletId(698983191)
                .keyPair(keyPair)
                .build();

        return contract.getAddress();
    }

    protected static String parseTonComment(String base64boc) {
        if (StringUtils.isNotEmpty(base64boc)) {
            //String base64boc = "te6cckEBAQEAEAAAHAAAAAAxOTg1NTk3NjUxSmLi/w==";
            byte[] rawBoc = Utils.base64ToSignedBytes(base64boc);
            Cell cell = CellBuilder.beginCell().fromBoc(rawBoc).endCell();
            byte[] newBytes = removePrefixZeros(cell.getBits().toByteArray());
            return new String(newBytes);
        } else {
            return null;
        }
    }

    protected static byte[] removePrefixZeros(byte[] bytes) {
        int i = 0;
        for (; i < bytes.length - 1; i++) {
            if (bytes[i] != 0) break;
        }
        return java.util.Arrays.copyOfRange(bytes, i, bytes.length);
    }

    protected static Boolean isContains(List<String> addressOrHexList, String targetAddressOrHex) {
        boolean isExists = false;
        for (String addressOrHex : addressOrHexList) {
            if (addressEquals(addressOrHex, targetAddressOrHex)) {
                isExists = true;
                break;
            }
        }
        return isExists;
    }

    protected static Boolean addressEquals(String addressOrHex, String addressOrHex2) {
        String address1 = new Address(addressOrHex).toHex();
        String address2 = new Address(addressOrHex2).toHex();
        return address1.equals(address2);
    }

    public static ChainBlockBO queryNewest(RpcConfigBO rpcConfig) {
        if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_CENTER_COM.getValue())) {
            return TonUtilTonCenter.queryNewest(rpcConfig);
        } else if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_API_IO.getValue())) {
            TonUtilTonApi.queryNewest(rpcConfig);
        }
        return null;
    }

    public static ChainBlockBO queryNewestByWorkChainShard(RpcConfigBO rpcConfig, Long workChain, String shard) {
        if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_CENTER_COM.getValue())) {
            return TonUtilTonCenter.queryNewestByWorkChainShard(rpcConfig, workChain, shard);
        } else if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_API_IO.getValue())) {
            TonUtilTonApi.queryNewestByWorkChainShard(rpcConfig, workChain, shard);
        }
        return null;
    }

    public static void fetchTransaction(
            List<TransferTransactionBO> transferTransactionBOList,
            boolean isProduction,
            RpcConfigBO rpcConfig,
            List<String> cryptoCurrencyAddressList,
            List<String> toAddressList,
            Long lt,
            Long timestamp
    ) {
        if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_CENTER_COM.getValue())) {
            TonUtilTonCenter.fetchTonTransaction(
                    transferTransactionBOList,
                    isProduction,
                    rpcConfig,
                    toAddressList,
                    lt
            );
            TonUtilTonCenter.fetchJettonTransaction(
                    transferTransactionBOList,
                    isProduction,
                    rpcConfig,
                    cryptoCurrencyAddressList,
                    toAddressList,
                    lt
            );
        } else if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_API_IO.getValue())) {
            TonUtilTonApi.fetchTonJettonTransaction(
                    transferTransactionBOList,
                    isProduction,
                    rpcConfig,
                    cryptoCurrencyAddressList,
                    toAddressList,
                    timestamp
            );
        }
    }

    public static ChainTransactionStatusEnum queryTransactionStatus(
            RpcConfigBO rpcConfig,
            TransferTransactionBO transferTransactionBO,
            boolean isProduction
    ) {
        if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_CENTER_COM.getValue())) {
            return TonUtilTonCenter.queryTransactionStatus(rpcConfig, transferTransactionBO, isProduction);
        } else if (rpcConfig.getRpcUrl().contains(ChainSiteEnum.TON_API_IO.getValue())) {
            return TonUtilTonApi.queryTransactionStatus(rpcConfig, transferTransactionBO, isProduction);
        }
        return ChainTransactionStatusEnum.UNKNOWN;
    }

    public static boolean verify(String publicKey, String message, String signature) {

        System.out.println("publicKey = " + publicKey);
        System.out.println("message = " + message);
        System.out.println("signature = " + signature);

        boolean verify = Ed25519.verify(publicKey.getBytes(), message.getBytes(), Utils.base64ToSignedBytes(signature));
        System.out.println(verify);

        return verify;
    }

    /**
     * {
     * "data": {
     * "tonProof": {
     * "name": "ton_proof",
     * "proof": {
     * "timestamp": 1721027928,
     * "domain": {
     * "lengthBytes": 14,
     * "value": "localhost:5173"
     * },
     * "signature": "d138DcRwbR/qhM42HMkSrvddLXXrSwZTTcB6UvnsP09zR3Bb/b9JTdBChGw7Q2w01ARIJHUkqIkG1nv5TQnaBA==",
     * "payload": "ded4933db94346b3a01236de86248851"
     * }
     * },
     * "account": {
     * "address": "0:66e4d9266f4f341c3d9066a8adf0635ca390d0b8d043e74d880cf086b932910f",
     * "chain": "-239",
     * "walletStateInit": "te6cckECFgEAAwQAAgE0AgEAUQAAAAApqaMXlOBALttLTiIAY89mClb1zYPxfei4wJnEheZrj1yCJ8lAART/APSkE/S88sgLAwIBIAkEBPjygwjXGCDTH9Mf0x8C+CO78mTtRNDTH9Mf0//0BNFRQ7ryoVFRuvKiBfkBVBBk+RDyo/gAJKTIyx9SQMsfUjDL/1IQ9ADJ7VT4DwHTByHAAJ9sUZMg10qW0wfUAvsA6DDgIcAB4wAhwALjAAHAA5Ew4w0DpMjLHxLLH8v/CAcGBQAK9ADJ7VQAbIEBCNcY+gDTPzBSJIEBCPRZ8qeCEGRzdHJwdIAYyMsFywJQBc8WUAP6AhPLassfEss/yXP7AABwgQEI1xj6ANM/yFQgR4EBCPRR8qeCEG5vdGVwdIAYyMsFywJQBs8WUAT6AhTLahLLH8s/yXP7AAIAbtIH+gDU1CL5AAXIygcVy//J0Hd0gBjIywXLAiLPFlAF+gIUy2sSzMzJc/sAyEAUgQEI9FHypwICAUgTCgIBIAwLAFm9JCtvaiaECAoGuQ+gIYRw1AgIR6STfSmRDOaQPp/5g3gSgBt4EBSJhxWfMYQCASAODQARuMl+1E0NcLH4AgFYEg8CASAREAAZrx32omhAEGuQ64WPwAAZrc52omhAIGuQ64X/wAA9sp37UTQgQFA1yH0BDACyMoHy//J0AGBAQj0Cm+hMYALm0AHQ0wMhcbCSXwTgItdJwSCSXwTgAtMfIYIQcGx1Z70ighBkc3RyvbCSXwXgA/pAMCD6RAHIygfL/8nQ7UTQgQFA1yH0BDBcgQEI9ApvoTGzkl8H4AXTP8glghBwbHVnupI4MOMNA4IQZHN0crqSXwbjDRUUAIpQBIEBCPRZMO1E0IEBQNcgyAHPFvQAye1UAXKwjiOCEGRzdHKDHrFwgBhQBcsFUAPPFiP6AhPLassfyz/JgED7AJJfA+IAeAH6APQEMPgnbyIwUAqhIb7y4FCCEHBsdWeDHrFwgBhQBMsFJs8WWPoCGfQAy2kXyx9SYMs/IMmAQPsABroccfA=",
     * "publicKey": "94e0402edb4b4e220063cf660a56f5cd83f17de8b8c099c485e66b8f5c8227c9"
     * }
     * }
     * }
     *
     * @param args
     */
    /*public static void main(String[] args) {
        //
        Address address = Address.of("0:66e4d9266f4f341c3d9066a8adf0635ca390d0b8d043e74d880cf086b932910f");
        //
        String tonProofPrefix = "ton-proof-item-v2/";

        //1
        //ton proof prefix
        byte[] msgBytes = tonProofPrefix.getBytes();

        //wc
        byte[] wcBytes = ByteBuffer.allocate(4).putInt(-239).array();
        msgBytes = Utils.concatBytes(msgBytes, wcBytes);

        //address hex
        msgBytes = Utils.concatBytes(msgBytes, address.hashPart);

        //domainLength
        byte[] domainLengthBytes = ByteBuffer.allocate(4).putInt(14).array();
        msgBytes = Utils.concatBytes(msgBytes, domainLengthBytes);

        //domain
        msgBytes = Utils.concatBytes(msgBytes, "localhost:5173".getBytes());

        //ts
        byte[] tsBytes = ByteBuffer.allocate(8).putLong(1721027928).array();
        msgBytes = Utils.concatBytes(msgBytes, tsBytes);

        //payload
        msgBytes = Utils.concatBytes("ded4933db94346b3a01236de86248851".getBytes(), tsBytes);

        //
        String msgHash = Utils.sha256(msgBytes);

        // 2
        byte[] fullMsg = new byte[2];
        fullMsg[0] = (byte) 255;
        fullMsg[1] = (byte) 255;

        fullMsg = Utils.concatBytes(fullMsg, "ton-connect".getBytes());

        //
        fullMsg = Utils.concatBytes(fullMsg, msgHash.getBytes());


        //3
        String fullMsgHash = Utils.sha256(fullMsg);

        //
        //byte[] signatureBytes = Utils.base64ToSignedBytes("d138DcRwbR/qhM42HMkSrvddLXXrSwZTTcB6UvnsP09zR3Bb/b9JTdBChGw7Q2w01ARIJHUkqIkG1nv5TQnaBA==");

        boolean verify = verify(
                "94e0402edb4b4e220063cf660a56f5cd83f17de8b8c099c485e66b8f5c8227c9",
                fullMsgHash,
                "d138DcRwbR/qhM42HMkSrvddLXXrSwZTTcB6UvnsP09zR3Bb/b9JTdBChGw7Q2w01ARIJHUkqIkG1nv5TQnaBA=="
        );

        System.out.println(verify);
    }*/

    /*public static byte[] removeLeadingZeros(byte[] bytes) {
        int i = 0;
        for (; i < bytes.length - 1; i++) {
            if (bytes[i] != 0) break;
        }
        return java.util.Arrays.copyOfRange(bytes, i, bytes.length);
    }*/

}