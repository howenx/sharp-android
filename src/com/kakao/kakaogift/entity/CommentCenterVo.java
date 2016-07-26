/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午1:20:09 
**/
package com.kakao.kakaogift.entity;

import java.util.List;

/**
 * 评价中心  接口返回数据类型
 * @author vince
 *
 */
public class CommentCenterVo {
	private HMessage message;
	private List<OrderRemark> orderRemark;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<OrderRemark> getOrderRemark() {
		return orderRemark;
	}
	public void setOrderRemark(List<OrderRemark> orderRemark) {
		this.orderRemark = orderRemark;
	}
	
}
