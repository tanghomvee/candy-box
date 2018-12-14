package com.slst.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/7/17.
 */
public class DateUtils {

    public static final String DATE_TIME_SEPARATOR = " ";
    public static final String TIME_POSTFIX = "00:00:00";



    public static class DateTimeFormatters{
        public static final DateTimeFormatter HH_MM = DateTimeFormat.forPattern("HH:mm");
        public static final DateTimeFormatter HH_MM_SS = DateTimeFormat.forPattern("HH:mm:ss");
        public static final DateTimeFormatter YYYY_MM = DateTimeFormat.forPattern("yyyy-MM");
        public static final DateTimeFormatter ZH_YYYY_MM = DateTimeFormat.forPattern("yyyy年MM月");
        public static final DateTimeFormatter YYYY_MM_DD = DateTimeFormat.forPattern("yyyy-MM-dd");
        public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        public static final DateTimeFormatter YYYY_SLASH_MM_SLASH_DD_HH_MM_SS = DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss");
        public static final DateTimeFormatter YYYYMMDD = DateTimeFormat.forPattern("yyyyMMdd");
        public static final DateTimeFormatter ZH_YYYY_MM_DD = DateTimeFormat.forPattern("yyyy年MM月dd日");
        public static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        public static final DateTimeFormatter YYYYMMDDHHMM = DateTimeFormat.forPattern("yyyyMMddHHmm");
    } ;
    
    /**
     * 日期转化字符串
     * @param date
     * @return
     */
    public static String date2Str(Date date) {
        return date2Str(date , DateUtils.DateTimeFormatters.YYYYMMDD);
    }
    public static String date2Str(Date date ,DateTimeFormatter dateTimeFormatter) {
        if(date == null){
            return null;
        }
        return new DateTime(date).toString(dateTimeFormatter);
    }

    /**
     * 日期时间转化字符串
     * @param dateTime
     * @return
     */
    public static String dateTime2Str(Date dateTime) {
        return dateTime2Str(dateTime , DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS);
    }
    public static String dateTime2Str(Date dateTime ,DateTimeFormatter dateTimeFormatter) {
        if(dateTime == null){
            return null;
        }
        return new DateTime(dateTime).toString(dateTimeFormatter);
    }


    /**
     * 字符串转化日期
     * @param dateStr
     * @return
     */

    public static Date str2Date(String dateStr) {
        return str2Date(dateStr , DateUtils.DateTimeFormatters.YYYYMMDD);
    }
    public static Date str2Date(String dateStr ,DateTimeFormatter dateTimeFormatter) {
        if(StringUtils.isEmpty(dateStr)){
            return null;
        }

        if(DateUtils.DateTimeFormatters.YYYY_MM_DD.equals(dateTimeFormatter)){

            dateStr = dateStr.trim().split(DATE_TIME_SEPARATOR)[0];
        }

        return dateTimeFormatter.parseDateTime(dateStr).toDate();
    }

    /**
     * 获取日期字符串
     * @param date 传入时间对象
     * @return 返回日期字符串
     */
    public static String getDateStr(Date date) {
        return date2Str(date , DateTimeFormatters.YYYY_MM_DD);
    }
    public static String getTimeStr(Date date){
        return date2Str(date, DateTimeFormatters.HH_MM_SS);
    }

    /**
     * 字符串转化日期时间
     * @param dateTimeStr
     * @return
     */
    public static Date str2DateTime(String dateTimeStr) {
        return str2DateTime(dateTimeStr , DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS);
    }
    public static Date str2DateTime(String dateTimeStr ,DateTimeFormatter dateTimeFormatter) {
        if(StringUtils.isEmpty(dateTimeStr)){
            return null;
        }
        if (DateUtils.DateTimeFormatters.YYYY_MM_DD_HH_MM_SS.equals(dateTimeFormatter)){
            String[] tmp = dateTimeStr.trim().split(DATE_TIME_SEPARATOR);
            if(tmp.length != 2){
                dateTimeStr = dateTimeStr + DATE_TIME_SEPARATOR + TIME_POSTFIX;
            }
        }
        return dateTimeFormatter.parseDateTime(dateTimeStr).toDate();
    }

    /**
     * 获取一周开始时间'YYYY-mm-dd'格式
     * @param endTime
     * @return
     */
    public static String getWeekStart(String endTime){
        Calendar ca=Calendar.getInstance();
        Date date=str2Date(endTime,DateTimeFormatters.YYYY_MM_DD);
        ca.setTime(date);
        int javaweek=ca.get(Calendar.DAY_OF_WEEK);
        int lifeweek=javaweek-1;
        if (lifeweek==0) {
            lifeweek=7;
        }
        Date startDate=addOrSubDate(date,-lifeweek+1);



        return date2Str(startDate,DateTimeFormatters.YYYY_MM_DD);
    }


    /**
     * 获取一月开始时间'YYYY-mm-dd'格式
     * @param endTime
     * @return
     */
    public static String getMonthStart(String endTime){
        endTime=endTime.substring(0,endTime.lastIndexOf("-"));
        StringBuilder sb=new StringBuilder(endTime);
        sb.append("-01");
        return sb.toString();
    }

    /**
     * get current month start time
     * @return
     */
    public static Date getMonthStartTime(Date date) {
        if (date == null){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return  dateTime.
                dayOfMonth().withMinimumValue().
                millisOfDay().withMinimumValue().
                toDate();
    }
    /**
     * get current month start time
     * @return
     */
    public static Date getDateStartTime(Date date) {
        if (date == null){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return  dateTime.
                millisOfDay().withMinimumValue().
                toDate();
    }

    /**
     * get current month start time
     * @return
     */
    public static Date getMonthEndTime(Date date){
        if (date == null){
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return  dateTime.
                dayOfMonth().withMaximumValue().
                millisOfDay().withMaximumValue().
                toDate();
    }

    public static boolean isSameDay(Date date, Date date1) {
        return differentDays(date ,date1) == 0;
    }

    /**
     * endDate比startDate多的天数
     * @param startDate
     * @param endDate
     * @return
     */
    public static int differentDays(Date startDate,Date endDate) {
        return new Period(new DateTime(endDate) , new DateTime(startDate)).getDays();
    }

    /**
     * 加上或减去一定天数得到一个新日期
     * @param date
     * @param addval 需要加或减的天数(减传复数)
     * @return Date
     */
    public static Date addOrSubDate(Date date,int addval) {
        Calendar ca=Calendar.getInstance();
        ca.setTime(date);
        ca.add(Calendar.DAY_OF_MONTH, addval);
        return ca.getTime();
    }

    /**
     * 获取一个月有多少天
     * @param date
     * @return
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int days=calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }






}
