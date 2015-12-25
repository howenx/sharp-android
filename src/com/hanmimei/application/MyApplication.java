package com.hanmimei.application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hanmimei.dao.DaoMaster;
import com.hanmimei.dao.DaoSession;
import com.hanmimei.dao.DaoMaster.DevOpenHelper;
import com.hanmimei.entity.User;
import com.umeng.socialize.PlatformConfig;

import android.app.Application;

public class MyApplication extends Application {
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private User loginUser;
	private RequestQueue queue;

	@Override
	public void onCreate() {
		super.onCreate();
		queue = Volley.newRequestQueue(this);
		initPlatformConfig();
	}
	
	private void initPlatformConfig() {
	       //微信 appid appsecret
		PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
	       //新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("794664710","0dc274fafeabec336673331c633a115e");
        // QQ和Qzone appid appkey
//		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba"); 
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
	public void clearLoginUser(){
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

}
