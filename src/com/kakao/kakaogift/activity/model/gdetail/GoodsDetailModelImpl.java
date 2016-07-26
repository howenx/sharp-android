/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:26:21 
 **/
package com.kakao.kakaogift.activity.model.gdetail;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.kakao.kakaogift.dao.ShoppingGoodsDao.Properties;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CartNumVo;
import com.kakao.kakaogift.entity.CollectionVo;
import com.kakao.kakaogift.entity.GoodsDetail;
import com.kakao.kakaogift.entity.HMessageVo;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.StockVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.DataBaseManager;

/**
 * @author vince
 * 
 */
public class GoodsDetailModelImpl implements GoodsDetailModel {
	
	public interface OnGetGoodsDetailListener {
		void onSuccess(GoodsDetail detail);

		void onSuccess(Integer cartNum);

		void cancelCollectionSuccess();

		void addCollectionSuccess(long collectId);

		void addToCartWithLoginSuccess();

		void addToCartWithoutLoginSuccess(ShoppingGoods goods);

		void onFailed(String msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#getGoodsDetail(java
	 * .util.Map, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift.activity.model
	 * .gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener)
	 */
	@Override
	public void getGoodsDetail(Map<String, String> headers, String url,
			String tag, final OnGetGoodsDetailListener listener) {
		// TODO Auto-generated method stub
		VolleyHttp.doGetRequestTask(headers, url, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				GoodsDetail detail = new Gson().fromJson(result,
						GoodsDetail.class);
				if (detail.getMessage().getCode() == 200) {
					listener.onSuccess(detail);
				} else {
					listener.onFailed(detail.getMessage().getMessage());
				}
			}

			@Override
			public void onError() {
				listener.onFailed(AppConstant.HTTP_ERROR);
			}
		}, tag);

	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#getCartNum(java.
	 * util.Map,
	 * com.kakao.kakaogift.activity.model.theme.HThemeGoodsModelImpl.OnCartNumListener)
	 */
	@Override
	public void getCartNumWithLogin(Map<String, String> headers,
			final OnGetGoodsDetailListener listenter) {
		// TODO Auto-generated method stub
		VolleyHttp.doGetRequestTask(headers, UrlUtil.GET_CART_NUM_URL,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						CartNumVo vo = new Gson().fromJson(result,
								CartNumVo.class);
						if (vo.getMessage().getCode() == 200) {
							listenter.onSuccess(vo.getCartNum());
						}
					}
					@Override
					public void onError() {
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#cancelCollection
	 * (java.util.Map, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift.activity.
	 * model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener)
	 */
	@Override
	public void cancelCollection(Map<String, String> headers, long collectId,
			String tag, final OnGetGoodsDetailListener listener) {
		// TODO Auto-generated method stub
		VolleyHttp.doGetRequestTask(headers,
				UrlUtil.DEL_COLLECTION + collectId, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						CollectionVo vo = new Gson().fromJson(result,
								CollectionVo.class);
						if (vo.getMessage().getCode() == 200) {
							listener.cancelCollectionSuccess();
						} else {
							listener.onFailed(vo.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						listener.onFailed(AppConstant.HTTP_ERROR);
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#addCollection(java
	 * .util.Map, java.lang.String, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift
	 * .activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener)
	 */
	@Override
	public void addCollection(Map<String, String> headers, StockVo stock, String tag, final OnGetGoodsDetailListener listener) {
		// TODO Auto-generated method stub
		JSONObject params = new JSONObject();
		try {
			params.put("skuId", stock.getId());
			params.put("skuType", stock.getSkuType());
			params.put("skuTypeId", stock.getSkuTypeId());
		} catch (JSONException e) {
		}
		VolleyHttp.doPostRequestTask2(headers, UrlUtil.ADD_COLLECTION,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						CollectionVo vo = new Gson().fromJson(result,
								CollectionVo.class);
						if (vo.getMessage().getCode() == 200) {
							listener.addCollectionSuccess(vo.getCollectId());
						} else {
							listener.onFailed(vo.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						listener.onFailed(AppConstant.HTTP_ERROR);
					}
				}, params.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#addToCartWithLogin
	 * (java.util.Map, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift.activity.
	 * model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener)
	 */
	@Override
	public void addToCartWithLogin(Map<String, String> headers,
			ShoppingGoods goods, final OnGetGoodsDetailListener listener) {
		// TODO Auto-generated method stub
		JSONArray array = null;
		try {
			array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("cartId", 0);
			object.put("skuId", goods.getGoodsId());
			object.put("amount", goods.getGoodsNums());
			object.put("state", goods.getState());
			object.put("skuType", goods.getSkuType());
			object.put("skuTypeId", goods.getSkuTypeId());
			object.put("orCheck", "Y");
			//购物车数据来源,1登陆后同步,2详细页面点击加入购物车,3点击购物车列表页操作(增删减)
			object.put("cartSource", 2);
			array.put(object);
		} catch (JSONException e) {
		}
		VolleyHttp.doPostRequestTask2(headers, UrlUtil.GET_CAR_LIST_URL,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						HMessageVo vo = new Gson().fromJson(result,
								HMessageVo.class);
						if (vo.getMessage().getCode() == 200) {
							listener.addToCartWithLoginSuccess();
						} else {
							listener.onFailed(vo.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						listener.onFailed(AppConstant.HTTP_ERROR);
					}
				}, array.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#addToCartWithoutLogin
	 * (java.lang.String, java.lang.String,
	 * com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModelImpl
	 * .OnGetGoodsDetailListener)
	 */
	@Override
	public void addToCartWithoutLogin(final ShoppingGoods goods,
			final OnGetGoodsDetailListener listener) {
		// TODO Auto-generated method stub
		ShoppingGoods goods2 = DataBaseManager.getInstance().getDaoSession().getShoppingGoodsDao().queryBuilder()
				.where(Properties.GoodsId.eq(goods.getGoodsId()),
						Properties.SkuType.eq(goods.getSkuType()),
						Properties.SkuTypeId.eq(goods.getSkuTypeId())).unique();
		if (goods2 == null) {
			goods.setGoodsNums(1);
			goods.setOrCheck("Y");
		}else{
			goods.setId(goods2.getId());
			goods.setGoodsNums(goods2.getGoodsNums()+1);
			goods.setOrCheck("Y");
		}
		
		JSONObject params = new JSONObject();
		try {
			params.put("skuId", goods.getGoodsId());
			params.put("amount", goods.getGoodsNums());
			params.put("skuType", goods.getSkuType());
			params.put("skuTypeId", goods.getSkuTypeId());
			params.put("orCheck", goods.getOrCheck());
			params.put("cartSource", 2);
		} catch (JSONException e) {
		}
		VolleyHttp.doPostRequestTask2(UrlUtil.POST_ADD_CART,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						HMessageVo vo = new Gson().fromJson(result,
								HMessageVo.class);
						if (vo.getMessage().getCode() == 200) {
							listener.addToCartWithoutLoginSuccess(goods);
						} else {
							listener.onFailed(vo.getMessage().getMessage());
						}
					}

					@Override
					public void onError() {
						listener.onFailed(AppConstant.HTTP_ERROR);
					}
				}, params.toString());
	}



	/* (non-Javadoc)
	 * @see com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModel#getCartNumWithoutLogin(com.kakao.kakaogift.activity.model.gdetail.GoodsDetailModelImpl.OnGetGoodsDetailListener)
	 */
	@Override
	public void getCartNumWithoutLogin(OnGetGoodsDetailListener listenter) {
		// TODO Auto-generated method stub
		int num  = 0;
		List<ShoppingGoods> goods = DataBaseManager.getInstance().getDaoSession().getShoppingGoodsDao()
				.queryBuilder().list();
		for (ShoppingGoods sg : goods) {
			num += sg.getGoodsNums();
		}
		listenter.onSuccess(num);
	}
}
