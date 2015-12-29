package com.hanmimei.entity;

import java.util.ArrayList;
import java.util.List;

public class HMMThemeGoods {
	
	private Integer cartNum;
	private HMessage message ;
	private List<HMMGoods> themeList;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<HMMGoods> getThemeList() {
		List<HMMGoods> list = new ArrayList<HMMGoods>();
		for(HMMGoods g: themeList){
			if(g.getOrMasterItem())
				continue;
			list.add(g);
		}
		return list;
	}
	public void setThemeList(List<HMMGoods> themeList) {
		this.themeList = themeList;
	}
	public HMMGoods getMasterItem() {
		for(HMMGoods g:themeList){
			if(g.getOrMasterItem())
				return g;
		}
		return null;
		
		
	}
//	public void setMasterItem(HMMGoods masterItem) {
//		this.masterItem = masterItem;
//	}
	public Integer getCartNum() {
		return cartNum;
	}
	public void setCartNum(Integer cartNum) {
		this.cartNum = cartNum;
	}
	
	
	
}
