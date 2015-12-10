package com.hanmimei.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID =  1;
	private HMessage message;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	private List<Customs> list;
	public List<Customs> getList() {
		return list;
	}
	public void setList(List<Customs> list) {
		this.list = list;
	}
	
	private double allPrice = 0;
	public double getAllPrice() {
		return allPrice;
	}
	public void setAllPrice(double allPrice) {
		this.allPrice = allPrice;
	}
	
	

}
