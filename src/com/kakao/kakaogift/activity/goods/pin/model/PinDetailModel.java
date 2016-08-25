/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-26 上午9:47:44 
**/
package com.kakao.kakaogift.activity.goods.pin.model;

import java.util.Map;

import com.kakao.kakaogift.activity.goods.pin.model.PinDetailModelImpl.OnPinDetailListener;
import com.kakao.kakaogift.entity.StockVo;

/**
 * @author vince
 *
 */
public interface PinDetailModel {
	void getPinDetail(Map<String, String> headers,String url ,String tag,OnPinDetailListener listener);
	void cancelCollection(Map<String, String> headers,long collectId , String tag , OnPinDetailListener listener);
	void addCollection(Map<String, String> headers,StockVo stock, String tag , OnPinDetailListener listener);
}
