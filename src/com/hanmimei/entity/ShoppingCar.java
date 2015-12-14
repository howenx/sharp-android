package com.hanmimei.entity;

import java.io.Serializable;
import java.util.List;

public class ShoppingCar implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private HMessage message;
	private List<Customs> list;
	
	private Double shipFee = Double.valueOf(0.00);			//理论邮费
	private Double portalFee =Double.valueOf(0.00);		//理论行邮税
	private Double factPortalFee = Double.valueOf(0.00);	//	实际行邮税
	private Double factShipFee = Double.valueOf(0.00); //实际邮费
	private Double denomination = 0.00; //优惠额度
	
	

	public Double getDenomination() {
		return denomination;
	}

	public void setDenomination(Double denomination) {
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

	public Double getAllPrice() {
		Double allPrice = 0.00;
		for(Customs cs :list){
			allPrice += cs.getAllPrice();
		}
		return allPrice;
	}

	public Double getShipFee() {
		return shipFee;
	}

	public void setShipFee(Double shipFee) {
		this.shipFee = shipFee;
	}

	public Double getPortalFee() {
		return portalFee;
	}

	public void setPortalFee(Double portalFee) {
		this.portalFee = portalFee;
	}

	public Double getFactPortalFee() {
		return factPortalFee;
	}

	public void setFactPortalFee(Double factPortalFee) {
		this.factPortalFee = factPortalFee;
	}

	public Double getFactShipFee() {
		return factShipFee;
	}

	public void setFactShipFee(Double factShipFee) {
		this.factShipFee = factShipFee;
	}
	
	
	public Double getAllMoney(){
		return getAllPrice() + this.factPortalFee +this.factShipFee - this.denomination;
	}

}
