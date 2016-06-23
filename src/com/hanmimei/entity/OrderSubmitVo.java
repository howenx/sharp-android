package com.hanmimei.entity;

import org.json.JSONArray;

public class OrderSubmitVo {
	
	public static final String PAY_TYPE_JD = "JD";
	public static final String PAY_TYPE_APAY = "APAY";
	public static final String PAY_TYPE_WX = "WEIXIN";
	
	private JSONArray settleDtos;
	 private Long addressId;         //用户收获地址ID
	    private String couponId;        //优惠券ID
	    private String clientIp ;        //客户端IP
	    private Integer clientType;     //1.android,2.ios,3.web
	    private Integer shipTime;       //1.工作日双休日与假期均可送货,2.只工作日送货,3.只双休日与假日送货
	    private String  orderDesc;      //订单备注
	    private String payMethod;       //支付方式:JD.京东 APAY.支付宝 WEIXIN.微信
	    private Integer buyNow;         //1.立即支付,2.购物车结算
	    
	    private String pinActiveId;       //拼购活动ID
	    
		public String getPinActiveId() {
			return pinActiveId;
		}
		public void setPinActiveId(String pinActiveId) {
			this.pinActiveId = pinActiveId;
		}
		public OrderSubmitVo() {
			super();
			this.clientType = 1;
			this.shipTime = 1;
			this.buyNow = 1;
			this.addressId =  Long.parseLong("0");
			this.clientIp = "127.0.0.1";
		}
		public JSONArray getSettleDtos() {
			return settleDtos;
		}
		public void setSettleDtos(JSONArray settleDtos) {
			this.settleDtos = settleDtos;
		}
		public Long getAddressId() {
			return addressId;
		}
		public void setAddressId(Long addressId) {
			this.addressId = addressId;
		}
		public String getCouponId() {
			return couponId;
		}
		public void setCouponId(String couponId) {
			this.couponId = couponId;
		}
		public String getClientIp() {
			return clientIp;
		}
		public void setClientIp(String clientIp) {
			this.clientIp = clientIp;
		}
		public Integer getClientType() {
			return clientType;
		}
		public void setClientType(Integer clientType) {
			this.clientType = clientType;
		}
		public Integer getShipTime() {
			return shipTime;
		}
		public void setShipTime(Integer shipTime) {
			this.shipTime = shipTime;
		}
		public String getOrderDesc() {
			return orderDesc;
		}
		public void setOrderDesc(String orderDesc) {
			this.orderDesc = orderDesc;
		}
		public String getPayMethod() {
			return payMethod;
		}
		public void setPayMethod(String payMethod) {
			this.payMethod = payMethod;
		}
		public Integer getBuyNow() {
			return buyNow;
		}
		public void setBuyNow(Integer buyNow) {
			this.buyNow = buyNow;
		}
	    
	    
}
