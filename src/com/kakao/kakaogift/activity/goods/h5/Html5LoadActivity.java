package com.kakao.kakaogift.activity.goods.h5;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.HMessageVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.webview.ProgressWebChromeClient;
import com.kakao.kakaogift.view.webview.ProgressWebView;

/**
 * 
 * @author vince
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class Html5LoadActivity extends BaseActivity {

	ProgressWebView mWebView;
	

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		initActionBar(null);
		mWebView = (ProgressWebView) findViewById(R.id.mWebView);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAppCacheEnabled(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			mWebView.getSettings().setMixedContentMode(
					WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		mWebView.setWebViewClient(new CustomWebViewCient());
		mWebView.setWebChromeClient(new HProgressWebChromeClient(mWebView
				.getProgressBar()));
		Log.i("url", getIntent().getStringExtra("url"));
		mWebView.loadUrl(getIntent().getStringExtra("url"), getHeaders());
		mWebView.addJavascriptInterface(new JavaScriptInterface(this),
				"android");
	}

	public class HProgressWebChromeClient extends ProgressWebChromeClient {
		public HProgressWebChromeClient(ProgressBar progressbar) {
			super(progressbar);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			initActionBar(title);
		}
	}

	private class CustomWebViewCient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.contains("detail")) {
				Intent intent = null;
				String[] u = url.split("/detail");
				if (url.contains("pin")) {
					intent = new Intent(getActivity(),
							PingouDetailActivity.class);
					intent.putExtra("url", UrlUtil.SERVERY3 + "/comm/detail"
							+ u[1]);
				} else {
					intent = new Intent(getActivity(),
							GoodsDetailActivity.class);
					intent.putExtra("url", UrlUtil.SERVERY3 + "/comm/detail"
							+ u[1]);
				}
				startActivity(intent);

			} else {
				view.loadUrl(url, getHeaders());
			}
			return true;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed(); // 接受所有网站的证书
			view.reload();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mWebView.loadUrl("javascript:$(function(){$(window).load(function(){document.getElementsByTagName(\"header\")[0].style.display=\"none\";});"
					+ "$('.banner').css(\"margin-top\",\"-44px\");});");
		}
	}

	// 网页调动js方法
	final class JavaScriptInterface {

		private BaseActivity activity;

		public JavaScriptInterface(BaseActivity context) {
			this.activity = context;
		}

		@JavascriptInterface
		public void getCoupon(String url) {
			if (getHeaders() == null) {
				activity.startActivity(new Intent(activity, LoginActivity.class));
				return;
			}
			Message msg = handler.obtainMessage(1, url);
			handler.sendMessage(msg);
		}
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				action(msg.obj.toString());
				break;
			default:
				break;
			}
		}
		
	};

	private void action(String url) {
		getLoading().show();
		VolleyHttp.doGetRequestTask(getHeaders(), url,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						Log.i("result", result);
						getLoading().dismiss();
						try {
							HMessageVo vo = new Gson().fromJson(result,
									HMessageVo.class);
							ToastUtils.Toast(getActivity(), vo.getMessage()
									.getMessage());
						} catch (Exception e) {
							ToastUtils.Toast(getActivity(), R.string.error);
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				});
	}

	private void initActionBar(String title) {
		if (title == null) {
			ActionBarUtil.setActionBarStyle(this, "KakaoGift");
			return;
		}
		ActionBarUtil.setActionBarStyle(this, title,new OnBackClickListener());
	}



	private class OnBackClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				finish();
			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
