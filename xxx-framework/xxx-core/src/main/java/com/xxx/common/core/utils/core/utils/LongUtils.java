package com.xxx.common.core.utils.core.utils;

public class LongUtils {

    /**
     * * 判断一个是否可以讲string转long
     *
     * @return true：为空 false：非空
     */
    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
            return true; // 字符串可以转换为long类型
        } catch (NumberFormatException e) {
            return false; // 字符串无法转换为long类型
        }
    }


}
