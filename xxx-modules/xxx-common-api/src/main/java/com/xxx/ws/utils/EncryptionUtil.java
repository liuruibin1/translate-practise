package com.xxx.ws.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 消息加密解密工具类 (AES)
 * 注意：实际生产环境应使用更安全的密钥管理和传输方式，并确保密钥长度符合要求。
 * 这里为了简化，密钥直接硬编码，实际应从配置中心获取。
 */
public class EncryptionUtil {

    // !!! 重要安全提示 !!!
    // 在生产环境中，密钥和 IV 绝不能硬编码。
    // 它们应该通过安全的方式管理和分发。
    // 这里的硬编码仅为演示目的，且与前端 aes.ts 中的密钥和 IV 保持一致。

    // 密钥 (16 字节 = 128 位)
    // 根据用户提供的后端密钥 "Jm2hB971MbHwWv89" 进行修改
    private static final String AES_KEY_STRING = "Jm2hB971MbHwWv89"; // 使用字符串密钥
    private static final byte[] AES_KEY_BYTES = AES_KEY_STRING.getBytes(StandardCharsets.UTF_8); // 将字符串密钥转换为 UTF-8 字节
    private static final SecretKeySpec SECRET_KEY = new SecretKeySpec(AES_KEY_BYTES, "AES");

    // 初始化向量 IV (16 字节)
    // 保持与前端一致
    private static final String AES_IV_HEX = "000102030405060708090a0b0c0d0e0f";
    private static final byte[] AES_IV_BYTES = hexStringToByteArray(AES_IV_HEX);
    private static final IvParameterSpec IV_SPEC = new IvParameterSpec(AES_IV_BYTES);

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding"; // PKCS5Padding 等同于 PKCS7Padding 对于 AES

    /**
     * 加密字符串
     * @param data 待加密的字符串
     * @return 加密后的 Base64 字符串
     * @throws Exception 加密过程中可能发生的异常
     */
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY, IV_SPEC);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 解密 Base64 字符串
     * @param encryptedDataBase64 待解密的 Base64 字符串
     * @return 解密后的原始字符串
     * @throws Exception 解密过程中可能发生的异常
     */
    public static String decrypt(String encryptedDataBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY, IV_SPEC);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedDataBase64);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 将十六进制字符串转换为字节数组
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    // 示例用法
    public static void main(String[] args) {
        try {
            String originalText = "Hello, WebSocket!";
            String encryptedText = encrypt(originalText);
            System.out.println("Original: " + originalText);
            System.out.println("Encrypted (Base64): " + encryptedText);

            String decryptedText = decrypt(encryptedText);
            System.out.println("Decrypted: " + decryptedText);

            // 验证是否一致
            if (originalText.equals(decryptedText)) {
                System.out.println("Encryption and decryption successful!");
            } else {
                System.out.println("Encryption and decryption failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}