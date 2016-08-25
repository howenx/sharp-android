/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午10:48:51 
**/
package com.kakao.kakaogift.activity.goods.category.model;

import com.kakao.kakaogift.activity.goods.category.presenter.CategoryGoodsPresenterImpl.CallBack;

/**
 * @author vince
 *
 */
public interface CategoryGoodsModel {
	void loadCategoryGoodsList(String url , int pageNo , CallBack callBack);
}
