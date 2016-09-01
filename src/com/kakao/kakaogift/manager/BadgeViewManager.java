package com.kakao.kakaogift.manager;

import android.content.Context;
import android.view.View;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.fragment.FragmentTabHost;
import com.kakao.kakaogift.view.BadgeView;

public class BadgeViewManager {

	private BadgeView view;

	/**
	 * 初始化购物车数量管理者
	 * 
	 * @param mContext
	 * @param mListView
	 */
	public void initBadgeViewManager(Context mContext, FragmentTabHost tabHost) {
		this.view = new BadgeView(mContext, tabHost.getTabWidget(), 1);
		view.setTextColor(mContext.getResources().getColor(R.color.yellow));
		view.setBackgroundResource(R.drawable.bg_badgeview2);
		view.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		view.setTextSize(10);
		view.setText("0");
	}

	/**
	 * 初始化购物车数量管理者
	 * 
	 * @param mContext
	 * @param mListView
	 */
	public void initBadgeViewManager(Context mContext, View target) {
		this.view = new BadgeView(mContext.getApplicationContext(), target);
		view.setTextColor(mContext.getResources().getColor(R.color.white));
		view.setBackgroundResource(R.drawable.bg_badgeview);
		view.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		view.setTextSize(10);
		view.setText("0");
	}

	public BadgeViewManager() {
	}

	public void setShopCartGoodsNum(int num) {
		nums = num;
		if (num <= 0) {
			view.hide();
			return;
		} else if (num >= 100) {
			view.setText("...");
			view.show();
		} else {
			view.setText(num + "");
			view.show();
		}
	}

	private int nums;

	public int getBadgeViewText() {
		return Integer.valueOf(view.getText().toString());
	}

	public void addShoppingCarNum(int addnum) {
		nums = nums + addnum;
		if (nums <= 0) {
			view.hide();
			return;
		} else if (nums >= 100) {
			view.setText("...");
			view.show();
		} else {
			view.setText(nums + "");
			view.show();
		}
	}

}
