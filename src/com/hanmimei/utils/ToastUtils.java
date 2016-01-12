package com.hanmimei.utils;

import android.content.Context;
import android.view.View;

import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.SuperToast.OnDismissListener;

public class ToastUtils {

	public ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static String oldMsg;
	protected static SuperToast toast = null;
	private static int duration = SuperToast.Duration.VERY_SHORT;

	public static void cancel() {
		if (toast == null)
			return;
		toast.cancelAllSuperToasts();
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
			toast.setAnimations(SuperToast.Animations.FLYIN);
			toast.setDuration(duration);
			toast.setBackground(SuperToast.Background.BLACK);
			toast.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(View view) {
					if(toast == null)
						return;
					if (!s.equals(oldMsg)) {
						oldMsg = s;
						toast.setText(s);
						toast.show();
					}
				}
			});
			toast.setText(s);
			toast.setTextSize(12);
			toast.show();
		} else {
			oldMsg = s;
			if(toast.isShowing())
				return;
			toast.setText(s);
			toast.show();	
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
