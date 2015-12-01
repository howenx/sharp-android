package com.hanbimei.entity;

import java.io.Serializable;

public class Result implements Serializable{

	private int code;
	private int result_id;
	private String tag;
	private String message;
	private boolean isSuccess;
	private int time;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getResult_id() {
		return result_id;
	}
	public void setResult_id(int result_id) {
		this.result_id = result_id;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public Result(int code, int result_id, String tag, String message,
			boolean isSuccess) {
		super();
		this.code = code;
		this.result_id = result_id;
		this.tag = tag;
		this.message = message;
		this.isSuccess = isSuccess;
	}
	public Result() {
		super();
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	
}
