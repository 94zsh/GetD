package com.future.getd.utils;

import java.util.Calendar;

public class TimeUtils {
    public static boolean isToday(long timestamp) {
        Calendar calendar = Calendar.getInstance();

        // 获取当前日期的年、月、日
        calendar.setTimeInMillis(System.currentTimeMillis());
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 获取时间戳对应的日期
        calendar.setTimeInMillis(timestamp);
        int timestampYear = calendar.get(Calendar.YEAR);
        int timestampMonth = calendar.get(Calendar.MONTH);
        int timestampDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 比较年、月、日
        return currentYear == timestampYear
                && currentMonth == timestampMonth
                && currentDay == timestampDay;
    }
}
