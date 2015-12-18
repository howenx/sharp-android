package com.hanmimei.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.ToastUtils;

public class OrderSubmitActivity extends BaseActivity {

	private WebView mWebView;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_submit_layout);
		ActionBarUtil.setActionBarStyle(this, "支付");
		
		mWebView = (WebView) findViewById(R.id.mWebView);
		OrderInfo orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
//		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(UrlUtil.CLIENT_PAY_ORDER_GET+orderInfo.getOrder().getOrderId());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.i("url", url);
	           view.loadUrl(url);  
	            return true;
			}
		});
	}

}
