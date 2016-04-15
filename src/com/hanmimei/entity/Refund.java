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
	public String getStateText() {
		if(state.equals("I")){
			return "退款状态：申请受理中";
		}else if(state.equals("A")){
			return "退款状态：退款受理中，资金会在1-5个工作日内退回您的账户";
		}else if(state.equals("R")){
			return "退款状态：拒绝退款";
		}else if(state.equals("N")){
			return "退款状态：退款受理失败";
		}else if(state.equals("Y")){
			return "退款状态：退款受理成功";
		}
		return null;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getContactTel() {
		if(contactTel !=null && !contactTel.equals("null"))
			return contactTel;
		return null;
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
