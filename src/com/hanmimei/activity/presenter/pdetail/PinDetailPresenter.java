/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-26 上午9:55:24 
 **/
package com.hanmimei.activity.presenter.pdetail;

import java.util.Map;

import com.hanmimei.entity.StockVo;

/**
 * @author vince
 * 
 */
public interface PinDetailPresenter {
	void getPinDetail(Map<String, String> headers, String url, String tag);

	void cancelCollection(Map<String, String> headers, long collectId);

	void addCollection(Map<String, String> headers, StockVo stock);
}
