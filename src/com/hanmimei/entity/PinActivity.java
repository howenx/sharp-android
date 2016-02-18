package com.hanmimei.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class PinActivity {
	private Long pinActiveId; // 拼购活动ID
	private String pinUrl; // 此团的分享短连接
	private Long pinId; // 拼购ID
	private Long masterUserId; // 团长用户ID
	private Integer personNum; // 拼购人数
	private String pinPrice; // 拼购价格
	private Integer joinPersons; // 已参加活动人数
	private String createAt; // 发起时间
	private String status; //   状态   Y-正常， N－取消 ，C－完成 ，F-失败
	private Integer endCountDown; // 截止时间
	private String pay; //new 参团成功查询  normal 普通查询
	
	
	private List<PinUser> pinUsers; // 参与拼购活动的用于
	
	private String          userType;       //订单支付成功后需要的用户类型,团长: master,团员:ordinary

    private Integer         orJoinActivity; //是否参团,0:未参团,1:参团
	
	  //拼购商品数据
    private String          pinImg;      //生成后列表图
    private String          pinSkuUrl;   //拼购商品链接
    private String          pinTitle;    //拼购商品标题

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getOrJoinActivity() {
		return orJoinActivity;
	}

	public void setOrJoinActivity(Integer orJoinActivity) {
		this.orJoinActivity = orJoinActivity;
	}

	public Long getPinActiveId() {
		return pinActiveId;
	}

	public void setPinActiveId(Long pinActiveId) {
		this.pinActiveId = pinActiveId;
	}

	public String getPinUrl() {
		return pinUrl;
	}

	public void setPinUrl(String pinUrl) {
		this.pinUrl = pinUrl;
	}

	public Long getPinId() {
		return pinId;
	}

	public void setPinId(Long pinId) {
		this.pinId = pinId;
	}

	public Long getMasterUserId() {
		return masterUserId;
	}

	public void setMasterUserId(Long masterUserId) {
		this.masterUserId = masterUserId;
	}

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	public String getPinPrice() {
		return pinPrice;
	}

	public void setPinPrice(String pinPrice) {
		this.pinPrice = pinPrice;
	}

	public Integer getJoinPersons() {
		return joinPersons;
	}

	public void setJoinPersons(Integer joinPersons) {
		this.joinPersons = joinPersons;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getEndCountDown() {
		return endCountDown;
	}
	Integer ss = 1000;  
    Integer mi = ss * 60;  
    Integer hh = mi * 60;  
    Integer dd = hh * 24;  
    
	public int getEndCountDownForDay(){
		return endCountDown/dd;
	}
	public int getEndCountDownForHour(){
		return (endCountDown-getEndCountDownForDay()*dd)/hh;
	}
	public int getEndCountDownForMinute(){
		return (endCountDown-getEndCountDownForDay()*dd-getEndCountDownForHour()*hh)/mi;
	}
	public int getEndCountDownForSecond(){
		return  (endCountDown - getEndCountDownForDay() * dd - getEndCountDownForHour() * hh - getEndCountDownForMinute() * mi) / ss;
	}

	public void setEndCountDown(Integer endCountDown) {
		this.endCountDown = endCountDown;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public List<PinUser> getPinUsers() {
		return pinUsers;
	}

	public void setPinUsers(List<PinUser> pinUsers) {
		this.pinUsers = pinUsers;
	}
	
	
	
	public ImgInfo getPinImg() {
		return new Gson().fromJson(pinImg, ImgInfo.class);
	}

	public void setPinImg(String pinImg) {
		this.pinImg = pinImg;
	}

	public String getPinSkuUrl() {
		return pinSkuUrl;
	}

	public void setPinSkuUrl(String pinSkuUrl) {
		this.pinSkuUrl = pinSkuUrl;
	}

	public String getPinTitle() {
		return pinTitle;
	}

	public void setPinTitle(String pinTitle) {
		this.pinTitle = pinTitle;
	}

	public PinUser getPinUsersForMaster(){
		for(PinUser p :getPinUsers()){
			if(p.isOrMaster())
				return p;
		}
		return null;
	}
	public List<PinUser> getPinUsersForMember(){
		List<PinUser> list =new ArrayList<PinUser>();
		for(PinUser p :getPinUsers()){
			if(!p.isOrMaster())
				list.add(p);
		}
		return list;
	}
}
