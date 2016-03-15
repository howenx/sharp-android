package com.hanmimei.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.TokenVo;
import com.hanmimei.entity.User;
import com.hanmimei.utils.DateUtil;
import com.hanmimei.utils.PostStringRequest;
import com.hanmimei.utils.SharedPreferencesUtil;
import com.hanmimei.utils.ToastUtils;

@SuppressLint("NewApi")
public class FirstShowActivity extends AppCompatActivity {

	private static final String FIRST = "first";
	private static final String FIRST_LOG_FLAG = "first_log_flag";
	private HMMApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_show_layout);
		getSupportActionBar().hide();
		application = (HMMApplication) getApplication();
		// 判断是否自动登录
		loginUser();
		// 判断是否是第一次进入app
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

	// 判断用户token信息
	private void loginUser() {
		User user = getDaoSession().getUserDao().queryBuilder().build().unique();
		if (user != null) {
			if (user.getToken() != null) {
				int difDay = DateUtil.getDate(user.getExpired());
				if (difDay < 24 && difDay >= 0) {
					getNewToken(user);
				} else if (difDay < 0) {
					getDaoSession().getUserDao().deleteAll();
				} else {
					application.setLoginUser(user);
				}
			}
		}
	}

	// 更新token
	private void getNewToken(final User user) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", user.getToken());
		PostStringRequest request = null;
		try {
			request = new PostStringRequest(Method.POST, UrlUtil.UPDATE_TOKEN,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							TokenVo tokenVo = new Gson().fromJson(arg0, TokenVo.class);
							if(tokenVo.getResult()){
								user.setToken(tokenVo.getToken());
								user.setExpired(DateUtil.turnToDate(tokenVo.getExpired()));
								user.setLast_login(DateUtil.getCurrentDate());
								application.setLoginUser(user);
							}else{
								ToastUtils.Toast(FirstShowActivity.this,tokenVo.getMessage());
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError arg0) {
							ToastUtils.Toast(FirstShowActivity.this,
									R.string.error);
						}
					}, params, null);
		} catch (IOException e) {
			ToastUtils.Toast(FirstShowActivity.this, R.string.error);
		}
		((HMMApplication) getApplication()).getRequestQueue().add(request);

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

	private DaoSession getDaoSession() {
		HMMApplication application = (HMMApplication) getApplication();
		return application.getDaoSession();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		JPushInterface.onResume(this);
	}

}
