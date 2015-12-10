package com.hanmimei.entity;

import java.io.Serializable;
import java.util.List;

public class ShoppingCar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private HMessage message;
	private Double allPrice = 0.00;
	private List<Customs> list;

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}

	public List<Customs> getList() {
		return list;
	}

	public void setList(List<Customs> list) {
		this.list = list;
	}

	public Double getAllPrice() {
		return allPrice;
	}

	public void setAllPrice(Double allPrice) {
		this.allPrice = allPrice;
	}
	
	
	

}
