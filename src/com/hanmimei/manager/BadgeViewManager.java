package com.hanmimei.manager;

import android.content.Context;

import com.hanmimei.R;
import com.hanmimei.fragment.FragmentTabHost;
import com.hanmimei.view.BadgeView;

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
		view.setTextColor(mContext.getResources().getColor(R.color.white));
		view.setBackgroundResource(R.drawable.bg_badgeview);
		view.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		view.setTextSize(10);
		view.setText("0");
	}


	private static class BadgeViewManagerHolder {
		public static final BadgeViewManager instance = new BadgeViewManager();
	}

	public static BadgeViewManager getInstance() {
		return BadgeViewManagerHolder.instance;
	}

	public void setShopCartGoodsNum(int num) {
		nums = num;
		if (num <= 0) {
			view.hide();
			return;
		}
		view.setText(num + "");
		view.show();
	}
	private int nums;

	public int getBadgeViewText() {
		return Integer.valueOf(view.getText().toString());
	}
	public void addShoppingCarNum(int addnum){
		nums = nums + addnum;
		view.setText(nums + "");
		view.show();
		if (nums <= 0) {
			view.hide();
			return;
		}
	}

}
