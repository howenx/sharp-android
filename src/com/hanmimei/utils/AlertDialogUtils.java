package com.hanmimei.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.utils.effects.BaseEffects;
import com.hanmimei.view.CustomDialog;
import com.hanmimei.view.UpdateDialog;
import com.hanmimei.view.UpdateNewDialog;

/**
 * 弹窗工具类
 * 
 * @author vince
 * 
 */
public class AlertDialogUtils {
	
	public interface OnPhotoSelListener{
		public void onSelPlay();
		public void onSelLocal();
	}
	
	
	public static AlertDialog showPhotoDialog(Context mContext,final OnPhotoSelListener l){
		final AlertDialog photoDialog = new AlertDialog.Builder(mContext).create();
		View view = LayoutInflater.from(mContext).inflate(R.layout.select_img_pop_layout,null);
		// 拍照
		view.findViewById(R.id.play_photo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				photoDialog.dismiss();
				if(l!=null)
					l.onSelPlay();
			}
		});
		// 本地照片
		view.findViewById(R.id.my_photo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				photoDialog.dismiss();
				if(l!=null)
					l.onSelLocal();
			}
		});
		// 取消
		view.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						photoDialog.dismiss();
					}
				});
		photoDialog.setView(view);
		return photoDialog;
	}

	
	/**
	 *  行邮税提示框
	 * @param mContext
	 * @param curItemPrice	 当前商品价格
	 * @param curPostalTaxRate	税率
	 * @param postalStandard		收费标准
	 * @return
	 */
	public static AlertDialog showPostDialog(Context mContext, double curItemPrice,double curPostalTaxRate,int postalStandard ) {
		final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
		View view = LayoutInflater.from(mContext).inflate(R.layout.panel_portalfee, null);
		TextView num_portalfee = (TextView) view.findViewById(R.id.num_portalfee);
		TextView prompt = (TextView) view.findViewById(R.id.prompt);
		Double postalFee = curPostalTaxRate * curItemPrice / 100;

		prompt.setText(mContext.getResources().getString(R.string.portalfee_biaozhun,
				curPostalTaxRate, postalStandard));

		if (postalFee <= 50) {
			num_portalfee.setText(mContext.getResources().getString(R.string.price,
					CommonUtils.DecimalFormat(postalFee)));
			num_portalfee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			num_portalfee.setText(mContext.getResources().getString(R.string.price,
					CommonUtils.DecimalFormat(postalFee)));
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
	
	
	/**
	 * 删除操作提示窗
	 * @param mContext	
	 * @param l
	 * @return
	 */
	public static AlertDialog showDialog(Context mContext, OnClickListener l) {
		return showDialog(mContext, l, "确定删除？", "取消", "确定");
	}
	/**
	 * 取消订单
	 * @param mContext	
	 * @param l
	 * @return
	 */
	public static AlertDialog showCancelDialog(Context mContext, OnClickListener l) {
		return showDialog(mContext, l, "确定取消？", "取消", "确定");
	}
	/**
	 * 操作提示窗
	 * @param mContext
	 * @param l	右边按钮回调
	 * @param top 顶部标题
	 * @param left 左边按钮
	 * @param right 右边按钮
	 * @return
	 */
	public static AlertDialog showDialog(Context mContext, OnClickListener l,
			String top, String left, String right) {
		String[] tb = { top, "", left, right};
		return showCustomDialog(mContext, tb, l, null);
	}
	

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showAddressDialog(Context context, final OnClickListener l) {
		String[] tb = { "请添加收货地址", "", "取消", "确定" };
		showCustomDialog(context, tb, l,null);
	}
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showDeliveryDialog(Context context, final OnClickListener l) {
		String[] tb = { "确定已经收到货物", "", "尚未收到", "确认收货" };
		showCustomDialog(context, tb, l,null);
	}
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showBackDialog(Context context, final OnClickListener l) {
		String[] tb = { "便宜不等人，请三思而行", "", "我再想想", "去意已决" };
		showCustomDialog(context, tb, l,null);
	}

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showPayDialog(Context context, final OnClickListener l) {
		String[] tb = { "确认要离开收银台", "若未在24小时内完成在线支付，逾期订单作废。", "继续支付", "确定离开" };
		showCustomDialog(context, tb, l,null);
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
	 * 版本更新2
	 * 
	 * @param context
	 * @param l
	 */
	public static void showUpdate2Dialog(Context context, final OnClickListener l) {
		UpdateNewDialog dialog = new UpdateNewDialog(context, l);
		dialog.show();
	}

	/**
	 * 
	 * @param context
	 * @param tb
	 *            tb[0] 标题 tb[1] 提示内容 tb[2] 取消按钮文本 tb[3] 确定按钮文本
	 * @param l
	 */
	@SuppressLint("InflateParams")
	public static CustomDialog showCustomDialog(Context context, String[] tb,
			final OnClickListener l,BaseEffects baseEffects) {
		CustomDialog c = new CustomDialog(context, tb, l).withBaseEffect(baseEffects);
		c.show();
		return c;
	}

}
