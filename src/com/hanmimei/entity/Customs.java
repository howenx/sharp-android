package com.hanmimei.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customs implements Serializable{
	private String invArea;
	private String  invCustoms;
	private int postFee;
	private double tax;
	private int postalLimit;
	private int postalStandard;
	
	
	
	public int getPostalLimit() {
		return postalLimit;
	}
	public void setPostalLimit(int postalLimit) {
		this.postalLimit = postalLimit;
	}
	public int getPostalStandard() {
		return postalStandard;
	}
	public void setPostalStandard(int postalStandard) {
		this.postalStandard = postalStandard;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public int getPostFee() {
		return postFee;
	}
	public void setPostFee(int postFee) {
		this.postFee = postFee;
	}

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
	
	public void addShoppingGoods(ShoppingGoods shoppingGoods){
		if(list == null)
			list = new ArrayList<ShoppingGoods>();
		list.add(shoppingGoods);
	}

}
