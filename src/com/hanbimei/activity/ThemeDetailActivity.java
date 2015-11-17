package com.hanbimei.activity;

import com.hanbimei.R;
import com.hanbimei.utils.HttpUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@SuppressLint("NewApi") public class ThemeDetailActivity extends Activity {

	private String url;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		url = getIntent().getStringExtra("url");
		loadUrl();
	}
	private void loadUrl() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String result = HttpUtils.get(url);
				
			}
		}).start();
	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				
				break;

			default:
				break;
			}
		}
		
	};
	
}
