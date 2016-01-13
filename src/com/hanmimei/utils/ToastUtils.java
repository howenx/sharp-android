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
<<<<<<< HEAD
	private static long oneTime = 0;
	private static long twoTime = 0;
	private static int duration = SuperToast.Duration.SHORT;
=======
	private static int duration = SuperToast.Duration.VERY_SHORT;
>>>>>>> 79a1e5a750fc8bd6fcd734288177ecd526a7e3d3

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
<<<<<<< HEAD
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
//			oneTime = System.currentTimeMillis();
		} else {
//			twoTime = System.currentTimeMillis();
			oldMsg = s;
		}
//		oneTime = twoTime;
=======
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
>>>>>>> 79a1e5a750fc8bd6fcd734288177ecd526a7e3d3
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
