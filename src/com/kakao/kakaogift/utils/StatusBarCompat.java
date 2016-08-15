package com.kakao.kakaogift.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.kakao.kakaogift.R;

/**
 * Created by zhy on 15/9/21.
 */
public class StatusBarCompat {
	private static final int INVALID_VAL = -1;
	public static final int COLOR_DEFAULT = Color.parseColor("#43000000");

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static void compat(Activity activity, int statusColor) {
		if(StatusBarLightMode(activity) !=0)
			return;
		
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//			activity.getWindow().getDecorView().setSystemUiVisibility(
//		      View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//			activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//		 }

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (statusColor != INVALID_VAL) {
				activity.getWindow().setStatusBarColor(statusColor);
			}
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			int color = COLOR_DEFAULT;
			ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
			if (statusColor != INVALID_VAL) {
				color = statusColor;
			}
			View statusBarView = contentView.getChildAt(0);
			// 改变颜色时避免重复添加statusBarView
			if (statusBarView != null
					&& statusBarView.getMeasuredHeight() == getStatusBarHeight(activity)) {
				statusBarView.setBackgroundColor(color);
				return;
			}
			statusBarView = new View(activity);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(activity));
			statusBarView.setBackgroundColor(color);
			contentView.addView(statusBarView, lp);
		}

	}

	public static void compat(Activity activity) {
		compat(activity, INVALID_VAL);
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
	
	/**
	 * 设置状态栏黑色字体图标， 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
	 * 
	 * @param activity
	 * @return 1:MIUUI 2:Flyme 3:android6.0
	 */
	@TargetApi(16)
	public static int StatusBarLightMode(Activity activity) {
		int result = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (MIUISetStatusBarLightMode(activity.getWindow(), true)) {
				result = 1;
			} else if (FlymeSetStatusBarLightMode(activity.getWindow(), true)) {
				result = 2;
			}
		}
		return result;
	}

	/**
	 * 已知系统类型时，设置状态栏黑色字体图标。 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
	 * 
	 * @param activity
	 * @param type
	 *            1:MIUUI 2:Flyme 3:android6.0
	 */
	public static void StatusBarLightMode(Activity activity, int type) {
		if (type == 1) {
			MIUISetStatusBarLightMode(activity.getWindow(), true);
		} else if (type == 2) {
			FlymeSetStatusBarLightMode(activity.getWindow(), true);
		} 

	}

	/**
	 * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
	 */
	public static void StatusBarDarkMode(Activity activity, int type) {
		if (type == 1) {
			MIUISetStatusBarLightMode(activity.getWindow(), false);
		} else if (type == 2) {
			FlymeSetStatusBarLightMode(activity.getWindow(), false);
		} 
	}

	/**
	 * 设置状态栏图标为深色和魅族特定的文字风格 可以用来判断是否为Flyme用户
	 * 
	 * @param window
	 *            需要设置的窗口
	 * @param dark
	 *            是否把状态栏字体及图标颜色设置为深色
	 * @return boolean 成功执行返回true
	 * 
	 */
	public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
		boolean result = false;
		if (window != null) {
			try {
				WindowManager.LayoutParams lp = window.getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class
						.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class
						.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark) {
					value |= bit;
				} else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				window.setAttributes(lp);
				result = true;
			} catch (Exception e) {

			}
		}
		return result;
	}

	/**
	 * 设置状态栏字体图标为深色，需要MIUIV6以上
	 * 
	 * @param window
	 *            需要设置的窗口
	 * @param dark
	 *            是否把状态栏字体及图标颜色设置为深色
	 * @return boolean 成功执行返回true
	 * 
	 */
	public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
		boolean result = false;
		if (window != null) {
			Class<? extends Window> clazz = window.getClass();
			try {
				int darkModeFlag = 0;
				Class<?> layoutParams = Class
						.forName("android.view.MiuiWindowManager$LayoutParams");
				Field field = layoutParams
						.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
				darkModeFlag = field.getInt(layoutParams);
				Method extraFlagField = clazz.getMethod("setExtraFlags",int.class, int.class);
				if (dark) {
					extraFlagField.invoke(window, darkModeFlag, darkModeFlag);// 状态栏透明且黑色字体
				} else {
					extraFlagField.invoke(window, 0, darkModeFlag);// 清除黑色字体
				}
				result = true;
			} catch (Exception e) {

			}
		}
		return result;
	}

}
