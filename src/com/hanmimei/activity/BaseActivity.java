package com.hanmimei.activity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.hanmimei.R;
import com.hanmimei.application.MyApplication;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.data.XmlParserHandler;
import com.hanmimei.entity.User;
import com.hanmimei.manager.ThreadPoolManager;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.SystemBarTintManager;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.LoadingDialog;
import com.hanmimei.wheel.entity.CityModel;
import com.hanmimei.wheel.entity.DistrictModel;
import com.hanmimei.wheel.entity.ProvinceModel;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

	private MyApplication application;
	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	protected String[] mProvinceEnDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前省的简称
	 */
	protected String mCurrentProviceEnName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				mCurrentProviceEnName = provinceList.get(0).getEnName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			mProvinceEnDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				mProvinceEnDatas[i] = provinceList.get(i).getEnName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	/*
	 * 获得用于数据库管理的DaoSession
	 */
	public DaoSession getDaoSession() {
		MyApplication application = (MyApplication) getApplication();
		return application.getDaoSession();
	}

	private Toast toast;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		getSupportActionBar().setElevation(0);
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setSessionContinueMillis(60000);
		MobclickAgent.setDebugMode(true);
		loadingDialog = new LoadingDialog(this);
		application = (MyApplication) getApplication();
		// 沉浸式状态栏的设置
//		if (VERSION.SDK_INT >= 19) {
			// 创建状态栏的管理实例
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			// 激活状态栏设置
			tintManager.setStatusBarTintEnabled(true);
			// 激活导航栏设置
			// tintManager.setNavigationBarTintEnabled(true);
			// 设置一个颜色给系统栏
			tintManager.setTintColor(getResources().getColor(R.color.theme));
//		}
	}

	public BaseActivity getActivity() {
		return this;
	}

	public User getUser() {
		return application.getLoginUser();
	}

	public MyApplication getMyApplication() {
		return (MyApplication) this.getApplication();
	}

	// 线程池 管理网络提交事务
	public void submitTask(Runnable runable) {
		ThreadPoolManager.getInstance().getExecutorService().execute(runable);
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

	private LoadingDialog loadingDialog;

	public LoadingDialog getLoading() {
		return loadingDialog;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onStop() {
		if (!isAppOnForeground()) {
			// 退出或者app进入后台将口令扔到剪切板
			ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			cbm.setText(application.getKouling());
		}
		ToastUtils.cancel();
		super.onStop();
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
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
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
				loadData();
				application.setKouling("");
			}
		}

	}

	private void loadData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					Message msg = mHandler.obtainMessage(1);
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
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				showKouLing();
				break;

			default:
				break;
			}
		}

	};

	private void showKouLing() {
		AlertDialogUtils
				.KouDialog(
						this,
						"耐克户外运动经典篮球鞋，nba官方正品，杜兰特专用。",
						"¥1999",
						"http://e.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=4ba6a9c4271f95caa6a09ab0f9275306/77094b36acaf2edd7f60e5538f1001e9380193f3.jpg");
	}

}
