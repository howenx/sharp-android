/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午4:00:06 
**/
package com.kakao.kakaogift.activity.main.presenter;

import com.kakao.kakaogift.activity.main.HMainView;
import com.kakao.kakaogift.activity.main.model.HMainModel;
import com.kakao.kakaogift.activity.main.model.HMainModelImpl;
import com.kakao.kakaogift.activity.main.model.HMainModelImpl.OnCheckVersionListener;
import com.kakao.kakaogift.entity.VersionVo;

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
	 * @see com.kakao.kakaogift.activity.presenter.hmain.HMainPresenter#checkVersionInfo(com.kakao.kakaogift.activity.model.hmain.HMainModelImpl.OnCheckVersionListener)
	 */
	@Override
	public void checkVersionInfo() {
		// TODO Auto-generated method stub
		hMainModel.checkVersionInfo(this);
	}

	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.model.hmain.HMainModelImpl.OnCheckVersionListener#onSuccess(com.kakao.kakaogift.entity.VersionVo)
	 */
	@Override
	public void onSuccess(VersionVo info) {
		// TODO Auto-generated method stub
		hMainView.loadVersionInfo(info);
	}

	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.model.hmain.HMainModelImpl.OnCheckVersionListener#onFailed(java.lang.String)
	 */
	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub
		hMainView.onLoadFailed(msg);
	}

}
