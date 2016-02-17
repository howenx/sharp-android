package com.hanmimei.activity;

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
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.ProgressWebView;
import com.hanmimei.view.ProgressWebView2;
import com.umeng.analytics.MobclickAgent;

public class OrderSubmitActivity extends BaseActivity {

	private ProgressWebView mWebView;
	private Date startTime; // 创建页面时间，用于标志页面过期的起始时间
	private boolean isSuccess = false;

	Map<String, String> extraHeaders;

	// private Date endTime;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_submit_layout);
		// 初始化actionbar
		ActionBarUtil.setActionBarStyle(this, "支付", new BackClickListener());
		OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra(
				"orderInfo");

		startTime = new Date();
		mWebView = (ProgressWebView) findViewById(R.id.mWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);

		// 获取用户token 添加到header中
		extraHeaders = new HashMap<String, String>();
		extraHeaders.put("id-token", getUser().getToken());

//		mWebView.loadUrl("https://www.baidu.com/",extraHeaders);

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
//		
//		
		mWebView.loadUrl(UrlUtil.CLIENT_PAY_ORDER_GET
				 + orderInfo.getOrderId(), extraHeaders);

		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.setWebChromeClient(new NewWebChromeClient());
		// 添加js交互
		mWebView.addJavascriptInterface(new JavaScriptInterface(this),
				"handler");

	}
	
	
	private class NewWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.i("newProgress", newProgress + "");
			if (newProgress >= 100) {
				mWebView.getProgressBar().setVisibility(View.GONE);
			} else {
				if (mWebView.getProgressBar().getVisibility() == View.GONE)
					mWebView.getProgressBar().setVisibility(View.VISIBLE);
				mWebView.getProgressBar().setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			String[] tb = { "提示", message, null, "确定" };
			AlertDialogUtils.showCustomDialog(getActivity(), tb,
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(getActivity(),
									MyOrderActivity.class));
							finish();
						}
					});
			return true;
		}

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			return false;
		}
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
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				showPayDialog();
			}
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
		public void clearHistory() {
			isSuccess = true;
			mWebView.clearHistory();
		}
		
		@JavascriptInterface
		public void loadurl(String url) {
			if (isOverdue(startTime, new Date())) {
				ToastUtils.Toast(getActivity(), "页面过期");
				startActivity(new Intent(getActivity(),
						MyOrderActivity.class));
				finish();
			} else {
				mWebView.loadUrl(url, extraHeaders);
			}
		}
	}

	// 显示取消支付窗口
	private void showPayDialog() {
		AlertDialogUtils.showPayDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getIntent().getStringExtra("orderType").equals("item"))
					startActivity(new Intent(getActivity(), MyOrderActivity.class));
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

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("OrderSubmitActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderSubmitActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
														// onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

}
