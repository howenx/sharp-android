package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.Order;
import com.hanmimei.fragment.OrderFragment;
import com.hanmimei.manager.OrderNumsMenager;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

public class MyOrderActivity extends BaseActivity implements OnClickListener,
		OnPageChangeListener {
	private static final String TAG_01_ID = "tag01";
	private static final String TAG_02_ID = "tag02";
	// private static final String TAG_03_ID = "tag03";
	private static final String TAG_04_ID = "tag04";
	// private static final String TAG_05_ID = "tag05";
	private static final String TAG_01 = "全部";
	private static final String TAG_02 = "待付款";
	// private static final String TAG_03 = "待发货";
	private static final String TAG_04 = "待收货";
	// private static final String TAG_05 = "待评价";
	private ViewPager viewPager;
	private List<Category> data;
	private List<Fragment> fragmentList;
	private TextView t1;
	private TextView t2;
	private TextView t3;
	private TextView t4;
	private TextView c1;
	private TextView c2;
	private TextView c3;
	private TextView c4;
	private TextView t2_nums;
	private TextView t3_nums;
	private TextView t4_nums;
	private List<Order> orders;
	private TextView reload;
	private LinearLayout topLayout;
	private LinearLayout no_net;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.order_list_layout_new);
		ActionBarUtil.setActionBarStyle(this, "我的订单", 0, true, null);
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		reload = (TextView) findViewById(R.id.reload);
		topLayout = (LinearLayout) findViewById(R.id.top);
		no_net = (LinearLayout) findViewById(R.id.no_net);
		reload.setOnClickListener(this);
		viewPager.addOnPageChangeListener(this);
		orders = new ArrayList<Order>();
		initTopListner();
		OrderNumsMenager.getInstance().initOrderMenager(this, t2_nums, t3_nums,
				t4_nums);
		initCategory();
		// loadData();
		initFragment();
//		registerReceivers();
	}

//	private void loadData() {
//		getLoading().show();
//		Http2Utils.doGetRequestTask(this, getHeaders(),
//				UrlUtil.GET_ORDER_LIST_URL, new VolleyJsonCallback() {
//
//					@Override
//					public void onSuccess(String result) {
//						no_net.setVisibility(View.GONE);
//						topLayout.setVisibility(View.VISIBLE);
//						getLoading().dismiss();
//						orders = DataParser.parserOrder(result);
//						initFragment();
//					}
//
//					@Override
//					public void onError() {
//						topLayout.setVisibility(View.GONE);
//						no_net.setVisibility(View.VISIBLE);
//						ToastUtils
//								.Toast(MyOrderActivity.this, "请求数据失败，请检查您的网络");
//						getLoading().dismiss();
//					}
//				});
//	}

//	private List<Order> getStateOrder(int state) {
//		showNums(orders);
//		List<Order> sData = new ArrayList<Order>();
//		if (state == 0) {
//			sData.addAll(orders);
//		} else if (state == 1) {
//			for (int i = 0; i < orders.size(); i++) {
//				if (orders.get(i).getOrderStatus().equals("I")) {
//					sData.add(orders.get(i));
//				}
//			}
//		} else if (state == 2) {
//			for (int i = 0; i < orders.size(); i++) {
//				if (orders.get(i).getOrderStatus().equals("D")) {
//					sData.add(orders.get(i));
//				}
//			}
//		}
//		return sData;
//	}

	private void initTopListner() {
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
		t3 = (TextView) findViewById(R.id.t3);
		t4 = (TextView) findViewById(R.id.t4);
		c1 = (TextView) findViewById(R.id.cursor1);
		c2 = (TextView) findViewById(R.id.cursor2);
		c3 = (TextView) findViewById(R.id.cursor3);
		c4 = (TextView) findViewById(R.id.cursor4);
		t2_nums = (TextView) findViewById(R.id.t2_nums);
		t3_nums = (TextView) findViewById(R.id.t3_nums);
		t4_nums = (TextView) findViewById(R.id.t4_nums);
		findViewById(R.id.tv_guid1).setOnClickListener(this);
		findViewById(R.id.tv_guid2).setOnClickListener(this);
		findViewById(R.id.tv_guid3).setOnClickListener(this);
		findViewById(R.id.tv_guid4).setOnClickListener(this);
	}

	// private void initFragment() {
	//
	// fragmentList = new ArrayList<Fragment>();
	// for (int i = 0; i < data.size(); i++) {
	// // Category category = data.get(i);
	// // getStateOrder(i);
	// // OrderFragment fragment = OrderFragment.newInstance(getStateOrder(i));
	// OrderFragment fragment = new OrderFragment();
	// Bundle bundle = new Bundle();
	// bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>)
	// getStateOrder(i));
	// // bundle.putSerializable("category", category);
	// // bundle.putParcelableArrayList("data", getStateOrder(i));
	// fragment.setArguments(bundle);
	// fragmentList.add(fragment);
	// }
	// MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(),
	// fragmentList,
	// data);
	// viewPager.removeAllViews();
	// viewPager.setAdapter(adapter);
	// // adapter.notifyDataSetChanged();
	// }
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
		MyPagerAdapter adapter = new MyPagerAdapter(
				getSupportFragmentManager(), fragmentList, data);
		viewPager.setAdapter(adapter);
	}

	private void initCategory() {
		data = new ArrayList<Category>();
		data.add(new Category(TAG_01_ID, TAG_01));
		data.add(new Category(TAG_02_ID, TAG_02));
		// data.add(new Category(TAG_03_ID, TAG_03));
		data.add(new Category(TAG_04_ID, TAG_04));
		// data.add(new Category(TAG_05_ID, TAG_05));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_guid1:
			viewPager.setCurrentItem(0);
			break;
		case R.id.tv_guid2:
			viewPager.setCurrentItem(1);
			break;
		case R.id.tv_guid3:
			viewPager.setCurrentItem(2);
			break;
		case R.id.tv_guid4:
			viewPager.setCurrentItem(3);
			break;
		case R.id.reload:
//			loadData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			setTopSelected(t1, c1);
			setTopUnSelected(t2, c2);
			setTopUnSelected(t3, c3);
			setTopUnSelected(t4, c4);
			break;
		case 1:
			setTopSelected(t2, c2);
			setTopUnSelected(t1, c1);
			setTopUnSelected(t3, c3);
			setTopUnSelected(t4, c4);
			break;
		case 2:
			setTopSelected(t3, c3);
			setTopUnSelected(t2, c2);
			setTopUnSelected(t1, c1);
			setTopUnSelected(t4, c4);
			break;
		case 3:
			setTopSelected(t4, c4);
			setTopUnSelected(t2, c2);
			setTopUnSelected(t3, c3);
			setTopUnSelected(t1, c1);
			break;

		default:
			break;
		}

	}

	private void setTopSelected(TextView textView, TextView cusor) {
		textView.setTextColor(getResources().getColor(R.color.theme));
		cusor.setVisibility(View.VISIBLE);
	}

	private void setTopUnSelected(TextView textView, TextView cusor) {
		textView.setTextColor(getResources().getColor(R.color.fontcolor));
		cusor.setVisibility(View.INVISIBLE);
	}

//	private void showNums(List<Order> orders) {
//		int nums_1 = 0, nums_2 = 0, nums_3 = 0;
//		for (int i = 0; i < orders.size(); i++) {
//			if (orders.get(i).getOrderStatus().equals("I")) {
//				nums_1 = nums_1 + 1;
//			} else if (orders.get(i).getOrderStatus().equals("D")) {
//				nums_2 = nums_2 + 1;
//			} else if (orders.get(i).getOrderStatus().equals("R")) {
//				nums_3 = nums_3 + 1;
//			}
//		}
//		OrderNumsMenager.getInstance().numsChanged(nums_1, nums_2, nums_3);
//	}

//	private MyBroadCastReceiver netReceiver;
//
//	// 广播接收者 注册
//	private void registerReceivers() {
//		netReceiver = new MyBroadCastReceiver();
//		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER);
//		registerReceiver(netReceiver, intentFilter);
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		unregisterReceiver(netReceiver);
//	}
//
//	private class MyBroadCastReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equals(
//					AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER)) {
//				loadData();
//			}
//		}
//	}
}
