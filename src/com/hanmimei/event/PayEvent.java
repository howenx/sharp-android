/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-20 下午1:57:57 
**/
package com.hanmimei.event;

/**
 * @author vince
 *
 */
public class PayEvent {
	private Integer code;

	public PayEvent(Integer code) {
		super();
		this.code = code;
	}

	public PayEvent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
	
}
