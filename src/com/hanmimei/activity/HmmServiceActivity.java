package com.hanmimei.activity;

import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.utils.ActionBarUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HmmServiceActivity extends BaseActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hhm_service_layout);
		ActionBarUtil.setActionBarStyle(this, "韩秘美服务条款");
		getLoading().show();
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl(UrlUtil.SERVICE);
		webView.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				getLoading().dismiss();
			}
			
		});
	}

}
