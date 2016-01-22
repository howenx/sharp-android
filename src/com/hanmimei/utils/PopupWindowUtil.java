package com.hanmimei.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;

public class PopupWindowUtil {
	/**
	 * 显示并得到 popwindow
	 * 
	 * @param contentView
	 */

	public static PopupWindow showPopWindow(final Context context, View contentView) {
		// 创建一个popupwindow
		PopupWindow popWindow = new PopupWindow(contentView,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		popWindow.setAnimationStyle(R.style.popwindow_anim_style);
		// 获取光标
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(true);
		// 设置弹出屏幕后背景色
		backgroundAlpha(context, 0.4f);
		// 设置一个默认的popupwindow背景色 实现 点击空白区 关闭popupwindow
		popWindow.setBackgroundDrawable(new ColorDrawable());
		popWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
		popWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				backgroundAlpha(context, 1f);
			}
		});
		return popWindow;
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * 
	 * @param bgAlpha
	 */
	private static void backgroundAlpha(Context context, float bgAlpha) {
		BaseActivity activity = (BaseActivity) context;
		
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		activity.getWindow().setAttributes(lp);
	}
	
	

}
