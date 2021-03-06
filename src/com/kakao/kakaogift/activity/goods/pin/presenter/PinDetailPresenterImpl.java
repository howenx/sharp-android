/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-26 上午10:03:19 
 **/
package com.kakao.kakaogift.activity.goods.pin.presenter;

import java.util.Map;

import com.kakao.kakaogift.activity.goods.pin.PinDetailView;
import com.kakao.kakaogift.activity.goods.pin.model.PinDetailModel;
import com.kakao.kakaogift.activity.goods.pin.model.PinDetailModelImpl;
import com.kakao.kakaogift.activity.goods.pin.model.PinDetailModelImpl.OnPinDetailListener;
import com.kakao.kakaogift.entity.PinDetail;
import com.kakao.kakaogift.entity.StockVo;

/**
 * @author vince
 * 
 */
public class PinDetailPresenterImpl implements PinDetailPresenter,
		OnPinDetailListener {

	private PinDetailModel mPinDetailModel;
	private PinDetailView mPinDetailView;

	public PinDetailPresenterImpl(PinDetailView mPinDetailView) {
		super();
		this.mPinDetailModel = new PinDetailModelImpl();
		this.mPinDetailView = mPinDetailView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModelImpl.OnPinDetailListener
	 * #onSuccess(com.kakao.kakaogift.entity.PinDetail)
	 */
	@Override
	public void onSuccess(PinDetail detail) {
		// TODO Auto-generated method stub
		mPinDetailView.loadPinDetailData(detail);
		mPinDetailView.hideLoading();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModelImpl.OnPinDetailListener
	 * #cancelCollectionSuccess()
	 */
	@Override
	public void cancelCollectionSuccess() {
		// TODO Auto-generated method stub
		mPinDetailView.cancelCollectionSuccess();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModelImpl.OnPinDetailListener
	 * #addCollectionSuccess(long)
	 */
	@Override
	public void addCollectionSuccess(long collectId) {
		// TODO Auto-generated method stub
		mPinDetailView.addCollectionSuccess(collectId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.pdetail.PinDetailModelImpl.OnPinDetailListener
	 * #onFailed(java.lang.String)
	 */
	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub
		mPinDetailView.hideLoading();
		mPinDetailView.showLoadFaild(msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.presenter.pdetail.PinDetailPresenter#getPinDetail
	 * (java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public void getPinDetail(Map<String, String> headers, String url, String tag) {
		// TODO Auto-generated method stub
		mPinDetailView.showLoading();
		mPinDetailModel.getPinDetail(headers, url, tag, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.presenter.pdetail.PinDetailPresenter#cancelCollection
	 * (java.util.Map, long)
	 */
	@Override
	public void cancelCollection(Map<String, String> headers, long collectId) {
		// TODO Auto-generated method stub
		mPinDetailModel.cancelCollection(headers, collectId, null, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.presenter.pdetail.PinDetailPresenter#addCollection
	 * (java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public void addCollection(Map<String, String> headers, StockVo stock) {
		// TODO Auto-generated method stub
		mPinDetailModel.addCollection(headers,stock, null, this);
	}

}
