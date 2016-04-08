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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.DaoMaster;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.PinDetail;
import com.hanmimei.entity.PinResult;
import com.hanmimei.entity.User;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.manager.ThreadPoolManager;
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.SystemBarTintManager;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.LoadingDialog;
import com.sliding.finish.SwipeBackActivity;


/**
 * @Author vince.liu
 * @Description 公用
 * 
 */
public class BaseActivity extends SwipeBackActivity  {

	private LoadingDialog loadingDialog;
	private boolean shoppingcarChanged = false;
	ShimmerFrameLayout shimmer_view_container ;

	/*
	 * 获得用于数据库管理的DaoSession
	 */
	public DaoSession getDaoSession() {
		return getMyApplication().getDaoSession();
	}
	public DaoMaster getDaoMaster(){
		return getMyApplication().getDaoMaster();
	}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setElevation(0);
		// 沉浸式状态栏的设置
		if (VERSION.SDK_INT >= 19) {
			// 创建状态栏的管理实例
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			// 激活状态栏设置
			tintManager.setStatusBarTintEnabled(true);
			// 设置一个颜色给系统栏
			tintManager.setTintColor(getResources().getColor(R.color.theme));
			if (VERSION.SDK_INT >= 21) {
				setStatus();
			}
		}
		getClipboard();
	}

	public boolean isShoppingcarChanged() {
		return shoppingcarChanged;
	}

	public void setShoppingcarChanged(boolean shoppingcarChanged) {
		this.shoppingcarChanged = shoppingcarChanged;
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

	public LoadingDialog getLoading() {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(this);
		}
		return loadingDialog;
	}

	@Override
	protected void onStop() {
		super.onStop();
//		getMyApplication().getRequestQueue().cancelAll(new VolleyRequestFilter());
		if (!isAppOnForeground()) {
			setClipboard();
		}
	}
	
	

	@Override
	protected void onPause() {
		super.onPause();
		ToastUtils.cancel();
		stopShimmerAnimation();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!isAppOnForeground()) {
			setClipboard();
		}
	}

	@SuppressWarnings("deprecation")
	protected void setClipboard() {
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
		startShimmerAnimation();
	}

	@SuppressWarnings("deprecation")
	private void getClipboard() {
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		if (!TextUtils.isEmpty(cbm.getText())) {
			if (cbm.getText().toString().trim().contains("KAKAO-HMM")) {
				if(cbm.getText().toString().trim().contains("】,")){
					String[] url = cbm.getText().toString().trim().split("】,");
				if (url[1].contains("－🔑")) {
					String cutUrl = url[1].split("－🔑")[0];
					if (cbm.getText().toString().trim().contains("<C>")) {
						what = 0;
						loadData(UrlUtil.SERVERY3 + "/comm/detail"
								+ cutUrl.split("detail")[1]);
					} else if (cbm.getText().toString().trim().contains("<P>")) {
						what = 2;
						loadData(UrlUtil.SERVERY3 + "/comm/detail"
								+ cutUrl.split("detail")[1]);
					} else if (cbm.getText().toString().trim().contains("<T>")) {
						what = 1;
						loadData(UrlUtil.SERVERY5 + "/promotion/pin/activity"
								+ cutUrl.split("activity")[1]);
					}
					cbm.setText("");
					getMyApplication().setKouling("");
				}
				}
			}
		}
	}

	private GoodsDetail detail;
	private int what;
	private PinResult pinResult;
	private PinDetail pinDetail;

	private void loadData(String url) {
		Http2Utils.doGetRequestTask(this, null, url, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				if (what == 0) {
					try {
						detail = new Gson().fromJson(result, GoodsDetail.class);
					} catch (Exception e) {
						ToastUtils.Toast(getActivity(), R.string.error);
						return;
					}
					if (detail.getMessage().getCode() == 200) {
						setKouDialog(detail.getCurrentStock().getInvTitle(),
								detail.getCurrentStock().getItemPrice() + "",
								detail.getCurrentStock().getInvImgForObj()
										.getUrl());
					} else {
						ToastUtils.Toast(getActivity(), "口令请求错误");
					}
				} else if (what == 1) {
					pinResult = new Gson().fromJson(result, PinResult.class);
					if (pinResult.getMessage().getCode() == 200) {
						setKouDialog(pinResult.getActivity().getPinTitle(),
								pinResult.getActivity().getPinPrice(),
								pinResult.getActivity().getPinImg().getUrl());
					} else {
						ToastUtils.Toast(getActivity(), "口令请求错误");
					}
				} else if (what == 2) {
					pinDetail = new Gson().fromJson(result, PinDetail.class);
					if (pinDetail.getMessage().getCode() == 200) {
						setKouDialog(pinDetail.getStock().getPinTitle(),
								pinDetail.getStock().getPinDiscount(),
								pinDetail.getStock().getInvImg());
					} else {
						ToastUtils.Toast(getActivity(), "口令请求错误");
					}
				}
			}

			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), "错误的M令！");
			}
		});
	}

	@SuppressLint("InflateParams")
	private void setKouDialog(String ti, String pri, String imgurl) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final AlertDialog dialog = new AlertDialog.Builder(this,
				R.style.CustomDialog).create();
		View view = inflater.inflate(R.layout.hanmimei_command_layout, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		GlideLoaderUtils.loadGoodsImage(getActivity(),imgurl, img);
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView price = (TextView) view.findViewById(R.id.price);
		title.setText(ti);
		if (what == 0) {
			price.setText("单价：¥" + pri);
		} else if (what == 1) {
			price.setText("拼购价：¥" + pri);
		} else if (what == 2) {
			price.setText("折扣率：" + pri + "折");
		}
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
				if (what == 0) {
					Intent intent = new Intent(BaseActivity.this,
							GoodsDetailActivity.class);
					intent.putExtra("url", detail.getCurrentStock().getInvUrl());
					startActivity(intent);
				} else if (what == 1) {
					Intent intent = new Intent(BaseActivity.this,
							PingouResultActivity.class);
					intent.putExtra("url", pinResult.getActivity().getPinUrl());
					startActivity(intent);
				} else {
					Intent intent = new Intent(BaseActivity.this,
							PingouDetailActivity.class);
					intent.putExtra("url", pinDetail.getStock()
							.getPinRedirectUrl());
					startActivity(intent);
				}
			}
		});
		dialog.setView(view);
		dialog.show();

	}

	// 线程池 管理网络提交事务
	public void submitTask(Runnable runable) {
		ThreadPoolManager.getInstance().getExecutorService().execute(runable);
	}

	public VersionVo getVersionInfo() {
		return getMyApplication().getVersionInfo();
	}

	public void setVersionInfo(VersionVo versionInfo) {
		getMyApplication().setVersionInfo(versionInfo);
	}

	public void setShimmer_view_container(ShimmerFrameLayout shimmer_view_container) {
		this.shimmer_view_container = shimmer_view_container;
	}
	
	protected void startShimmerAnimation(){
		if(shimmer_view_container !=null)
			shimmer_view_container.startShimmerAnimation();
	}
	protected void stopShimmerAnimation(){
		if(shimmer_view_container !=null)
			shimmer_view_container.stopShimmerAnimation();
	}
	
}
