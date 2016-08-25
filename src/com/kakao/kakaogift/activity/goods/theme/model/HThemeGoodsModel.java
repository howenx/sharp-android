/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-24 下午4:34:52 
 **/
package com.kakao.kakaogift.activity.goods.theme.model;

import java.util.Map;

import com.kakao.kakaogift.activity.goods.theme.model.HThemeGoodsModelImpl.OnHThemeGoodsLoadListenter;

/**
 * @author vince
 * 
 */
public interface HThemeGoodsModel {
	void getThemeGoods(Map<String, String> headers, String url, String tag,
			OnHThemeGoodsLoadListenter listenter);
}
