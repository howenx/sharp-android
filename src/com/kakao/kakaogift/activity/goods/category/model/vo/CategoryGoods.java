/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午11:17:28 
**/
package com.kakao.kakaogift.activity.goods.category.model.vo;

import java.util.List;

import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.HMessageVo;

/**
 * @author vince
 *
 */
public class CategoryGoods {
	private HMessage message;
	private int page_count;
	private List<HGoodsVo> themeItemList;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public int getPage_count() {
		return page_count;
	}
	public void setPage_count(int page_count) {
		this.page_count = page_count;
	}
	public List<HGoodsVo> getThemeItemList() {
		return themeItemList;
	}
	public void setThemeItemList(List<HGoodsVo> themeItemList) {
		this.themeItemList = themeItemList;
	}
	
	
	
}
