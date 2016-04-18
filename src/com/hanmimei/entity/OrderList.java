/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-18 上午10:43:57 
**/
package com.hanmimei.entity;

import java.util.List;

/**
 * @author eric
 *
 */
public class OrderList {

	private List<Order> list;
	private HMessage message;
	public List<Order> getList() {
		return list;
	}
	public void setList(List<Order> list) {
		this.list = list;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public OrderList() {
		super();
	}
	
}
