package com.hanmimei.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtil;
import com.hanmimei.utils.ToastUtils;

public class OrderSubmitActivity extends BaseActivity {

	private WebView mWebView;
	private Date startTime;

	// private Date endTime;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_submit_layout);
		ActionBarUtil.setActionBarStyle(this, "支付", new BackClickListener());
		startTime = new Date();

		mWebView = (WebView) findViewById(R.id.mWebView);
		OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra(
				"orderInfo");
		Map<String, String> extraHeaders = new HashMap<String, String>();
		extraHeaders.put("id-token", getUser().getToken());

		mWebView.getSettings().setJavaScriptEnabled(true);
		
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (isOverdue(startTime, new Date())) {
					ToastUtils.Toast(getActivity(), "页面过期");
					startActivity(new Intent(getActivity(),
							MyOrderActivity.class));
					finish();
					return true;
				}
				return false;
			}
		});

		 mWebView.loadUrl(UrlUtil.CLIENT_PAY_ORDER_GET
		 + orderInfo.getOrder().getOrderId(), extraHeaders);
		 
		 mWebView.addJavascriptInterface(new JavaScriptInterface(this),
					"handler");
	}

	private class BackClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				showPayDialog();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				showPayDialog();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	final class JavaScriptInterface {

		private Context context;

		public JavaScriptInterface(Context context) {
			this.context = context;
		}
		@JavascriptInterface
		public void openOrder() {
			Intent intent = new Intent(context, MyOrderActivity.class);
			startActivity(intent);
		}
		@JavascriptInterface
		public void openHome() {
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
		}
	}

	private void showPayDialog() {
		AlertDialogUtil.showPayDialog(getActivity(), new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MyOrderActivity.class));
				finish();
			}
		});
	}

	private boolean isOverdue(Date startTime, Date endTime) {
		return endTime.getTime() - startTime.getTime() > formatTime(10);
	}

	/*
	 * 毫秒转化
	 */
	public static long formatTime(long mi) {
		long minute = mi * 1000 * 60;
		return minute;
	}

}
