package com.hanmimei.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hanmimei.R;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.ShoppingGoods;

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
	private boolean isSelected = false;
	
	private Activity activity;
	private List<Customs> list = new ArrayList<Customs>();

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
	public void initShoppingCarMenager(Context mContext, List<Customs> list, boolean isSelected, ImageView checkBox, TextView totalPrice, TextView pay, LinearLayout no_data, LinearLayout bottom){
		this.activity = (Activity) mContext;
		this.list.clear();
		this.isSelected = isSelected;
		this.list.addAll(list);
		this.checkBox_b = checkBox;
		this.totalPrice_t = totalPrice;
		this.pay = pay;
		this.no_data = no_data;
		this.bottom = bottom;
	}
	public List<Customs> getData(){
		return list;
	}
	
	public void isMoreThan(){
		boolean isfirst = false;
		for(int i = 0; i < list.size(); i ++){
			int morePrice = 0;
			List<ShoppingGoods> goods = list.get(i).getList();
			for(int j = 0; j < goods.size(); j ++){
				if(goods.get(j).getState().equals("G"))
					morePrice = morePrice + goods.get(j).getGoodsPrice() * goods.get(j).getGoodsNums();
			}
			if(morePrice > 1000){
				isfirst = true;
			}
		}
		if(isfirst){
			setPayNoClick();
		}else{
			setPayClick();
		}
	}
	public void setBottom(){
		int totalNums = 0;
		nums_e = 0;
		totalPrice_e = 0;
		for(int i = 0; i < list.size(); i ++){
			List<ShoppingGoods> goods = list.get(i).getList();
			for(int j = 0; j < goods.size(); j ++){
				totalNums = totalNums + goods.get(j).getGoodsNums();
				if(goods.get(j).getState().equals("G")){
					nums_e = nums_e + goods.get(j).getGoodsNums();
					totalPrice_e = totalPrice_e + goods.get(j).getGoodsNums() * goods.get(j).getGoodsPrice();
				}	
			}
		}
		if(nums_e == totalNums){
			isSelected = true;
			checkBox_b.setImageDrawable(check_Drawable);
		}else{
			isSelected = false;
			checkBox_b.setImageDrawable(uncheck_Drawable);
		}
		pay.setText("结算" +"("+ nums_e + ")");
		totalPrice_t.setText("总计：" + totalPrice_e);
		if(list.size() <= 0 && list == null){
			no_data.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.GONE);
		}else{
			no_data.setVisibility(View.GONE);
			bottom.setVisibility(View.VISIBLE);
		}
		isMoreThan();
	}
	
//	public void setBottom(int nums, int  price){
//		nums_e = nums_e + nums;
//		totalPrice_e = totalPrice_e + price * nums;
//		pay.setText("结算" +"("+ nums_e + ")");
//		totalPrice_t.setText("总计：" + totalPrice_e);
//	}
//	public void setChecked(){
//		checkBox_b.setImageDrawable(check_Drawable);
//	}
//	public void setUnChecked(){
//		checkBox_b.setImageDrawable(uncheck_Drawable);
//	}
//	public void setAll(int nums, int totalPrice){
//		nums_e = nums;
//		totalPrice_e = totalPrice;
//		pay.setText("结算" +"("+ nums_e + ")");
//		totalPrice_t.setText("总计：" + totalPrice_e);
//	}
//	public void ClearAll(){
//		nums_e = 0;
//		totalPrice_e = 0;
//		pay.setText("结算" +"("+ nums_e + ")");
//		totalPrice_t.setText("总金额：" + totalPrice_e);
//	}
//	public void setNoGoods(){
//		no_data.setVisibility(View.VISIBLE);
//		bottom.setVisibility(View.GONE);
//	}
	public void setPayNoClick(){
		pay.setClickable(false);
		pay.setBackgroundColor(activity.getResources().getColor(R.color.huise));
	}
	public void setPayClick(){
		pay.setClickable(true);
		pay.setBackgroundColor(activity.getResources().getColor(R.color.theme));
	}
	public boolean getChecked(){
		return isSelected;
	}
}