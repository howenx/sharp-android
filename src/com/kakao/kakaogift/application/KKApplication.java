package com.kakao.kakaogift.application;

import java.util.Map;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.kakao.kakaogift.dao.DaoMaster;
import com.kakao.kakaogift.dao.DaoMaster.DevOpenHelper;
import com.kakao.kakaogift.dao.DaoSession;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.entity.VersionVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.testin.agent.TestinAgent;
import com.umeng.socialize.PlatformConfig;

public class KKApplication extends Application {
	private User loginUser;
	private String kouling;
	private VersionVo versionInfo;
	private Map<String, String> map;
	private DaoMaster daoMaster;
	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		initPlatformConfig();
		VolleyHttp.registRequestQueue(this);
		TestinAgent.init(this);
		JPushInterface.init(this); // 初始化 JPush
	}

	// 初始化PlatformConfig
	private void initPlatformConfig() {
		// 微信 appid appsecret
		PlatformConfig.setWeixin("wx578f993da4b29f97",
				"e78a99aec4b6860370107be78a5faf9d");
		// 新浪微博 appkey appsecret
		PlatformConfig.setSinaWeibo("2077801979",
				"934587e123fe9520f577708a601649a9");
		// QQ和Qzone appid appkey
		PlatformConfig.setQQZone("1105527443", "lXkPvxWXufYAIzHT");
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
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
