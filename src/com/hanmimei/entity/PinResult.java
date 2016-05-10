package com.hanmimei.entity;

import java.util.List;


public class PinResult {
	private PinActivity activity;
	private HMessage message;
	//推荐的拼购商品
    private List<HGoodsVo> themeList;
	
	public List<HGoodsVo> getThemeList() {
		return themeList;
	}
	public void setThemeList(List<HGoodsVo> themeList) {
		this.themeList = themeList;
	}
	public PinActivity getActivity() {
		return activity;
	}
	public void setActivity(PinActivity activity) {
		this.activity = activity;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	
	
}
