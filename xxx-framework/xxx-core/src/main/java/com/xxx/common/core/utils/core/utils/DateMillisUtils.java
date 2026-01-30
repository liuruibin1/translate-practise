package com.xxx.common.core.utils.core.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author xiezm
 */
public class DateMillisUtils {

    public static final long DAY_MILLIS = 86400000;

    public static final long MINUTE_MILLIS = 60000;

    /**
     * 根据 java.util.Date日期毫秒数 获取 日期的第一毫秒
     *
     * @param millis java.util.Date 日期对象的毫秒数
     * @return 日期的第一秒
     */
    public static long getDateFirstMillisByDateMillis(long millis) {
        //long days = dateMilliseconds / DAY_MILLISECONDS;
        //return days * DAY_MILLISECONDS;
        // 1. 将毫秒值转换为 Instant（UTC 时间）
        Instant instant = Instant.ofEpochMilli(millis);

        // 2. 转换为当前系统时区的 ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        // 3. 获取该日期的第一毫秒（即当天的 00:00:00.000）
        ZonedDateTime firstMillisecond = zonedDateTime.toLocalDate() // 提取日期部分
                .atStartOfDay(ZoneId.systemDefault()); // 获取当天的 00:00:00

        // 4. 转换为 java.util.Date（如果需要）
        Date result = Date.from(firstMillisecond.toInstant());
        return result.getTime();
    }

    /**
     * 根据 java.util.Date日期毫秒数 获取 日期的第一毫秒
     *
     * @param millis java.util.Date 日期对象的毫秒数
     * @return 日期的第一秒
     */
    public static long getDateFirstMsByDateMsUTC(long millis) {
        //long days = dateMilliseconds / DAY_MILLISECONDS;
        //return days * DAY_MILLISECONDS;
        // 1. 将毫秒值转换为 Instant（UTC 时间）
        Instant instant = Instant.ofEpochMilli(millis);

        // 2. 转换为UTC的 ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(ZoneOffset.UTC);

        // 3. 获取该日期的第一毫秒（即当天的 00:00:00.000）
        ZonedDateTime firstMillisecond = zonedDateTime.toLocalDate() // 提取日期部分
                .atStartOfDay(ZoneOffset.UTC); // 获取当天的 00:00:00

        // 4. 转换为 java.util.Date（如果需要）
        Date result = Date.from(firstMillisecond.toInstant());
        return result.getTime();
    }

    /**
     * 根据 日期 获取 日期的第一毫秒
     *
     * @param date 日期对象
     * @return 日期第一毫秒
     */
    public static long getDateFirstMsByDate(Date date) {
        return getDateFirstMillisByDateMillis(date.getTime());
    }

    /**
     * 根据 日期 获取 日期的第一毫秒
     *
     * @param date 日期对象
     * @return 日期第一毫秒
     */
    public static long getDateFirstMsByDateUTC(Date date) {
        return getDateFirstMsByDateMsUTC(date.getTime());
    }

    /**
     * 根据毫秒值取星期几
     *
     * @param millis java.util.Date毫秒值
     * @return 星期几
     */
    public static int getWeekDateByMs(long millis) {
        // 创建 Calendar 实例，指定时区（例如 UTC）
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault()); // 取当前默认时区
        calendar.setTimeInMillis(millis);
        // 获取星期几（Calendar.SUNDAY=1, MONDAY=2, ..., SATURDAY=7）
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据毫秒值 取之前或之后天的第一毫秒
     *
     * @param millis 毫秒值
     * @param days         第几天,正值取之后，负值取之前
     * @return 取之前或之后天的第一毫秒
     */
    public static long getDayFirstMillisByMillisAndDays(long millis, int days) {
        long ms_ = getDateFirstMillisByDateMillis(millis);
        return ms_ + DAY_MILLIS * days;
    }

    /**
     * 根据毫秒值 取之前或之后天的第一毫秒
     *
     * @param millis 毫秒值
     * @param days         第几天,正值取之后，负值取之前
     * @return 取之前或之后天的第一毫秒
     */
    public static long getDayFirstMillisByMillisAndDaysUTC(long millis, int days) {
        long ms_ = getDateFirstMsByDateMsUTC(millis);
        return ms_ + DAY_MILLIS * days;
    }

    /*public static void main(String[] args) {
        long dateFirstMillisecond = getDayFirstMillisecondByMillisecondsAndDays(1747142049013L,0);
        System.out.println(dateFirstMillisecond);
        long dateFirstMillisecond2 = (dateFirstMillisecond + DateMilliSecondUtils.DAY_MILLISECONDS - 1);
        System.out.println(dateFirstMillisecond2);
    }*/

}