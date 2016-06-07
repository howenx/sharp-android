package com.hanmimei.activity.goods.h5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.goods.detail.GoodsDetailActivity;
import com.hanmimei.activity.goods.pin.PingouDetailActivity;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.view.webview.ProgressWebView;

/**
 * 
 * @author vince
 * 
 */
public class Html5LoadActivity extends BaseActivity {

	ProgressWebView mWebView;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		ActionBarUtil.setActionBarStyle(this, "韩秘美");

		mWebView = (ProgressWebView) findViewById(R.id.mWebView);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAppCacheEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
        	mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("detail")) {
					Intent intent = null;
					String[] u = url.split("/detail");
					if (url.contains("pin")) {
						intent = new Intent(getActivity(),
								PingouDetailActivity.class);
						intent.putExtra("url", UrlUtil.SERVERY3
								+ "/comm/detail" + u[1]);
					} else {
						intent = new Intent(getActivity(),
								GoodsDetailActivity.class);
						intent.putExtra("url", UrlUtil.SERVERY3
								+ "/comm/detail" + u[1]);
					}
					startActivity(intent);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}


			@Override
			public void onReceivedSslError(WebView view,SslErrorHandler handler, SslError error) {
				 handler.proceed();  // 接受所有网站的证书
				 view.reload();
			}


			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mWebView.loadUrl("javascript:$(function(){$(window).load(function(){document.getElementsByTagName(\"header\")[0].style.display=\"none\";});"
						+ "$('.banner').css(\"margin-top\",\"-44px\");});");
			}
		});
		mWebView.setWebChromeClient(new HProgressWebChromeClient(mWebView.getProgressBar()));
		Log.i("url", getIntent().getStringExtra("url"));
		mWebView.loadUrl(getIntent().getStringExtra("url"), getHeaders());
	}

	public class HProgressWebChromeClient extends
			com.hanmimei.view.webview.ProgressWebChromeClient {

		/**
		 * @param progressbar
		 */
		public HProgressWebChromeClient(ProgressBar progressbar) {
			super(progressbar);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			ActionBarUtil.setActionBarStyle(getActivity(), title,
					new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							if (mWebView.canGoBack()) {
								mWebView.goBack();
							} else {
								finish();
							}
						}
					});
		}

	}

}
