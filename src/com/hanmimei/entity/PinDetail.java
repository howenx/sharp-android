package com.hanmimei.entity;

import java.util.List;

public class PinDetail {
	private HMessage message;
	private MainVo main;
	private StockVo stock;
	private List<HGoodsVo> push;
	
	private CommentVo comment;
	
	
	
	public CommentVo getComment() {
		if(comment == null)
			comment = new CommentVo();
		return comment;
	}
	public void setComment(CommentVo comment) {
		this.comment = comment;
	}
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public MainVo getMain() {
		return main;
	}
	public void setMain(MainVo main) {
		this.main = main;
	}
	public StockVo getStock() {
		return stock;
	}
	public void setStock(StockVo stock) {
		this.stock = stock;
	}
	public List<HGoodsVo> getPush() {
		return push;
	}
	public void setPush(List<HGoodsVo> push) {
		this.push = push;
	}
	
}
