package com.xxx.common.core.utils.core.utils.uuid;

import java.util.Arrays;
import java.util.List;

public class UUIDUtil {

    /**
     * 随机 简单模式不带'-'的UUID字符串
     * @return UUID
     */
    public static String randomUUID() {
        return randomUUID(true);
    }

    /**
     * 随机 isSimple=true 为简单模式 不带'-'的UUID字符串
     * @param isSimple Boolean
     * @return UUID
     */
    public static String randomUUID(boolean isSimple) {
        return UUID.randomUUID(true).toString(isSimple);
    }

    public static String generateBySeed(String seed) {
        byte[] bytes = seed.getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(bytes);
        return uuid.toString().replaceAll("-", "");
    }

    public static String generate32ByLowerCaseSeedArray(String... seedArray) {
        List<String> seedList = Arrays.asList(seedArray);
        String seedStr = String.join("", seedList);
        return generateBySeed(seedStr.toLowerCase());
    }

    public static String generate8ByUpperCaseSeedArray(String... seedArray) {
        List<String> seedList = Arrays.asList(seedArray);
        String seedStr = String.join("", seedList);
        return generateBySeed(seedStr).toUpperCase().substring(0, 8);
    }

    public static String generate6ByUpperCaseSeedArray(String... seedArray) {
        List<String> seedList = Arrays.asList(seedArray);
        String seedStr = String.join("", seedList);
        return generateBySeed(seedStr).toUpperCase().substring(0, 6);
    }

    public static void main(String[] args) {
        System.out.println(randomUUID());
        System.out.println(randomUUID(false));
    }

}