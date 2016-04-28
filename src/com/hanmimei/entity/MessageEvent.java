/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-28 下午2:13:07 
 **/
package com.hanmimei.entity;

/**
 * @author vince
 * 
 */
public class MessageEvent {
	private String message;
	private Integer positon;
	
	public MessageEvent(String message, Integer positon) {
		super();
		this.message = message;
		this.positon = positon;
	}

	public MessageEvent(Integer positon) {
		super();
		this.positon = positon;
	}

	public MessageEvent() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getPositon() {
		return positon;
	}

	public void setPositon(Integer positon) {
		this.positon = positon;
	}

}
