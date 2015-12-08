package com.hanmimei.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hanmimei.R;

public class ShoppingCarMenager {
	private ImageView checkBox_b;
	private TextView totalPrice_t;
	private TextView pay;
	private LinearLayout no_data;
	private LinearLayout bottom;
	//
	private int nums_e = 0;
	private int totalPrice_e = 0;
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
	public void initShoppingCarMenager(ImageView checkBox, TextView totalPrice, TextView pay, LinearLayout no_data, LinearLayout bottom){
		this.checkBox_b = checkBox;
		this.totalPrice_t = totalPrice;
		this.pay = pay;
		this.no_data = no_data;
		this.bottom = bottom;
	}
	public void setBottom(int nums, int  price){
		nums_e = nums_e + nums;
		totalPrice_e = totalPrice_e + price * nums;
		pay.setText("结算" +"("+ nums_e + ")");
		totalPrice_t.setText("总计：" + totalPrice_e);
	}
	public void setChecked(){
		checkBox_b.setImageDrawable(check_Drawable);
	}
	public void setUnChecked(){
		checkBox_b.setImageDrawable(uncheck_Drawable);
	}
	public void setAll(int nums, int totalPrice){
		nums_e = nums;
		totalPrice_e = totalPrice;
		pay.setText("结算" +"("+ nums_e + ")");
		totalPrice_t.setText("总计：" + totalPrice_e);
	}
	public void ClearAll(){
		nums_e = 0;
		totalPrice_e = 0;
		pay.setText("结算" +"("+ nums_e + ")");
		totalPrice_t.setText("总金额：" + totalPrice_e);
	}
	public void setNoGoods(){
		no_data.setVisibility(View.VISIBLE);
		bottom.setVisibility(View.GONE);
	}
}