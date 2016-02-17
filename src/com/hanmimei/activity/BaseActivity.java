package com.hanmimei.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.User;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.manager.ThreadPoolManager;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
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
			//tintManager.setNavigationBarTintEnabled(true);
			// 设置一个颜色给系统栏
			tintManager.setTintColor(getResources().getColor(R.color.theme));
			if (VERSION.SDK_INT >= 21) {
				setStatus();
			}
		}
		getClipboard();
	}

	@SuppressLint("NewApi")
	private void setStatus() {
		Window window = getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
				| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		window.getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
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

	public HMMApplication getMyApplication() {
		return (HMMApplication) this.getApplication();
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

	// 获取null token
	public Map<String, String> getNullHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("null", "");
		return headers;
	}

	public LoadingDialog getLoading() {
		return loadingDialog;
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!isAppOnForeground()) {
			setClipboard();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ToastUtils.cancel();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!isAppOnForeground()) {
			setClipboard();
		}
	}
	protected void setClipboard(){
			// 退出或者app进入后台将口令扔到剪切板
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cbm.setText(getMyApplication().getKouling());
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
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	public void onResume() {
		super.onResume();
		getClipboard();
	}

	private void getClipboard(){
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		if (!TextUtils.isEmpty(cbm.getText())) {
			if (cbm.getText().toString().trim().contains("KAKAO-HMM")) {
				String url[] = cbm.getText().toString().trim().split("】,");
				if(url[1].contains(",－")){
					loadData("http://172.28.3.51:9001/comm/detail"
						+ url[1].split(",－")[0]);
					cbm.setText("");
					getMyApplication().setKouling("");
				}
			}
		}
	}
	private GoodsDetail detail;

	private void loadData(String url) {
		Http2Utils.doGetRequestTask(this, getHeaders(), url,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						detail = DataParser.parserGoodsDetail(result);
						setKouDialog(detail.getCurrentStock().getInvTitle(),
								detail.getCurrentStock().getItemPrice() + "",
								detail.getCurrentStock().getInvImgForObj()
										.getUrl());
					}

					@Override
					public void onError() {
						ToastUtils.Toast(getActivity(), "错误的M令！");
					}
				});
	}

	private void setKouDialog(String ti, String pri, String imgurl) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final AlertDialog dialog = new AlertDialog.Builder(this,
				R.style.CustomDialog).create();
		View view = inflater.inflate(R.layout.hanmimei_password_layout, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		ImageLoaderUtils.loadImage(this, imgurl, img);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView price = (TextView) view.findViewById(R.id.price);
		title.setText(ti);
		price.setText(pri);
		view.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
		view.findViewById(R.id.now).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				Intent intent = new Intent(BaseActivity.this,GoodsDetailActivity.class);
				intent.putExtra("url", detail.getCurrentStock().getInvUrl());
				startActivity(intent);
			}
		});
		dialog.setView(view);
		dialog.show();

	}
	
	//线程池 管理网络提交事务
		public void submitTask(Runnable runable) {
			ThreadPoolManager.getInstance().getExecutorService().execute(runable);
		}
	
	public VersionVo getVersionInfo() {
		return getMyApplication().getVersionInfo();
	}

	public void setVersionInfo(VersionVo versionInfo) {
		getMyApplication().setVersionInfo(versionInfo);
	}

}
