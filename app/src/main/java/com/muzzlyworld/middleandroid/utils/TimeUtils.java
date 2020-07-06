package com.muzzlyworld.middleandroid.utils;

final public class TimeUtils {

    private TimeUtils() {}

    public static long getMinutesInMillis(int minutes) {
        return minutes * 60 * 1000;
    }

    public static long getHoursInMillis(int hour) {
        return hour * 60 * getMinutesInMillis(60);
    }

    public static long getDayInMillis(int days) {
        return days * getHoursInMillis(24);
    }

}
