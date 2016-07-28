package com.kakao.kakaogift.entity;

public class PinUser {
    private boolean orMaster;   //是否团长
    private String userImg;     //用户头像
    private String joinAt; //参团时间
    private String userNm; //用户名称

    
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getJoinAt() {
		return joinAt;
	}
	public void setJoinAt(String joinAt) {
		this.joinAt = joinAt;
	}
	
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public boolean isOrMaster() {
		return orMaster;
	}
	public void setOrMaster(boolean orMaster) {
		this.orMaster = orMaster;
	}
    
    
}
