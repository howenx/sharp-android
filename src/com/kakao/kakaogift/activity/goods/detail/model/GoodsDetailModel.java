/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:26:21 
 **/
package com.kakao.kakaogift.activity.goods.detail.model;

import java.util.Map;

import android.content.Context;

import com.kakao.kakaogift.activity.goods.detail.model.GoodsDetailModelImpl.OnGetGoodsDetailListener;
import com.kakao.kakaogift.dao.ShoppingGoodsDao;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.StockVo;

/**
 * @author vince
 * 
 */
public interface GoodsDetailModel {
	void getGoodsDetail(Map<String, String> headers, String url, String tag,
			OnGetGoodsDetailListener listener);

	void getCartNumWithLogin(Map<String, String> headers,
			OnGetGoodsDetailListener listenter);

	void getCartNumWithoutLogin(ShoppingGoodsDao dao,OnGetGoodsDetailListener listenter);

	void cancelCollection(Map<String, String> headers, long collectId,
			String tag, OnGetGoodsDetailListener listener);

	void addCollection(Map<String, String> headers, StockVo stock, String tag,
			OnGetGoodsDetailListener listener);

	void addToCartWithLogin(Map<String, String> headers, ShoppingGoods goods,
			OnGetGoodsDetailListener listener);

	void addToCartWithoutLogin(ShoppingGoodsDao dao,ShoppingGoods goods,
			final OnGetGoodsDetailListener listener);
}
