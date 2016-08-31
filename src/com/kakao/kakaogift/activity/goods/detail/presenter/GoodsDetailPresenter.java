/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:39:34 
**/
package com.kakao.kakaogift.activity.goods.detail.presenter;

import java.util.Map;

import android.content.Context;

import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.StockVo;

/**
 * @author vince
 *
 */
public interface GoodsDetailPresenter {
	void getGoodsDetailData(Map<String, String> headers, String url, String tag);
	void getCartNumData(Map<String, String> headers, String tag);
	void addCollection(Map<String, String> headers,StockVo stock);
	void cancelCollection(Map<String, String> headers, long collectId);
	void addToCart(Map<String, String> headers,ShoppingGoods goods);
}
