package com.xxx.common.core.utils.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.Random;

public class RandomUtil {

    public static String randomDigit(int count) {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        // 循环指定次，每次生成一个 0-9 的数字
        for (int i = 0; i < count; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }

    public static int getByMinMax(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }

    public static BigDecimal getBigDecimalByMinMax(BigDecimal min, BigDecimal max, int scale) {
        float minF = min.floatValue();
        float maxF = max.floatValue();
        BigDecimal db = new BigDecimal(Math.random() * (maxF - minF) + minF);
        return db.setScale(scale, RoundingMode.DOWN);
    }

}