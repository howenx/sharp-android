package com.hanmimei.entity;

import java.io.Serializable;

import com.google.gson.Gson;


public class Sku implements Serializable{
	private String orderId;
	private String skuId;
	private int amount;
	private int price;
	private String skuTitle;
	private String invImg;
	private String invUrl;
	private String itemColor;
	private String itemSize;
	private String what;
	private String skuType;
	private String skuTypeId;
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSkuType() {
		return skuType;
	}
	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}
	public String getSkuTypeId() {
		return skuTypeId;
	}
	public void setSkuTypeId(String skuTypeId) {
		this.skuTypeId = skuTypeId;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public Sku(String skuId, int amount, int price, String skuTitle,
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
	public Sku(String skuId, String invImg) {
		super();
		this.skuId = skuId;
		this.invImg = invImg;
	}
	public Sku() {
		super();
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
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
	public String getInvImg_() {
		return new Gson().fromJson(invImg, ImageVo.class).getUrl();
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