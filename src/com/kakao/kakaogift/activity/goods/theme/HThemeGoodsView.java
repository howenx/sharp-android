/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-24 下午4:41:07 
**/
package com.kakao.kakaogift.activity.goods.theme;

import com.kakao.kakaogift.entity.HThemeGoods;

/**
 * @author vince
 *
 */
public interface HThemeGoodsView {
	/**
	 * <a> 预加载方法
	 */
	    void  showLoading();
	    /**
		 * <a> 隐藏预加载loading
		 */
	    void  hideLoading();
	    /**
		 * <a> 获取主题商品参数
		 */
	    void  GetHThemeGoodsData(HThemeGoods detail);
	    /**
		 * <a> 获取购物车数量参数
		 */
	    void GetCartNumData(Integer cartNum);
	    /**
		 * <a> 加载失败
		 */
	    void  showLoadFaild(String str);
}
