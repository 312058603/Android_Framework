package com.example.wpx.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <h3>日期工具</h3>
 * <h3>创建人</h3> （王培学）
 * <h3>创建日期</h3> 2017/12/18 9:56
 * <h3>著作权</h3> 2017 Shenzhen Guomaichangxing Technology Co., Ltd. Inc. All rights reserved.
 */
public class DateUtil {

    /**
     * 常用的日期格式
     */
    public static String DATE_PATTERN = "yyyy-MM-dd";
    public static String SHORT_DATE_PATTERN = "yy-MM-dd";
    public static String WEEK_PATTERN = "yyyy/MM/dd";
    public static String MONTH_PATTERN = "yyyy-MM";
    public static String TIME_PATTERN = "HH:mm:ss";
    public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String METHOD_DATE_TIME_PATTERN = "yyyyMMddHHmmss";
    public static String NO_YEAR_DATE_TIME_PATTERN = "MM-dd HH:mm";
    public static String GPS_PATTERN="yyMMddHHmmss";


    /**
     * Date转时间字符转
     * @param date
     * @return
     */
    public static String getTimeByDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 获取指定时间的指定格式
     * @return
     */
    public static String getFormatDate(long date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date(date));
    }

    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }

    /**
     * 获取明天日期
     * @return
     */
    public static String getTomorrowDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return String.valueOf(Integer.valueOf(df.format(new Date())) + 1);
    }

    /**
     * 获取当前日期字符串
     * @return
     */
    public static String getCurrentDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        return df.format(new Date());
    }

    /**
     * 获取当前年
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前日
     * @return
     */
    public static int getCurrentDay() {
        Calendar cal= Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    /**
     * 将时间戳转化为字符串
     * @param showTime
     * @return
     */
    public static String formatTime2String(long showTime) {
        return formatTime2String(showTime,false);
    }

    public static String formatTime2String(long showTime , boolean haveYear) {
            String str = "";
            long distance = System.currentTimeMillis()/1000 - showTime;
            if(distance < 300){
                str = "刚刚";
            }else if(distance >= 300 && distance < 600){
                str = "5分钟前";
            }else if(distance >= 600 && distance < 1200){
                str = "10分钟前";
            }else if(distance >= 1200 && distance < 1800){
                str = "20分钟前";
            }else if(distance >= 1800 && distance < 2700){
                str = "半小时前";
        }else if(distance >= 2700){
            Date date = new Date(showTime * 1000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            str = formatDateTime(sdf.format(date) , haveYear);
        }
        return str;

    }

    public static String formatDateTime(String time , boolean haveYear) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(time == null){
            return "";
        }
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        Calendar yesterday = Calendar.getInstance();
        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH)-1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);
        if(current.after(today)){
            return "今天 "+time.split(" ")[1];
        }else if(current.before(today) && current.after(yesterday)){
            return "昨天 "+time.split(" ")[1];
        }else{
            if(haveYear) {
                int index = time.indexOf(" ");
                return time.substring(0,index);
            }else {
                int yearIndex = time.indexOf("-")+1;
                int index = time.indexOf(" ");
                return time.substring(yearIndex,time.length()).substring(0,index);
            }
        }
    }
}
