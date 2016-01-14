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
	
	private BigDecimal shipFee = new BigDecimal(0);			//理论邮费
	private BigDecimal portalFee =new BigDecimal(0);		//理论行邮税
	private BigDecimal factPortalFee = new BigDecimal(0);	//	实际行邮税
	private BigDecimal factShipFee = new BigDecimal(0); //实际邮费
	private BigDecimal denomination = new BigDecimal(0); //优惠额度
	
	 private Integer buyNow = 1;         //1.立即支付,2.购物车结算

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

	public BigDecimal getAllPrice() {
		BigDecimal allPrice = new BigDecimal(0);
		for(Customs cs :list){
			allPrice = allPrice.add(new BigDecimal(cs.getAllPrice()));
		}
		return allPrice;
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

	public BigDecimal getAllMoney(){
		return getAllPrice().add(this.factPortalFee).add(this.factShipFee).subtract(this.denomination) ;
	}

}
