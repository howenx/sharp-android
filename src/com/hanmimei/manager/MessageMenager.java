package com.hanmimei.manager;

import com.hanmimei.override.OnGetMessageListener;


public class MessageMenager {
	
	
	private OnGetMessageListener listener;
	
	private static class MessageMenagerHolder {
		public static final MessageMenager instance = new MessageMenager();
	}

	public static MessageMenager getInstance() {
		return MessageMenagerHolder.instance;
	}
	
	public void setOnGetMessageListener(OnGetMessageListener listener){
		this.listener = listener;
	}
	
	public OnGetMessageListener getListener(){
		return listener;
	}
	
	
	
}