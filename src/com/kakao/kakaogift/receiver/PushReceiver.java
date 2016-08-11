package com.kakao.kakaogift.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.HMainActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.h5.Html5LoadActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouResultActivity;
import com.kakao.kakaogift.activity.goods.theme.ThemeGoodsActivity;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.activity.mine.coupon.MyCouponActivity;
import com.kakao.kakaogift.application.KKApplication;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.entity.Notify;
import com.kakao.kakaogift.manager.MessageMenager;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class PushReceiver extends BroadcastReceiver {
	// private static final String TAG = "JPush";
	private KKApplication application;

	@Override
	public void onReceive(Context context, Intent intent) {
		application = (KKApplication) context.getApplicationContext();
		Bundle bundle = intent.getExtras();
		msgIsCouspon(context,bundle);
//		context.sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_COUNPON_ACTION));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			// String regId =
			// bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			// Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...


		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " +
			// bundle.getString(JPushInterface.EXTRA_MESSAGE));
//			ToastUtils.Toast(
//					context,
//					"[MyReceiver] 接收到推送下来的自定义消息: "
//							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			// processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			// int notifactionId =
			// bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
			// Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
			clickJPush(context, bundle);

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			// Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " +
			// bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			// boolean connected =
			// intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE,
			// false);
			// Log.w(TAG, "[MyReceiver]" + intent.getAction()
			// +" connected state change to "+connected);
		} else {
			// Log.d(TAG, "[MyReceiver] Unhandled intent - " +
			// intent.getAction());
		}
	}

	/**
	 * @param bundle
	 */
	private void msgIsCouspon(Context context, Bundle bundle) {
//		MessageMenager.getInstance().setMsgDrawble(R.drawable.hmm_icon_message_h);
		if(bundle != null){
			String other = bundle.getString(JPushInterface.EXTRA_EXTRA);
			if(other != null){
				Notify notify = DataParser.parserJPush(other);
				if (notify.getTargetType() != null) {
					if (notify.getTargetType().equals("C")) {
						MessageMenager.getInstance().getListener().onGetMessage(R.drawable.hmm_icon_message);
						if(application.getLoginUser() != null){
							context.sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_COUNPON_ACTION));
						}
					}
				} 
			}
		}
	}

	private void clickJPush(Context context, Bundle bundle) {
		String other = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Notify notify = DataParser.parserJPush(other);
		Intent i = null;
		if (notify.getTargetType() != null) {
			if (notify.getTargetType().equals("D")) {
				i = new Intent(context, GoodsDetailActivity.class);
			} else if (notify.getTargetType().equals("T")) {
				i = new Intent(context, ThemeGoodsActivity.class);
			} else if (notify.getTargetType().equals("P")) {
				i = new Intent(context, PingouDetailActivity.class);
			} else if (notify.getTargetType().equals("A")) {
				i = new Intent(context, HMainActivity.class);
			} else if (notify.getTargetType().equals("U")) {
				i = new Intent(context, Html5LoadActivity.class);
			} else if (notify.getTargetType().equals("V")) {
				i = new Intent(context, PingouResultActivity.class);
			} else if(notify.getTargetType().equals("C")){
				if(application.getLoginUser() == null){
					i = new Intent(context,LoginActivity.class);
				}else{
					i = new Intent(context, MyCouponActivity.class);
				}
			}else{
				i = new Intent(context, HMainActivity.class);
			}
		} else {
			i = new Intent(context, HMainActivity.class);
		}
		i.putExtra("url", notify.getUrl());
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);

	}

}
