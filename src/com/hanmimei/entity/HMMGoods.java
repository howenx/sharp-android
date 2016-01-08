package com.hanmimei.entity;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "THEME_ITEM".
 */
public class HMMGoods {

	private Long id;
	private String themeId; //  主题id
	private String itemId; // 商品id
	private String itemImg; // 商品图片
	private String itemUrl; // 商品详情页链接
	private String itemUrlAndroid; //商品详情页链接(android 专用)
	private String itemTitle; // 商品标题
	private String itemPrice; // 商品价格
	private double itemDiscount; // 商品折扣
	private String itemSoldAmount; // 商品销售量
	private Boolean orMasterItem = false; // 是否是主题主打宣传商品
	private String collectCount; // 商品收藏数
	private String masterItemTag; // 如果是主打宣传商品，会需要tag json串
	private String masterItemImg; // 如果是主宣传商品图片url
	private String onShelvesAt; //
	private String offShelvesAt; //
	private String invWeight; // 商品重量
	private String invArea; // 发货方式
	private String invAreaNm; // 发货方式
	private String postalTaxRate; // 税率
	private String invCustoms; //保税区

	// 新添
	private String itemMasterImg;
	private String itemSrcPrice; // 主sku原价
	private String state; // 商品状态
	

	// KEEP FIELDS - put your custom fields here
	// KEEP FIELDS END
	
	public ImgInfo getItemMasterImgForImgInfo(){
		return new Gson().fromJson(itemMasterImg, ImgInfo.class);
	}
	
	public ImgInfo getItemImgForImgInfo(){
		return new Gson().fromJson(itemImg, ImgInfo.class);
	}
	
	public List<ImgTag> getMasterItemTagForTag(){
		return new Gson().fromJson(masterItemTag, new TypeToken<List<ImgTag>>(){}.getType());
	}
	
	
	
	public class ImgTag{
		private double top;
		private String url;
		private String name;
		private double left;
		private int angle;
		public double getTop() {
			return top;
		}
		public void setTop(double top) {
			this.top = top;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getLeft() {
			return left;
		}
		public void setLeft(double left) {
			this.left = left;
		}
		public int getAngle() {
			return angle;
		}
		public void setAngle(int angle) {
			this.angle = angle;
		}
	}

	public String getItemMasterImg() {
		return itemMasterImg;
	}

	public String getInvAreaNm() {
		return invAreaNm;
	}

	public void setInvAreaNm(String invAreaNm) {
		this.invAreaNm = invAreaNm;
	}

	public String getItemUrlAndroid() {
		return itemUrlAndroid;
	}

	public void setItemUrlAndroid(String itemUrlAndroid) {
		this.itemUrlAndroid = itemUrlAndroid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThemeId() {
		return themeId;
	}

	public void setThemeId(String themeId) {
		this.themeId = themeId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemImg() {
		return itemImg;
	}

	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public String getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public double getItemDiscount() {
		return itemDiscount;
	}

	public void setItemDiscount(double itemDiscount) {
		this.itemDiscount = itemDiscount;
	}

	public String getItemSoldAmount() {
		return itemSoldAmount;
	}

	public void setItemSoldAmount(String itemSoldAmount) {
		this.itemSoldAmount = itemSoldAmount;
	}

	public Boolean getOrMasterItem() {
		return orMasterItem;
	}

	public void setOrMasterItem(Boolean orMasterItem) {
		this.orMasterItem = orMasterItem;
	}

	public String getCollectCount() {
		return collectCount;
	}

	public void setCollectCount(String collectCount) {
		this.collectCount = collectCount;
	}

	public String getMasterItemTag() {
		return masterItemTag;
	}

	public void setMasterItemTag(String masterItemTag) {
		this.masterItemTag = masterItemTag;
	}

	public String getMasterItemImg() {
		return masterItemImg;
	}

	public void setMasterItemImg(String masterItemImg) {
		this.masterItemImg = masterItemImg;
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

	public String getInvWeight() {
		return invWeight;
	}

	public void setInvWeight(String invWeight) {
		this.invWeight = invWeight;
	}

	public String getInvArea() {
		return invArea;
	}

	public void setInvArea(String invArea) {
		this.invArea = invArea;
	}

	public String getPostalTaxRate() {
		return postalTaxRate;
	}

	public void setPostalTaxRate(String postalTaxRate) {
		this.postalTaxRate = postalTaxRate;
	}

	public String getInvCustoms() {
		return invCustoms;
	}

	public void setInvCustoms(String invCustoms) {
		this.invCustoms = invCustoms;
	}

	public String getItemSrcPrice() {
		return itemSrcPrice;
	}

	public void setItemSrcPrice(String itemSrcPrice) {
		this.itemSrcPrice = itemSrcPrice;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setItemMasterImg(String itemMasterImg) {
		this.itemMasterImg = itemMasterImg;
	}

	
	

	// KEEP METHODS - put your custom methods here
	// KEEP METHODS END

}
