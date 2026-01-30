package com.xxx.common.core.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtil {

    private static final String ALGORITHM = "AES";

    public static String encode(String rawValue, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(rawValue.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    public static String decode(String encryptedValue, String secretKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
        return new String(decryptedData);
    }

    public static boolean isValid(String secretKey) {
        return secretKey != null && (secretKey.length() == 16 || secretKey.length() == 24 || secretKey.length() == 32);
    }

    public static void main(String[] args) throws Exception {
        /*String encode = encode("09526025de7f0803b70d9dbffe58be5a15a2e16b5574eb37229f70da71487511", "12345678abcdefgh");
        System.out.println(encode);
        String decode = decode(encode, "12345678abcdefgh");
        System.out.println(decode);*/
        //
        String decode = decode("NDADLK9pnoUESeWOA1HdqnIfmWc5vf7trePK0IKbP50/Xe5I8Ez38S5TWGOMsPyCZCilZS9w3JiHRenhrST/+y+XJ6nR2uNBe9nT5ddwvEM=", "abcdefgh12345678");
        System.out.println(decode);
        //
        decode = decode("NAvb2X4O4l+lcs0irTta3UPzzwANaBSDE3omkLAC09rxEaKURoGcU818XxTIvSYYNesCXcW3tEmS1/EN3sRJFi+XJ6nR2uNBe9nT5ddwvEM=", "abcdefgh12345678");
        System.out.println(decode);
        //
        decode = decode("HkehV67dlJ7ana4EH+7ZSuKhbLdKRrWWv7sldUhuZ40YxMtJhYTsYRpf+zKJLHS4jg9ChpYyke8e7cbyXXBrrS+XJ6nR2uNBe9nT5ddwvEM=", "abcdefgh12345678");
        System.out.println(decode);

    }

}