/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:41:03 
**/
package com.hanmimei.activity.presenter.gdetail;

import java.util.Map;

import com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl;
import com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener;
import com.hanmimei.activity.view.gdetail.GoodsDetailView;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.manager.DataBaseManager;

/**
 * @author vince
 *
 */
public class GoodsDetailPresenterImpl implements GoodsDetailPresenter,OnGetGoodsDetailListener{
	
	private GoodsDetailView mGoodsDetailView;
	private GoodsDetailModelImpl mGoodsDetailModelImpl;
	
	public GoodsDetailPresenterImpl(GoodsDetailView mGoodsDetailView) {
		this.mGoodsDetailView = mGoodsDetailView;
		this.mGoodsDetailModelImpl = new GoodsDetailModelImpl();
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.presenter.gdetail.GoodsDetailPresenter#getGoodsDetailData(java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public void getGoodsDetailData(Map<String, String> headers, String url,
			String tag) {
		// TODO Auto-generated method stub
		mGoodsDetailView.showLoading();
		mGoodsDetailModelImpl.getGoodsDetail(headers, url, tag, this);
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener#onSuccess(com.hanmimei.entity.GoodsDetail)
	 */
	@Override
	public void onSuccess(GoodsDetail detail) {
		// TODO Auto-generated method stub
		mGoodsDetailView.GetGoodsDetailData(detail);
		mGoodsDetailView.hideLoading();
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener#onSuccess()
	 */
	@Override
	public void onSuccess(Integer cartNum) {
		// TODO Auto-generated method stub
		mGoodsDetailView.GetCartNumData(cartNum);
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener#onFailed(java.lang.String)
	 */
	@Override
	public void onFailed(String msg) {
		// TODO Auto-generated method stub
		mGoodsDetailView.hideLoading();
		mGoodsDetailView.showLoadFaild(msg);
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.presenter.gdetail.GoodsDetailPresenter#getCartNumData(java.util.Map, java.lang.String)
	 */
	@Override
	public void getCartNumData(Map<String, String> headers, String tag) {
		// TODO Auto-generated method stub
		if(headers !=null){
			mGoodsDetailModelImpl.getCartNumWithLogin(headers, this);
		}else{
			mGoodsDetailModelImpl.getCartNumWithoutLogin(this);
		}
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener#cancelCollection()
	 */
	@Override
	public void cancelCollectionSuccess() {
		// TODO Auto-generated method stub
		mGoodsDetailView.cancelCollection();
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener#addCollection(long)
	 */
	@Override
	public void addCollectionSuccess(long collectId) {
		// TODO Auto-generated method stub
		mGoodsDetailView.addCollection(collectId);
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.presenter.gdetail.GoodsDetailPresenter#addCollection(java.util.Map, java.lang.String, java.lang.String)
	 */
	@Override
	public void addCollection(Map<String, String> headers, StockVo stock) {
		// TODO Auto-generated method stub
		mGoodsDetailModelImpl.addCollection(headers, stock, null, this);
	}




	/* (non-Javadoc)
	 * @see com.hanmimei.activity.presenter.gdetail.GoodsDetailPresenter#cancelCollection(java.util.Map, java.lang.String)
	 */
	@Override
	public void cancelCollection(Map<String, String> headers, long collectId) {
		// TODO Auto-generated method stub
		mGoodsDetailModelImpl.cancelCollection(headers, collectId, null, this);
	}




	@Override
	public void addToCart(Map<String, String> headers, ShoppingGoods goods) {
		if(headers == null){
			mGoodsDetailModelImpl.addToCartWithoutLogin(goods, this);
		}else{
			mGoodsDetailView.showLoading();
			mGoodsDetailModelImpl.addToCartWithLogin(headers, goods, this);
		}
	}


	@Override
	public void addToCartWithLoginSuccess() {
		// TODO Auto-generated method stub
		mGoodsDetailView.hideLoading();
		mGoodsDetailView.addToCartSuccess();
	}




	@Override
	public void addToCartWithoutLoginSuccess(ShoppingGoods goods) {
		// TODO Auto-generated method stub
		DataBaseManager.getInstance().getDaoSession().getShoppingGoodsDao().insertOrReplace(goods);
		mGoodsDetailView.addToCartSuccess();
	}

}
