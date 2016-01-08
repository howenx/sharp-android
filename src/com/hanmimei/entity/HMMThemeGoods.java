package com.hanmimei.entity;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanmimei.entity.HMMGoods.ImgTag;

public class HMMThemeGoods {
	
	private Integer cartNum;
	private HMessage message ;
	private String themeId;
	private String themeImg;
	private String masterItemTag;
	
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
	

	public Integer getCartNum() {
		return cartNum;
	}
	public void setCartNum(Integer cartNum) {
		this.cartNum = cartNum;
	}
	public String getThemeId() {
		return themeId;
	}
	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}
	public ImgInfo getThemeImg() {
		return  new Gson().fromJson(themeImg, ImgInfo.class);
	}
	public List<ImgTag> getMasterItemTag(){
		return new Gson().fromJson(masterItemTag, new TypeToken<List<ImgTag>>(){}.getType());
	}
	public void setThemeImg(String themeImg) {
		this.themeImg = themeImg;
	}
	public void setMasterItemTag(String masterItemTag) {
		this.masterItemTag = masterItemTag;
	}
}
