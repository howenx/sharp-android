package com.hanmimei.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ShoppingCar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private HMessage message;
	private List<Customs> list;
	 private String orderType = "item";       //订单类型:1.vary,2.item,3.customize,4.pin
	
	private BigDecimal shipFee =  BigDecimal.ZERO;			//理论邮费
	private BigDecimal portalFee = BigDecimal.ZERO;		//理论行邮税
	private BigDecimal factPortalFee =  BigDecimal.ZERO;	//	实际行邮税
	private BigDecimal factShipFee =  BigDecimal.ZERO; //实际邮费
	private BigDecimal denomination =  BigDecimal.ZERO; //优惠券额度
	private BigDecimal discountFee = BigDecimal.ZERO;	//商品活动优惠额度
	private BigDecimal totalFee = BigDecimal.ZERO;
	
	 private Integer buyNow = 1;         //1.立即支付,2.购物车结算

	public void setDiscountFee(BigDecimal discountFee) {
		if(discountFee !=null)
			this.discountFee = discountFee;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Integer getBuyNow() {
		return buyNow;
	}

	public void setBuyNow(Integer buyNow) {
		this.buyNow = buyNow;
	}

	public BigDecimal getDenomination() {
		return denomination;
	}

	public void setDenomination(BigDecimal denomination) {
		this.denomination = denomination;
	}

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

	
	public BigDecimal getShipFee() {
		return shipFee;
	}

	public void setShipFee(BigDecimal shipFee) {
		this.shipFee = shipFee;
	}

	public BigDecimal getPortalFee() {
		return portalFee;
	}

	public void setPortalFee(BigDecimal portalFee) {
		this.portalFee = portalFee;
	}

	public BigDecimal getFactPortalFee() {
		return factPortalFee;
	}

	public void setFactPortalFee(BigDecimal factPortalFee) {
		this.factPortalFee = factPortalFee;
	}

	public BigDecimal getFactShipFee() {
		return factShipFee;
	}

	public void setFactShipFee(BigDecimal factShipFee) {
		this.factShipFee = factShipFee;
	}

	public BigDecimal getTotalPayFee(){
		return this.totalFee.add(this.factPortalFee).add(this.factShipFee).subtract(this.denomination) ;
	}
	
	public BigDecimal getDiscountFee() {
		return discountFee.add(denomination);
	}

}
