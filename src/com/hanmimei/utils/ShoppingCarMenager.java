package com.hanmimei.utils;

import com.hanmimei.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoppingCarMenager {
	private ImageView checkBox_b;
	private TextView nums_t;
	private TextView totalPrice_t;
	private TextView payPrice_t;
	private TextView cutPrice_t;
	private LinearLayout no_data;
	private RelativeLayout bottom;
	//
	private int nums_e = 0;
	private int totalPrice_e = 0;
	private int cutPrice_e = 0;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;

	private static class ShopCartManagerHolder {
		public static final ShoppingCarMenager instance = new ShoppingCarMenager();
	}

	public static ShoppingCarMenager getInstance() {
		return ShopCartManagerHolder.instance;
	}
	public void initDrawable(Context mContext){
		check_Drawable = mContext.getResources().getDrawable(R.drawable.checked);
		uncheck_Drawable = mContext.getResources().getDrawable(R.drawable.check_un);
	}
	public void initShoppingCarMenager(ImageView checkBox, TextView nums, TextView totalPrice, TextView payPrice, TextView cutPrice, LinearLayout no_data, RelativeLayout bottom){
		this.checkBox_b = checkBox;
		this.nums_t = nums;
		this.totalPrice_t = totalPrice;
		this.payPrice_t = payPrice;
		this.cutPrice_t = cutPrice;
		this.no_data = no_data;
		this.bottom = bottom;
	}
	public void setBottom(int nums, int  price, int cutPrice){
		nums_e = nums_e + nums;
		totalPrice_e = totalPrice_e + price * nums;
		cutPrice_e = cutPrice_e + cutPrice;
		nums_t.setText("商品数量：" + nums_e);
		totalPrice_t.setText("总金额：" + totalPrice_e);
		cutPrice_t.setText("已节省：" + cutPrice_e);
		payPrice_t.setText("应付金额：" + (totalPrice_e - cutPrice_e));
	}
	public void setChecked(){
		checkBox_b.setImageDrawable(check_Drawable);
	}
	public void setUnChecked(){
		checkBox_b.setImageDrawable(uncheck_Drawable);
	}
	public void setAll(int nums, int totalPrice, int cutPrice){
		nums_e = nums;
		totalPrice_e = totalPrice;
		cutPrice_e = cutPrice;
		nums_t.setText("商品数量：" + nums_e);
		totalPrice_t.setText("总金额：" + totalPrice_e);
		cutPrice_t.setText("已节省：" + cutPrice_e);
		payPrice_t.setText("应付金额：" + (totalPrice_e - cutPrice_e));
	}
	public void ClearAll(){
		nums_e = 0;
		totalPrice_e = 0;
		cutPrice_e = 0;
		nums_t.setText("商品数量：" + nums_e);
		totalPrice_t.setText("总金额：" + totalPrice_e);
		cutPrice_t.setText("已节省：" + cutPrice_e);
		payPrice_t.setText("应付金额：" + (totalPrice_e - cutPrice_e));
	}
	public void setNoGoods(){
		no_data.setVisibility(View.VISIBLE);
		bottom.setVisibility(View.GONE);
	}
}
