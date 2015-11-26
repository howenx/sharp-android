package com.hanbimei.entity;

import java.io.Serializable;


public class Sku implements Serializable{
	private int skuId;
	private int amount;
	private int price;
	private String skuTitle;
	private String invImg;
	private String invUrl;
	private String itemColor;
	private String itemSize;
	public Sku(int skuId, int amount, int price, String skuTitle,
			String invImg, String invUrl, String itemColor, String itemSize) {
		super();
		this.skuId = skuId;
		this.amount = amount;
		this.price = price;
		this.skuTitle = skuTitle;
		this.invImg = invImg;
		this.invUrl = invUrl;
		this.itemColor = itemColor;
		this.itemSize = itemSize;
	}
	public Sku() {
		super();
	}
	public int getSkuId() {
		return skuId;
	}
	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getSkuTitle() {
		return skuTitle;
	}
	public void setSkuTitle(String skuTitle) {
		this.skuTitle = skuTitle;
	}
	public String getInvImg() {
		return invImg;
	}
	public void setInvImg(String invImg) {
		this.invImg = invImg;
	}
	public String getInvUrl() {
		return invUrl;
	}
	public void setInvUrl(String invUrl) {
		this.invUrl = invUrl;
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
	
}