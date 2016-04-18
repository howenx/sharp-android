package com.hanmimei.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.hanmimei.R;
import com.hanmimei.activity.GoodsDetailActivity;
import com.hanmimei.activity.PingouDetailActivity;
import com.hanmimei.adapter.ThemeAdapter;
import com.hanmimei.entity.HGoods;

public class HotFragment extends ScrollAbleFragment {
	
	private List<HGoods> themeList;
	private ThemeAdapter adapter;
	
	
//	public void setThemeList(List<HMMGoods> themeList) {
//		this.themeList = themeList;
//	}

//	public static HotFragment newInstance(List<HMMGoods> themeList) {
//		HotFragment newFragment = new HotFragment();
//		newFragment.setThemeList(themeList);
//		// bundle还可以在每个标签里传送数据
//		return newFragment;
//	}

	private GridView mGridView;

	@SuppressLint("InflateParams")
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.gridview_layout, null);
		mGridView = (GridView) view.findViewById(R.id.mGridView);
		themeList = new ArrayList<HGoods>();
		adapter = new ThemeAdapter(themeList, getActivity());
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				if (data.get(arg2).getState().equals("Y")
//						|| data.get(arg2).getState().equals("P")) {
					Intent intent = null;
					if (themeList.get(arg2).getItemType().equals("pin")) {
						intent = new Intent(getActivity(),
								PingouDetailActivity.class);
					} else {
						intent = new Intent(getActivity(),GoodsDetailActivity.class);
					}
					intent.putExtra("url", themeList.get(arg2).getItemUrl());
					startActivityForResult(intent, 1);
//				}
			}
		});
		return view;
	}

	@Override
	public View getScrollableView() {
		return mGridView;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "热门商品";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void showData(Object obj) {
		themeList.addAll((List<HGoods>)obj);
		adapter.notifyDataSetChanged();
		
	}
}
