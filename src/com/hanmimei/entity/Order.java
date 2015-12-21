package com.hanmimei.entity;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Order implements Serializable{

	private String orderId;		//订单id
	private double payTotal;		//支付总费用
	private String payMethod;		//充值渠道
	private String orderCreateAt;			//订单创建时间
	private String orderStatus;			//I:初始化，S:成功，C：取消，F:失败
	private double discount;				//优惠了多少钱
	private String orderDesc;			//订单备注
	private int addId;					//用户订单地址
	private double shipFee;				//邮费
	private String orderDetailUrl;		//订单相信页面url
	private double totalFee;
	private double postalFee;
	private List<Sku> list;
	private HMMAddress adress;
	private Result result;
	public Order() {
		super();
	}
	
	public double getPostalFee() {
		return postalFee;
	}

	public void setPostalFee(double postalFee) {
		this.postalFee = postalFee;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public double getPayTotal() {
		return payTotal;
	}
	public void setPayTotal(double payTotal) {
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
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
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
	public double getShipFee() {
		return shipFee;
	}
	public void setShipFee(double shipFee) {
		this.shipFee = shipFee;
	}
	public String getOrderDetailUrl() {
		return orderDetailUrl;
	}
	public void setOrderDetailUrl(String orderDetailUrl) {
		this.orderDetailUrl = orderDetailUrl;
	}
	public double getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}
	public List<Sku> getList() {
		return list;
	}
	public void setList(List<Sku> list) {
		this.list = list;
	}
	public HMMAddress getAdress() {
		return adress;
	}
	public void setAdress(HMMAddress adress) {
		this.adress = adress;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}

	

	
}
