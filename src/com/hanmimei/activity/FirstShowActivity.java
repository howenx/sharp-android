package com.hanmimei.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;

import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.User;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.manager.DataBaseManager;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.PreferenceUtil.IntroConfig;
import com.hanmimei.utils.ToastUtils;

@SuppressLint("NewApi")
public class FirstShowActivity extends AppCompatActivity {

	private HMMApplication application;
	private User user;

	private Handler mHandler = new Handler();

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
		if (user != null && user.getExpired() != null) {
			if (user.getToken() != null) {
				int difDay = DateUtils.getDate(user.getExpired());
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
		} else {
			mHandler.postDelayed(new FirstRun(1), 1500);
		}
	}

	private class FirstRun implements Runnable {
		private int what;

		public FirstRun(int what) {
			this.what = what;
		}

		@Override
		public void run() {
			switch (what) {
			case 0:
				startActivity(new Intent(FirstShowActivity.this,
						IndroductionActivity.class));
				finish();
				break;
			case 1:
				startActivity(new Intent(FirstShowActivity.this,
						HMainActivity.class));
				finish();
				break;
			default:
				break;
			}
		}

	}

	// 更新token
	private void getNewToken() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("id-token", user.getToken());
		VolleyHttp.doGetRequestTask(headers, UrlUtil.UPDATE_TOKEN,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						HMessage loginInfo = DataParser.paserResultMsg(result);
						if (loginInfo.getCode() == 200) {
							user.setToken(loginInfo.getTag());
							user.setExpired(DateUtils.turnToDate(loginInfo
									.getTime()));
							user.setLast_login(DateUtils.getCurrentDate());
							getDaoSession().getUserDao().insertOrReplace(user);
							application.setLoginUser(user);
						} else {
							ToastUtils.Toast(FirstShowActivity.this,
									loginInfo.getMessage());
						}
						mHandler.postDelayed(new FirstRun(1), 1000);

					}

					@Override
					public void onError() {
						ToastUtils.Toast(FirstShowActivity.this, R.string.error);
					}
				});
	}

	private DaoSession getDaoSession() {
		return DataBaseManager.getInstance().getDaoSession();
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

}
