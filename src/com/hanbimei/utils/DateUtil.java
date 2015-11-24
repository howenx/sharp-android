package com.hanbimei.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss");
		String date = format.format(new Date());
		return date;
		
	}
	@SuppressLint("SimpleDateFormat")
	public static String getStringDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = format.format(new Date());
		return date;
	}
}
