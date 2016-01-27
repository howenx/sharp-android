package com.hanmimei.entity;

public class Member {
	private String name;
	private int role = 1;
	
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Member(int role) {
		super();
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	
	
}
