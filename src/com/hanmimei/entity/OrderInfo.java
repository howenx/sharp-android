package com.hanmimei.entity;

import java.io.Serializable;

public class OrderInfo implements Serializable{
	private Order order;
	private HMessage message;
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	
	
}
