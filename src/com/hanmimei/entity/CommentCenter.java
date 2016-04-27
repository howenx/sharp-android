/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午1:20:09 
**/
package com.hanmimei.entity;

import java.util.List;

/**
 * @author eric
 *
 */
public class CommentCenter {
	private HMessage message;
	private List<OrderRemark> list;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<OrderRemark> getList() {
		return list;
	}
	public void setList(List<OrderRemark> list) {
		this.list = list;
	}
	
}
