package com.hanmimei.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hanmimei.R;
import com.hanmimei.application.MyApplication;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.entity.User;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.SystemBarTintManager;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.LoadingDialog;

public class BaseActivity extends AppCompatActivity {

	private LoadingDialog loadingDialog;
	/*
	 * 获得用于数据库管理的DaoSession
	 */
	public DaoSession getDaoSession() {
		return getMyApplication().getDaoSession();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		getSupportActionBar().setElevation(0);
		loadingDialog = new LoadingDialog(this);
		// 沉浸式状态栏的设置
		if (VERSION.SDK_INT >= 19) {
			setTranslucentStatus(true);  
			// 创建状态栏的管理实例
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			// 激活状态栏设置
			tintManager.setStatusBarTintEnabled(true);
			// 激活导航栏设置
//			 tintManager.setNavigationBarTintEnabled(true);
			// 设置一个颜色给系统栏
			tintManager.setTintColor(getResources().getColor(R.color.theme));
			if(VERSION.SDK_INT >= 21){
				setStatus();
			}	
		}
	}
	@SuppressLint("NewApi") 
	private void setStatus(){
	            Window window = getWindow();
	            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
	                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
	            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
	            window.setStatusBarColor(Color.TRANSPARENT);
	}
	private void setTranslucentStatus(boolean on) {  
	     Window win = getWindow();  
	     WindowManager.LayoutParams winParams = win.getAttributes();  
	     final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;  
	     if (on) {  
	         winParams.flags |= bits;  
	     } else {  
	         winParams.flags &= ~bits;  
	     }  
	     win.setAttributes(winParams);  
	 } 

	public BaseActivity getActivity() {
		return this;
	}

	public User getUser() {
		return getMyApplication().getLoginUser();
	}

	public MyApplication getMyApplication() {
		return (MyApplication) this.getApplication();
	}


	// 获取token
	public Map<String, String> getHeaders() {
		Map<String, String> headers = null;
		if (getUser() != null) {
			headers = new HashMap<String, String>();
			headers.put("id-token", getUser().getToken());
		}
		return headers;
	}

	// 获取null  token
	public Map<String, String> getNullHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("null", "");
		return headers;
	}


	public LoadingDialog getLoading() {
		return loadingDialog;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStop() {
		if (!isAppOnForeground()) {
			// 退出或者app进入后台将口令扔到剪切板
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cbm.setText(getMyApplication().getKouling());
		}
		super.onStop();
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		ToastUtils.cancel();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * 程序是否在前台运行
	 * 
	 */
	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public void onResume() {
		super.onResume();
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		if (!TextUtils.isEmpty(cbm.getText())) {
			if (cbm.getText().toString().trim().equals("hanmimei")) {
				cbm.setText("");
//				loadData();
				getMyApplication().setKouling("");
			}
		}

	}

//	private void loadData() {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(2000);
//					Message msg = mHandler.obtainMessage(1);
//					mHandler.sendMessage(msg);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}

//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 1:
//				showKouLing();
//				break;
//
//			default:
//				break;
//			}
//		}
//
//	};
	private void showKouLing() {
		AlertDialogUtils
				.KouDialog(
						this,
						"耐克户外运动经典篮球鞋，nba官方正品，杜兰特专用。",
						"¥1999",
						"http://e.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=4ba6a9c4271f95caa6a09ab0f9275306/77094b36acaf2edd7f60e5538f1001e9380193f3.jpg");
	}

}
