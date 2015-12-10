package com.hanmimei.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Customs implements Serializable{
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
	
	
	public Double getAllPrice() {
		Double allPrice = 0.00;
		for(ShoppingGoods sg : list){
			allPrice += sg.getGoodsPrice() *sg.getGoodsNums();
		}
		return allPrice;
	}
	
	public void addShoppingGoods(ShoppingGoods shoppingGoods){
		if(list == null)
			list = new ArrayList<ShoppingGoods>();
		list.add(shoppingGoods);
	}
	
	public Double getAllPoastalFee(){
		Double postFee = 0.00;
		for(ShoppingGoods sg : list){
			Log.i(sg.getGoodsName(), postFee+"");
			postFee += sg.getPoastalFee();
		}
		return postFee;
	}

}
