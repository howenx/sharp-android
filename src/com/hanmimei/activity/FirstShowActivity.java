package com.hanmimei.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.hanmimei.utils.PreferenceUtil.IntroConfig;
import com.hanmimei.utils.ToastUtils;

/**
 * @author eric
 *
 */
@SuppressLint("NewApi")
public class FirstShowActivity extends AppCompatActivity {

	private HMMApplication application;
	private User user;
	
	private Handler mHandler = new Handler() ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_show_layout);
		getSupportActionBar().hide();
		application = (HMMApplication) getApplication();
		// 判断是否是第一次进入app
		if (IntroConfig.getIntroCfg(this).equals(
				IntroConfig.INTRO_CONFIG_VALUE_IS)) {
			mHandler.postDelayed(new FirstRun(0), 2000);
		} else {
			loginUser();
		}
	}

	// 判断用户token信息
	private void loginUser() {
		user = getDaoSession().getUserDao().queryBuilder().build().unique();
		if (user != null && user.getExpired()!=null ) {
			if (user.getToken() != null) {
				int difDay = DateUtil.getDate(user.getExpired());
				if (difDay < 24 && difDay >= 0) {
					getNewToken();
				} else if (difDay < 0) {
					getDaoSession().getUserDao().deleteAll();
					mHandler.postDelayed(new FirstRun(1), 1500);
				} else {
					application.setLoginUser(user);
					mHandler.postDelayed(new FirstRun(1), 1500);
				}
			}
		}else{
			mHandler.postDelayed(new FirstRun(1), 1500);
		}
	}

	private class FirstRun implements Runnable {
		private int what;

		public FirstRun(int what) {
			super();
			this.what = what;
		}

		@Override
		public void run() {
			switch (what) {
			case 0:
				startActivity(new Intent(FirstShowActivity.this,IndroductionActivity.class));
				finish();
				break;
			case 1:
				startActivity(new Intent(FirstShowActivity.this,MainTestActivity.class));
				finish();
				break;
			default:
				break;
			}
		}

	}

	// 更新token
	private void getNewToken() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("token", user.getToken());
		PostStringRequest request = null;
		try {
			request = new PostStringRequest(Method.POST, UrlUtil.UPDATE_TOKEN,
					new Listener<String>() {

						@Override
						public void onResponse(String arg0) {
							TokenVo tokenVo = new Gson().fromJson(arg0,
									TokenVo.class);
							if (tokenVo.getResult()) {
								user.setToken(tokenVo.getToken());
								user.setExpired(DateUtil.turnToDate(tokenVo
										.getExpired()));
								user.setLast_login(DateUtil.getCurrentDate());
								getDaoSession().getUserDao().insertOrReplace(
										user);
								application.setLoginUser(user);
							} else {
								ToastUtils.Toast(FirstShowActivity.this,
										tokenVo.getMessage());
							}
							mHandler.postDelayed(new FirstRun(1), 1000);
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
