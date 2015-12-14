package com.hanmimei.entity;

public class Coupon {
	private String coupId;
	private String userId;
	private String cateId;
	private String denomination;
	private String startAt;
	private String endAt;
	private String state;
	private String orderId;
	private String userAt;
	private String cateNm;
	private double limitQuota;
	
	public String getCateNm() {
		return cateNm;
	}
	public void setCateNm(String cateNm) {
		this.cateNm = cateNm;
	}
	public double getLimitQuota() {
		return limitQuota;
	}
	public void setLimitQuota(double limitQuota) {
		this.limitQuota = limitQuota;
	}
	public String getCoupId() {
		return coupId;
	}
	public void setCoupId(String coupId) {
		this.coupId = coupId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCateId() {
		return cateId;
	}
	public void setCateId(String cateId) {
		this.cateId = cateId;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public String getStartAt() {
		return startAt;
	}
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	public String getEndAt() {
		return endAt;
	}
	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getUserAt() {
		return userAt;
	}
	public void setUserAt(String userAt) {
		this.userAt = userAt;
	}
	
	
}
