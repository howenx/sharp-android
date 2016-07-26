package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PinList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<PinActivity> activityList;
	private HMessage message;

	public List<PinActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<PinActivity> activityList) {
		this.activityList = activityList;
	}

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}
	
	public List<PinActivity> getActivityListForMaster(){
		List<PinActivity> list = new ArrayList<PinActivity>();
		for(PinActivity p : activityList){
			if(p.getOrMaster() == 1){
				list.add(p);
			}
		}
		return list;
	}
	public List<PinActivity> getActivityListForMember(){
		List<PinActivity> list = new ArrayList<PinActivity>();
		for(PinActivity p : activityList){
			if(p.getOrMaster() == 0){
				list.add(p);
			}
		}
		return list;
	}

}
