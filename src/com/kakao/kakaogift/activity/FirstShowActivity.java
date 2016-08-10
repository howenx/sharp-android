package com.kakao.kakaogift.activity;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.application.KKApplication;
import com.kakao.kakaogift.dao.DaoSession;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.DataBaseManager;
import com.kakao.kakaogift.utils.DateUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.utils.PreferenceUtil.IntroConfig;

@SuppressLint("NewApi")
public class FirstShowActivity extends AppCompatActivity {

	private KKApplication application;
	private User user;

	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_show_layout);
		getSupportActionBar().hide();
		application = (KKApplication) getApplication();
		// 判断是否是第一次进入app
//		if (IntroConfig.getIntroCfg(this).equals(
//				IntroConfig.INTRO_CONFIG_VALUE_IS)) {
//			mHandler.postDelayed(new FirstRun(1), 2000);
//		} else {
			loginUser();
//		}
		
	}

	// 判断用户token信息
	private void loginUser() {
		user = getDaoSession().getUserDao().queryBuilder().build().unique();
		//用户登录过并且token未过期
		if (user != null && user.getExpired() != null) {
			//token 存在
			if (user.getToken() != null) {
				int difDay = DateUtils.getDate(user.getExpired());
				//token 查24小时过期 更新token
				if (difDay < 24 && difDay >= 0) {
					getNewToken();
				} else if (difDay < 0) {
					//token已过期
					getDaoSession().getUserDao().deleteAll();
					mHandler.postDelayed(new FirstRun(1), 1500);
				} else {
					//token尚未过期 设置用户登录
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

	/**
	 * 更新token
	 */
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
