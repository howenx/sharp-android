package com.hanmimei.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsDetail {
	private Main main;
	private List<Stock> stocks;
	
	 public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}

	public class Main{
		private Integer id;
		private String itemTitle;
		private String itemMasterImg;
		private String onShelvesAt;
		private String offShelvesAt;
		private List<String> itemDetailImgs;
		private List<ItemFeature> itemFeatures;
		private Integer themeId;
		private String state;
		private String shareUrl;
		private Integer collectCount;
		private String itemNotice;
		private List<String> publicity;
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
		public List<String> getItemDetailImgs() {
			return itemDetailImgs;
		}
		public void setItemDetailImgs(List<String> itemDetailImgs) {
			this.itemDetailImgs = itemDetailImgs;
		}
		
		
		public List<ItemFeature> getItemFeatures() {
			return itemFeatures;
		}
		public void setItemFeatures(List<ItemFeature> itemFeatures) {
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
			String result = "";
			for(String s:publicity){
				result +=s+"\n";
			}
			return result;
		}
		public void setPublicity(List<String> publicity) {
			this.publicity = publicity;
		}
		public Integer getMasterInvId() {
			return masterInvId;
		}
		public void setMasterInvId(Integer masterInvId) {
			this.masterInvId = masterInvId;
		}
		
	}
	
	public class Stock{
		private Integer id;
		private String itemColor;
		private String itemSize;
		private Double itemSrcPrice;
		private Double itemPrice;
		private Double itemDiscount;
		private Boolean orMasterInv;
		private String state;
		private Integer shipFee;
		private String invArea;
		private Integer restrictAmount;
		private Integer restAmount;
		private String invImg;
		private List<String> itemPreviewImgs;
		private String invUrl;
		private String invTitle;
		private String invCustom;
		
		
		public String getInvCustom() {
			return invCustom;
		}
		public void setInvCustom(String invCustom) {
			this.invCustom = invCustom;
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
		
		public String getInvImg() {
			return invImg;
		}
		public void setInvImg(String invImg) {
			this.invImg = invImg;
		}
		public List<String> getItemPreviewImgs() {
			return itemPreviewImgs;
		}
		public void setItemPreviewImgs(List<String> itemPreviewImgs) {
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
		public String getItemSrcPrice() {
			return new DecimalFormat("##0.00").format(itemSrcPrice);
		}
		public void setItemSrcPrice(Double itemSrcPrice) {
			this.itemSrcPrice = itemSrcPrice;
		}
		public String getItemPriceFormat() {
			return new DecimalFormat("##0.00").format(itemPrice);
		}
		public Double getItemPrice(){
			return itemPrice;
		}
		public void setItemPrice(Double itemPrice) {
			this.itemPrice = itemPrice;
		}
		
		public Double getItemDiscount() {
			return itemDiscount;
		}
		public void setItemDiscount(Double itemDiscount) {
			this.itemDiscount = itemDiscount;
		}
		public Integer getShipFee() {
			return shipFee;
		}
		public void setShipFee(Integer shipFee) {
			this.shipFee = shipFee;
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
	
	public class ItemFeature implements Parcelable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private String key;
		private String value;
		
		
		public ItemFeature(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}
		public ItemFeature() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel out, int flags) {
			out.writeString(key);
	        out.writeString(value);
		}
		
		
		public final Parcelable.Creator<ItemFeature> CREATOR = new Creator<ItemFeature>()
			    {
			        @Override
			        public ItemFeature[] newArray(int size)
			        {
			            return new ItemFeature[size];
			        }
			        
			        @Override
			        public ItemFeature createFromParcel(Parcel in)
			        {
			            return new ItemFeature(in);
			        }
			    };
			    
			    public ItemFeature(Parcel in)
			    {
			        key = in.readString();
			        value = in.readString();
			    }
	}
}
