package com.kakao.kakaogift.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomOutExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.view.UpdateDialog;

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
	 * 删除操作提示窗
	 * @param mContext	
	 * @param l
	 * @return
	 */
	public static NormalDialog showDialog(Context mContext, OnClickListener l) {
		return showDialog(mContext, l, null,"确定删除？", "取消", "确定");
	}
	/**
	 * 取消订单
	 * @param mContext	
	 * @param l
	 * @return
	 */
	public static NormalDialog showCancelDialog(Context mContext, OnClickListener l) {
		return showDialog(mContext, l,null, "确定取消？", "取消", "确定");
	}
	
	

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showAddressDialog(Context context, final OnClickListener l) {
		showDialog(context, l,null, "请添加收货地址", "取消", "确定");
	}
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showPayDialog(Context context, final OnClickListener l,final OnClickListener dl) {
		showDialog(context, l,null, "支付失败", "放弃支付", "重新支付");
	}
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showDeliveryDialog(Context context, final OnClickListener l) {
		showDialog(context, l,null, "请确认已经收到货物", "尚未收到", "确认收货");
	}
	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showBackDialog(Context context, final OnClickListener l) {
		showDialog(context, l,null, "便宜不等人，请三思而行", "我再想想", "去意已决");
	}

	/**
	 * 
	 * @param context
	 * @param l
	 */
	public static void showPayDialog(Context context, final OnClickListener l) {
		showDialog(context, l,"确认要离开收银台", "若未在24小时内完成在线支付，逾期订单作废。", "继续支付", "确定离开");
	}
	
	public static NormalDialog showDialog(Context mContext,final OnClickListener l,String content, String left, String right) {
		return showDialog(mContext, l, null, content, left, right);
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
	public static NormalDialog showDialog(Context mContext,final OnClickListener l,
			String title,String content, String left, String right) {
		String[] btns = {  left, right };
		float[] btnsSize = {  14, 14 };
		int[] btnsColor = {  mContext.getResources().getColor(R.color.red), mContext.getResources().getColor(R.color.red) };
		final NormalDialog dialog = new NormalDialog(mContext);
		if(title == null){
			dialog.isTitleShow(false);
		}else{
			dialog.title(title).titleTextSize(16);
		}
		if(content !=null){
			dialog.content(content).contentTextSize(14);
		}
        dialog.style(NormalDialog.STYLE_TWO).btnText(btns).btnTextColor(btnsColor).btnTextSize(btnsSize)
        .showAnim(new ZoomInEnter()).
        dismissAnim(new ZoomOutExit()).show();
        
        dialog.setOnBtnClickL(new OnBtnClickL() {
			
			@Override
			public void onBtnClick() {
				dialog.dismiss();
			}
		},new OnBtnClickL() {
			
			@Override
			public void onBtnClick() {
				dialog.dismiss();
				if(l !=null)
					l.onClick(null);
			}
		} );
        
        return dialog;
	}
	

	/**
	 * 版本更新
	 * 
	 * @param context
	 * @param l
	 */
	public static void showUpdateDialog(Context context, 
			final OnClickListener l) {
		UpdateDialog c = new UpdateDialog(context, l);
		c.show();
	}

}
