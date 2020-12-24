package com.shop.manage.system.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 时间工具类
 * @author Mr.joey
 */
public class DateUtil {


    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_2 = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTime(String format){
        Date date = new Date();
        return dateToString(date,format);
    }

    /**
     * 获取指定格式时间字符串
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        SimpleDateFormat f=new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return f.format(date);
    }


    /**
     * 自定义指定格式，时间转换成字符串
     * @param date
     * @param format
     * @return
     */
    public static String dateToString(Date date,String format){
        SimpleDateFormat f=new SimpleDateFormat(format);
        return f.format(date);
    }

    /**
     * 获取当前月，第一天的时间
     * @return
     */
    public static Date getCurrentMonthFirstDay(){
        Calendar cal_1=Calendar.getInstance();
        cal_1.set(Calendar.DAY_OF_MONTH,1);
        cal_1.set(Calendar.HOUR_OF_DAY, 0);
        cal_1.set(Calendar.MINUTE, 0);
        cal_1.set(Calendar.SECOND, 0);
        return cal_1.getTime();
    }

    /**
     * 获取当前月，第一天的时间 字符串
     * @param format
     * @return
     */
    public static String getCurrentMonthFirstDay(String format){
        return dateToString(getCurrentMonthFirstDay(), format);
    }

    /**
     * 获取今日，第一时刻
     * @return
     */
    public static Date getCurrentDayFirstMoment(){
        Calendar cal_1=Calendar.getInstance();
        cal_1.set(Calendar.HOUR_OF_DAY, 0);
        cal_1.set(Calendar.MINUTE, 0);
        cal_1.set(Calendar.SECOND, 0);
        return cal_1.getTime();
    }

    /**
     * 获取今日，第一时刻 字符串
     * @param format
     * @return
     */
    public static String getCurrentDayFirstMoment(String format){
        return dateToString(getCurrentDayFirstMoment(), format);
    }

    /**
     * 获取当前小时，第一时刻
     * @return
     */
    public static Date getCurrentHourFirstMoment(){
        Calendar cal_1=Calendar.getInstance();
        cal_1.set(Calendar.MINUTE, 0);
        cal_1.set(Calendar.SECOND, 0);
        return cal_1.getTime();
    }

    /**
     *获取当前小时，第一时刻 字符串
     * @param format
     * @return
     */
    public static String getCurrentHourFirstMoment(String format){
        return dateToString(getCurrentHourFirstMoment(), format);
    }


    /**
     * 获取最后一个月，第一天
     * @param nowDate
     * @return
     */
    public static Date getLastMonthFirstDay(String nowDate){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);
        cal_1.set(Calendar.HOUR_OF_DAY, 0);
        cal_1.set(Calendar.MINUTE, 0);
        cal_1.set(Calendar.SECOND, 0);
        return cal_1.getTime();
    }

    public static String getLastMonthFirstDay(String nowDate, String format){
        return dateToString(getLastMonthFirstDay(nowDate), format);
    }

    public static String getLastMonthFinalDay(String nowDate, String format){
        return dateToString(getLastMonthFinalDay(nowDate), format);
    }

    public static Date getLastMonthFinalDay(String nowDate){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH, cal_1.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal_1.set(Calendar.HOUR_OF_DAY, 23);
        cal_1.set(Calendar.MINUTE, 59);
        cal_1.set(Calendar.SECOND, 59);
        return cal_1.getTime();
    }

    public static String getLastDayFinalMoment(String nowDate,String format){
        return dateToString(getLastDayFinalMoment(nowDate), format);
    }

    public static String getLastDayFirstMoment(String nowDate, String format){
        return dateToString(getLastDayFirstMoment(nowDate), format);
}

    public static Date getLastDayFinalMoment(String nowDate){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.DATE, -1);
        cal_1.set(Calendar.HOUR_OF_DAY, 23);
        cal_1.set(Calendar.MINUTE, 59);
        cal_1.set(Calendar.SECOND, 59);
        return cal_1.getTime();
    }

    public static Date getLastDayFirstMoment(String nowDate){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.DATE, -1);
        cal_1.set(Calendar.HOUR_OF_DAY, 0);
        cal_1.set(Calendar.MINUTE, 0);
        cal_1.set(Calendar.SECOND, 0);
        return cal_1.getTime();
    }

    public static String getLastHourFinalMoment(String nowDate, String format){
        return dateToString(getLastHourFinalMoment(nowDate), format);
    }

    public static String getLastHourFirstMoment(String nowDate, String format){
        return dateToString(getLastHourFirstMoment(nowDate), format);
    }

    public static Date getLastHourFinalMoment(String nowDate){
        return getHourFinalMoment(nowDate, -2);
    }

    public static Date getLastHourFirstMoment(String nowDate){
        return getHourFirstMoment(nowDate, -2);
    }

    public static String getHourFinalMoment(String nowDate, String format, int amount){
        return dateToString(getHourFinalMoment(nowDate, amount), format);
    }

    public static String getHourFirstMoment(String nowDate, String format, int amount){
        return dateToString(getHourFirstMoment(nowDate, amount), format);
    }

    public static Date getHourFinalMoment(String nowDate, int amount){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.HOUR, amount);
        cal_1.set(Calendar.MINUTE, 59);
        cal_1.set(Calendar.SECOND, 59);
        return cal_1.getTime();
    }

    public static Date getHourFirstMoment(String nowDate, int amount){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.HOUR, amount);
        cal_1.set(Calendar.MINUTE, 0);
        cal_1.set(Calendar.SECOND, 0);
        return cal_1.getTime();
    }

    /**
     * 获取当前时间N天后的时间
     * @param nowDate
     * @param amount
     * @return
     */
    public static Date getDayBefore(String nowDate, Integer amount){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        Date createDate = null;
        try {
            createDate = sdf.parse(nowDate);
        } catch (Exception e){

        }
        Calendar cal_1 = Calendar.getInstance();
        cal_1.setTime(createDate);
        cal_1.add(Calendar.DAY_OF_MONTH, amount);
        return cal_1.getTime();
    }
    public static String getDayBefore(String nowDate, Integer amount, String format){
        return dateToString(getDayBefore(nowDate, amount), format);
    }

    public static Date stringToDate(String date,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date createDate = null;
        try {
            createDate = sdf.parse(date);
        } catch (Exception e){

        }
        return createDate;
    }

    public static Date stringToDate(String date){
        return stringToDate(date, YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取两个日期之间的间隔
     * @param startTime
     * @param endTime
     * @param timeType  所需的时间类型 0-月 1-日 2-时
     * @param timeSpace  所需的时间间隔
     * @return
     */
    public static List getDateBetweenList(Date startTime, Date endTime, String timeType, int timeSpace){
        List dateList = new ArrayList();
        if (startTime == null || endTime == null){
            return dateList;
        }
        int monthSpace = 0;
        int daySpace = 0;
        int hourSpace = 0;
        if ("0".equals(timeType)){
            monthSpace = timeSpace;
        } else if ("1".equals(timeType)){
            daySpace = timeSpace;
        } else if ("2".equals(timeType)){
            hourSpace = timeSpace;
        }
        Date nowTime = startTime;
        while (nowTime.before(endTime)){
            dateList.add(dateToString(nowTime));
            Calendar cal_1 = Calendar.getInstance();
            cal_1.setTime(nowTime);
            cal_1.add(Calendar.MONTH, monthSpace);
            cal_1.add(Calendar.DAY_OF_MONTH, daySpace);
            cal_1.add(Calendar.HOUR_OF_DAY, hourSpace);
            nowTime = cal_1.getTime();
        }
        dateList.add(dateToString(endTime));
        return dateList;
    }

    public static List getDateBetweenList(String startTime, String endTime, String timeType, int timeSpace){
        return getDateBetweenList(stringToDate(startTime), stringToDate(endTime), timeType, timeSpace);
    }

    public static List getDateBetweenList(String startTime, String endTime, String timeType, int timeSpace, String format){
        return getDateBetweenList(stringToDate(startTime, format), stringToDate(endTime, format), timeType, timeSpace);
    }

    public static Long dateCompare(String time1, String time2){
        Date date1, date2;
        try {
            date1 = stringToDate(time1);
            date2 = stringToDate(time2);
        } catch (Exception e){
            date1 = null;
            date2 = null;
        }
        if (date1 == null || date2 == null){
            return null;
        }

        return date1.getTime() - date2.getTime();
    }

    public static String getCurrentDayFinalMoment(String format){
        return dateToString(getCurrentDayFinalMoment(), format);
    }

    public static Date getCurrentDayFinalMoment(){
        Calendar cal_1=Calendar.getInstance();
        cal_1.set(Calendar.HOUR_OF_DAY, 23);
        cal_1.set(Calendar.MINUTE, 59);
        cal_1.set(Calendar.SECOND, 59);
        return cal_1.getTime();
    }

    public static void main(String[] args) throws Exception {
        System.out.println(dateCompare("2019/11/22 15:00:00", "2019/11/22 10:00:00"));
    }
}
