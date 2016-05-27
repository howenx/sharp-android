/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:38:33 
**/
package com.hanmimei.activity.view.gdetail;

import java.util.Map;

import com.hanmimei.entity.GoodsDetail;

/**
 * @author vince
 *
 */
public interface GoodsDetailView {
	void  showLoading();
    void  hideLoading();
    void  GetGoodsDetailData(GoodsDetail detail);
    void GetCartNumData(Integer cartNum);
    void addCollection(long collectId);
    void cancelCollection();
    void addToCartSuccess();
    void  showLoadFaild(String str);
}
