/**
 * @Description: TODO(商品评价实体类) 
 * @author vince
 * @date 2016-4-25 下午3:19:44 
 **/
package com.hanmimei.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author vince
 * 
 */
public class GoodsCommentVo implements Serializable{
	private HMessage message;
	private List<RemarkVo> remarkList;
	private Integer page_count;
	private String count_num;
	

	public String getCount_num() {
		return count_num;
	}

	public void setCount_num(String count_num) {
		this.count_num = count_num;
	}

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}

	public List<RemarkVo> getRemarkList() {
		return remarkList;
	}

	public void setRemarkList(List<RemarkVo> remarkList) {
		this.remarkList = remarkList;
	}

	public Integer getPage_count() {
		return page_count;
	}

	public void setPage_count(Integer page_count) {
		this.page_count = page_count;
	}

}
