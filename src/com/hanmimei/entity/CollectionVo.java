package com.hanmimei.entity;

import java.util.List;
/**
 * 收藏接口返回数据类型
 * @author vince
 *
 */
public class CollectionVo {
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
	public CollectionVo() {
		super();
	}
	
}
