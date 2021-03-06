package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Timestamp;

import android.text.TextUtils;

public class RefundVo implements Serializable{
	private Long        id;//主键
    private String        orderId;//订单ID
    private String        splitOrderId;//子订单ID
    private String        skuId;//商品ID
    private BigDecimal  payBackFee;//退款金额
    private String      reason;//申请退款原因
    private String      state;//状态
    private String      pgTradeNo;//支付流水号
    private String      pgCode;//支付返回码
    private String      pgMessage;//支付返回消息

    private Timestamp   createAt;//创建时间
    private Timestamp   updateAt;//更新时间
    private String     amount;//申请退款数量
    private String      refundImg;//退款上传图片
    private String      contactName;//联系人姓名
    private String      contactTel;//联系人电话
    private String      expressCompany;//快递公司名称
    private String      expressCompCode;//快递公司编码
    private String      expressNum;//快递编号
    private String      rejectReason;//客服拒绝退款原因
    
    private String refundType; //退款类型
    
    
	public String getRefundType() {
		return refundType;
	}
	public void setRefundType(String refundType) {
		this.refundType = refundType;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
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
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public BigDecimal getPayBackFee() {
		return payBackFee;
	}
	public void setPayBackFee(String payBackFee) {
		this.payBackFee = BigDecimal.valueOf(Double.valueOf(payBackFee));
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
	public String getPgTradeNo() {
		return pgTradeNo;
	}
	public void setPgTradeNo(String pgTradeNo) {
		this.pgTradeNo = pgTradeNo;
	}
	public String getPgCode() {
		return pgCode;
	}
	public void setPgCode(String pgCode) {
		this.pgCode = pgCode;
	}
	public String getPgMessage() {
		return pgMessage;
	}
	public void setPgMessage(String pgMessage) {
		this.pgMessage = pgMessage;
	}
	public Timestamp getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Timestamp createAt) {
		this.createAt = createAt;
	}
	public Timestamp getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Timestamp updateAt) {
		this.updateAt = updateAt;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRefundImg() {
		return refundImg;
	}
	public void setRefundImg(String refundImg) {
		this.refundImg = refundImg;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactTel() {
		if(TextUtils.isEmpty(contactTel) && !contactTel.equals("null"))
		return contactTel;
		return null;
	}
	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}
	public String getExpressCompany() {
		return expressCompany;
	}
	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}
	public String getExpressCompCode() {
		return expressCompCode;
	}
	public void setExpressCompCode(String expressCompCode) {
		this.expressCompCode = expressCompCode;
	}
	public String getExpressNum() {
		return expressNum;
	}
	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}
	public String getRejectReason() {
		return rejectReason;
	}
	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}
	
	public String getStateText() {
		if(state.equals("I")){
			return "退款状态：申请受理中";
		}else if(state.equals("A")){
			return "退款状态：退款受理中，资金会在1-5个工作日内退回您的账户";
		}else if(state.equals("R")){
			return "退款状态：拒绝退款";
		}else if(state.equals("N")){
			return "退款状态：由于某种不可抗力量，导致退款受理失败，我们客服MM会及时联系您";
		}else if(state.equals("Y")){
			return "退款状态：退款受理成功";
		}
		return null;
	}
    
}
