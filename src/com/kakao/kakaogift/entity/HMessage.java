package com.kakao.kakaogift.entity;

import java.io.Serializable;

public class HMessage implements Serializable{
	private String message;
	private Integer code ;
	private String tag;
	private int time;
	private String userId;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	
}
