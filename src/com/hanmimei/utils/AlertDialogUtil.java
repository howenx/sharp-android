package com.hanmimei.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.view.CustomDialog;


/**
 * popupwindow 弹窗工具类
 * 
 * @author 刘奇
 *
 */

public class AlertDialogUtil {

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showPayDialog(Context context,
			final OnClickListener l) {
		String[] tb = { "确认要离开收银台","下单后24小时订单将被取消，请尽快完成支付", "继续支付", "确定离开" };
		showCustomDialog(context, tb, l);
	}


	/**
	 * 
	 * @param context
	 * @param tb tb[0] 标题 tb[1] 提示内容 tb[2] 取消按钮文本 tb[3] 确定按钮文本
	 * @param l
	 */
	@SuppressLint("InflateParams")
	public static void showCustomDialog(Context context, String[] tb,
			final OnClickListener l) {
		CustomDialog c = new CustomDialog(context, tb,l);
		c.show();
	}

}