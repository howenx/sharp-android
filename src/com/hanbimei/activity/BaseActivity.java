package com.hanbimei.activity;


import com.hanbimei.application.MyApplication;
import com.hanbimei.dao.DaoSession;

import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {
	/*
	* 获得用于数据库管理的DaoSession
	*/
	public DaoSession getDaoSession() {
		MyApplication application = (MyApplication) getApplication();
		return application.getDaoSession();
	}

}
