package com.hanmimei.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.view.CustomDialog;
import com.hanmimei.view.UpdateDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * 弹窗工具类
 * 
 * @author vince
 * 
 */
public class AlertDialogUtils {
	
	
	public static AlertDialog showPostDialog(Context mContext, double curItemPrice,int curPostalTaxRate,int postalStandard ) {
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View view = LayoutInflater.from(mContext).inflate(R.layout.panel_portalfee, null);
		TextView num_portalfee = (TextView) view.findViewById(R.id.num_portalfee);
		TextView prompt = (TextView) view.findViewById(R.id.prompt);
		Double postalFee = curPostalTaxRate * curItemPrice / 100;

		prompt.setText(mContext.getResources().getString(R.string.portalfee_biaozhun,
				curPostalTaxRate, postalStandard));

		if (postalFee <= 50) {
			num_portalfee.setText(mContext.getResources().getString(R.string.price,
					CommonUtil.DecimalFormat(postalFee)));
			num_portalfee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			postalFee = curItemPrice * 100 / (100 - curPostalTaxRate)
					- curItemPrice;
			num_portalfee.setText(mContext.getResources().getString(R.string.price,
					CommonUtil.DecimalFormat(postalFee)));
		}

		view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setView(view);
		dialog.show();
		return dialog;
	}
	
	

	public static AlertDialog showDialog(Context mContext, OnClickListener l) {
		return showDialog(mContext, l, null, null, null);
	}

	public static AlertDialog showDialog(Context mContext, OnClickListener l,
			String top, String left, String right) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View view = inflater.inflate(R.layout.dialog_layout, null);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView cancel = (TextView) view.findViewById(R.id.cancel);
		TextView confirm = (TextView) view.findViewById(R.id.confirm);
		if (top != null)
			title.setText(top);
		if (left != null)
			cancel.setText(left);
		if (right != null)
			confirm.setText(right);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		confirm.setOnClickListener(l);
		dialog.setView(view);
		dialog.show();
		return dialog;
	}
	

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showBackDialog(Context context, final OnClickListener l) {
		String[] tb = { "便宜不等人，请三思而行", "", "我再想想", "去意已决" };
		showCustomDialog(context, tb, l);
	}

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showPayDialog(Context context, final OnClickListener l) {
		String[] tb = { "确认要离开收银台", "若未在24小时内完成在线支付，逾期订单作废。", "继续支付", "确定离开" };
		showCustomDialog(context, tb, l);
	}

	/**
	 * 版本更新
	 * 
	 * @param context
	 * @param l
	 */
	public static void showUpdateDialog(Context context, VersionVo info,
			final OnClickListener l) {
		String[] tb = { info.getReleaseDesc(), "暂不更新", "马上下载" };
		UpdateDialog c = new UpdateDialog(context, tb, l);
		c.show();
	}

	/**
	 * 
	 * @param context
	 * @param tb
	 *            tb[0] 标题 tb[1] 提示内容 tb[2] 取消按钮文本 tb[3] 确定按钮文本
	 * @param l
	 */
	@SuppressLint("InflateParams")
	public static void showCustomDialog(Context context, String[] tb,
			final OnClickListener l) {
		CustomDialog c = new CustomDialog(context, tb, l);
		c.show();
	}

}
