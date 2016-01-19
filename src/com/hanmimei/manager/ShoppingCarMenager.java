package com.hanmimei.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.ShoppingCarPullListAdapter;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.utils.CommonUtil;

public class ShoppingCarMenager {
	private ImageView checkBox_b;
	private TextView totalPrice_t;
	private TextView pay;
	private LinearLayout no_data;
	private LinearLayout bottom;
	private TextView attention;
	private PullToRefreshListView mListView;
	//
	private int nums_e = 0;
	private double totalPrice_e = 0;
	private Drawable check_Drawable;
	private Drawable uncheck_Drawable;
	private boolean isSelected = false;
	private String customName;
	private int bottommorePrice;
	
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
	public void initShoppingCarMenager(Context mContext, ShoppingCarPullListAdapter adapter, List<Customs> list, boolean isSelected, TextView attention, ImageView checkBox, TextView totalPrice, TextView pay, LinearLayout no_data, LinearLayout bottom,PullToRefreshListView mListView){
		this.activity = (Activity) mContext;
		this.adapter = adapter;
		this.list.clear();
		this.isSelected = isSelected;
		this.list.addAll(list);
		this.attention = attention;
		this.checkBox_b = checkBox;
		this.totalPrice_t = totalPrice;
		this.pay = pay;
		this.no_data = no_data;
		this.bottom = bottom;
		this.mListView = mListView;
	}
	public List<Customs> getData(){
		return list;
	}
	
	public void isMoreThan(){
		boolean isfirst = false;
		for(int i = 0; i < list.size(); i ++){
			double morePrice = 0;
			List<ShoppingGoods> goods = list.get(i).getList();
			for(int j = 0; j < goods.size(); j ++){
				if(goods.get(j).getState().equals("G"))
					morePrice = morePrice + goods.get(j).getGoodsPrice()* goods.get(j).getGoodsNums();
			}
			if(morePrice > list.get(i).getPostalLimit()){
				customName = list.get(i).getInvAreaNm();
				bottommorePrice = list.get(i).getPostalLimit();
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
		isMoreThan();
		upCustoms();
		int totalNums = 0;
		nums_e = 0;
		totalPrice_e = 0;
		for(int i = 0; i < list.size(); i ++){
			List<ShoppingGoods> goods = list.get(i).getList();
			if(list.get(i).getList().size() == 0)
				list.remove(list.get(i));
			for(int j = 0; j < goods.size(); j ++){
				if(!goods.get(j).getState().equals("S"))
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
		if(nums_e != 0){
			totalPrice_t.setText("总计：¥" + CommonUtil.doubleTrans(totalPrice_e));
		}else{
			totalPrice_t.setText("总计：¥0");
		}
		if(list.size() <= 0 || list == null){
			mListView.setVisibility(View.GONE);
			attention.setVisibility(View.INVISIBLE);
			no_data.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.GONE);
		}else{
			mListView.setVisibility(View.VISIBLE);
			attention.setVisibility(View.VISIBLE);
			no_data.setVisibility(View.GONE);
			bottom.setVisibility(View.VISIBLE);
		}
	}
	public void setPayNoClick(){
		attention.setText("超额提示：" + customName+"商品总额超过 ¥" + bottommorePrice);
		pay.setClickable(false);
		pay.setBackgroundColor(activity.getResources().getColor(R.color.huise));
	}
	public void setPayClick(){
		attention.setText("友情提示：同一保税区商品总额有限制");
		pay.setClickable(true);
		pay.setBackgroundColor(activity.getResources().getColor(R.color.theme));
	}
	public boolean getChecked(){
		return isSelected;
	}
	private ShoppingCarPullListAdapter adapter;
	
	public void upCustoms(){
		for(int i = 0; i < list.size(); i ++){
			double tax = 0;
			int custom_checknum = 0;
			int custom_num = 0;
			List<ShoppingGoods> goods = list.get(i).getList();
			for(int j = 0; j < goods.size(); j ++){
				if(goods.get(j).getState().equals("G")){
					tax = tax + goods.get(j).getGoodsPrice() * goods.get(j).getGoodsNums() * goods.get(j).getPostalTaxRate() * 0.01;
					custom_checknum = custom_checknum + 1;
				}
				if(!goods.get(j).getState().equals("S"))
					custom_num = custom_num + 1;
			}
			if(custom_checknum == custom_num && custom_num != 0){
				list.get(i).setState("G");
			}else{
				list.get(i).setState("");
			}
//			if(Double.compare(tax, list.get(i).getPostalStandard()) > 0){
//				tax = 0;
//				for(int j = 0; j < goods.size(); j ++){
//					if(goods.get(j).getState().equals("G")){
//						tax = tax + goods.get(j).getGoodsPrice() * goods.get(j).getGoodsNums() / (1 - goods.get(j).getPostalTaxRate()*0.01);
//						allprice = allprice + goods.get(j).getGoodsPrice() * goods.get(j).getGoodsNums();
//					}
//				}
//				list.get(i).setTax(tax - allprice);
//			}else{
//				list.get(i).setTax(tax);
//			}
			String o = new BigDecimal(tax).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
			list.get(i).setTax(o);
		}
		adapter.notifyDataSetChanged();
	}
	public void setCustomState(){
		for(int i = 0; i < list.size(); i ++){
			for(int j = 0; j < list.get(i).getList().size(); j ++){
				if(list.get(i).getState().equals("G")){
					if(!list.get(i).getList().get(j).getState().equals("S"))
						list.get(i).getList().get(j).setState("G");
				}else{
					if(!list.get(i).getList().get(j).getState().equals("S"))
						list.get(i).getList().get(j).setState("I");
				}
			}
		}
		setBottom();
	}
}