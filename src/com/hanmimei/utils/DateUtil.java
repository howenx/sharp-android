package com.hanmimei.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

public class DateUtil {

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
