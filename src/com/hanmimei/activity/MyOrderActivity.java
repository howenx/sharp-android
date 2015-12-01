package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import com.hanmimei.R;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.entity.Category;
import com.hanmimei.fragment.OrderFragment;
import com.viewpagerindicator.TabPageIndicator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyOrderActivity extends BaseActivity implements OnClickListener{
	
	private static final String ALL_ID = "tag00";
	private static final String NO_PAY_ID = "tag01";
	private static final String PAY_ID = "tag02";
	private static final String ALL = "全部";
	private static final String NO_PAY = "待付款";
	private static final String PAY = "已付款";
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
	private TextView header;
	private ImageView back;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_order_layout);
		getActionBar().hide();
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		header = (TextView) findViewById(R.id.header);
		header.setText("我的订单");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
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
		data.add(new Category(ALL_ID, ALL));
		data.add(new Category(NO_PAY_ID, NO_PAY));
		data.add(new Category(PAY_ID, PAY));
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
