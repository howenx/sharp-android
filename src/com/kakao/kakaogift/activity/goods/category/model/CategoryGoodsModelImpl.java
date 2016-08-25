/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午11:06:53 
**/
package com.kakao.kakaogift.activity.goods.category.model;

import com.google.gson.Gson;
import com.kakao.kakaogift.activity.goods.category.model.vo.CategoryGoods;
import com.kakao.kakaogift.activity.goods.category.presenter.CategoryGoodsPresenterImpl.CallBack;
import com.kakao.kakaogift.entity.HThemeGoods;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;

/**
 * @author vince
 *
 */
public class CategoryGoodsModelImpl implements CategoryGoodsModel{
	

	public CategoryGoodsModelImpl() {
		super();
	}

	@Override
	public void loadCategoryGoodsList(String url , int pageNo,final CallBack callBack) {
			VolleyHttp.doGetRequestTask(url +pageNo, new VolleyJsonCallback() {
				
				@Override
				public void onSuccess(String result) {
					CategoryGoods data = new Gson().fromJson(result, CategoryGoods.class);
					if(data.getMessage().getCode() == 200){
						callBack.onSuccess(data.getThemeItemList());
					}else{
						callBack.onFailed(data.getMessage().getMessage());
					}
				}
				
				@Override
				public void onError() {
					callBack.onFailed("请求错误");
				}
			});
	}

}
