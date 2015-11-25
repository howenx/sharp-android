package com.hanbimei.fragment.goodstab;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanbimei.R;
import com.hanbimei.adapter.GoodsDetailImgAdapter;
import com.hanbimei.listener.DataLoadListener;
import com.hanbimei.view.CustomListView;

public class ImgFragment extends Fragment implements DataLoadListener{

	private List<String> datas;
	private CustomListView mListView;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.goods_detail_img_layout, null);
		mListView = (CustomListView) view.findViewById(R.id.mListView);
		
		return view;
	}


	@Override
	public void dataLoad(Object obj) {
		datas = (List<String>) obj;
		mListView.setAdapter(new GoodsDetailImgAdapter(datas, getActivity()));
	}
	
	

}
