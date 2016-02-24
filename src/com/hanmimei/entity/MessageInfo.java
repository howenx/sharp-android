package com.hanmimei.entity;

public class MessageInfo {
	private String msgId;
	private long createAt;
	private String msgTitle;
	private String msgContent;
	private String msgUrl;
	private String msgImg;
	private String msgType;
	private String targetType;
	
	public String getMsgId() {
		return msgId;
	}

	public String getMsgImg() {
		return msgImg;
	}

	public void setMsgImg(String msgImg) {
		this.msgImg = msgImg;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public long getCreateAt() {
		return createAt;
	}

	public void setCreateAt(long createAt) {
		this.createAt = createAt;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public MessageInfo() {
		super();
	}
	
}
