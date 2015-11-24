package com.hanbimei.entity;

import java.util.List;

public class Order {

	private String orderId;
	private String orderNums;
	private String state;
	private String orderTime;
	private List<Goods> list;
	
	
	
	public Order() {
		super();
	}


	
	public Order(String orderNums, String state, String orderTime,
			List<Goods> list) {
		super();
		this.orderNums = orderNums;
		this.state = state;
		this.orderTime = orderTime;
		this.list = list;
	}

	public String getOrderNums() {
		return orderNums;
	}

	public void setOrderNums(String orderNums) {
		this.orderNums = orderNums;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public List<Goods> getList() {
		return list;
	}
	public void setList(List<Goods> list) {
		this.list = list;
	}
	
}
