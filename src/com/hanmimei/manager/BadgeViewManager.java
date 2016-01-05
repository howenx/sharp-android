package com.hanmimei.manager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.hanmimei.R;
import com.hanmimei.fragment.FragmentTabHost;
import com.hanmimei.view.BadgeView;

public class BadgeViewManager {

	private BadgeView view;
	private BadgeView bView;

	/**
	 * 初始化购物车数量管理者
	 * 
	 * @param mContext
	 * @param mListView
	 */
	public void initBadgeViewManager(Context mContext, FragmentTabHost tabHost) {
		this.view = new BadgeView(mContext, tabHost.getTabWidget(), 2);
		view.setBackgroundResource(R.drawable.bg_badgeview);
		view.setBadgePosition(BadgeView.POSITION_CENTER_HORIZONTAL);
		view.setTextSize(10);
		view.setText("0");
	}

	public void showCartNum(Context context, View target, int num) {
		if (bView == null) {
			bView = new BadgeView(context, target);
			bView.setBackgroundResource(R.drawable.bg_badgeview2);
			bView.setBadgePosition(BadgeView.POSITION_CENTER);
			bView.setTextSize(12);
			bView.setTextColor(Color.parseColor("#F9616A"));
		}
		bView.setText(num + "");
		bView.show();
	}
	public void clearBView(){
		bView =null;
	}

	private static class BadgeViewManagerHolder {
		public static final BadgeViewManager instance = new BadgeViewManager();
	}

	public static BadgeViewManager getInstance() {
		return BadgeViewManagerHolder.instance;
	}

	public void setShopCartGoodsNum(int num) {
		if (num <= 0) {
			view.hide();
			return;
		}
		view.setText(num + "");
		view.show();
	}

	public int getBadgeViewText() {
		return Integer.valueOf(view.getText().toString());
	}

}
