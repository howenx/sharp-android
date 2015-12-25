package com.hanmimei.utils;

import com.hanmimei.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtils {

	public ToastUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	public static void Toast(Context context) {
				Toast toast = new Toast(context);
				toast.setDuration(Toast.LENGTH_SHORT);
				View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_to_shopcart_success_panel, null);
				toast.setView(view);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
	}

	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param content
	 */
	public static void Toast(Context context, int content) {

		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		TextView view = new TextView(context);
		view.setPadding(CommonUtil.dip2px(20),  CommonUtil.dip2px(5)
				, CommonUtil.dip2px(20),  CommonUtil.dip2px(5));
		view.setBackgroundResource(R.drawable.bg_toast);
		view.setTextColor(Color.WHITE);
		view.setTextSize(13);
		view.setText(content);
		toast.setView(view);
		toast.show();
	}

	/**
	 * toast 工具类 自定义toast
	 * 
	 * @param context
	 * @param content
	 */
	public static void Toast(Context context, String content) {

		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		TextView view = new TextView(context);
		view.setPadding(CommonUtil.dip2px(10), 0
				, CommonUtil.dip2px(10),  0);
		view.setBackgroundResource(R.drawable.bg_toast);
		view.setTextColor(Color.WHITE);
		view.setTextSize(12);
		view.setText(content);
		toast.setView(view);
		toast.show();
	}
}
