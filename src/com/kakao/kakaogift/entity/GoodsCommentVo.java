/**
 * @Description: TODO(商品评价实体类) 
 * @author vince
 * @date 2016-4-25 下午3:19:44 
 **/
package com.kakao.kakaogift.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vince
 * 
 */
public class GoodsCommentVo implements Serializable {
	private HMessage message;
	private List<RemarkVo> remarkList;
	private Integer page_count;
	private Integer count_num;

	private int position;
	private String loadUrl;
	private int index;

	private List<RemarkVo> pics;

//	public List<RemarkVo> getPics_() {
//		List<RemarkVo> list = new ArrayList<RemarkVo>();
//		for(RemarkVo re : pics){
//			for(String url: re.getPictureList()){
//				RemarkVo vo = new RemarkVo();
//				vo.setBuyAt(re.getBuyAt());
//				vo.setContent(re.getContent());
//				vo.setCreateAt(re.getCreateAt());
//				vo.setGrade(re.getGrade());
//				vo.setPicture(url);
//				vo.setUserImg(re.getUserImg());
//				vo.setUserName(re.getUserName());
//				list.add(vo);
//			}
//		}
//		return list;
//	}
	
	public List<RemarkVo> getPics(){
		return pics;
	}

	public void setPics(List<RemarkVo> pics) {
		this.pics = pics;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getLoadUrl() {
		return loadUrl;
	}

	public void setLoadUrl(String loadUrl) {
		this.loadUrl = loadUrl;
	}

	public Integer getCount_num() {
		if(count_num == null)
			count_num = 0;
		return count_num;
	}

	public void setCount_num(Integer count_num) {
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
	
	public List<RemarkVo> getRemarkList_(){
		List<RemarkVo> list = new ArrayList<RemarkVo>();
		for(RemarkVo re : remarkList){
			for(String url: re.getPictureList()){
				RemarkVo vo = new RemarkVo();
				vo.setBuyAt(re.getBuyAt());
				vo.setContent(re.getContent());
				vo.setCreateAt(re.getCreateAt());
				vo.setGrade(re.getGrade());
				vo.setPicture(url);
				vo.setUserImg(re.getUserImg());
				vo.setUserName(re.getUserName());
				list.add(vo);
			}
		}
		return list;
	}

}
