package com.hanmimei.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.view.CustomDialog;
import com.hanmimei.view.UpdateDialog;
/**
 * 弹窗工具类
 * @author vince
 *
 */
public class AlertDialogUtils {
	
	public static void MyDialog(Context mContext, String title, String left, String right){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View view = inflater.inflate(R.layout.dialog_layout, null);
		dialog.setView(view);
		dialog.show();
	}
	
	
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showBackDialog(Context context,
			final OnClickListener l) {
		String[] tb = { "便宜不等人，请三思而行","", "我再想想", "去意已决" };
		showCustomDialog(context, tb, l);
	}
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showPayDialog(Context context,
			final OnClickListener l) {
		String[] tb = { "确认要离开收银台","若未在24小时内完成在线支付，逾期订单作废。", "继续支付", "确定离开" };
		showCustomDialog(context, tb, l);
	}
	/**
	 * 版本更新
	 * @param context
	 * @param l
	 */
	public static void showUpdateDialog(Context context,VersionVo info,
			final OnClickListener l) {
		String[] tb = { info.getReleaseDesc(), "暂不更新", "马上下载" };
		UpdateDialog c = new UpdateDialog(context, tb,l);
		c.show();
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
