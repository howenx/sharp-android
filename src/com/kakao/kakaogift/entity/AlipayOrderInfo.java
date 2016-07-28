/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-27 上午10:15:46 
**/
package com.kakao.kakaogift.entity;

/**
 * @author vince
 *
 */
public class AlipayOrderInfo {
	
	private String service ="mobile.android.pay";
	private String partner;
	private String _input_charset = "utf-8";
	private String sign_type = "RSA";
	private String sign;
	private String notify_url;
	private String app_id;
	private String appenv;
	private String out_trade_no;
	private String subject;
	private String payment_type;
	private String seller_id;
	private String total_fee;
	private String body;
	private String goods_type;
	private String rn_check;
	private String it_b_pay;
	private String extern_token;
	
	public String getService() {
		return "service=\""+service+"\"";
	}
	public void setService(String service) {
		this.service = service;
	}
	public String getPartner() {
		return "partner=\""+partner+"\"";
	}
	public void setPartner(String partner) {
		this.partner = partner;
	}
	public String get_input_charset() {
		return "_input_charset=\""+_input_charset+"\"";
	}
	public void set_input_charset(String _input_charset) {
		this._input_charset = _input_charset;
	}
	public String getSign_type() {
		return "sign_type=\""+sign_type+"\"";
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return "sign=\""+sign+"\"";
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getNotify_url() {
		return "notify_url=\""+notify_url+"\"";
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getApp_id() {
		return "app_id=\""+app_id+"\"";
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getAppenv() {
		return "appenv=\""+appenv+"\"";
	}
	public void setAppenv(String appenv) {
		this.appenv = appenv;
	}
	public String getOut_trade_no() {
		return "out_trade_no=\""+out_trade_no+"\"";
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getSubject() {
		return "subject=\""+subject+"\"";
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getPayment_type() {
		return "payment_type=\""+payment_type+"\"";
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getSeller_id() {
		return "seller_id=\""+seller_id+"\"";
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getTotal_fee() {
		return "total_fee=\""+total_fee+"\"";
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getBody() {
		return "body=\""+body+"\"";
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getGoods_type() {
		return "goods_type=\""+goods_type+"\"";
	}
	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}
	public String getRn_check() {
		return "rn_check=\""+rn_check+"\"";
	}
	public void setRn_check(String rn_check) {
		this.rn_check = rn_check;
	}
	public String getIt_b_pay() {
		return "it_b_pay=\""+it_b_pay+"\"";
	}
	public void setIt_b_pay(String it_b_pay) {
		this.it_b_pay = it_b_pay;
	}
	public String getExtern_token() {
		return "extern_token=\""+extern_token+"\"";
	}
	public void setExtern_token(String extern_token) {
		this.extern_token = extern_token;
	}
	
	@Override
	public String toString() {
		return getPartner()+"&"+getSeller_id()+"&"+getOut_trade_no()
				+"&"+getSubject()+"&"+getBody()+"&"+getTotal_fee()+"&"+getNotify_url()
				+"&"+getService()+"&"+getPayment_type()+"&"+get_input_charset()+"&"+
				getIt_b_pay()+"&"+getSign()+"&"+getSign_type();
	}
	
	
	

}
