/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午3:56:36 
**/
package com.kakao.kakaogift.activity.main;

import com.kakao.kakaogift.entity.VersionVo;

/**
 * @author vince
 *
 */
public interface HMainView {
	void loadVersionInfo(VersionVo info);
	void onLoadFailed(String msg);
}
