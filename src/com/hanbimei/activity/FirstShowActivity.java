package com.hanbimei.activity;

import com.hanbimei.R;
import com.hanbimei.utils.SharedPreferencesUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@SuppressLint("NewApi") 
public class FirstShowActivity extends BaseActivity {

	private static final String FIRST = "first";
	private static final String FIRST_LOG_FLAG = "first_log_flag";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.first_show_layout);
		SharedPreferencesUtil util = new SharedPreferencesUtil(
				FirstShowActivity.this, FIRST);
		String flag = util.getString(FIRST_LOG_FLAG);
		if (flag == null) {
			util.putString(FIRST_LOG_FLAG, "not_first");
			startActivity(new Intent(FirstShowActivity.this,
					ViewPagerActivity.class));
			finish();
		} else {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					mHandler.obtainMessage(1).sendToTarget();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		}
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				startActivity(new Intent(FirstShowActivity.this,
						MainActivity.class));
				finish();
				break;

			default:
				break;
			}
		}
		
	};
}
