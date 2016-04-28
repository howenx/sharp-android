package com.hanmimei.activity.mine.pin.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.hanmimei.activity.mine.pin.fragment.MyPinFragment;

public class PinPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments;
	private List<String> titles;
	
	

	public PinPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public PinPagerAdapter(FragmentManager fm, List<Fragment> fragments,
			List<String> titles) {
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
