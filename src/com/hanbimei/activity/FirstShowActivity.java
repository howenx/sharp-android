package com.hanbimei.activity;

import com.hanbimei.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

@SuppressLint("NewApi") 
public class FirstShowActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_show_layout);
		getActionBar().hide();
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
