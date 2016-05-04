
package com.hanmimei.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;


public class DateUtils {

    private static Date preRefreshDateTime;

    public static boolean toRefresh() {
        Date curdate = new Date();
        if (null == preRefreshDateTime) {
            preRefreshDateTime = curdate;
            return true;
        }

        long timediff = (curdate.getTime() - preRefreshDateTime.getTime());
        preRefreshDateTime = curdate;
        return (timediff >= 60 * 60 * 1000);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date getDate(Long dateLong) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(dateLong);
        Date date;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public static boolean needDisplayTime(Date predate, Date curdate) {
        if (predate == null || curdate == null) {
            return true;
        }

        long timediff = (curdate.getTime() - predate.getTime());
        return (timediff >= 5 * 60 * 1000);
    }

    public static String getTimeDiffDesc(Date date) {

        if (date == null) {
            return null;
        }

        String strDesc = null;
        Calendar curCalendar = Calendar.getInstance();
        Date curDate = new Date();
        curCalendar.setTime(curDate);
        Calendar thenCalendar = Calendar.getInstance();
        thenCalendar.setTime(date);

        int w = thenCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance(); // 今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天
        yesterday.setTime(curDate);
        yesterday.add(Calendar.DATE, -1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        Calendar tomorrow = Calendar.getInstance(); // 明天
        tomorrow.setTime(curDate);
        tomorrow.add(Calendar.DATE, +1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);

        Calendar tomorrowNext = Calendar.getInstance(); // 明天
        tomorrowNext.setTime(curDate);
        tomorrowNext.add(Calendar.DATE, +2);
        tomorrowNext.set(Calendar.HOUR_OF_DAY, 0);
        tomorrowNext.set(Calendar.MINUTE, 0);
        tomorrowNext.set(Calendar.SECOND, 0);

        int thenMonth = thenCalendar.get(Calendar.MONTH);
        int thenDay = thenCalendar.get(Calendar.DAY_OF_MONTH);
        int h = thenCalendar.get(Calendar.HOUR_OF_DAY);
        int m = thenCalendar.get(Calendar.MINUTE);
        String sh = "", sm = "";
        if (h < 10)
            sh = "0";

        if (m < 10)
            sm = "0";
        if (thenCalendar.after(today) && thenCalendar.before(tomorrow))// today
        {
            if (h < 6) {
                strDesc = "凌晨 " + sh + h + " : " + sm + m;
            } else if (h < 12) {
                strDesc = "上午 " + sh + h + " : " + sm + m;
            } else if (h < 13) {
                strDesc = "下午 " + h + " : " + sm + m;
            } else if (h < 19) {
                strDesc = "下午 " + (h - 12) + " : " + sm + m;
            } else {
                strDesc = "晚上 " + (h - 12) + " : " + sm + m;
            }
        } else if (thenCalendar.before(today) && thenCalendar.after(yesterday)) {// yestoday
            // System.out.println("yestoday");
            if (h < 6) {
                strDesc = "昨天凌晨 " + sh + h + " : " + sm + m;
            } else if (h < 12) {
                strDesc = "昨天上午 " + sh + h + " : " + sm + m;
            } else if (h < 13) {
                strDesc = "昨天下午 " + h + " : " + sm + m;
            } else if (h < 19) {
                strDesc = "昨天下午 " + (h - 12) + " : " + sm + m;
            } else {
                strDesc = "昨天晚上 " + (h - 12) + " : " + sm + m;
            }
        } else if (thenCalendar.before(tomorrowNext)
                && thenCalendar.after(tomorrow)) {// 2 ~ 7days ago
            // System.out.println("2~7");
            if (h < 6) {
                strDesc =  "明天凌晨 " + sh + h + " : " + sm + m;
            } else if (h < 12) {
                strDesc = "明天上午 " + sh + h + " : " + sm + m;
            } else if (h < 13) {
                strDesc =  "明天下午 " + h + " : " + sm + m;
            } else if (h < 19) {
                strDesc = "明天下午 " + (h - 12) + " : " + sm + m;
            } else {
                strDesc =  "明天晚上 " + (h - 12) + " : " + sm + m;
            }
        } else {
           
            if (h < 6) {
                strDesc = (thenMonth + 1) + "月" + thenDay + "日" + "凌晨 " + sh
                        + h + " : " + sm + m;
            } else if (h < 12) {
                strDesc = (thenMonth + 1) + "月" + thenDay + "日" + "上午 " + sh
                        + h + " : " + sm + m;
            } else if (h < 13) {
                strDesc = (thenMonth + 1) + "月" + thenDay + "日" + "下午 " + h
                        + " : " + sm + m;
            } else if (h < 19) {
                strDesc = (thenMonth + 1) + "月" + thenDay + "日" + "下午 "
                        + (h - 12) + " : " + sm + m;
            } else {
                strDesc = (thenMonth + 1) + "月" + thenDay + "日" + "晚上 "
                        + (h - 12) + " : " + sm + m;
            }
        }
        return strDesc;
    }


    public static String getTime4TimeTitle(Date date) {
        // M:月 d:天 a:上午或下午 h:12小时制的小时 m:分钟
        SimpleDateFormat format = new SimpleDateFormat("MM-dd a hh:mm",
                Locale.CHINA);
        return format.format(date);
    }

    public static int getCurTimeStamp() {

        return (int) (System.currentTimeMillis() / 1000);

    }
    
    @SuppressLint("SimpleDateFormat") 
    public static String sortDate(long date){
		String formatStr="yyyyMMddHHmmss";
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}

    @SuppressLint("SimpleDateFormat") 
    public static String sortDate(Date date){
		String formatStr="yyyyMMddHHmmss";
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(date);
	}
    
    @SuppressLint("SimpleDateFormat")
	public static String getCurrentDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date());
		return date;

	}

	@SuppressLint("SimpleDateFormat")
	public static String getStringDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = format.format(new Date());
		return date;
	}

	public static String turnToDate(int s) {
		int days = s / 60 / 60 / 24;
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, days);
		d = ca.getTime();
		String backTime = format.format(d);
		return backTime;
	}
	public static int getDate(String eDate){
		
		try {
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date end  = format.parse(eDate);
			Calendar cu = Calendar.getInstance();
			cu.setTime(date);
			Calendar ed = Calendar.getInstance();
			ed.setTime(end);
			long l=ed.getTimeInMillis()-cu.getTimeInMillis();
			int days=new Long(l/(1000*60*60)).intValue();
			return days;
		} catch (ParseException e) {	
			e.printStackTrace();
			return 0;
		}
	}
    
}
