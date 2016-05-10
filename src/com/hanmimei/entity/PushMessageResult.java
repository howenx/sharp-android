package com.hanmimei.entity;

import java.util.List;

public class PushMessageResult {

	private HMessage message;
	private List<PushMessageVo> list;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<PushMessageVo> getList() {
		return list;
	}
	public void setList(List<PushMessageVo> list) {
		this.list = list;
	}
	public PushMessageResult() {
		super();
	}
	
}
