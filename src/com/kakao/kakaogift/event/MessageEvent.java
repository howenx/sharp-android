/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-9-1 下午2:34:18 
**/
package com.kakao.kakaogift.event;

/**
 * @author vince
 *
 */
public class MessageEvent {
	private boolean hasMessage;

	public boolean isHasMessage() {
		return hasMessage;
	}

	public void setHasMessage(boolean hasMessage) {
		this.hasMessage = hasMessage;
	}

	public MessageEvent(boolean hasMessage) {
		super();
		this.hasMessage = hasMessage;
	}
	
	
}
