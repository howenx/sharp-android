/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-26 上午9:47:44 
**/
package com.hanmimei.activity.model.pdetail;

import java.util.Map;

import com.hanmimei.activity.model.pdetail.PinDetailModelImpl.OnPinDetailListener;
import com.hanmimei.entity.StockVo;

/**
 * @author vince
 *
 */
public interface PinDetailModel {
	void getPinDetail(Map<String, String> headers,String url ,String tag,OnPinDetailListener listener);
	void cancelCollection(Map<String, String> headers,long collectId , String tag , OnPinDetailListener listener);
	void addCollection(Map<String, String> headers,StockVo stock, String tag , OnPinDetailListener listener);
}
