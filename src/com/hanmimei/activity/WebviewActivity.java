package com.hanmimei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.utils.ActionBarUtil;


public class WebviewActivity extends BaseActivity {
	
	WebView mWebView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		
		mWebView = (WebView) findViewById(R.id.mWebView);
		
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.addJavascriptInterface(new JavaScriptInterface(), "handler");  
		mWebView.setWebViewClient(new WebViewClient(){
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.contains("detail")){
					Intent intent = null;
					String[] u = url.split("/detail");
					if(url.contains("pin")){
						intent  = new Intent(getActivity(), PingouDetailActivity.class);
						intent.putExtra("url", UrlUtil.SERVERY3+"/comm/detail"+u[1]);
					}else{
						intent  = new Intent(getActivity(), GoodsDetailActivity.class);
						intent.putExtra("url", UrlUtil.SERVERY3+"/comm/detail"+u[1]);
					}
					startActivity(intent);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mWebView.loadUrl("javascript:handler.hideHeader()");
//				mWebView.loadUrl("javascript:function myFunction(){x=document.getElementById(\"header\");  x.innerHTML=\"改变了html内容!\";}");
			}
			
		});
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress>=40){
					findViewById(R.id.mProgressBar).setVisibility(View.INVISIBLE);
					mWebView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				ActionBarUtil.setActionBarStyle(getActivity(), title,new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(mWebView.canGoBack()){
							mWebView.goBack();
						}else{
							onBackPressed();
							finish();
						}
					}
				});
			}
		});
		Log.i("url", getIntent().getStringExtra("url"));
		mWebView.loadUrl(getIntent().getStringExtra("url"),getHeaders());
	}
	
	// 网页调动js方法
		final class JavaScriptInterface {

			public JavaScriptInterface() {}

			@JavascriptInterface
			public void hideHeader() {
				mWebView.loadUrl("javascript:document.getElementsByTagName(\"header\")[0].style.display=\"none\";");
			}
		}


}
