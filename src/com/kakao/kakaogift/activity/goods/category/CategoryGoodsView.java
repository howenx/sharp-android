/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午11:02:59 
**/
package com.kakao.kakaogift.activity.goods.category;

import java.util.List;

import com.kakao.kakaogift.entity.HGoodsVo;


/**
 * @author vince
 *
 */
public interface CategoryGoodsView {
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
    void  CategoryGoodsData(List<HGoodsVo> data);
    
    void title(String title);
    
    /**
     * 加载失败 返回信息
     * @param str
     */
    void  showLoadFaild(String str);
}
