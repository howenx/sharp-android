package com.kakao.kakaogift.activity.mine.order;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.coupon.adapter.CouponPagerAdapter;
import com.kakao.kakaogift.activity.mine.order.fragment.OrderFragment;
import com.kakao.kakaogift.entity.Category;
import com.kakao.kakaogift.manager.BadgeViewManager;
import com.kakao.kakaogift.manager.MyOrderNumsManager;
import com.kakao.kakaogift.manager.OrderNumsMenager;
import com.kakao.kakaogift.utils.ActionBarUtil;

/**
 * @author eric
 *
 */
public class CopyOfMyOrderActivity extends BaseActivity {
	private static final String TAG_01_ID = "tag01";
	private static final String TAG_02_ID = "tag02";
	// private static final String TAG_03_ID = "tag03";
	private static final String TAG_04_ID = "tag04";
	 private static final String TAG_05_ID = "tag05";
	private static final String TAG_01 = "全部";
	private static final String TAG_02 = "待付款";
	// private static final String TAG_03 = "待发货";
	private static final String TAG_04 = "待收货";
	private static final String TAG_05 = "待评价";
	private List<Category> data;
	private List<Fragment> fragmentList;
	private CouponPagerAdapter adapter;
	private ViewPager viewPager;
	private PagerSlidingTabStrip pagerSlidingTabStrip;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.copyorder_list_layout_new);
		ActionBarUtil.setActionBarStyle(this, "我的订单", 0, true, null);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setOffscreenPageLimit(1);
		pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		initCategory();
		initFragment();
	}


	private void initFragment() {
		fragmentList = new ArrayList<Fragment>();
		for (int i = 0; i < data.size(); i++) {
			Category category = data.get(i);
			OrderFragment fragment = new OrderFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("category", category);
			fragment.setArguments(bundle);
			fragmentList.add(fragment);
		}
		adapter = new CouponPagerAdapter(getSupportFragmentManager(), fragmentList,
				data);
		viewPager.setAdapter(adapter);
		pagerSlidingTabStrip.setViewPager(viewPager);
	}

	private void initCategory() {
		data = new ArrayList<Category>();
		data.add(new Category(TAG_01_ID, TAG_01));
		data.add(new Category(TAG_02_ID, TAG_02));
		// data.add(new Category(TAG_03_ID, TAG_03));
		data.add(new Category(TAG_04_ID, TAG_04));
		data.add(new Category(TAG_05_ID, TAG_05));
	}


}
