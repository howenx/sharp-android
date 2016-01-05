package com.hanmimei.manager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

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
		this.view = new BadgeView(mContext, tabHost.getTabWidget(), 2);
		view.setBackgroundResource(R.drawable.bg_badgeview);
		view.setBadgePosition(BadgeView.POSITION_CENTER_HORIZONTAL);
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
