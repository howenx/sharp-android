package com.kakao.kakaogift.activity.mine.config;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.utils.ActionBarUtil;

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
		ActionBarUtil.setActionBarStyle(this, "关于Kakao Gift");
		webView = (WebView) findViewById(R.id.web);
		webView.loadUrl(UrlUtil.ABOUT_WE);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed(); // 接受所有网站的证书
			}
		});
	}

}
