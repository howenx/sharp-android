package com.kakao.kakaogift.entity;

import java.io.Serializable;

public class ImageVo implements Serializable{
	private String url;
	private int width;
	private int height;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public ImageVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ImageVo(String url) {
		super();
		this.url = url;
	}
	
	
}
