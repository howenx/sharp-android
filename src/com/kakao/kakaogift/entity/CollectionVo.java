package com.kakao.kakaogift.entity;

import java.util.List;
/**
 * 收藏接口返回数据类型
 * @author vince
 *
 */
public class CollectionVo {
	private HMessage message;
	private List<Collection> list;
	
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
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
	
	private Long collectId;
	public Long getCollectId() {
		return collectId;
	}
	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}
	
	
}
