/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午3:52:31 
**/
package com.hanmimei.activity.model.hmain;

import com.hanmimei.activity.model.hmain.HMainModelImpl.OnCheckVersionListener;

/**
 * @author vince
 *
 */
public interface HMainModel {
	void checkVersionInfo(OnCheckVersionListener listener);
}
