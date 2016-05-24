/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午4:14:30 
**/
package com.hanmimei.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 商品评价数据结构
 * @author vince
 *
 */
public class CommentVo implements Serializable{

	private String skuId;
	private String createAt;
	private String skuImg;
	private String comment;
	private ArrayList<String> photoList = new ArrayList<>();
	private float score;
	public String getSkuId() {
		return skuId;
	}
	
	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getSkuImg() {
		return skuImg;
	}
	public void setSkuImg(String skuImg) {
		this.skuImg = skuImg;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ArrayList<String> getPhotoList() {
		return photoList;
	}
	public void setPhotoList(ArrayList<String> photoList) {
		this.photoList = photoList;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	private String content;
	private String picture;
	private Integer grade;
	private String skuType;
	private String skuTypeId;
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicture() {
		return picture;
	}
	public List<String> getPicture_() {
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

	public String getSkuType() {
		return skuType;
	}

	public void setSkuType(String skuType) {
		this.skuType = skuType;
	}

	public String getSkuTypeId() {
		return skuTypeId;
	}

	public void setSkuTypeId(String skuTypeId) {
		this.skuTypeId = skuTypeId;
	}
	
	
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
