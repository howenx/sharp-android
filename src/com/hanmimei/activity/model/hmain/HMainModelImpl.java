/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-30 下午3:50:14 
**/
package com.hanmimei.activity.model.hmain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.VersionVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.XMLPaserTools;

/**
 * @author vince
 *
 */
public class HMainModelImpl implements HMainModel{

	public interface OnCheckVersionListener{
		void onSuccess(VersionVo info);
		void onFailed(String msg);
	}

	/* (non-Javadoc)
	 * @see com.hanmimei.activity.model.hmain.HMainModel#checkVersionInfo()
	 */
	@Override
	public void checkVersionInfo(final OnCheckVersionListener listener) {
		// TODO Auto-generated method stub
		VolleyHttp.doGetRequestTask( UrlUtil.UPDATE_HMM,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							InputStream is = new ByteArrayInputStream(result
									.getBytes());
							VersionVo info = XMLPaserTools.getUpdataInfo(is);
							listener.onSuccess(info);
						} catch (Exception e) {
							listener.onFailed( "获取更新信息错误");
						}
					}

					@Override
					public void onError() {
						listener.onFailed( "获取服务器更新信息失败");
					}
				});
	}
}
