package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;

@SuppressLint("InflateParams")
public class OrderFragment extends Fragment implements OnRefreshListener2<ListView>{

	private PullToRefreshListView mListView;
	private List<Order> data;
	private OrderPullListAdapter adapter;
	private Category category;
	private int state = 1;
	private User user;
	private BaseActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new ArrayList<Order>();
		adapter = new OrderPullListAdapter(data, getActivity());
		Bundle bundle = getArguments();
		category = (Category) bundle.getSerializable("category");
		activity = (BaseActivity) getActivity();
		user = activity.getUser();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pulltorefresh_list_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
		mListView.setOnRefreshListener(this);
		registerReceivers();
		loadOrder();
		return view;
	}

	private void loadOrder() {
		if (category.getId().equals("tag01")) {
			state = 1;
		} else if (category.getId().equals("tag02")) {
			state = 2;
		} else if (category.getId().equals("tag03")) {
			state = 3;
		} else if (category.getId().equals("tag04")) {
			state = 4;
		} else {
			state = 5;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.getToken(UrlUtil.GET_ORDER_LIST_URL,
						"id-token", user.getToken());
				List<Order> list = DataParser.parserOrder(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void getOrderByState(List<Order> orders) {
		if (state == 1) {
			data.addAll(orders);
		} else if (state == 2) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getOrderStatus().equals("I")) {
					data.add(orders.get(i));
				}
			}
		} else if (state == 3) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getOrderStatus().equals("S")) {
					data.add(orders.get(i));
				}
			}
		} else if (state == 4) {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getOrderStatus().equals("D")) {
					data.add(orders.get(i));
				}
			}
		} else {
			for (int i = 0; i < orders.size(); i++) {
				if (orders.get(i).getOrderStatus().equals("R")) {
					data.add(orders.get(i));
				}
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				List<Order> orders = (List<Order>) msg.obj;
				if (orders != null && orders.size() > 0) {
					data.clear();
					getOrderByState(orders);
					adapter.notifyDataSetChanged();
				} else {

				}
				break;

			default:
				break;
			}
		}

	};
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
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(netReceiver);
	}

	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER)){
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

}
