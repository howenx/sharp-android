/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-25 上午9:38:33 
**/
package com.hanmimei.activity.view.gdetail;

import com.hanmimei.entity.GoodsDetail;

/**
 * @author vince
 *
 */
public interface GoodsDetailView {
	/**
	 * 显示加载动画
	 */
	void  showLoading();
	/**
	 * 隐藏加载动画
	 */
    void  hideLoading();
    /**
     * 获取商品信息
     * @param detail
     */
    void  GetGoodsDetailData(GoodsDetail detail);
    /**
     * 获取购物车数量
     * @param cartNum
     */
    void GetCartNumData(Integer cartNum);
    /**
     * 添加收藏成功
     * @param collectId
     */
    void addCollection(long collectId);
    /**
     * 取消收藏成功
     */
    void cancelCollection();
    /**
     * 添加收藏成功
     */
    void addToCartSuccess();
    /**
     * 加载商品信息失败
     * @param str
     */
    void  showLoadFaild(String str);
}
