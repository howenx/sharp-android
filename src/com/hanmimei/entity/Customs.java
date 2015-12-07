package com.hanmimei.entity;

import java.util.List;

public class Customs {
	private String invArea;
	private String  invCustoms;
	private List<ShoppingGoods> list;
	
	public String getInvArea() {
		return invArea;
	}
	public void setInvArea(String invArea) {
		this.invArea = invArea;
	}
	public String getInvCustoms() {
		return invCustoms;
	}
	public void setInvCustoms(String invCustoms) {
		this.invCustoms = invCustoms;
	}
	public List<ShoppingGoods> getList() {
		return list;
	}
	public void setList(List<ShoppingGoods> list) {
		this.list = list;
	}
	
	public Customs(String invArea, String invCustoms, List<ShoppingGoods> list) {
		super();
		this.invArea = invArea;
		this.invCustoms = invCustoms;
		this.list = list;
	}
	public Customs() {
		super();
	}
	

}
