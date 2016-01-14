package com.hanmimei.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;

public class GoodsDetailPagerAdapter extends FragmentPagerAdapter {
	private List<ScrollAbleFragment> fragments;
	private List<String> titles;

	public GoodsDetailPagerAdapter(FragmentManager fm, List<ScrollAbleFragment> fragments, List<String> titles) {
		super(fm);
		this.fragments = fragments;
		this.titles = titles;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return titles.get(position);
	}

}
