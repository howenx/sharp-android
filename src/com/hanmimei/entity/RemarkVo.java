/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午5:30:57 
**/
package com.hanmimei.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hanmimei.R;

/**
 * @author vince
 *
 */
public class RemarkVo implements Serializable{
	private String createAt;// 评价时间
	private String content;// 评价内容
	private String picture;// 晒图
	private Integer grade;// 评分1,2,3,4,5
	private String userImg;// 用户头像
	private String userName;// 用户名
	
	private String size;
	private String buyAt;
	

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getBuyAt() {
		return buyAt;
	}

	public void setBuyAt(String buyAt) {
		this.buyAt = buyAt;
	}

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicture() {
		return picture;
	}
	public List<String> getPictureList() {
		return new Gson().fromJson(picture, new TypeToken<List<String>>(){}.getType());
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getGrade() {
		return grade;
	}
	public int getGradeResource() {
		if(grade<=1){
			return R.drawable.hmm_star_1;
		}else if(grade==2){
			return R.drawable.hmm_star_2;
		}else if(grade==3){
			return R.drawable.hmm_star_3;
		}else if(grade==4){
			return R.drawable.hmm_star_4;
		}else if(grade>=5){
			return R.drawable.hmm_star_5;
		}
		return R.drawable.hmm_star_5;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public List<RemarkVo> getRemarkVos(){
		List<RemarkVo> list = new ArrayList<RemarkVo>();
		for(String pic : this.getPictureList()){
			RemarkVo vo = new RemarkVo();
			vo.setBuyAt(this.getBuyAt());
			vo.setContent(this.getContent());
			vo.setCreateAt(this.getCreateAt());
			vo.setGrade(this.getGrade());
			vo.setPicture(pic);
			vo.setSize(this.getSize());
			vo.setUserImg(this.getUserImg());
			vo.setUserName(this.getUserName());
			list.add(vo);
		}
		return list;
	}
	
}
