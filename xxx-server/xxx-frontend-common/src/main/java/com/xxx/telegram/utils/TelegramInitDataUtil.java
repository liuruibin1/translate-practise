package com.xxx.telegram.utils;

import com.xxx.common.core.utils.URLUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TelegramInitDataUtil {

    /**
     * 验证 Telegram Mini App 的 initData 签名
     *
     * @param rawInitData 原始初始化数据（格式：key1=value1&key2=value2...）
     * @param botToken    机器人 Token（从 @BotFather 获取）
     * @return 验证通过返回 true，否则 false
     */
    public static boolean validate(String rawInitData, String botToken) {
        if (rawInitData == null || rawInitData.isEmpty() || botToken == null || botToken.isEmpty()) {
            return false;
        }

        // 解析查询参数
        Map<String, String> params = parseQueryParams(rawInitData);
        String receivedHash = params.get("hash");
        if (receivedHash == null) {
            return false;
        }

        // 准备待验证数据：移除 hash 参数，按键名排序
        params.remove("hash");
        String dataCheckString = buildDataCheckString(params);

        try {
            // 步骤1：使用 "WebAppData" 对 botToken 生成密钥
            byte[] secretKey = hmacSha256("WebAppData".getBytes(StandardCharsets.UTF_8), botToken);

            // 步骤2：使用密钥计算 dataCheckString 的哈希
            String computedHash = hmacSha256Hex(secretKey, dataCheckString);

            // 安全比较哈希值
            return MessageDigest.isEqual(
                    computedHash.getBytes(StandardCharsets.UTF_8),
                    receivedHash.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析查询字符串为键值对
     */
    private static Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0) {
                String key = pair.substring(0, idx);
                String value = URLUtil.decodeUTF_8(pair.substring(idx + 1));
                params.put(key, value);
            }
        }
        return params;
    }

    /**
     * 构建待签名字符串（格式：key1=value1\nkey2=value2）
     */
    private static String buildDataCheckString(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            sb.append(key).append("=").append(params.get(key));
            if (i < keys.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * HMAC-SHA256 计算（二进制输出）
     */
    private static byte[] hmacSha256(byte[] key, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(new SecretKeySpec(key, "HmacSHA256"));
        return hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * HMAC-SHA256 计算（十六进制输出）
     */
    private static String hmacSha256Hex(byte[] key, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] hash = hmacSha256(key, data);
        return bytesToHex(hash);
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // 示例数据（实际应从Telegram请求中获取）
        String rawInitData = "user=%7B%22id%22%3A279058397%2C%22first_name%22%3A%22Vladislav%20%2B%20-%20%3F%20%5C%2F%22%2C%22last_name%22%3A%22Kibenko%22%2C%22username%22%3A%22vdkfrost%22%2C%22language_code%22%3A%22ru%22%2C%22is_premium%22%3Atrue%2C%22allows_write_to_pm%22%3Atrue%2C%22photo_url%22%3A%22https%3A%5C%2F%5C%2Ft.me%5C%2Fi%5C%2Fuserpic%5C%2F320%5C%2F4FPEE4tmP3ATHa57u6MqTDih13LTOiMoKoLDRG4PnSA.svg%22%7D&chat_instance=8134722200314281151&chat_type=private&auth_date=1733509682&signature=TYJxVcisqbWjtodPepiJ6ghziUL94-KNpG8Pau-X7oNNLNBM72APCpi_RKiUlBvcqo5L-LAxIc3dnTzcZX_PDg&hash=a433d8f9847bd6addcc563bff7cc82c89e97ea0d90c11fe5729cae6796a36d73";
        String botToken = "7342037359:AAHI25ES9xCOMPokpYoz-p8XVrZUdygo2J4"; // 替换为真实Token
        //String rawInitData = "query_id=AAHdF6IQAAAAAN0XohDhrOrc" +
        //        "&user=%7B%22id%22%3A279058397%2C%22first_name%22%3A%22Vladislav%22%2C%22last_name%22%3A%22Kibenko%22%2C%22username%22%3A%22vdkfrost%22%2C%22language_code%22%3A%22ru%22%2C%22is_premium%22%3Atrue%7D" +
        //        "&auth_date=1662771648" +
        //        "&hash=c501b71e775f74ce10e377dea85a7ea24ecd640b223ea86dfe453e0eaed2e2b2";
        //String botToken = "5768337691:AAH5YkoiEuPk8-FZa32hStHTqXiLPtAEhx8"; // 替换为真实Token
        boolean isValid = TelegramInitDataUtil.validate(rawInitData, botToken);
        System.out.println("InitData验证结果: " + isValid);
    }

}