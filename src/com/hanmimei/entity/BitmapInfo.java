package com.hanmimei.entity;

public class BitmapInfo {
	private String url;
	private float scaleSize;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getScaleSize() {
		return scaleSize;
	}
	public void setScaleSize(float scaleSize) {
		this.scaleSize = scaleSize;
	}
	public BitmapInfo(String url, float scaleSize) {
		super();
		this.url = url;
		this.scaleSize = scaleSize;
	}
	public BitmapInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
