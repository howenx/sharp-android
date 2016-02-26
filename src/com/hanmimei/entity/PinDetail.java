package com.hanmimei.entity;

import java.util.List;

public class PinDetail {
	private HMessage message;
	private MainVo main;
	private StockVo stock;
	private List<HMMGoods> push;
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
	public List<HMMGoods> getPush() {
		return push;
	}
	public void setPush(List<HMMGoods> push) {
		this.push = push;
	}
	
}
