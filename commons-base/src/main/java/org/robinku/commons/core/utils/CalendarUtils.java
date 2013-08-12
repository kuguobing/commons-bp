package org.robinku.commons.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author RobinHood
 * 
 */
public class CalendarUtils {

    public static String firstTimestampOfThisWeek(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int value = cal.getActualMinimum(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_WEEK, value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        return sdf.format(cal.getTime());
    }

    public static String firstTimestampOfThisWeek() {
        return firstTimestampOfThisWeek(new Date());
    }

    public static String lastTimestampOfThisWeek(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int value = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_WEEK, value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 23:59:59");
        return sdf.format(cal.getTime());
    }

    public static String lastTimestampOfThisWeek() {
        return lastTimestampOfThisWeek(new Date());
    }

    public static String firstTimestampOfThisMonth(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int value = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        return sdf.format(cal.getTime());
    }

    public static String firstTimestampOfThisMonth() {
        return firstTimestampOfThisMonth(new Date());
    }

    public static String lastTimestampOfThisMonth(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 23:59:59");
        return sdf.format(cal.getTime());
    }

    public static String lastTimestampOfThisMonth() {
        return lastTimestampOfThisMonth(new Date());
    }

    public static String firstTimestampOfToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        return sdf.format(new Date());
    }

    public static String lastTimestampOfToday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 23:59:59");
        return sdf.format(new Date());

    }

    public static String firstTimestampOfThisYear(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int value = cal.getActualMinimum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        return sdf.format(cal.getTime());
    }

    public static String firstTimestampOfThisYear() {
        return firstTimestampOfThisYear(new Date());
    }

    public static String lastTimestampOfThisYear(Date theDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(theDate);
        int value = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        cal.set(Calendar.DAY_OF_YEAR, value);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 23:59:59");
        return sdf.format(cal.getTime());
    }

    public static String lastTimestampOfThisYear() {
        return lastTimestampOfThisYear(new Date());
    }

    @SuppressWarnings("unused")
    private static Date lastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    @SuppressWarnings("unused")
    private static Date lastDayOfMonth2(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int value = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, value);
        return cal.getTime();
    }

    @SuppressWarnings("unused")
    private static Date lastDayOfMonth3(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @SuppressWarnings("unused")
    private static Date lastDayOfMonth4(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        do {
            cal.add(Calendar.DATE, 1);
        } while (cal.get(Calendar.DATE) != 1);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("first day of this week:" + firstTimestampOfThisWeek());
        System.out.println("last day of this week:" + lastTimestampOfThisWeek());
        System.out.println("first day of this month:" + firstTimestampOfThisMonth());
        System.out.println("last day of this month:" + lastTimestampOfThisMonth());
        System.out.println("first day of this year:" + firstTimestampOfThisYear());
        System.out.println("last day of this year:" + lastTimestampOfThisYear());

    }
}
