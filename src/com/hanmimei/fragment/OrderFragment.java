package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.adapter.OrderPullListAdapter;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.User;
import com.hanmimei.utils.HttpUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("InflateParams")
public class OrderFragment extends Fragment {

	private PullToRefreshListView mListView;
	private List<Order> data;
	private OrderPullListAdapter adapter;
	private Category category;
	private int state = 0;
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
		if (category.getId().equals("tag00")) {
			state = 0;
		}else if(category.getId().equals("tag01")){
			state = 1;
		}else{
			state = 2;
		}
		loadOrder();
		return view;
	}

	private void loadOrder() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String result = HttpUtils.getToken("http://172.28.3.18:9003/client/order", "id-token", user.getToken());
				List<Order> list = DataParser.parserOrder(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void getOrderByState(List<Order> orders){
		if(state == 0){
			data.addAll(orders);
		}else if(state == 1){
			for(int i = 0; i < orders.size(); i ++){
				if(orders.get(i).getOrderStatus().equals("I")){
					data.add(orders.get(i));
				}
			}
		}else{
			for(int i = 0; i < orders.size(); i ++){
				if(orders.get(i).getOrderStatus().equals("S")){
					data.add(orders.get(i));
				}
			}
		}
		
	}
	
	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				List<Order> orders = (List<Order>) msg.obj;
				if(orders != null && orders.size() > 0){
					data.clear();
					getOrderByState(orders);
					adapter.notifyDataSetChanged();
				}else{
					
				}
				break;

			default:
				break;
			}
		}
		
	};

}
