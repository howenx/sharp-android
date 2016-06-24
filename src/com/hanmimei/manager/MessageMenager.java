package com.hanmimei.manager;

import android.content.Context;
import android.widget.ImageView;


public class MessageMenager {
	
	private ImageView msgDrawble;
	private static class MessageMenagerHolder {
		public static final MessageMenager instance = new MessageMenager();
	}

	public static MessageMenager getInstance() {
		return MessageMenagerHolder.instance;
	}
	public void initMessageMenager(Context mContext, ImageView msgDrawble){
		this.msgDrawble = msgDrawble;
	}
	public void setMsgDrawble(int resId){
		if(msgDrawble!=null)
			msgDrawble.setImageResource(resId);
	}
	
	
}