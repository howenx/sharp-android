package com.hanmimei.manager;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


public class ViewPagerManager {
	
	private ViewPager mViewPager;

	private static class ViewPagerManagerHolder {
		public static final ViewPagerManager instance = new ViewPagerManager();
	}

	public static ViewPagerManager getInstance() {
		return ViewPagerManagerHolder.instance;
	}
	
	public void initViewPagerManager(ViewPager mViewPager){
		this.mViewPager = mViewPager;
	}
	
	
	/**
	 * 重新设置viewPager高度
	 * 
	 * @param position
	 */
	public void resetViewPagerHeight(int position) {
		View child = mViewPager.getChildAt(position);
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (LayoutParams) mViewPager
					.getLayoutParams();
			params.height = h;
			mViewPager.setLayoutParams(params);
		}
	}
}
