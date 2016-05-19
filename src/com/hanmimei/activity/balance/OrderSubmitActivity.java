package com.hanmimei.activity.balance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.activity.MainActivity;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.goods.pin.PingouResultActivity;
import com.hanmimei.activity.mine.order.MyOrderActivity;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.ProgressWebView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class OrderSubmitActivity extends BaseActivity {

	private ProgressWebView mWebView;
	private Date startTime; // 创建页面时间，用于标志页面过期的起始时间
	private boolean isSuccess = false; // 用于标志支付是否成功

	Map<String, String> extraHeaders;
	
	private IWXAPI api;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBackEnable(false);
		setContentView(R.layout.order_submit_layout);
		// 初始化actionbar
		ActionBarUtil.setActionBarStyle(this, "收银台", new BackClickListener());
		OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra(
				"orderInfo");

		startTime = new Date();
		mWebView = (ProgressWebView) findViewById(R.id.mWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);

		// 获取用户token 添加到header中
		extraHeaders = new HashMap<String, String>();
		if(getUser() !=null)
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
		
		api = WXAPIFactory.createWXAPI(this, "wx578f993da4b29f97");

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
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
		}

		@JavascriptInterface
		public void clearHistory(String url) {
			Log.e("error", url);
			isSuccess = true;
		}

		@JavascriptInterface
		public void pin(String url) {
			Intent intent = new Intent(getActivity(),
					PingouResultActivity.class);
			intent.putExtra("url", url);
			startActivity(intent);
			finish();
		}
		@JavascriptInterface
		public void weixinpay(String appid,String partnerId,String prepayId,String pack,String nonceStr,String timeStamp,String sign) {
			wxPay(appid,partnerId,prepayId,pack,nonceStr,timeStamp,sign);
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
			IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);

			// 将该app注册到微信
			msgApi.registerApp("wx578f993da4b29f97");
			
			PayReq req = new PayReq();
			//req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
			req.appId			= appid;
			req.partnerId		= partnerId;
			req.prepayId		= prepayId;
			req.nonceStr		= nonceStr;
			req.timeStamp		= timeStamp;
			req.packageValue	= pack;
			req.sign			= sign;
			ToastUtils.Toast(OrderSubmitActivity.this, "正常调起支付");
			// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
			api.sendReq(req);
		}
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

}
