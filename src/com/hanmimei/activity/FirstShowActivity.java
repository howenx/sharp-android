package com.hanmimei.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;

import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.dao.UserDao;
import com.hanmimei.entity.User;
import com.hanmimei.utils.DateUtil;
import com.hanmimei.utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class FirstShowActivity extends AppCompatActivity {

	private static final String FIRST = "first";
	private static final String FIRST_LOG_FLAG = "first_log_flag";
	private User user;
	private UserDao userDao;
	private HMMApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_show_layout);
		getSupportActionBar().hide();
		application = (HMMApplication) getApplication();
		//判断是否自动登录
		loginUser();
		//判断是否是第一次进入app
		SharedPreferencesUtil util = new SharedPreferencesUtil(
				FirstShowActivity.this, FIRST);
		util.putString("isLoadNet", "true");
		String flag = util.getString(FIRST_LOG_FLAG);
		if (flag == null) {
			startActivity(new Intent(FirstShowActivity.this,
					IndroductionActivity.class));
			finish();
		} else {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(2000);
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
		if(user.getToken() != null){
		int difDay = DateUtil.getDate(user.getExpired());
		if(difDay < 24 && difDay >=0){
			getNewToken();
		}else if(difDay <0){
			userDao.deleteAll();
		}else{
			application.setLoginUser(user);
		}
		}
		}
	}
	//更新token
	private void getNewToken() {
		
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
	
	public void onResume() {
	    super.onResume();
	    JPushInterface.onResume(this);
	    MobclickAgent.onPageStart("FirstShowActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
		JPushInterface.onPause(this);
	    MobclickAgent.onPageEnd("FirstShowActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}
	private DaoSession getDaoSession() {
		HMMApplication application = (HMMApplication) getApplication();
		return application.getDaoSession();
	}

}
