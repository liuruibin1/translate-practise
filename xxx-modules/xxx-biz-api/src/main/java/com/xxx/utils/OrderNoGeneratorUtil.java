package com.xxx.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class OrderNoGeneratorUtil {
    /**
     * 生成INDIA前缀的唯一订单号
     * 格式：INDIA + yyyyMMddHHmmss + 毫秒 + 随机数
     * 示例：INDIA202412081450235481234567890
     */
    public static String generateOrderId(String prefix) {
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);

        // 获取毫秒
        long millis = System.currentTimeMillis() % 1000;

        // 获取纳秒的后6位
        long nanos = System.nanoTime() % 1000000;

        // 生成6-8位随机数
        Random random = new Random();
        long randomNum = Math.abs(random.nextLong() % 100000000L); // 8位随机数

        return String.format("%s%s%03d%06d%08d", prefix, timestamp, millis, nanos, randomNum);
    }

    public static void main(String[] args) {
        String india = generateOrderId("INDIA");
        System.out.println(india);
    }
}
