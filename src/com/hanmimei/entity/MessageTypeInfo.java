package com.hanmimei.entity;

import java.util.List;

public class MessageTypeInfo {

	private List<MessageType> list;
	private HMessage message;
	public MessageTypeInfo() {
		super();
	}
	public List<MessageType> getList() {
		return list;
	}
	public void setList(List<MessageType> list) {
		this.list = list;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	
}
