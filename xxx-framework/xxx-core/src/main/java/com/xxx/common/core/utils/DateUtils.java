package com.xxx.common.core.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static final int DAY_SECONDS = 86400;

    //public static final String yyyyMM = "yyyyMM";

    public static final String yyyy = "yyyy";

    public static final String yyyyMMdd = "yyyyMMdd";

    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";

    //public static final String yyyy_MM = "yyyy-MM";

    public static final String yyyy_MM_dd = "yyyy-MM-dd";

    //public static final String MM_dd = "MM-dd";

    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    public static final String HH_mm_ss = "HH:mm:ss";

    //private static final String[] PARSE_PATTERNS = {
    //        "yyyy-MM-dd",
    //        "yyyy-MM-dd HH:mm:ss",
    //        "yyyy-MM-dd HH:mm",
    //        "yyyy-MM",
    //        "yyyy/MM/dd",
    //        "yyyy/MM/dd HH:mm:ss",
    //        "yyyy/MM/dd HH:mm",
    //        "yyyy/MM",
    //        "yyyy.MM.dd",
    //        "yyyy.MM.dd HH:mm:ss",
    //        "yyyy.MM.dd HH:mm",
    //        "yyyy.MM"
    //};

    /**
     * 正则表达式 yyyy-MM-dd HH:mm:ss
     */
    public static final String REGEX_yyyy_MM_dd_HH_mm_ss = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])\\s(0\\d|1\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$";

    /**
     * 正则表达式 格式 yyyy-MM-dd
     */
    public static final String REGEX_yyyy_MM_dd = "^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";

    /**
     * 格式化日期对象为日期格式
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期格式化字符
     */
    public static String dateToString(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期对象为日期格式
     *
     * @param dateMilliseconds 日期毫秒
     * @param pattern          日期格式
     * @return 日期格式化字符
     */
    public static String millisToString(long dateMilliseconds, String pattern) {
        Date date = new Date(dateMilliseconds);
        return dateToString(date, pattern);
    }

    /**
     * 根据日期格式解析日期字符串为日期毫秒
     *
     * @param dateStr 日期字符；例: 2025-01-01
     * @param pattern 日期格式
     * @return 日期毫秒
     */
    public static long dateStrToMillis(String dateStr) {
        if (!isValidDateStr(dateStr, REGEX_yyyy_MM_dd)) {
            throw new RuntimeException("格式异常");
        }
        String[] dateArr = dateStr.split("-");
        // 设置目标日期
        LocalDate localDate = LocalDate.of(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
        // 将 LocalDateTime 转换为带时区的 ZonedDateTime（假设使用系统默认时区）
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        // 获取时间戳（毫秒值）
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 根据日期格式解析日期字符串为日期毫秒
     *
     * @param datetimeStr 日期字符；例: 2025-01-01 01:01:01
     * @param pattern     日期格式
     * @return 日期毫秒
     */
    public static long datetimeStrToMillis(String datetimeStr) {
        if (!isValidDateStr(datetimeStr, REGEX_yyyy_MM_dd_HH_mm_ss)) {
            throw new RuntimeException("格式异常");
        }
        // 使用正则分割 年、月、日、时、分、秒
        String[] dateArr = datetimeStr.split("[-: ]");
        // 设置目标日期
        LocalDateTime localDateTime = LocalDateTime.of(
                Integer.parseInt(dateArr[0]),
                Integer.parseInt(dateArr[1]),
                Integer.parseInt(dateArr[2]),
                Integer.parseInt(dateArr[3]),
                Integer.parseInt(dateArr[4]),
                Integer.parseInt(dateArr[5])
        );
        // 将 LocalDateTime 转换为带时区的 ZonedDateTime（假设使用系统默认时区）
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        // 获取时间戳（毫秒值）
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * 是否匹配日期格式
     *
     * @param dateStr 例: 2025-02-08 00:08:08,2025-02-08
     * @param regex   正则表达式
     * @return 是否匹配
     */
    public static boolean isValidDateStr(String dateStr, String regex) {
        return Pattern.compile(regex).matcher(dateStr).matches();
    }

    public static DateTimeFormatter getFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * 判断两时间，不是同一天
     * @param lastLoginTs
     * @param currentTs
     * @return
     */
    public static boolean isDiffDay(Date lastLoginTs, Date currentTs) {
        if (lastLoginTs == null || currentTs == null) {
            return false;
        }
        // 转成 LocalDate，只保留年月日
        LocalDate lastLoginDate = lastLoginTs.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate currentDate = currentTs.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return !lastLoginDate.equals(currentDate);
    }

    public static Date getStartOfDayUTC(Date date) {
        Instant instant = date.toInstant();

        ZonedDateTime utcStartOfDay = instant.atZone(ZoneOffset.UTC)
                .toLocalDate()
                .atStartOfDay(ZoneOffset.UTC);

        return Date.from(utcStartOfDay.toInstant());
    }

    public static Long getNextMondayStartOfDayUTCLongValue(Date date) {
        Date d = Date.from(
                date.toInstant()
                        .atZone(ZoneOffset.UTC)
                        .with(TemporalAdjusters.next(DayOfWeek.MONDAY)) // 获取下周一
                        .toLocalDate()
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC)
        );
        return Long.valueOf(DateUtils.dateToString(d, yyyyMMddHHmmss));
    }

}