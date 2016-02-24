package com.hanmimei.activity.fragment;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.MyPingouDetailActivity;
import com.hanmimei.adapter.MyPinTuanAdapter;
import com.hanmimei.entity.PinActivity;

public class MyPinFragment extends Fragment {

	private MyPinTuanAdapter adapter;
	private PullToRefreshListView mListView;
	private List<PinActivity> data;

	public void setData(List<PinActivity> data) {
		this.data = data;
	}

	/**
	 * 添加标识,返回带标识的对象。
	 * 
	 * @param tag
	 *            标识
	 * @return 带标识的Fragment
	 */
	public static MyPinFragment newInstance(List<PinActivity> data) {
		MyPinFragment fragment = new MyPinFragment();
		fragment.setData(data);
		return fragment;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.my_pingou_list_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.setMode(Mode.DISABLED);
		if (data != null) {
			adapter = new MyPinTuanAdapter(getActivity(), data);
			mListView.setAdapter(adapter);
		}
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				startActivity(new Intent(getActivity(), MyPingouDetailActivity.class));
			}
		});
		return view;
	}

}
