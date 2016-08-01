package com.kakao.kakaogift.activity.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.anzewei.parallaxbacklayout.ParallaxActivityBase;
import com.google.gson.Gson;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouResultActivity;
import com.kakao.kakaogift.application.KKApplication;
import com.kakao.kakaogift.dao.DaoSession;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.GoodsDetail;
import com.kakao.kakaogift.entity.PinDetail;
import com.kakao.kakaogift.entity.PinResult;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.entity.VersionVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.DataBaseManager;
import com.kakao.kakaogift.manager.ThreadPoolManager;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.kakao.kakaogift.utils.StatusBarCompat;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.LoadingDialog;

/**
 * @Author vince.liu
 * @Description 公用
 * 
 */
public class BaseActivity extends ParallaxActivityBase {

	private LoadingDialog loadingDialog;
	private boolean shoppingcarChanged = false;

	
	public Map<String, String> getMap() {
		return getMyApplication().getMap();
	}
	public void setMap(Map<String, String> map) {
		getMyApplication().setMap(map);
	}


	/*
	 * 获得用于数据库管理的DaoSession
	 */
	public DaoSession getDaoSession() {
		return DataBaseManager.getInstance().getDaoSession();
	}


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 沉浸式状态栏的设置
		getSupportActionBar().setElevation(0);
		StatusBarCompat.compat(this,StatusBarCompat.COLOR_DEFAULT);
		getClipboard();
	}
	
	


	public boolean isShoppingcarChanged() {
		return shoppingcarChanged;
	}

	public void setShoppingcarChanged(boolean shoppingcarChanged) {
		this.shoppingcarChanged = shoppingcarChanged;
	}

	public BaseActivity getActivity() {
		return this;
	}

	public User getUser() {
		return getMyApplication().getLoginUser();
	}

	public KKApplication getMyApplication() {
		return (KKApplication) this.getApplication();
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
		if(loadingDialog == null)
			loadingDialog = new LoadingDialog(this);
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
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(getLoading().isShowing())
			getLoading().dismiss();
		if (!isAppOnForeground()) {
			setClipboard();
		}
	}

	protected void setClipboard() {
		// 退出或者app进入后台将口令扔到剪切板
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//		cbm.setText(getMyApplication().getKouling());
		cbm.setPrimaryClip(ClipData.newPlainText("kouling", getMyApplication().getKouling()));
	}

	/**
	 * 判断程序是否在前台运行
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
	/**
	 * 获取剪切版内容，秘口令需要
	 */
	@SuppressWarnings("deprecation")
	private void getClipboard() {
		ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		if (!TextUtils.isEmpty(cbm.getText())) {
			//根据剪切版内容，拼接跳转链接
			if (cbm.getText().toString().trim().contains("KAKAO-HMM")) {
				if (cbm.getText().toString().trim().contains("】,")) {
					String[] url = cbm.getText().toString().trim().split("】,");
					if (url[1].contains("－🔑")) {
						String cutUrl = url[1].split("－🔑")[0];
						if (cbm.getText().toString().trim().contains("<C>")) {
							what = 0;
							loadData(UrlUtil.SERVERY3 + "/comm/detail"
									+ cutUrl.split("detail")[1]);
						} else if (cbm.getText().toString().trim()
								.contains("<P>")) {
							what = 2;
							loadData(UrlUtil.SERVERY3 + "/comm/detail"
									+ cutUrl.split("detail")[1]);
						} else if (cbm.getText().toString().trim()
								.contains("<T>")) {
							what = 1;
							loadData(UrlUtil.SERVERY5
									+ "/promotion/pin/activity"
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
	//加载秘口令，弹窗显示内容的请求
	private void loadData(String url) {
		VolleyHttp.doGetRequestTask(url, new VolleyJsonCallback() {

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
	//给口令弹窗赋值
	@SuppressLint("InflateParams")
	private void setKouDialog(String ti, String pri, String imgurl) {
		LayoutInflater inflater = LayoutInflater.from(this);
		final AlertDialog dialog = new AlertDialog.Builder(this,
				R.style.CustomDialog).create();
		View view = inflater.inflate(R.layout.hanmimei_command_layout, null);
		ImageView img = (ImageView) view.findViewById(R.id.img);
		GlideLoaderTools.loadSquareImage(getActivity(), imgurl, img);
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
}
