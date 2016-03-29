package com.hanmimei.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.viewpagerindicator.BaseIconFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {

	List<BaseIconFragment> fragments;

	public TabPagerAdapter(FragmentManager fm, List<BaseIconFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragments.get(arg0);
	}

	@Override
	public int getIconResId(int index) {
		return fragments.get(index).getIconId();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).getTitle();
	}

}
