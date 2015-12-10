package com.hanmimei.entity;

import java.util.List;

public class HMMThemeGoods {
	
	private HMessage message ;
	private List<HMMGoods> themeList;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<HMMGoods> getThemeList() {
		
		return themeList;
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
	
	
	
}
