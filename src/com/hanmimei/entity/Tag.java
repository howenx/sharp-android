package com.hanmimei.entity;

public class Tag {
	private String content;
	private String state="Y";			//Y'--正常,'D'--下架,'N'--删除,'K'--售空
	private Boolean orMasterInv=false;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public Boolean getOrMasterInv() {
		return orMasterInv;
	}
	public void setOrMasterInv(Boolean orMasterInv) {
		this.orMasterInv = orMasterInv;
	}
	
	public Tag(String content, String state, Boolean orMasterInv) {
		super();
		this.content = content;
		this.state = state;
		this.orMasterInv = orMasterInv;
	}
	public Tag() {
		super();
		// TODO Auto-generated constructor stub
	} 
	
}
