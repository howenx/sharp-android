package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hanmimei.R;
import com.hanmimei.activity.PingouDetailActivity;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.adapter.PingouListAdapter;
import com.hanmimei.entity.Goods;
import com.hanmimei.view.CustomListView;
import com.hanmimei.view.TimerTextView;


public class PinFragment extends Fragment implements TimeEndListner, OnRefreshListener2<ScrollView>{
	
	private TimerTextView timer;
	private CustomListView mListView;
	private PullToRefreshScrollView mScrollView;
	private PingouListAdapter adapter;
	private List<Goods> data;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.data = new ArrayList<Goods>();
		adapter = new PingouListAdapter(getActivity(), data);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.pingou_list_layout, null);
		mScrollView = (PullToRefreshScrollView) view.findViewById(R.id.myscroll);
		mScrollView.setMode(Mode.BOTH);
		mScrollView.setOnRefreshListener(this);
		timer = (TimerTextView) view.findViewById(R.id.timer);
		timer.setTimeEndListner(this);
		mListView = (CustomListView) view.findViewById(R.id.mylist);
		mListView.setFocusable(false);
		mListView.setAdapter(adapter);
		long [] time = {0, 3, 10};
		timer.setTimes(time,"拼购已经结束，请等待下次");
		loadData();
		return view;
	}

	private void loadData() {
		data.clear();
		for(int i = 0; i < 10; i ++){
			data.add(new Goods());
		}
		adapter.notifyDataSetChanged();
		timer.beginRun();
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), PingouDetailActivity.class));
			}
		});
		mScrollView.onRefreshComplete();
	}

	@Override
	public void isTimeEnd() {
		
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		loadData();
	}

}
