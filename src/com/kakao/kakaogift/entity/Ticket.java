package com.kakao.kakaogift.entity;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "TICKET".
 */
public class Ticket {
	private List<CouponVo> coupons;
	private HMessage message;
	public List<CouponVo> getCoupons() {
		return coupons;
	}
	
	public Ticket() {
		super();
	}

	public void setCoupons(List<CouponVo> coupons) {
		this.coupons = coupons;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	

}
