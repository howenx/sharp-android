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
	
	//初始化PlatformConfig
	private void initPlatformConfig() {
	    //微信 appid appsecret
		PlatformConfig.setWeixin("wx4ee4a992a10d1253", "b1a54352a4e78028fc54de89b29505a6");
	    //新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("794664710","0dc274fafeabec336673331c633a115e");
        // QQ和Qzone appid appkey
		PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba"); 
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
