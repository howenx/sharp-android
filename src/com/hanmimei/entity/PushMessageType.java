package com.hanmimei.entity;

public class PushMessageType {
	private int num ;
//	private int goodNum = -1;
//	private int wuliuNum = -1;
//	private int zichanNum = -1;
//	private int huodongNum = -1;
//	private int code;
//	private String message;
	private String type;
	private String content;
	private long time;

	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public PushMessageType() {
		super();
	}

}
