package com.hanmimei.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hanmimei.entity.Category;

public class MyPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> data;
	private List<Category> cats;

	public MyPagerAdapter(FragmentManager fm, List<Fragment> data, List<Category> cats) {
		super(fm);
		this.data = data;
		this.cats = cats;
	}

	@Override
	public Fragment getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return cats.get(position).getName();
	}

}
