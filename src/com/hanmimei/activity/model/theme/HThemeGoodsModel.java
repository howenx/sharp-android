/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-24 下午4:34:52 
 **/
package com.hanmimei.activity.model.theme;

import java.util.Map;

import com.hanmimei.activity.model.theme.HThemeGoodsModelImpl.OnCartNumListener;
import com.hanmimei.activity.model.theme.HThemeGoodsModelImpl.OnHThemeGoodsLoadListenter;

/**
 * @author vince
 * 
 */
public interface HThemeGoodsModel {
	void getThemeGoods(Map<String, String> headers, String url, String tag,
			OnHThemeGoodsLoadListenter listenter);
	void getCartNum(Map<String, String> headers,OnCartNumListener listenter);
}
