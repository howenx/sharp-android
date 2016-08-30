/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 下午12:11:50 
 **/
package com.kakao.kakaogift.manager;

import com.kakao.kakaogift.dao.DaoMaster;
import com.kakao.kakaogift.dao.DaoSession;
import com.kakao.kakaogift.dao.DaoMaster.DevOpenHelper;

import android.content.Context;

/**
 * @author vince
 * 
 */
public class DataBaseManager {

	private static DataBaseManager instance;
	private DaoMaster daoMaster;
	private DaoSession daoSession;

	public static synchronized void initializeInstance(Context context) {
		if (instance == null) {
			instance = new DataBaseManager(context);
		}
	}

	private DataBaseManager(Context context) {
		if (daoMaster == null) {
			DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,
					"hmmdb_greedao.db", null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
			daoSession = getDaoMaster().newSession();
		}
	}

	public static synchronized DataBaseManager getInstance() {
		if (instance == null) {
			throw new IllegalStateException(
					DataBaseManager.class.getSimpleName()
							+ " is not initialized, call initializeInstance(..) method first.");
		}
		return instance;
	}

	private DaoMaster getDaoMaster() {
		return daoMaster;
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}

}
