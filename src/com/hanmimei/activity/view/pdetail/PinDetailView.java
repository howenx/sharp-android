/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-26 上午9:51:32 
**/
package com.hanmimei.activity.view.pdetail;

import com.hanmimei.entity.PinDetail;

/**
 * @author vince
 *
 */
public interface PinDetailView {
	/**
	 * 显示加载动画
	 */
	void  showLoading();
	/**
	 * 隐藏加载动画
	 */
    void  hideLoading();
    /**
     * 加载拼购信息
     * @param detail
     */
    void  loadPinDetailData(PinDetail detail);
    /**
     * 添加收藏成功
     * @param collectId
     */
    void addCollectionSuccess(long collectId);
    /**
     * 取消收藏成功
     */
    void cancelCollectionSuccess();
    /**
     * 加载失败 返回信息
     * @param str
     */
    void  showLoadFaild(String str);
}
