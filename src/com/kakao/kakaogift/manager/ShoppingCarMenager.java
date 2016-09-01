package com.kakao.kakaogift.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.car.adapter.ShoppingCarPullListAdapter;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.view.DataNoneLayout;

/**
 * @author eric
 *
 */
public class ShoppingCarMenager {
	private TextView totalPrice_t;
	private TextView pay;
	private LinearLayout bottom;
	private TextView attention;
	private PullToRefreshListView mListView;
	//
	private int nums_e = 0;
	private double totalPrice_e = 0;
	private String customName;
	private int bottommorePrice;
	private List<CustomsVo> list = new ArrayList<CustomsVo>();
	private Context mActivity;
	private DataNoneLayout dataLayout;

	
	
	public ShoppingCarMenager() {
		super();
	}
	public void initShoppingCarMenager(Context mContext, ShoppingCarPullListAdapter adapter, List<CustomsVo> list, TextView attention, TextView totalPrice, TextView pay, LinearLayout bottom,PullToRefreshListView mListView, DataNoneLayout dataLayout){
		mActivity = mContext.getApplicationContext();
		this.adapter = adapter;
		this.list.clear();
		this.list.addAll(list);
		this.attention = attention;
		this.totalPrice_t = totalPrice;
		this.pay = pay;
		this.bottom = bottom;
		this.mListView = mListView;
		this.dataLayout = dataLayout;
	}
	public List<CustomsVo> getData(){
		return list;
	}
	
	public void isMoreThan(){
		boolean isfirst = false;
		List<CustomsVo> selectCustoms = new ArrayList<CustomsVo>();
		for(int i = 0; i < list.size(); i ++){
			boolean customHas = false;
			double morePrice = 0;
			List<ShoppingGoods> goods = list.get(i).getList();
//			if(list.get(i).getin)
			for(int j = 0; j < goods.size(); j ++){
				if(goods.get(j).getOrCheck().equals("Y")){
					morePrice = morePrice + goods.get(j).getGoodsPrice()* goods.get(j).getGoodsNums();
					customHas = true;
				}
			}
			if(customHas){
				selectCustoms.add(list.get(i));	
			}
			if(!list.get(i).getInvArea().equals("K") && !list.get(i).getInvArea().equals("NK")){
			if(morePrice > list.get(i).getPostalLimit()){
				customName = list.get(i).getInvAreaNm();
				bottommorePrice = list.get(i).getPostalLimit();
				isfirst = true;
			}
			}
		}
		if(isDifCustoms(selectCustoms)){
			setPayNoClick(0);
		}else{
			if(isfirst){
				setPayNoClick(1);
			}else{
				setPayClick();
			}
		}
		
	}
	//判断选中的商品所属仓库是否一致
	private boolean isDifCustoms(List<CustomsVo> list){
		boolean isDif = false;
		for(int i = 0; i < list.size() - 1; i ++){
			if(!list.get(i).getInvArea().equals(list.get(i+1).getInvArea())){
				isDif = true;
			}
		}
		return isDif;
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
				if(goods.get(j).getOrCheck().equals("Y")){
					nums_e = nums_e + goods.get(j).getGoodsNums();
					totalPrice_e = totalPrice_e + goods.get(j).getGoodsNums() * goods.get(j).getGoodsPrice();
				}
			}
		}
		pay.setText("结算" +"("+ nums_e + ")");
		if(nums_e != 0){
			totalPrice_t.setText("总计：¥" + CommonUtils.doubleTrans(totalPrice_e));
		}else{
			totalPrice_t.setText("总计：¥0");
		}
		if(list.size() <= 0 || list == null){
			mListView.setVisibility(View.GONE);
			attention.setVisibility(View.INVISIBLE);
			setDataNone();
			bottom.setVisibility(View.GONE);
		}else{
			mListView.setVisibility(View.VISIBLE);
			attention.setVisibility(View.VISIBLE);
			bottom.setVisibility(View.VISIBLE);
		}
	}
	private void setDataNone() {
		dataLayout.loadData(1);
		dataLayout.setVisible();
	}
	public void setPayNoClick(int i){
		if(i == 0){
			attention.setText("提示：" + "单次购买，只能购买同一保税区商品");
		}else{
			attention.setText("提示：" + customName+"商品总额超过 ¥" + bottommorePrice);
		}
		pay.setClickable(false);
		pay.setBackgroundResource(R.color.unClicked);
		pay.setTextColor(mActivity.getResources().getColor(R.color.white));
	}
	public void setPayClick(){
		attention.setText("友情提示：同一保税区商品总额有限制");
		pay.setClickable(true);
		pay.setBackgroundResource(R.drawable.btn_buy_selector);
		pay.setTextColor(mActivity.getResources().getColor(R.color.black));
	}
	private ShoppingCarPullListAdapter adapter;
	
	public void upCustoms(){
		for(int i = 0; i < list.size(); i ++){
			double tax = 0;
			int custom_checknum = 0;
			int custom_num = 0;
			List<ShoppingGoods> goods = list.get(i).getList();
			for(int j = 0; j < goods.size(); j ++){
				if(goods.get(j).getOrCheck().equals("Y")){
					tax = tax + goods.get(j).getGoodsPrice() * goods.get(j).getGoodsNums() * goods.get(j).getPostalTaxRate_() * 0.01;
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
						list.get(i).getList().get(j).setOrCheck("Y");
				}
				if(list.get(i).getState().equals("N")){
					if(!list.get(i).getList().get(j).getState().equals("S"))
						list.get(i).getList().get(j).setOrCheck("null");
				}
			}
		}
		setBottom();
	}
}