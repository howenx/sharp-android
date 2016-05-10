/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午1:21:43 
**/
package com.hanmimei.entity;

/**
 * @author eric
 *
 */
public class OrderRemark {
	private Sku sku;
	private CommentVo comment;
	public Sku getSku() {
		return sku;
	}
	public void setSku(Sku sku) {
		this.sku = sku;
	}
	public CommentVo getComment() {
		return comment;
	}
	public void setComment(CommentVo comment) {
		this.comment = comment;
	}
	
	private Sku orderLine;
	public Sku getOrderLine() {
		return orderLine;
	}
	public void setOrderLine(Sku orderLine) {
		this.orderLine = orderLine;
	}
	
	
	
}
