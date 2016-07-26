package com.kakao.kakaogift.activity.balance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alipay.sdk.app.PayTask;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.HMainActivity;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouResultActivity;
import com.kakao.kakaogift.activity.mine.order.MyOrderActivity;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.AlipayResult;
import com.kakao.kakaogift.entity.OrderInfo;
import com.kakao.kakaogift.event.PayEvent;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.webview.ProgressWebView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ypy.eventbus.EventBus;

public class OrderSubmitActivity extends BaseActivity {

	private static final int SDK_PAY_FLAG = 1;

	private ProgressWebView mWebView;
	private Date startTime; // 创建页面时间，用于标志页面过期的起始时间
	private boolean isSuccess = false; // 用于标志支付是否成功

	Map<String, String> extraHeaders;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBackEnable(false);
		setContentView(R.layout.order_submit_layout);
		// 初始化actionbar
		ActionBarUtil.setActionBarStyle(this, "收银台", new BackClickListener());
		EventBus.getDefault().register(this);
		OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra(
				"orderInfo");

		startTime = new Date();
		mWebView = (ProgressWebView) findViewById(R.id.mWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);

		// 获取用户token 添加到header中
		extraHeaders = new HashMap<String, String>();
		if (getUser() != null)
			extraHeaders.put("id-token", getUser().getToken());

		mWebView.setWebViewClient(new WebViewClient() { 

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (isOverdue(startTime, new Date())) {
					ToastUtils.Toast(getActivity(), "页面过期");
					startActivity(new Intent(getActivity(),
							MyOrderActivity.class));
					finish();
				} else {
					view.loadUrl(url, extraHeaders);
				}
				return true;
			}
		});

		mWebView.loadUrl(UrlUtil.CLIENT_PAY_ORDER_GET + orderInfo.getOrderId(),
				extraHeaders);

		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		// 添加js交互
		mWebView.addJavascriptInterface(new JavaScriptInterface(this),
				"handler");
	}

	// 返回按钮点击事件
	private class BackClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			backEvent();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 菜单返回按钮点击事件
			backEvent();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void backEvent() {
		if (!isSuccess) {
			showPayDialog();
		} else {
			finish();
		}
	}

	// 网页调动js方法
	final class JavaScriptInterface {

		private Context context;

		public JavaScriptInterface(Context context) {
			this.context = context;
		}

		@JavascriptInterface
		public void openOrder() {
			Intent intent = new Intent(context, MyOrderActivity.class);
			startActivity(intent);
			finish();
		}

		@JavascriptInterface
		public void openHome() {
			Intent intent = new Intent(context, HMainActivity.class);
			startActivity(intent);
		}

		@JavascriptInterface
		public void clearHistory(String url) {
			Log.e("error", url);
			isSuccess = true;
		}

		@JavascriptInterface
		public void pin(String url) {
			Intent intent = new Intent(getActivity(),PingouResultActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
			finish();
		}

		@JavascriptInterface
		public void weixinpay(String appid, String partnerId, String prepayId,
				String pack, String nonceStr, String timeStamp, String sign) {
			wxPay(appid, partnerId, prepayId, pack, nonceStr, timeStamp, sign);
		}

		@JavascriptInterface
		public void alipayapp(String alipayInfo) {
			aliPay(alipayInfo);
		}

	}

	/**
	 * @param appid
	 * @param partnerId
	 * @param prepayId
	 * @param pack
	 * @param nonceStr
	 * @param timeStamp
	 * @param sign
	 */
	private void wxPay(String appid, String partnerId, String prepayId,
			String pack, String nonceStr, String timeStamp, String sign) {
		IWXAPI msgApi = WXAPIFactory.createWXAPI(this, AppConstant.WEIXIN_APP);
		
		PayReq req = new PayReq();
		req.appId = appid;
		req.partnerId = partnerId;
		req.prepayId = prepayId;
		req.nonceStr = nonceStr;
		req.timeStamp = timeStamp;
		req.packageValue = pack;
		req.sign = sign;
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		msgApi.sendReq(req);
	}

	/**
	 * @param alipayInfo
	 */
	private void aliPay(final String alipayInfo) {
		// TODO Auto-generated method stub
		submitTask(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				PayTask payTask = new PayTask(getActivity());
				String result = payTask.pay(alipayInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		});
	}

	// 显示取消支付窗口
	private void showPayDialog() {
		AlertDialogUtils.showPayDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getIntent().getStringExtra("orderType").equals("item")) {
					startActivity(new Intent(getActivity(),
							MyOrderActivity.class));
				} else {
					onBackPressed();
				}
				finish();
			}
		});
	}

	// 判断是否超时
	private boolean isOverdue(Date startTime, Date endTime) {
		return endTime.getTime() - startTime.getTime() > formatTime(10);
	}

	// 毫秒转化成分钟
	private long formatTime(long mi) {
		return mi * 1000 * 60;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEvent(PayEvent event) {
		if (event.getCode() != 0) {
			showPayFailDialog();
		}
	}

	/**
	 * 
	 */
	public void showPayFailDialog() {
		AlertDialogUtils.showPayDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {
				mWebView.goBack();
			}
		}, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (getIntent().getStringExtra("orderType").equals("item")) {
					startActivity(new Intent(getActivity(),
							MyOrderActivity.class));
				} else {
					onBackPressed();
				}
				finish();
			}
		});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			AlipayResult result = new AlipayResult((String) msg.obj);
			String resultStatus = result.getResultStatus();
			
			if (TextUtils.equals(resultStatus, AlipayResult.PAY_SUCCESS)) {
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				ToastUtils.Toast(getActivity(), "支付成功");
			} else if (TextUtils.equals(resultStatus, AlipayResult.PAY_LOADING)){
				// 判断resultStatus 为非"9000"则代表可能支付失败
				// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				ToastUtils.Toast(getActivity(), "支付结果确认中");
				} else {
					// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
					showPayFailDialog();
				}
		}
	};
}
