package com.kakao.kakaogift.entity;

import java.math.BigDecimal;
import java.util.List;

public class GoodsBalanceVo {
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
		private List<CouponVo> coupons;
		
		private List<SingleCustoms> singleCustoms;
		private int postalStandard;
		
		private BigDecimal discountFee;
		private BigDecimal totalFee;
		private BigDecimal totalPayFee;
		
		public BigDecimal getTotalFee() {
			return totalFee;
		}

		public void setTotalFee(BigDecimal totalFee) {
			this.totalFee = totalFee;
		}

		public BigDecimal getTotalPayFee() {
			return totalPayFee;
		}

		public void setTotalPayFee(BigDecimal totalPayFee) {
			this.totalPayFee = totalPayFee;
		}

		public BigDecimal getDiscountFee() {
			return discountFee;
		}

		public void setDiscountFee(BigDecimal discountFee) {
			this.discountFee = discountFee;
		}

		public int getPostalStandard() {
			return postalStandard;
		}

		public void setPostalStandard(int postalStandard) {
			this.postalStandard = postalStandard;
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

		public List<CouponVo> getCoupons() {
			return coupons;
		}

		public void setCoupons(List<CouponVo> coupons) {
			this.coupons = coupons;
		}

	}
	
	
	
	public class Address{
		private Long addId;
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
		public Long getAddId() {
			return addId;
		}
		public void setAddId(Long addId) {
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
		private Integer singleCustomsSumAmount;	//保税区商品数量
		private BigDecimal singleCustomsSumFee;		//商品总价格
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
		
		public Integer getSingleCustomsSumAmount() {
			return singleCustomsSumAmount;
		}
		public void setSingleCustomsSumAmount(Integer singleCustomsSumAmount) {
			this.singleCustomsSumAmount = singleCustomsSumAmount;
		}
		
		public BigDecimal getSingleCustomsSumFee() {
			return singleCustomsSumFee;
		}
		public void setSingleCustomsSumFee(BigDecimal singleCustomsSumFee) {
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
