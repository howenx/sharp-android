/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午3:52:31 
**/
package com.kakao.kakaogift.activity.main.model;

import com.kakao.kakaogift.activity.main.model.HMainModelImpl.OnCheckVersionListener;

/**
 * @author vince
 *
 */
public interface HMainModel {
	void checkVersionInfo(OnCheckVersionListener listener);
}
