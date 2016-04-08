package com.hanmimei.utils;

import android.app.Activity;
import android.content.Context;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

public class ToastUtils {

	public ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static String oldMsg;
//	protected static SuperToast toast = null;
	static SuperToast toast = null;
	private static int duration =500;

	public static void cancel() {
		if (toast == null)
			return;
		SuperActivityToast.cancelAllSuperActivityToasts();
		toast = null;
	}

	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param s
	 */
	public static void Toast(Context context, final String s) {
		if (toast == null) {
			toast = new SuperToast(context);
			toast.setAnimations(SuperToast.Animations.POPUP);
			toast.setDuration(duration);
			toast.setBackground(SuperToast.Background.RED);
			oldMsg = s;
			toast.setText(s);
			toast.setTextSize(SuperToast.TextSize.SMALL);
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
	
	public static void CardToast(Context context,String s){
		SuperCardToast cardToast = new SuperCardToast((Activity) context);
		cardToast.setBackground(SuperToast.Background.RED);
		cardToast.setTextSize(SuperToast.TextSize.SMALL);
		cardToast.setText(s);
		cardToast.show();
	}

}
