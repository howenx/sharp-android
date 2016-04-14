/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-14 下午5:52:46 
**/
package com.hanmimei.entity;

import java.io.Serializable;

/**
 * @author eric
 *
 */
public class Refund implements Serializable{

	private String orderId;
	private String splitOrderId;
	private String payBackFee;
	private String reason;
	private String state;
	private String contactTel;
	private String rejectReason;
	private String refundType;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getSplitOrderId() {
		return splitOrderId;
	}
	public void setSplitOrderId(String splitOrderId) {
		this.splitOrderId = splitOrderId;
	}
	public String getPayBackFee() {
		return payBackFee;
	}
	public void setPayBackFee(String payBackFee) {
		this.payBackFee = payBackFee;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getContactTel() {
		return contactTel;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	public Refund() {
		super();
	}
	
}
