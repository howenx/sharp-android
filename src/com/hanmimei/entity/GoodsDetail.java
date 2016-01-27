package com.hanmimei.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GoodsDetail {
	private MainVo main;
	private List<StockVo> stock;
	private HMessage message;
	private Integer cartNum;
	
	
	public StockVo getCurrentStock(){
		StockVo sto = new StockVo();
		for (int i = 0; i < stock.size(); i++) {
			if (stock.get(i).getOrMasterInv()) {
				sto = stock.get(i);
			}
		}
		return sto;
	}
	public Integer getCartNum() {
		return cartNum;
	}

	public void setCartNum(Integer cartNum) {
		this.cartNum = cartNum;
	}

	public MainVo getMain() {
		return main;
	}

	public void setMain(MainVo main) {
		this.main = main;
	}

	public List<StockVo> getStock() {
		return stock;
	}

	public void setStock(List<StockVo> stock) {
		this.stock = stock;
	}

	public HMessage getMessage() {
		return message;
	}

	public void setMessage(HMessage message) {
		this.message = message;
	}

	
}
