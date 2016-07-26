package com.kakao.kakaogift.utils;

import android.content.Context;

import com.github.johnpersano.supertoasts.SuperToast;

public class ToastUtils {

	public ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static String oldMsg;
	static SuperToast toast = null;
	private static int duration =500;


	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param s
	 */
	public static void Toast(Context context, final String s) {
		if (toast == null) {
			toast = new SuperToast(context);
			toast.setDuration(duration);
			toast.setBackground(SuperToast.Background.RED);
			oldMsg = s;
			toast.setText(s);
			toast.show();
		} else {
			if(toast.isShowing()){
				if(!oldMsg.equals(s)){
					toast.setText(s);
					toast.show();
				}
				oldMsg = s;
			}else{
				toast.setText(s);
				toast.show();
			}
		}
	}

	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param s
	 */
	public static void Toast(Context context, int resId) {
		Toast(context, context.getString(resId));
	}

}
