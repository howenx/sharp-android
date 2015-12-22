package com.hanmimei.activity;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtil;

public class TestActivity extends BaseActivity {

	private WebView mWebView;
	private Date startTime;
	private boolean isSuccess = false;

	// private Date endTime;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_submit_layout);
		ActionBarUtil.setActionBarStyle(this, "支付", new BackClickListener());
		startTime = new Date();

		mWebView = (WebView) findViewById(R.id.mWebView);

		mWebView.getSettings().setJavaScriptEnabled(true);
		
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
			
			
		});
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000*20);
					mHandler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();

		 mWebView.loadUrl("http://www.baidu.com");
		 
		 mWebView.addJavascriptInterface(new JavaScriptInterface(this),
					"handler");
	}
	
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			
		}
		
	};

	private class BackClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if(!isSuccess) {
				if(mWebView.canGoBack()){
					mWebView.goBack();
				}else{
					showPayDialog();
				}
			} else{
				finish();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(!isSuccess) {
				if(mWebView.canGoBack()){
					mWebView.goBack();
				}else{
					showPayDialog();
				}
			} else{
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	final class JavaScriptInterface {

		private Context context;

		public JavaScriptInterface(Context context) {
			this.context = context;
		}
		@JavascriptInterface
		public void openOrder() {
			Intent intent = new Intent(context, MyOrderActivity.class);
			startActivity(intent);
			finish();
		}
		@JavascriptInterface
		public void openHome() {
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
		}
		
		@JavascriptInterface
		public void clearHistory(){
			mWebView.clearHistory();
		}
	}

	private void showPayDialog() {
		AlertDialogUtil.showPayDialog(getActivity(), new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MyOrderActivity.class));
				finish();
			}
		});
	}

	private boolean isOverdue(Date startTime, Date endTime) {
		return endTime.getTime() - startTime.getTime() > formatTime(10);
	}

	/*
	 * 毫秒转化
	 */
	public static long formatTime(long mi) {
		long minute = mi * 1000 * 60;
		return minute;
	}

}
