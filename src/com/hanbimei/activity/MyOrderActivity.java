package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import com.hanbimei.R;
import com.hanbimei.adapter.MyPagerAdapter;
import com.hanbimei.entity.Category;
import com.hanbimei.fragment.OrderFragment;
import com.viewpagerindicator.TabPageIndicator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

@SuppressLint("NewApi") public class MyOrderActivity extends BaseActivity {
	
	private static final String NO_PAY_ID = "tag01";
	private static final String PAY_ID = "tag02";
	private static final String NO_RECEIVE_ID = "tag01";
	private static final String NO_PAY = "待付款";
	private static final String PAY = "已付款";
	private static final String NO_RECEIVE = "待收货";
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_order_layout);
		getActionBar().hide();
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		initCategory();
		initFragment();
	}

	private void initFragment() {
		fragmentList = new ArrayList<Fragment>();
		for(int i = 0; i < data.size(); i ++){
			Category category = data.get(i);
			OrderFragment fragment = new OrderFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("category", category);
			fragment.setArguments(bundle);
			fragmentList.add(fragment);
		}
		adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, data);
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
	}

	private void initCategory() {
		data = new ArrayList<Category>();
		data.add(new Category(NO_PAY_ID, NO_PAY));
		data.add(new Category(PAY_ID, PAY));
		data.add(new Category(NO_RECEIVE_ID, NO_RECEIVE));
	}

}
