package com.hanmimei.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;


public class WebviewActivity extends BaseActivity {
	
	WebView mWebView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		
		ActionBarUtil.setActionBarStyle(this, "商品展示");
		mWebView = (WebView) findViewById(R.id.mWebView);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(getIntent().getStringExtra("url"),getHeaders());
		mWebView.setWebViewClient(new WebViewClient() {

			// 点击页面中的链接会调用这个方法
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
	}

}
