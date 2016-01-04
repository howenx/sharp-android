package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hanmimei.R;
import com.hanmimei.adapter.PingouListAdapter;
import com.hanmimei.entity.Goods;
import com.hanmimei.view.CustomListView;
import com.hanmimei.view.TimerTextView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PinFragment extends Fragment {
	
	private TimerTextView timer;
	private CustomListView mListView;
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
		timer = (TimerTextView) view.findViewById(R.id.timer);
		mListView = (CustomListView) view.findViewById(R.id.mylist);
		mListView.setAdapter(adapter);
		long [] time = {24, 0, 0};
		timer.setTimes(time);
		loadData();
		return view;
	}

	private void loadData() {
		for(int i = 0; i < 10; i ++){
			data.add(new Goods());
		}
		adapter.notifyDataSetChanged();
		timer.beginRun();
	}


}
