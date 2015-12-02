package com.hanmimei.utils;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

public class ViewHeightUtil {
	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		BaseAdapter listAdapter = (BaseAdapter) listView
				.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + listView.getPaddingBottom()
				+ listView.getPaddingTop()
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	/**
	 * 动态设置ListView的高度
	 * 
	 * @param listView
	 */
	@SuppressLint("NewApi")
	public static void setGridViewHeightBasedOnChildren(GridView mGridView,int column) {
		if (mGridView == null)
			return;

		BaseAdapter listAdapter = (BaseAdapter) mGridView
				.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, mGridView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = mGridView.getLayoutParams();
		params.height = totalHeight/column + mGridView.getPaddingBottom()
				+ mGridView.getPaddingTop()
				+ (mGridView.getVerticalSpacing() * (listAdapter.getCount()/column - 1));
		mGridView.setLayoutParams(params);
	}
}
