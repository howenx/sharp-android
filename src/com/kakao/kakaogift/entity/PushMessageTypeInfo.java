package com.kakao.kakaogift.entity;

import java.util.List;

public class PushMessageTypeInfo {

	private List<PushMessageType> list;
	private HMessage message;
	public PushMessageTypeInfo() {
		super();
	}
	public List<PushMessageType> getList() {
		return list;
	}
	public void setList(List<PushMessageType> list) {
		this.list = list;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	
}
