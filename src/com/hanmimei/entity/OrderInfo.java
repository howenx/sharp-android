package com.hanmimei.entity;

import java.io.Serializable;

public class OrderInfo implements Serializable{
	private String orderId;
	private HMessage message;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	
	
}
