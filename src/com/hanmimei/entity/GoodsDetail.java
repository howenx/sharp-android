package com.hanmimei.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GoodsDetail {
	private Main main;
	private List<Stock> stock;
	private HMessage message;
	private Integer cartNum;
	
	
	public Stock getCurrentStock(){
		Stock sto = new Stock();
		for (int i = 0; i < stock.size(); i++) {
			if (stock.get(i).getOrMasterInv()) {
				sto = stock.get(i);
			}
		}
		return sto;
	}
	public Integer getCartNum() {
		return cartNum;
	}

	public void setCartNum(Integer cartNum) {
		this.cartNum = cartNum;
	}

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public List<Stock> getStock() {
		return stock;
	}

	public void setStock(List<Stock> stock) {
		this.stock = stock;
	}

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}

	public class Main {
		private Integer id;
		private String itemTitle;
		private String itemMasterImg;
		private String onShelvesAt;
		private String offShelvesAt;
		private String itemDetailImgs;
		// private List<ItemFeature> itemFeatures;
		private String itemFeatures;
		private Integer themeId;
		private String state;
		private String shareUrl;
		private Integer collectCount;
		private String itemNotice;
		// private List<String> publicity;
		private String publicity;
		private Integer masterInvId;

		public Main() {
			super();
			// TODO Auto-generated constructor stub
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
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
			List<String> result = new Gson().fromJson(publicity,
					new TypeToken<List<String>>() {
					}.getType());
			String str = "";
			for (String s : result) {
				str += s + "\n";
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

	public class Stock {
		private Integer id;
		private String itemColor;
		private String itemSize;
		private BigDecimal itemSrcPrice;
		private BigDecimal itemPrice;
		private BigDecimal itemDiscount;
		private Boolean orMasterInv;
		private String state;
		private BigDecimal shipFee;
		private String invArea;
		private String invAreaNm;
		private Integer restrictAmount;  
		private Integer restAmount;
		private String invImg;
		private String itemPreviewImgs;
		private String invUrl;
		private String invTitle;
		private String invCustoms;
		private Integer postalTaxRate;
		private Integer postalStandard;// 关税收费标准
		
		
		public ImgInfo getInvImgForObj(){
			return new Gson().fromJson(invImg, ImgInfo.class);
		}
		public String getInvImg(){
			return invImg;
		}
		
		public ArrayList<ImgInfo> getItemPreviewImgsForList(){
			return new Gson().fromJson(itemPreviewImgs, new TypeToken<ArrayList<ImgInfo>>(){}.getType());
		}

		public String getItemPreviewImgs() {
			return itemPreviewImgs;
		}
		public String getInvAreaNm() {
			return invAreaNm;
		}

		public void setInvAreaNm(String invAreaNm) {
			this.invAreaNm = invAreaNm;
		}

		public Integer getPostalStandard() {
			return postalStandard;
		}

		public void setPostalStandard(Integer postalStandard) {
			this.postalStandard = postalStandard;
		}


		public void setPostalTaxRate(int postalTaxRate) {
			this.postalTaxRate = postalTaxRate;
		}

		public String getInvCustoms() {
			return invCustoms;
		}

		public void setInvCustoms(String invCustoms) {
			this.invCustoms = invCustoms;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getItemColor() {
			return itemColor;
		}

		public void setItemColor(String itemColor) {
			this.itemColor = itemColor;
		}

		public String getItemSize() {
			return itemSize;
		}

		public void setItemSize(String itemSize) {
			this.itemSize = itemSize;
		}

		public Boolean getOrMasterInv() {
			return orMasterInv;
		}

		public void setOrMasterInv(Boolean orMasterInv) {
			this.orMasterInv = orMasterInv;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getInvArea() {
			return invArea;
		}

		public void setInvArea(String invArea) {
			this.invArea = invArea;
		}


		public void setInvImg(String invImg) {
			this.invImg = invImg;
		}


		public void setItemPreviewImgs(String itemPreviewImgs) {
			this.itemPreviewImgs = itemPreviewImgs;
		}

		public String getInvUrl() {
			return invUrl;
		}

		public void setInvUrl(String invUrl) {
			this.invUrl = invUrl;
		}

		public String getInvTitle() {
			return invTitle;
		}

		public void setInvTitle(String invTitle) {
			this.invTitle = invTitle;
		}

		public BigDecimal getItemSrcPrice() {
			return itemSrcPrice;
		}

		public void setItemSrcPrice(BigDecimal itemSrcPrice) {
			this.itemSrcPrice = itemSrcPrice;
		}

		
		public BigDecimal getItemPrice() {
			return itemPrice;
		}

		public void setItemPrice(BigDecimal itemPrice) {
			this.itemPrice = itemPrice;
		}

		public BigDecimal getItemDiscount() {
			return itemDiscount;
		}

		public void setItemDiscount(BigDecimal itemDiscount) {
			this.itemDiscount = itemDiscount;
		}

		
		public BigDecimal getShipFee() {
			return shipFee;
		}

		public void setShipFee(BigDecimal shipFee) {
			this.shipFee = shipFee;
		}

		public Integer getPostalTaxRate() {
			return postalTaxRate;
		}

		public void setPostalTaxRate(Integer postalTaxRate) {
			this.postalTaxRate = postalTaxRate;
		}

		public Integer getRestrictAmount() {
			return restrictAmount;
		}

		public void setRestrictAmount(Integer restrictAmount) {
			this.restrictAmount = restrictAmount;
		}

		public Integer getRestAmount() {
			return restAmount;
		}

		public void setRestAmount(Integer restAmount) {
			this.restAmount = restAmount;
		}
	}

	
}
