package com.hanmimei.entity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Order implements Serializable{

	private String orderId;		//订单id
	private int payTotal;		//支付总费用
	private String payMethod;		//充值渠道
	private String orderCreateAt;			//订单创建时间
	private String orderStatus;			//I:初始化，S:成功，C：取消，F:失败
	private int discount;				//优惠了多少钱
	private String orderDesc;			//订单备注
	private int addId;					//用户订单地址
	private int shipFee;				//邮费
	private String orderDetailUrl;		//订单相信页面url
	private List<Sku> list;
	private Adress adress;
	private Result result;
	public Order() {
		super();
	}

	public Order(String orderId, int payTotal, String payMethod,
			String orderCreateAt, String orderStatus, int discount,
			String orderDesc, int addId, int shipFee, String orderDetailUrl,
			List<Sku> list, Adress adress, Result result) {
		super();
		this.orderId = orderId;
		this.payTotal = payTotal;
		this.payMethod = payMethod;
		this.orderCreateAt = orderCreateAt;
		this.orderStatus = orderStatus;
		this.discount = discount;
		this.orderDesc = orderDesc;
		this.addId = addId;
		this.shipFee = shipFee;
		this.orderDetailUrl = orderDetailUrl;
		this.list = list;
		this.adress = adress;
		this.result = result;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getPayTotal() {
		return payTotal;
	}

	public void setPayTotal(int payTotal) {
		this.payTotal = payTotal;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getOrderCreateAt() {
		return orderCreateAt;
	}

	public void setOrderCreateAt(String orderCreateAt) {
		this.orderCreateAt = orderCreateAt;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public int getAddId() {
		return addId;
	}

	public void setAddId(int addId) {
		this.addId = addId;
	}

	public int getShipFee() {
		return shipFee;
	}

	public void setShipFee(int shipFee) {
		this.shipFee = shipFee;
	}

	public String getOrderDetailUrl() {
		return orderDetailUrl;
	}

	public void setOrderDetailUrl(String orderDetailUrl) {
		this.orderDetailUrl = orderDetailUrl;
	}

	public List<Sku> getList() {
		return list;
	}

	public void setList(List<Sku> list) {
		this.list = list;
	}

	public Adress getAdress() {
		return adress;
	}

	public void setAdress(Adress adress) {
		this.adress = adress;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}


	
}
