package com.hanmimei.utils;

import android.widget.CheckBox;
import android.widget.TextView;

public class ShoppingCarMenager {
	private CheckBox checkBox_b;
	private TextView nums_t;
	private TextView totalPrice_t;
	private TextView payPrice_t;
	private TextView cutPrice_t;
	
	//
	private int nums_e;
	private int totalPrice_e;
	private int cutPrice_e;
	public ShoppingCarMenager(CheckBox checkBox, TextView nums, TextView totalPrice, TextView payPrice, TextView cutPrice){
		this.checkBox_b = checkBox;
		this.nums_t = nums;
		this.totalPrice_t = totalPrice;
		this.payPrice_t = payPrice;
		this.cutPrice_t = cutPrice;
	}
	public void setBottom(boolean isChecked, int nums, int  price, int cutPrice){
		nums_e = nums_e + nums;
		totalPrice_e = totalPrice_e + price * nums;
		cutPrice_e = cutPrice_e + cutPrice;
		checkBox_b.setChecked(isChecked);
		nums_t.setText("商品数量：" + nums_e);
		totalPrice_t.setText("总金额：" + totalPrice_e);
		cutPrice_t.setText("已节省：" + cutPrice_e);
		payPrice_t.setText("应付金额：" + (totalPrice_e - cutPrice_e));
	}
	public void setChecked(){
		checkBox_b.setChecked(true);
	}
	public void setUnChecked(){
		checkBox_b.setChecked(false);
	}
}
