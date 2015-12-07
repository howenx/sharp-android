package com.hanmimei.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCar implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID =  1;
	private HMessage message;
	private List<ShoppingGoods> list;
	public HMessage getMessage() {
		return message;
	}
	public void setMessage(HMessage message) {
		this.message = message;
	}
	public List<ShoppingGoods> getList() {
		return list;
	}
	public void setList(List<ShoppingGoods> list) {
		this.list = list;
	}
	
	public void addShoppingGoods(ShoppingGoods shoppingGoods){
		if(list == null)
			list = new ArrayList<ShoppingGoods>();
		list.add(shoppingGoods);
	}
	private Integer allPrice = 0;
	public Integer getAllPrice() {
		return allPrice;
	}
	public void setAllPrice(Integer allPrice) {
		this.allPrice = allPrice;
	}
	

}
