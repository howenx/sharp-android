/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午5:30:57 
**/
package com.hanmimei.entity;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author vince
 *
 */
public class RemarkVo {
	private String createAt;// 评价时间
	private String content;// 评价内容
	private String picture;// 晒图
	private Integer grade;// 评分1,2,3,4,5
	private String userImg;// 用户头像
	private String userName;// 用户名

	

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

	public List<String> getPicture() {
		return new Gson().fromJson(picture, new TypeToken<List<String>>(){}.getType());
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getGrade() {
		return grade;
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

}
