package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.view.ProgressWebView;

import android.os.Bundle;
import android.support.annotation.Nullable;


public class WebviewActivity extends BaseActivity {
	
	ProgressWebView mWebView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		
		ActionBarUtil.setActionBarStyle(this, "商品展示");
		mWebView = (ProgressWebView) findViewById(R.id.mWebView);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(getIntent().getStringExtra("url"),getHeaders());
		
	}

}
