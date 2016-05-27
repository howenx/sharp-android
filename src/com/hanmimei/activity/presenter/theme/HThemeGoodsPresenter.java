/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-24 下午4:43:07 
 **/
package com.hanmimei.activity.presenter.theme;

import java.util.Map;

/**
 * @author vince
 * 
 */
public interface HThemeGoodsPresenter {
	void getHThemeGoodsData(Map<String, String> headers, String url, String tag);
	void getCartNumData(Map<String, String> headers, String tag);
}
