package com.hanmimei.application;

import java.io.File;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hanmimei.R;
import com.hanmimei.dao.DaoMaster;
import com.hanmimei.dao.DaoMaster.DevOpenHelper;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.entity.User;
import com.hanmimei.entity.VersionVo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.testin.agent.TestinAgent;
import com.testin.agent.TestinAgentConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;

public class HMMApplication extends Application {
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private User loginUser;
	private RequestQueue queue;
	private String kouling;
	private VersionVo versionInfo;

	@Override
	public void onCreate() {
		super.onCreate();
		initTestinAgent();
		queue = Volley.newRequestQueue(this);
		initPlatformConfig();
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.setSessionContinueMillis(60000);
		// MobclickAgent.setDebugMode(true);
		ImageLoader.getInstance().init(initImageLoaderConfiguration());
	}

	private void initTestinAgent() {
		TestinAgentConfig config = new TestinAgentConfig.Builder(this)
				.withDebugModel(true) // Output the crash log in local if you
										// open debug mode
				.withErrorActivity(true) // Output the activity info in crash or
											// error log
				.withCollectNDKCrash(true) // Collect NDK crash or not if you
											// use our NDK
				.withOpenCrash(true) // Monitor crash if true
				.withReportOnlyWifi(true) // Report data only on wifi mode
				.withReportOnBack(true) // allow to report data when application
										// in background
				.build();
		TestinAgent.init(config);
	}

	private ImageLoaderConfiguration initImageLoaderConfiguration() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(this,
				"imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5加密
				.defaultDisplayImageOptions(initImageLoaderDisplayImageOptions()).build();
		return config;
	}

	private static DisplayImageOptions initImageLoaderDisplayImageOptions() {
		DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.color.background)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.displayer(new FadeInBitmapDisplayer(0))//是否图片加载好后渐入的动画时间  
				.build();
		return imageOptions;
	}

	// 初始化PlatformConfig
	private void initPlatformConfig() {
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wx4ee4a992a10d1253",
				"b1a54352a4e78028fc54de89b29505a6");
		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("794664710",
				"0dc274fafeabec336673331c633a115e");
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone("1104980747", "eDLFyqWCM2GzdkMB");
	}

	public RequestQueue getRequestQueue() {
		return this.queue;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public void clearLoginUser() {
		this.loginUser = null;
	}

	public DaoMaster getDaoMaster() {
		if (daoMaster == null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,
					"hmmdb_greedao.db", null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	public DaoSession getDaoSession() {
		if (daoSession == null) {
			daoSession = getDaoMaster().newSession();
		}
		return daoSession;
	}

	public String getKouling() {
		return kouling;
	}

	public void setKouling(String kouling) {
		this.kouling = kouling;
	}

	public VersionVo getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(VersionVo versionInfo) {
		this.versionInfo = versionInfo;
	}
	
	
	
}
