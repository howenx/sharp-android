package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hanmimei.R;
import com.hanmimei.application.MyApplication;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.dao.UserDao;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.User;
import com.hanmimei.utils.DateUtil;
import com.hanmimei.utils.SharedPreferencesUtil;

@SuppressLint("NewApi")
public class FirstShowActivity extends BaseActivity {

	private static final String FIRST = "first";
	private static final String FIRST_LOG_FLAG = "first_log_flag";
	private User user;
	private UserDao userDao;
	private MyApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_show_layout);
		//判断是否自动登录
		loginUser();
		//判断是否是第一次进入app
		SharedPreferencesUtil util = new SharedPreferencesUtil(
				FirstShowActivity.this, FIRST);
		util.putString("isLoadNet", "true");
		String flag = util.getString(FIRST_LOG_FLAG);
		if (flag == null) {
			util.putString(FIRST_LOG_FLAG, "not_first");
			startActivity(new Intent(FirstShowActivity.this,
					IndroductionActivity.class));
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
	//判断用户token信息
	private void loginUser() {
		userDao = getDaoSession().getUserDao();
		user = userDao.queryBuilder().build().unique();
		if(user != null){
		int difDay = DateUtil.getDate(user.getExpired());
		if(difDay < 24 && difDay >=0){
			getNewToken();
		}else if(difDay <0){
			userDao.deleteAll();
		}else{
			application = (MyApplication) getApplication();
			application.setLoginUser(user);
		}
		}
	}
	//更新token
	private void getNewToken() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					Message msg = mHandler.obtainMessage(2);
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				startActivity(new Intent(FirstShowActivity.this,
						MainActivity.class));
				finish();
				break;
			case 2:
				
				break;
			default:
				break;
			}
		}

	};
}
