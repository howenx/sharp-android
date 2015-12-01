package com.hanmimei.entity;

import java.io.Serializable;

public class Category implements Serializable{

	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
