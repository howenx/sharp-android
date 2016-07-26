/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-24 下午4:43:07 
**/
package com.kakao.kakaogift.activity.presenter.theme;

import java.util.Map;

import com.kakao.kakaogift.activity.model.theme.HThemeGoodsModelImpl;
import com.kakao.kakaogift.activity.model.theme.HThemeGoodsModelImpl.OnCartNumListener;
import com.kakao.kakaogift.activity.model.theme.HThemeGoodsModelImpl.OnHThemeGoodsLoadListenter;
import com.kakao.kakaogift.activity.view.theme.HThemeGoodsView;
import com.kakao.kakaogift.entity.HThemeGoods;


/**
 * @author vince
 *
 */
public class HThemeGoodsPresenterImpl implements HThemeGoodsPresenter ,OnHThemeGoodsLoadListenter,OnCartNumListener{

	private HThemeGoodsView mHThemeGoodsView;
	private HThemeGoodsModelImpl mHThemeGoodsModelImpl;
	

	public HThemeGoodsPresenterImpl(HThemeGoodsView mHThemeGoodsView) {
		super();
		this.mHThemeGoodsView = mHThemeGoodsView;
		this.mHThemeGoodsModelImpl = new HThemeGoodsModelImpl();
	}

	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.goods.theme.presenter.HThemeGoodsPresenter#getHThemeGoodsData()
	 */
	@Override
	public void getHThemeGoodsData(Map<String, String> headers, String url,String tag) {
		mHThemeGoodsView.showLoading();
		mHThemeGoodsModelImpl.getThemeGoods(headers, url, tag, this);
	}
	
	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.presenter.theme.HThemeGoodsPresenter#getCartNumData(java.util.Map, java.lang.String)
	 */
	@Override
	public void getCartNumData(Map<String, String> headers, String tag) {
		// TODO Auto-generated method stub
		mHThemeGoodsModelImpl.getCartNum(headers, this);
		
	}

	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.goods.theme.model.HThemeGoodsModelImpl.OnHThemeGoodsLoadListenter#onSuccess(com.kakao.kakaogift.entity.HThemeGoods)
	 */
	@Override
	public void onSuccess(HThemeGoods detail) {
		// TODO Auto-generated method stub
		mHThemeGoodsView.GetHThemeGoodsData(detail);
		mHThemeGoodsView.GetCartNumData(detail.getCartNum());
		mHThemeGoodsView.hideLoading();
	}

	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.goods.theme.model.HThemeGoodsModelImpl.OnHThemeGoodsLoadListenter#onFailed(java.lang.String)
	 */
	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub
		mHThemeGoodsView.hideLoading();
		mHThemeGoodsView.showLoadFaild(msg);
	}

	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.model.theme.HThemeGoodsModelImpl.OnCartNumListener#onSuccess(java.lang.Integer)
	 */
	@Override
	public void onSuccess(Integer cartNum) {
		mHThemeGoodsView.GetCartNumData(cartNum);
	}

	
}
