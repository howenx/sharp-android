package com.hanbimei.entity;

public class Goods {

	private String id;
	private String imgUrl;
	private String title;
	private String price;
	private int nums;
	private String url;
	
	
	public Goods() {
		super();
	}
	
	public Goods(String imgUrl, String title, String price, int nums, String url) {
		super();
		this.imgUrl = imgUrl;
		this.title = title;
		this.price = price;
		this.nums = nums;
		this.url = url;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getNums() {
		return nums;
	}
	public void setNums(int nums) {
		this.nums = nums;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
