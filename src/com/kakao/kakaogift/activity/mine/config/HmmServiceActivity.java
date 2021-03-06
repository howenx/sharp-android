package com.kakao.kakaogift.activity.mine.config;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.utils.ActionBarUtil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author eric
 *
 */
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
