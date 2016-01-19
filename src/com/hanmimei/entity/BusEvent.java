package com.hanmimei.entity;

public class BusEvent {

	public static final int ACTION_UPDATE = 1;
	public static final int ACTION_DELETE = 2;
	public static final int ACTION_ADD = 3;
	public static final int ACTION_FIND = 4;

	private String message;
	private int action;
	private int code;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public BusEvent(String message, int action, int code) {
		super();
		this.message = message;
		this.action = action;
		this.code = code;
	}

	public BusEvent(int action) {
		this.action = action;
	}

	public BusEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

}
