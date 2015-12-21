package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.hanmimei.R;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.entity.Category;
import com.hanmimei.fragment.OrderFragment;
import com.hanmimei.utils.ActionBarUtil;
import com.viewpagerindicator.TabPageIndicator;

public class MyOrderActivity extends BaseActivity implements OnClickListener{
	
	private static final String TAG_01_ID = "tag01";
	private static final String TAG_02_ID = "tag02";
	private static final String TAG_03_ID = "tag03";
	private static final String TAG_04_ID = "tag04";
	private static final String TAG_05_ID = "tag05";
	private static final String TAG_01 = "全部";
	private static final String TAG_02 = "待付款";
	private static final String TAG_03 = "待发货";
	private static final String TAG_04 = "待收货";
	private static final String TAG_05 = "已完成";
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_order_layout);
		ActionBarUtil.setActionBarStyle(this, "我的订单", 0, true, null);
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
		data.add(new Category(TAG_01_ID, TAG_01));
		data.add(new Category(TAG_02_ID, TAG_02));
		data.add(new Category(TAG_03_ID, TAG_03));
		data.add(new Category(TAG_04_ID, TAG_04));
		data.add(new Category(TAG_05_ID, TAG_05));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
	}

}
