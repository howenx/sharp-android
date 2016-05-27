/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:39:34 
**/
package com.hanmimei.activity.presenter.gdetail;

import java.util.Map;

import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;

/**
 * @author vince
 *
 */
public interface GoodsDetailPresenter {
	void getGoodsDetailData(Map<String, String> headers, String url, String tag);
	void getCartNumData(Map<String, String> headers, String tag);
	void addCollection(Map<String, String> headers,StockVo stock);
	void cancelCollection(Map<String, String> headers, long collectId );
	void addToCart(Map<String, String> headers,ShoppingGoods goods);
}
