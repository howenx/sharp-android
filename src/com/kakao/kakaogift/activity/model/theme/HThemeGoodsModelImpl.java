/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-24 下午4:34:52 
 **/
package com.kakao.kakaogift.activity.model.theme;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CartNumVo;
import com.kakao.kakaogift.entity.HThemeGoods;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.DataBaseManager;

/**
 * @author vince
 * 
 */
public class HThemeGoodsModelImpl implements HThemeGoodsModel {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.goods.theme.model.HThemeGoodsModel#getImageNewList
	 * (int, ImageListModelImpl.OnImageNewLoadListenter)
	 */
	@Override
	public void getThemeGoods(final Map<String, String> headers, String url,
			String tag, final OnHThemeGoodsLoadListenter listenter) {
		VolleyHttp.doGetRequestTask(headers, url, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				HThemeGoods detail = new Gson().fromJson(result,
						HThemeGoods.class);
				if (detail.getMessage().getCode() == 200) {
					if (headers == null) {
						List<ShoppingGoods> goods = DataBaseManager
								.getInstance().getDaoSession()
								.getShoppingGoodsDao().queryBuilder().list();
						int cartNum = 0;
						for (ShoppingGoods sg : goods) {
							cartNum += sg.getGoodsNums();
						}
						detail.setCartNum(cartNum);
					}
					listenter.onSuccess(detail);
				} else {
					listenter.onFailed(detail.getMessage().getMessage());
				}
			}

			@Override
			public void onError() {
				listenter.onFailed(AppConstant.HTTP_ERROR);
			}
		}, tag);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.model.theme.HThemeGoodsModel#getCartNum(java.util
	 * .Map, java.lang.String, java.lang.String,
	 * com.kakao.kakaogift.activity.model.theme
	 * .HThemeGoodsModelImpl.OnHThemeGoodsLoadListenter)
	 */
	@Override
	public void getCartNum(Map<String, String> headers,
			final OnCartNumListener listenter) {
		// TODO Auto-generated method stub
		if (headers == null) {
			List<ShoppingGoods> goods = DataBaseManager.getInstance()
					.getDaoSession().getShoppingGoodsDao().queryBuilder()
					.list();
			int num = 0;
			for (ShoppingGoods sg : goods) {
				num += sg.getGoodsNums();
			}
			listenter.onSuccess(num);
			return;
		}
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

	public interface OnHThemeGoodsLoadListenter {
		void onSuccess(HThemeGoods detail);

		void onFailed(String msg);
	}

	public interface OnCartNumListener {
		void onSuccess(Integer cartNum);
	}

}
