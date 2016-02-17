package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyCollectionAdapter;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.ToastUtils;

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
		mListView.getRefreshableView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ToastUtils.Toast(MyCollectionActivity.this, "您长按了item" + arg2);
				return false;
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ToastUtils.Toast(MyCollectionActivity.this, "点击item  跳转到商品的详情页面" + arg2);
			}
		});
	}
	
	private void loadCollectionData(){
		for(int i = 0; i < 100; i++){
			Sku sku = new Sku();
			sku.setInvImg("http://img10.360buyimg.com/n1/jfs/t2404/75/2184548534/472835/e796835d/569e333eN46914d00.jpg");
			sku.setSkuTitle("创维（Skyworth）55M5 55英寸 4K超高清智能酷开网络液晶电视（黑色");
			sku.setPrice(3499);
			datas.add(sku);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		if(mListView.isRefreshing())
			mListView.onRefreshComplete();
	}
}
