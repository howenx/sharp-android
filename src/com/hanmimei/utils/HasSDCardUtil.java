package com.hanmimei.utils;

import android.os.Environment;
public class HasSDCardUtil {
	/**
	 * 判断是否存在SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
}
