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

	
	private DaoMaster daoMaster;
	private DaoSession daoSession;

	private static class DataBaseManagerHolder{
		static DataBaseManager instance = new DataBaseManager();
	}

	public DataBaseManager() {
	}

	public static  DataBaseManager getInstance() {
		return DataBaseManagerHolder.instance;
	}


	public DaoSession getDaoSession() {
		return daoSession;
	}
	public void setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
	}

}
