package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class ShoppingCar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private HMessage message;
	private List<CustomsVo> list;
	 private String orderType = "item";       //订单类型:1.vary,2.item,3.customize,4.pin
	
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

	public List<CustomsVo> getList() {
		return list;
	}

	public void setList(List<CustomsVo> list) {
		this.list = list;
	}

	

	public BigDecimal getTotalPayFee(){
		BigDecimal fee = this.totalFee.subtract(this.denomination) ;
		if(fee.compareTo(BigDecimal.ONE) <= 0){
			fee = BigDecimal.ONE;
		}
		return fee;
	}
	
	public BigDecimal getDiscountFee() {
		return discountFee.add(denomination);
	}

}
