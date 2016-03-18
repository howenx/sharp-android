package com.hanmimei.utils;

import android.app.Activity;
import android.content.Context;

import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.SuperToast.Animations;

public class ToastUtils {

	public ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static String oldMsg;
//	protected static SuperToast toast = null;
	static SuperActivityToast toast = null;
	private static int duration = SuperToast.Duration.VERY_SHORT;

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
			toast = new SuperActivityToast((Activity) context);
			toast.setAnimations(Animations.POPUP);
			toast.setTextSize(SuperToast.TextSize.SMALL);
			toast.setBackground(SuperToast.Background.RED);
//			toast = new SuperToast(context);
//			toast.setAnimations(SuperToast.Animations.POPUP);
//			toast.setDuration(duration);
//			toast.setBackground(SuperToast.Background.BLACK);
			oldMsg = s;
			toast.setText(s);
			toast.setTextSize(12);
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
