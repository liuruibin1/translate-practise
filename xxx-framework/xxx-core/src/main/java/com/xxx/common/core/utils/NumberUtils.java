package com.xxx.common.core.utils;

import static org.apache.commons.lang3.math.NumberUtils.isCreatable;

public class NumberUtils {

    public static boolean isLong(String str) {
        return isCreatable(str) && !str.contains(".");
    }

}