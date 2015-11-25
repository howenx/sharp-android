package com.hanbimei.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hanbimei.R;
import com.hanbimei.adapter.OrderPullListAdapter;
import com.hanbimei.entity.Category;
import com.hanbimei.entity.Goods;
import com.hanbimei.entity.Order;
import com.hanbimei.view.CustomListView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

@SuppressLint("InflateParams")
public class OrderFragment extends Fragment {

	private PullToRefreshListView mListView;
	private List<Order> data;
	private OrderPullListAdapter adapter;
	private Category category;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new ArrayList<Order>();
		adapter = new OrderPullListAdapter(data, getActivity());
		Bundle bundle = getArguments();
		category = (Category) bundle.getSerializable("category");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		if (category.getId().equals("tag00")) {
			view = inflater.inflate(R.layout.pulltorefresh_list_layout, null);
			mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
			mListView.setAdapter(adapter);
			loadOrder();
		}
		return view;
	}

	private void loadOrder() {
		for (int i = 0; i < 3; i++) {
			List<Goods> list = new ArrayList<Goods>();
			list.add(new Goods(
					"http://image.cn.made-in-china.com/4f0j01wsiEjcvWEPzI/%E6%88%91%E7%9A%84%E5%BF%83%E6%9C%BA%E9%9D%A2%E8%86%9C.jpg",
					"我的心机丝瓜补水面膜 X5张 ＋  火山泥去黑头美白面膜 X2", "112.50", 1,
					"www.baidu.com"));
			list.add(new Goods(
					"http://image.cn.made-in-china.com/4f0j01wsiEjcvWEPzI/%E6%88%91%E7%9A%84%E5%BF%83%E6%9C%BA%E9%9D%A2%E8%86%9C.jpg",
					"我的心机丝瓜补水面膜 X5张 ＋  火山泥去黑头美白面膜 X2", "112.50", 1,
					"www.baidu.com"));
			data.add(new Order("201511241134092734", "待付款",
					"2015-11-24 09:20:01", list));
		}
		adapter.notifyDataSetChanged();
	}

}
