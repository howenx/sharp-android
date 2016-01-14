package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.hanmimei.R;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.entity.Category;
import com.hanmimei.fragment.CouponFragment;
import com.hanmimei.utils.ActionBarUtil;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

@SuppressLint("NewApi") 
public class CouponActivity extends BaseActivity{

	
	private static final String TAG_ID_01 = "tag01";
	private static final String TAG_ID_02 = "tag02";
	private static final String TAG_ID_03 = "tag03";
	private static final String TAG_01 = "未使用";
	private static final String TAG_02 = "已使用";
	private static final String TAG_03 = "已过期";
	private TabPageIndicator indicator;
	private ViewPager viewPager;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private MyPagerAdapter adapter;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.my_order_layout);
		ActionBarUtil.setActionBarStyle(this, "优惠券", 0, true, null);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		viewPager = (ViewPager) findViewById(R.id.pager);
		initCategory();
		initFragment();
	}

	private void initFragment() {
		fragmentList = new ArrayList<Fragment>();
		for(int i = 0; i < data.size(); i ++){
			Category category = data.get(i);
			CouponFragment fragment = new CouponFragment();
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
		data.add(new Category(TAG_ID_01, TAG_01));
		data.add(new Category(TAG_ID_02, TAG_02));
		data.add(new Category(TAG_ID_03, TAG_03));
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

}
