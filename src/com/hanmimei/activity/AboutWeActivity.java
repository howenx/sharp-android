package com.hanmimei.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.utils.ActionBarUtil;
import com.umeng.analytics.MobclickAgent;

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
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("AboutWeActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("AboutWeActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}

}
