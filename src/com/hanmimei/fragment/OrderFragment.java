package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.adapter.OrderPullListAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.OrderList;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.manager.OrderNumsMenager;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("InflateParams")
public class OrderFragment extends Fragment implements
		OnRefreshListener2<ListView>, OnClickListener {

	private PullToRefreshListView mListView;
	private TextView no_order;
	private List<Order> data;
	private OrderPullListAdapter adapter;
	private Category category;
	private int state = 1;
	private BaseActivity activity;
	private LinearLayout no_net;
	private TextView reload;	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new ArrayList<Order>();
		adapter = new OrderPullListAdapter(data, getActivity());
		Bundle bundle = getArguments();
		category = (Category) bundle.getSerializable("category");
		activity = (BaseActivity) getActivity();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pulltorefresh_list_layout, null);
		no_order = (TextView) view.findViewById(R.id.no_order);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		reload = (TextView) view.findViewById(R.id.reload);
		reload.setOnClickListener(this);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setOnRefreshListener(this);
		registerReceivers();
		loadOrder();
		return view;
	}

	private void loadOrder() {
		activity.getLoading().show();
		if (category.getId().equals("tag01")) {
			state = 1;
		} else if (category.getId().equals("tag02")) {
			state = 2;
		}
//		else if (category.getId().equals("tag03")) {
//			state = 3;
//		} 
		else if (category.getId().equals("tag04")) {
			state = 4;
		} else {
			state = 5;
		}
//		else {
//			state = 5;
//		}
		VolleyHttp.doGetRequestTask(activity.getHeaders(), UrlUtil.GET_ORDER_LIST_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				OrderList orderList = DataParser.parserOrder(result);
				activity.getLoading().dismiss();
				mListView.onRefreshComplete();
				data.clear();
				if(orderList.getMessage().getCode() == 200){
					no_net.setVisibility(View.GONE);
					if (orderList.getList() != null) {
						getOrderByState(orderList.getList());
						if (data.size() > 0) {
							no_order.setVisibility(View.GONE);
						} else {
							no_order.setVisibility(View.VISIBLE);
						}
						
					} else {
						no_order.setVisibility(View.VISIBLE);
					}
				}else{
					no_order.setVisibility(View.GONE);
					no_net.setVisibility(View.VISIBLE);
				}
				adapter.notifyDataSetChanged();
			}
			
			@Override
			public void onError() {
				activity.getLoading().dismiss();
				no_order.setVisibility(View.GONE);
				no_net.setVisibility(View.VISIBLE);
			}
		});
	}
	

	private void getOrderByState(List<Order> orders) {
		showNums(orders);
		if (state == 1) {
			data.addAll(orders);
		} else if (state == 2) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getOrderStatus().equals("I")) {
					data.add(orders.get(i));
				}
			}
		} 
//		else if (state == 3) {
//			for (int i = 0; i < orders.size(); i++) {
//				if (orders.get(i).getOrderStatus().equals("S")) {
//					data.add(orders.get(i));
//				}
//			}
//		}
		else if (state == 4) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getOrderStatus().equals("D")) {
					data.add(orders.get(i));
				}
			}
		}else if(state == 5){
			for (int i = 0; i < orders.size(); i++) {
				if(orders.get(i).getRemark() != null){
				if (orders.get(i).getRemark().equals("N")) {
					data.add(orders.get(i));
				}
				}
			}
		}
	}

	private void showNums(List<Order> orders) {
		int nums_1 = 0,nums_2 = 0,nums_3 = 0;
		for(int i = 0; i < orders.size(); i ++){
			if (orders.get(i).getOrderStatus().equals("I")) {
				nums_1 = nums_1 + 1;
			}else if(orders.get(i).getOrderStatus().equals("D")){
				nums_2 = nums_2 + 1;
			}else if(orders.get(i).getOrderStatus().equals("R")){
				if(orders.get(i).getRemark() != null){
					if(orders.get(i).getRemark().equals("N"))
						nums_3 = nums_3 + 1;
				}
			}
		}
		OrderNumsMenager.getInstance().numsChanged(nums_1, nums_2, nums_3);
	}


	private MyBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(netReceiver);
	}

	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER)) {
				loadOrder();
			}
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadOrder();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadOrder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reload:
			loadOrder();
			break;
		default:
			break;
		}
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("OrderFragment"); //统计页面，"MainScreen"为页面名称，可自定义
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("OrderFragment"); 
	}

}
