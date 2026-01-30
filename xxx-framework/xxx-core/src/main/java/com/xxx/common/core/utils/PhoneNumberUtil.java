package com.xxx.common.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberUtil {

    private final static Pattern UNIVERSAL_PATTERN = Pattern.compile("^\\+[1-9]\\d{0,2}-\\d{4,14}$");

    private final static Pattern DIALING_CODE_PATTERN = Pattern.compile("^\\+(\\d{1,3})");

    /**
     * 通用电话号码判断（全球适用）
     * 基于 E.164 国际电话号码标准
     * <p>
     * 规则：
     * 1. 以 + 开头
     * 2. 国家代码：1-3位数字
     * 3. 连字符 -
     * 4. 号码部分：4-15位数字
     * 5. 总长度（不含+和-）：5-15位数字
     * <p>
     * 支持格式示例：
     * +1-2025551234       (美国)
     * +44-7912345678      (英国)
     * +86-13812345678     (中国)
     * +91-9876543210      (印度)
     * +81-9012345678      (日本)
     * +33-612345678       (法国)
     * +61-412345678       (澳大利亚)
     * +852-91234567       (香港)
     * +886-912345678      (台湾)
     * +65-81234567        (新加坡)
     */
    public static boolean isUniversalPhoneNumber(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }

        // 通用正则：+[1-9]\d{0,2}-\d{4,14}
        // 解释：
        // ^\\+         : 必须以 + 开头
        // [1-9]\\d{0,2}: 国家代码1-3位，第一位不能是0
        // -            : 连字符
        // \\d{4,14}    : 电话号码4-14位数字
        // $            : 结束

        if (!UNIVERSAL_PATTERN.matcher(input).matches()) {
            return false;
        }
        // 额外验证：提取纯数字部分，总长度应在5-15位之间
        String digitsOnly = input.replaceAll("[^\\d]", "");
        int totalDigits = digitsOnly.length();

        return totalDigits >= 5 && totalDigits <= 15;
    }

    public static boolean isValid(String mobile, String pattern) {
        if (mobile == null || mobile.trim().isEmpty()) {
            return false;
        }
        Matcher matcher = Pattern.compile(pattern).matcher(mobile.trim());
        return matcher.matches();
    }

    //    /**
    //     * @Deprecated
    //     * 中国手机号正则表达式
    //     * 支持:
    //     * - 11位数字
    //     * - 以1开头
    //     * - 第二位为3-9之间的数字
    //     * - 支持可选的国际区号前缀(+86或0086)
    //     */
    //    private static final String PHONE_NUMBER_PATTERN = "^(\\+86|0086)?1[3-9]\\d{9}$";
    //
    //    private static final Pattern PHONE_NUMBER_REGEX = Pattern.compile(PHONE_NUMBER_PATTERN);
    //
    //    /**
    //     * @Deprecated
    //     * 校验手机号格式是否正确
    //     * @param mobile 待校验的手机号
    //     * @return 如果格式正确返回true，否则返回false
    //     */
    //    public static boolean isValid(String mobile) {
    //        if (mobile == null || mobile.trim().isEmpty()) {
    //            return false;
    //        }
    //        Matcher matcher = PHONE_NUMBER_REGEX.matcher(mobile.trim());
    //        return matcher.matches();
    //    }
    //
    //    /**
    //     * @Deprecated
    //     * 格式化手机号
    //     * @param mobile 待格式化的手机号
    //     * @return 格式化后的手机号，如: 138****5678
    //     */
    //    private static String format(String mobile) {
    //        if (!isValid(mobile)) {
    //            return mobile;
    //        }
    //        // 去除国际区号前缀
    //        String cleanPhone = mobile.replaceAll("^(\\+86|0086)", "").trim();
    //        // 只保留数字
    //        cleanPhone = cleanPhone.replaceAll("\\D", "");
    //        // 格式化显示
    //        return cleanPhone.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1****$3");
    //    }

    /**
     * 提取电话号码的国家代码
     *
     * @param phoneNumber 电话号码字符串
     * @return 国家代码（带+号），如果无法提取则返回 null
     * <p>
     * 示例：
     * extractDialingCode("+91-9876543210") -> "+91"
     * extractDialingCode("+86-13812345678") -> "+86"
     * extractDialingCode("+1-2025551234") -> "+1"
     * extractDialingCode("+852-91234567") -> "+852"
     */
    public static String extractDialingCode(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return null;
        }

        // 必须以 + 开头
        if (!phoneNumber.startsWith("+")) {
            return null;
        }

        // 使用正则提取 + 后面的数字，直到遇到非数字字符
        //Pattern pattern = Pattern.compile("^\\+(\\d{1,3})");
        Matcher matcher = DIALING_CODE_PATTERN.matcher(phoneNumber);

        if (matcher.find()) {
            return "+" + matcher.group(1);
        }

        return null;
    }

    /**
     * 提取电话号码的国家代码（仅数字）
     *
     * @param phoneNumber 电话号码字符串
     * @return 国家代码（纯数字），如果无法提取则返回 null
     * <p>
     * 示例：
     * extractDialingCodeDigits("+91-9876543210") -> "91"
     * extractDialingCodeDigits("+86-13812345678") -> "86"
     */
    public static String extractDialingCodeDigits(String phoneNumber) {
        String code = extractDialingCode(phoneNumber);
        return code != null ? code.substring(1) : null;
    }

    /**
     * 提取电话号码的本地号码部分（去除国家代码）
     *
     * @param phoneNumber 电话号码字符串
     * @return 本地号码，如果无法提取则返回 null
     * <p>
     * 示例：
     * extractLocalNumber("+91-9876543210") -> "9876543210"
     * extractLocalNumber("+86-13812345678") -> "13812345678"
     */
    public static String extractLocalNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return null;
        }

        String countryCode = extractDialingCode(phoneNumber);
        if (countryCode == null) {
            return null;
        }

        // 移除国家代码和所有非数字字符
        String remaining = phoneNumber.substring(countryCode.length());
        String digitsOnly = remaining.replaceAll("[^\\d]", "");

        return digitsOnly.isEmpty() ? null : digitsOnly;
    }

    public static void main(String[] args) {
        String phoneNumber = "+44-7912345678";
        String phoneNumber2 = "+44-7912345678";
    }

}