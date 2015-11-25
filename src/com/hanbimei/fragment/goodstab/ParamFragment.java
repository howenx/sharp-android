package com.hanbimei.fragment.goodstab;

import com.hanbimei.R;
import com.hanbimei.adapter.GoodsDetailImgAdapter;
import com.hanbimei.listener.DataLoadListener;
import com.hanbimei.view.CustomListView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ParamFragment extends Fragment implements DataLoadListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.goods_detail_img_layout, null);

		return view;
	}

	@Override
	public void dataLoad(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
