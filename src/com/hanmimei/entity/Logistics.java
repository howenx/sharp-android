/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-15 下午2:48:18 
**/
package com.hanmimei.entity;

import java.util.List;

/**
 * @author eric
 *
 */
public class Logistics {

	private String nu;
	private String comcontact;
	private String companytype;
	private String com;
	private String signname;
	private String condition;
	private String status;
	private String codenumber;
	private String signedtime;
	private int state;
	private String addressee;
	private String departure;
	private String destination;
	private String message;
	private String ischeck;
	private String pickuptime;
	private String comurl;
	private List<LogisticsData> list;
	
	public List<LogisticsData> getList() {
		return list;
	}
	public void setList(List<LogisticsData> list) {
		this.list = list;
	}
	public String getNu() {
		return nu;
	}
	public void setNu(String nu) {
		this.nu = nu;
	}
	public String getComcontact() {
		return comcontact;
	}
	public void setComcontact(String comcontact) {
		this.comcontact = comcontact;
	}
	public String getCompanytype() {
		return companytype;
	}
	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}
	public String getCom() {
		return com;
	}
	public void setCom(String com) {
		this.com = com;
	}
	public String getSignname() {
		return signname;
	}
	public void setSignname(String signname) {
		this.signname = signname;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCodenumber() {
		return codenumber;
	}
	public void setCodenumber(String codenumber) {
		this.codenumber = codenumber;
	}
	public String getSignedtime() {
		return signedtime;
	}
	public void setSignedtime(String signedtime) {
		this.signedtime = signedtime;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getAddressee() {
		return addressee;
	}
	public void setAddressee(String addressee) {
		this.addressee = addressee;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public String getPickuptime() {
		return pickuptime;
	}
	public void setPickuptime(String pickuptime) {
		this.pickuptime = pickuptime;
	}
	public String getComurl() {
		return comurl;
	}
	public void setComurl(String comurl) {
		this.comurl = comurl;
	}
	public Logistics() {
		super();
	}
	
	
}
