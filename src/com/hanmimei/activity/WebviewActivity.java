package com.hanmimei.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
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
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress>=99){
					findViewById(R.id.mProgressBar).setVisibility(View.INVISIBLE);
					mWebView.setVisibility(View.VISIBLE);
				}
			}
			
		});
	}

}
