package com.hanmimei.entity;

import java.util.List;

public class ShoppingCar {
	private HMessage message;
	private List<ShoppingGoods> list;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<ShoppingGoods> getList() {
		return list;
	}
	public void setList(List<ShoppingGoods> list) {
		this.list = list;
	}
	

}
