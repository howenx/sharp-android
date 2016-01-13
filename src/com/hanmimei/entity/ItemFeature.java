package com.hanmimei.entity;

import java.io.Serializable;

public class ItemFeature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String key;
	private String value;

	public ItemFeature(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public ItemFeature() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
