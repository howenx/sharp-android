package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyCollectionAdapter;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.ActionBarUtil;

public class MyCollectionActivity  extends BaseActivity implements OnRefreshListener<ListView>{
	
	private PullToRefreshListView mListView;
	private List<Sku> datas;
	private MyCollectionAdapter adapter;
	
	

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "我的收藏");
		setContentView(R.layout.my_collection_layout);
		mListView = (PullToRefreshListView) findViewById(R.id.mListView);
		datas = new ArrayList<Sku>();
		adapter = new MyCollectionAdapter(datas, this);
		mListView.setAdapter(adapter);
		loadCollectionData();
		mListView.setMode(Mode.PULL_FROM_END);
	}
	
	private void loadCollectionData(){
		for(int i = 0;i<100;i++){
			datas.add(new Sku());
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if(mListView.isRefreshing())
			mListView.onRefreshComplete();
	}
	
	

}
