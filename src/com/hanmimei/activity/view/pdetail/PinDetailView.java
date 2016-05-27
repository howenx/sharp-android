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
	void  showLoading();
    void  hideLoading();
    void  loadPinDetailData(PinDetail detail);
    void addCollectionSuccess(long collectId);
    void cancelCollectionSuccess();
    void  showLoadFaild(String str);
}
