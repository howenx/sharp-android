package com.hanmimei.activity.mine.config;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.utils.ActionBarUtil;

/**
 * @author eric
 *
 */
@SuppressLint("NewApi") 
public class AboutWeActivity extends BaseActivity {

	private WebView webView;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.about_we_layout);
		ActionBarUtil.setActionBarStyle(this, "关于韩秘美");
		getLoading().show();
		webView = (WebView) findViewById(R.id.web);
		webView.loadUrl(UrlUtil.ABOUT_WE);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				getLoading().dismiss();
			}
		});
	}

}