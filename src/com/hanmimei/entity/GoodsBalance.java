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
		private Integer shipFee;
		private Double portalFee;
		private Integer freePortalFee;
		
		

		public Integer getFreePortalFee() {
			return freePortalFee;
		}

		public void setFreePortalFee(Integer freePortalFee) {
			this.freePortalFee = freePortalFee;
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

		public Integer getShipFee() {
			return shipFee;
		}

		public void setShipFee(Integer shipFee) {
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
	
	
}
