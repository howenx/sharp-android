/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午4:14:30 
**/
package com.hanmimei.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author eric
 *
 */
public class Comment implements Serializable{

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
	
	
}
