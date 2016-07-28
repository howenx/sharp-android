package com.kakao.kakaogift.entity;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.kakaogift.entity.HGoodsVo.ImgTag;

public class HThemeGoods {

	private Integer cartNum;

	private HMessage message;
	private ThemeList themeList;

	public ThemeList getThemeList() {
		return themeList;
	}

	public void setThemeList(ThemeList themeList) {
		this.themeList = themeList;
	}

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}

	public Integer getCartNum() {
		return cartNum;
	}

	public void setCartNum(Integer cartNum) {
		this.cartNum = cartNum;
	}

	public class ThemeList {
		private String themeId;
		private String themeImg;
		private String masterItemTag;
		private String masterItemTagAndroid;

		private List<HGoodsVo> themeItemList;

		private String title;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public List<HGoodsVo> getThemeItemList() {
			return themeItemList;
		}

		public void setThemeItemList(List<HGoodsVo> themeItemList) {
			this.themeItemList = themeItemList;
		}

		public String getThemeId() {
			return themeId;
		}

		public void setThemeId(String themeId) {
			this.themeId = themeId;
		}

		public ImageVo getThemeImg() {
			if(themeImg == null)
				return null;
			return new Gson().fromJson(themeImg, ImageVo.class);
		}

		public List<ImgTag> getMasterItemTag() {
			return new Gson().fromJson(masterItemTag,
					new TypeToken<List<ImgTag>>() {
					}.getType());
		}

		public void setThemeImg(String themeImg) {
			this.themeImg = themeImg;
		}

		public void setMasterItemTag(String masterItemTag) {
			this.masterItemTag = masterItemTag;
		}

		public List<ImgTag> getMasterItemTagAndroid() {
			return new Gson().fromJson(masterItemTagAndroid,
					new TypeToken<List<ImgTag>>() {
					}.getType());
		}

		public void setMasterItemTagAndroid(String masterItemTagAndroid) {
			this.masterItemTagAndroid = masterItemTagAndroid;
		}
	}

}
