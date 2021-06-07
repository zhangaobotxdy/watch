package com.wrist.watch.utils;


import org.apache.http.util.TextUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class OnTheDayTime {
    /**
     * 获取本日剩余 秒
     *
     * @return 时间戳/1000
     */
    public static int getLastSeconds() {
        // 得到今天 晚上的最后一刻 最后时间
        String last = getTime() + " 23:59:59";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(last, fmt);
        long latDate = parse.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // 得到的毫秒 除以1000转换 为秒
        return (int) (latDate - System.currentTimeMillis()) / 1000;
    }

    /**
     * 获取本日剩余 毫秒
     *
     * @return 时间戳
     */
    public static int getLastSecondsHm() {
        Calendar calendar = Calendar.getInstance();
        // 得到今天 晚上的最后一刻 最后时间
        String last = getTime() + " 23:59:59";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(last, fmt);
        long latDate = parse.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        // 得到的毫秒 除以1000转换 为秒
        return (int) (latDate - System.currentTimeMillis());
    }

    /**
     * 获取时间 yyyy-MM-dd
     *
     * @return 时间
     */
    public static String getTime() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return now.format(fmt);
    }

    /**
     * 获取时间 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTimeTo() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(fmt);
    }

    /**
     * 时间戳转日期/时间
     *
     * @param seconds 时间戳
     * @param pattern 格式
     * @return
     */
    public static String timeStamp2Date(long seconds, String pattern) {
        String time = "暂无数据";
        if (TextUtils.isEmpty(pattern)){ pattern = "yyyy-MM-dd  HH:mm:ss";}
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds / 1000L, 0, ZoneOffset.ofHours(8));
        if (seconds != 0) {time = dateTime.format(DateTimeFormatter.ofPattern(pattern)); }
        return time;

    }

    /**
     * 日期/时间转时间戳
     *
     * @param date    时间
     * @param pattern 格式
     * @return
     */
    public static long date2TimeStamp(String date, String pattern) {
        long timeStamp;
        if (TextUtils.isEmpty(pattern)) { pattern = "yyyy-MM-dd HH:mm:ss"; }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        timeStamp = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
        return timeStamp;
    }

    /**
     * 日期/时间转时间戳 yyyy-MM-dd
     *
     * @param date    时间
     * @param pattern 格式
     * @return
     */
    public static long date2Stamp(String date, String pattern) {
        long timeStamp;
        if (TextUtils.isEmpty(pattern)) { pattern = "yyyy-MM-dd"; }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDateTime = LocalDate.parse(date, formatter);
        timeStamp = localDateTime.atStartOfDay(ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
        return timeStamp;
    }
}
