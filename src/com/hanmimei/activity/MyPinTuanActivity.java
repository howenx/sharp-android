package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyPinTuanAdapter;
import com.hanmimei.entity.Goods;
import com.hanmimei.utils.ActionBarUtil;

public class MyPinTuanActivity extends BaseActivity {

	private PullToRefreshListView mListView;
	private MyPinTuanAdapter adapter;
	private List<Goods> data;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pintuan_list_layout);
		ActionBarUtil.setActionBarStyle(this, "我的拼团");
		data = new ArrayList<Goods>();
		adapter = new MyPinTuanAdapter(this, data);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
		mListView.setAdapter(adapter);
		loadData();
	}
	private void loadData() {
		for(int i = 0; i < 6; i ++){
			data.add(new Goods("http://img10.360buyimg.com/n1/jfs/t2404/75/2184548534/472835/e796835d/569e333eN46914d00.jpg", "创维（Skyworth）55M5 55英寸 4K超高清智能酷开网络液晶电视（黑色", "3499.00", 1, ""));
		}
		adapter.notifyDataSetChanged();
	}

}
