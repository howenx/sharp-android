package com.kakao.kakaogift.entity;

public class TokenVo {
	private Boolean result;
	private String message;
	private String token;
	private Integer expired;
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getExpired() {
		return expired;
	}
	public void setExpired(Integer expired) {
		this.expired = expired;
	}
	
	
	
}
