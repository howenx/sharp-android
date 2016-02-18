package com.hanmimei.entity;

import java.util.List;

public class PinList {

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

}
