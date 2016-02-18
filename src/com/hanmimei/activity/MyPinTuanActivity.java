package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyPinTuanAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Goods;
import com.hanmimei.entity.PinActivity;
import com.hanmimei.entity.PinList;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;

public class MyPinTuanActivity extends BaseActivity {

	private PullToRefreshListView mListView;
	private MyPinTuanAdapter adapter;
	private List<PinActivity> data;
	private PinList list;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_pintuan_list_layout);
		ActionBarUtil.setActionBarStyle(this, "我的拼团");
		data = new ArrayList<PinActivity>();
		adapter = new MyPinTuanAdapter(this, data);
		mListView = (PullToRefreshListView) findViewById(R.id.mylist);
		mListView.setAdapter(adapter);
		loadData();
	}
	private void loadData() {
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.GET_MY_PINTUAN, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				try {
					initData(new Gson().fromJson(result, PinList.class));
				} catch (Exception e) {
					ToastUtils.Toast(getActivity(), R.string.error);
				}
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}
	
	private void initData(PinList list){
		if(list.getMessage().getCode() == 200){
			data.clear();
			data.addAll(list.getActivityList());
			adapter.notifyDataSetChanged();
		}else{
			ToastUtils.Toast(this, list.getMessage().getMessage());
		}
	}

}
