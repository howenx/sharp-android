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
	/**
	 * 获取拼购信息
	 * @param headers
	 * @param url
	 * @param tag
	 */
	void getPinDetail(Map<String, String> headers, String url, String tag);
	/**
	 * 取消收藏
	 * @param headers
	 * @param collectId
	 */
	void cancelCollection(Map<String, String> headers, long collectId);
	/**
	 * 添加收藏
	 * @param headers
	 * @param stock
	 */
	void addCollection(Map<String, String> headers, StockVo stock);
}
