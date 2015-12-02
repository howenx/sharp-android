package com.hanmimei.entity;

import java.util.List;

public class ThemeDetail {
	
	private HMessage message ;
	private List<ThemeItem> themeList;
	private ThemeItem masterItem;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<ThemeItem> getThemeList() {
		
		return themeList;
	}
	public void setThemeList(List<ThemeItem> themeList) {
		this.themeList = themeList;
	}
	public ThemeItem getMasterItem() {
		return masterItem;
	}
	public void setMasterItem(ThemeItem masterItem) {
		this.masterItem = masterItem;
	}
	
	
	
}
