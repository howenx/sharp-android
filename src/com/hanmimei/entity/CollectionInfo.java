package com.hanmimei.entity;

import java.util.List;

public class CollectionInfo {
	private HMessage hMessage;
	private List<Collection> list;
	public HMessage gethMessage() {
		return hMessage;
	}
	public void sethMessage(HMessage hMessage) {
		this.hMessage = hMessage;
	}
	public List<Collection> getList() {
		return list;
	}
	public void setList(List<Collection> list) {
		this.list = list;
	}
	public CollectionInfo() {
		super();
	}
	
}
