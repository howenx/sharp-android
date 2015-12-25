package com.hanmimei.entity;

import java.util.List;

public class GoodsBalance {
	private Settle settle;
	private HMessage message;

	public Settle getSettle() {
		return settle;
	}

	public void setSettle(Settle settle) {
		this.settle = settle;
	}

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}

	public class Settle {
		private Address address;
		private List<Coupon> coupons;
		private Double shipFee;			//理论邮费
		private Double portalFee;		//理论行邮税
		private Double factPortalFee;	//	实际行邮税
		private Double factShipFee	; //实际邮费
		
		private List<SingleCustoms> singleCustoms;
		
		


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

		public List<SingleCustoms> getSingleCustoms() {
			return singleCustoms;
		}

		public void setSingleCustoms(List<SingleCustoms> singleCustoms) {
			this.singleCustoms = singleCustoms;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public List<Coupon> getCoupons() {
			return coupons;
		}

		public void setCoupons(List<Coupon> coupons) {
			this.coupons = coupons;
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
	}
	
	
	
	public class Address{
		private Integer addId;
		private String tel;
		private String name;
		private String deliveryCity;
		private String deliveryDetail;
		private String idCardNum;
		
		public boolean isEmpty(){
			return addId==null;
		}
		
		public String getIdCardNum() {
			return idCardNum;
		}
		public void setIdCardNum(String idCardNum) {
			this.idCardNum = idCardNum;
		}
		public Integer getAddId() {
			return addId;
		}
		public void setAddId(Integer addId) {
			this.addId = addId;
		}
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDeliveryCity() {
			return deliveryCity;
		}
		public void setDeliveryCity(String deliveryCity) {
			this.deliveryCity = deliveryCity;
		}
		public String getDeliveryDetail() {
			return deliveryDetail;
		}
		public void setDeliveryDetail(String deliveryDetail) {
			this.deliveryDetail = deliveryDetail;
		}
	}
	
	public class SingleCustoms{
		private String invCustoms;						//发货仓库
		private Double portalSingleCustomsFee;		//该保税区行邮税
		private Integer singleCustomsSumAmount;	//保税区商品数量
		private Double shipSingleCustomsFee;			//报税区邮费
		private Double factPortalFeeSingleCustoms;	//报税区实际行邮税
		private Double factSingleCustomsShipFee; //保税区实际邮费
		private Double singleCustomsSumFee;		//商品总价格
		private String invArea;								//邮递fangshi
		private String invAreaNm;								//邮递fangshi
		
		public String getInvAreaNm() {
			return invAreaNm;
		}
		public void setInvAreaNm(String invAreaNm) {
			this.invAreaNm = invAreaNm;
		}
		public String getInvCustoms() {
			return invCustoms;
		}
		public void setInvCustoms(String invCustoms) {
			this.invCustoms = invCustoms;
		}
		public Double getPortalSingleCustomsFee() {
			return portalSingleCustomsFee;
		}
		public void setPortalSingleCustomsFee(Double portalSingleCustomsFee) {
			this.portalSingleCustomsFee = portalSingleCustomsFee;
		}
		public Integer getSingleCustomsSumAmount() {
			return singleCustomsSumAmount;
		}
		public void setSingleCustomsSumAmount(Integer singleCustomsSumAmount) {
			this.singleCustomsSumAmount = singleCustomsSumAmount;
		}
		public Double getShipSingleCustomsFee() {
			return shipSingleCustomsFee;
		}
		public void setShipSingleCustomsFee(Double shipSingleCustomsFee) {
			this.shipSingleCustomsFee = shipSingleCustomsFee;
		}
		public Double getFactPortalFeeSingleCustoms() {
			return factPortalFeeSingleCustoms;
		}
		public void setFactPortalFeeSingleCustoms(Double factPortalFeeSingleCustoms) {
			this.factPortalFeeSingleCustoms = factPortalFeeSingleCustoms;
		}
		public Double getFactSingleCustomsShipFee() {
			return factSingleCustomsShipFee;
		}
		public void setFactSingleCustomsShipFee(Double factSingleCustomsShipFee) {
			this.factSingleCustomsShipFee = factSingleCustomsShipFee;
		}
		public Double getSingleCustomsSumFee() {
			return singleCustomsSumFee;
		}
		public void setSingleCustomsSumFee(Double singleCustomsSumFee) {
			this.singleCustomsSumFee = singleCustomsSumFee;
		}
		public String getInvArea() {
			return invArea;
		}
		public void setInvArea(String invArea) {
			this.invArea = invArea;
		}
		
		
	}
	
	
}
