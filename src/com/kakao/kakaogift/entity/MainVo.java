package com.kakao.kakaogift.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainVo {
	private String id;
	private String itemTitle;
	private String itemMasterImg;
	private String onShelvesAt;
	private String offShelvesAt;
	private String itemDetailImgs;
	private String itemFeatures;
	private Integer themeId;
	private String state;
	private String shareUrl;
	private Integer collectCount;
	private String itemNotice;
	private String publicity;
	private Integer masterInvId;

	public MainVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemMasterImg() {
		return itemMasterImg;
	}

	public void setItemMasterImg(String itemMasterImg) {
		this.itemMasterImg = itemMasterImg;
	}

	public String getOnShelvesAt() {
		return onShelvesAt;
	}

	public void setOnShelvesAt(String onShelvesAt) {
		this.onShelvesAt = onShelvesAt;
	}

	public String getOffShelvesAt() {
		return offShelvesAt;
	}

	public void setOffShelvesAt(String offShelvesAt) {
		this.offShelvesAt = offShelvesAt;
	}

	public String getItemDetailImgs() {
		return itemDetailImgs;
	}

	public void setItemDetailImgs(String itemDetailImgs) {
		this.itemDetailImgs = itemDetailImgs;
	}

	public List<ItemFeature> getItemFeatures() {
		List<ItemFeature> list = new ArrayList<ItemFeature>();
		Map<String, String> map = new Gson().fromJson(itemFeatures,
				new TypeToken<Map<String, String>>() {
				}.getType());
		ItemFeature f = null;
		for (String key : map.keySet()) {
			f = new ItemFeature();
			f.setKey(key);
			f.setValue(map.get(key));
			list.add(f);
		}
		return list;
	}

	public String getItemFeaturess() {
		return itemFeatures;
	}
	
	public void setItemFeatures(String itemFeatures) {
		this.itemFeatures = itemFeatures;
	}

	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public Integer getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(Integer collectCount) {
		this.collectCount = collectCount;
	}

	public String getItemNotice() {
		return itemNotice;
	}

	public void setItemNotice(String itemNotice) {
		this.itemNotice = itemNotice;
	}

	public String getPublicity() {
		if(publicity == null)
			return null;
		List<String> result = new Gson().fromJson(publicity,
				new TypeToken<List<String>>() {
				}.getType());
		String str = "";
		for (int i=0;i<result.size();i++) {
			str += result.get(i);
			if(i <result.size()-1)
				str +="\n";
		}
		return str;
	}

	public void setPublicity(String publicity) {
		this.publicity = publicity;
	}

	public Integer getMasterInvId() {
		return masterInvId;
	}

	public void setMasterInvId(Integer masterInvId) {
		this.masterInvId = masterInvId;
	}
}
