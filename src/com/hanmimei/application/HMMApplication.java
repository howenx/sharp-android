package com.hanmimei.application;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hanmimei.dao.DaoMaster;
import com.hanmimei.dao.DaoMaster.DevOpenHelper;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.entity.User;
import com.hanmimei.entity.VersionVo;
import com.testin.agent.TestinAgent;
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
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this); // 初始化 JPush
		TestinAgent.init(this);
		queue = Volley.newRequestQueue(this);
		initPlatformConfig();
	}




	// 初始化PlatformConfig
	private void initPlatformConfig() {
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wx578f993da4b29f97",
				"e78a99aec4b6860370107be78a5faf9d");
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
