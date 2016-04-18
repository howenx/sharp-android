package com.hanmimei.entity;

import java.util.List;

public class MessageResult {

	private HMessage message;
	private List<MessageInfo> list;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<MessageInfo> getList() {
		return list;
	}
	public void setList(List<MessageInfo> list) {
		this.list = list;
	}
	public MessageResult() {
		super();
	}
	
}
