/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午4:00:06 
**/
package com.hanmimei.activity.presenter.hmain;

import com.hanmimei.activity.model.hmain.HMainModel;
import com.hanmimei.activity.model.hmain.HMainModelImpl;
import com.hanmimei.activity.model.hmain.HMainModelImpl.OnCheckVersionListener;
import com.hanmimei.activity.view.HMainView;
import com.hanmimei.entity.VersionVo;

/**
 * @author vince
 *
 */
public class HMainPresenterImpl implements HMainPresenter,OnCheckVersionListener{
	
	private HMainModel hMainModel;
	private HMainView hMainView;
	
	
	

	public HMainPresenterImpl(HMainView hMainView) {
		super();
		this.hMainModel = new HMainModelImpl();
		this.hMainView = hMainView;
	}

	/* (non-Javadoc)
	 * @see com.hanmimei.activity.presenter.hmain.HMainPresenter#checkVersionInfo(com.hanmimei.activity.model.hmain.HMainModelImpl.OnCheckVersionListener)
	 */
	@Override
	public void checkVersionInfo() {
		// TODO Auto-generated method stub
		hMainModel.checkVersionInfo(this);
	}

	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.hmain.HMainModelImpl.OnCheckVersionListener#onSuccess(com.hanmimei.entity.VersionVo)
	 */
	@Override
	public void onSuccess(VersionVo info) {
		// TODO Auto-generated method stub
		hMainView.loadVersionInfo(info);
	}

	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.hmain.HMainModelImpl.OnCheckVersionListener#onFailed(java.lang.String)
	 */
	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub
		hMainView.onLoadFailed(msg);
	}

}
