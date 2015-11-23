package com.hanbimei.entity;

public class Result {

	private int code;
	private int result_id;
	private String tag;
	private String message;
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
	public Result(int code, int result_id, String tag, String message) {
		super();
		this.code = code;
		this.result_id = result_id;
		this.tag = tag;
		this.message = message;
	}
	public Result() {
		super();
	}
	
}
