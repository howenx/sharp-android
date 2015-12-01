package com.hanbimei.application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hanbimei.dao.DaoMaster;
import com.hanbimei.dao.DaoSession;
import com.hanbimei.dao.DaoMaster.DevOpenHelper;
import com.hanbimei.entity.User;

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
					"jredb_greedao.db", null);
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
