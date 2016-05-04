/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午3:50:20 
 **/
package com.hanmimei.entity;

/**
 * @author vince
 * 
 */
public class CommentVo {
	private Integer remarkCount = 0;
	private String remarkRate = "100";

	public Integer getRemarkCount() {
		return remarkCount;
	}

	public void setRemarkCount(Integer remarkCount) {
		this.remarkCount = remarkCount;
	}

	public String getRemarkRate() {
		return remarkRate+"%";
	}

	public void setRemarkRate(String remarkRate) {
		this.remarkRate = remarkRate;
	}

}
