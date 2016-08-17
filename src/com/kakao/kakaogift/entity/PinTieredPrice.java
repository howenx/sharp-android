package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class PinTieredPrice implements Serializable{
	private String id; // 阶梯价格ID
	private String masterCouponClass; // 团长返券类别
	private String masterCouponClassName; // 团长返券类别名称

	private String masterCouponStartAt; // 团长优惠券开始时间
	private String masterCouponEndAt; // 团长优惠券结束时间
	private String masterCouponQuota; // 团长优惠券限额
	private String memberCoupon; // 团员优惠券额度
	private String memberCouponClass; // 团员返券类别
	private String memberCouponClassName; // 团长返券类别名称
	private String memberCouponStartAt; // 团员优惠券开始时间
	private String memberCouponEndAt; // 团员优惠券结束时间
	private String memberCouponQuota; // 团员优惠券限额
	private Long pinId; // pin库存ID
	private Integer peopleNum; // 人数
	private BigDecimal price; // 价格
	private BigDecimal masterMinPrice; // 团长减价
	private BigDecimal memberMinPrice; // 团员减价
	private BigDecimal masterCoupon; // 团长返券额度
	
	private boolean isSelected;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMasterCouponClass() {
		return masterCouponClass;
	}

	public void setMasterCouponClass(String masterCouponClass) {
		this.masterCouponClass = masterCouponClass;
	}

	public String getMasterCouponClassName() {
		return masterCouponClassName;
	}

	public void setMasterCouponClassName(String masterCouponClassName) {
		this.masterCouponClassName = masterCouponClassName;
	}

	public String getMasterCouponStartAt() {
		return masterCouponStartAt;
	}

	public void setMasterCouponStartAt(String masterCouponStartAt) {
		this.masterCouponStartAt = masterCouponStartAt;
	}

	public String getMasterCouponEndAt() {
		return masterCouponEndAt;
	}

	public void setMasterCouponEndAt(String masterCouponEndAt) {
		this.masterCouponEndAt = masterCouponEndAt;
	}

	public String getMasterCouponQuota() {
		return masterCouponQuota;
	}

	public void setMasterCouponQuota(String masterCouponQuota) {
		this.masterCouponQuota = masterCouponQuota;
	}

	public String getMemberCoupon() {
		return memberCoupon;
	}

	public void setMemberCoupon(String memberCoupon) {
		this.memberCoupon = memberCoupon;
	}

	public String getMemberCouponClass() {
		return memberCouponClass;
	}

	public void setMemberCouponClass(String memberCouponClass) {
		this.memberCouponClass = memberCouponClass;
	}

	public String getMemberCouponClassName() {
		return memberCouponClassName;
	}

	public void setMemberCouponClassName(String memberCouponClassName) {
		this.memberCouponClassName = memberCouponClassName;
	}

	public String getMemberCouponStartAt() {
		return memberCouponStartAt;
	}

	public void setMemberCouponStartAt(String memberCouponStartAt) {
		this.memberCouponStartAt = memberCouponStartAt;
	}

	public String getMemberCouponEndAt() {
		return memberCouponEndAt;
	}

	public void setMemberCouponEndAt(String memberCouponEndAt) {
		this.memberCouponEndAt = memberCouponEndAt;
	}

	public String getMemberCouponQuota() {
		return memberCouponQuota;
	}

	public void setMemberCouponQuota(String memberCouponQuota) {
		this.memberCouponQuota = memberCouponQuota;
	}

	public Long getPinId() {
		return pinId;
	}

	public void setPinId(Long pinId) {
		this.pinId = pinId;
	}

	public Integer getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(Integer peopleNum) {
		this.peopleNum = peopleNum;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMasterMinPrice() {
		return masterMinPrice;
	}

	public void setMasterMinPrice(BigDecimal masterMinPrice) {
		this.masterMinPrice = masterMinPrice;
	}

	public BigDecimal getMemberMinPrice() {
		return memberMinPrice;
	}

	public void setMemberMinPrice(BigDecimal memberMinPrice) {
		this.memberMinPrice = memberMinPrice;
	}

	public BigDecimal getMasterCoupon() {
		return masterCoupon;
	}

	public void setMasterCoupon(BigDecimal masterCoupon) {
		this.masterCoupon = masterCoupon;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	
	

}
