package com.hanmimei.fragment.goodstab;

import com.hanmimei.R;
import com.hanmimei.listener.DataLoadListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HotFragment extends Fragment  implements DataLoadListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.goods_detail_img_layout, null);
		return view;
	}

	@Override
	public void dataLoad(Object obj,String itemNotice) {
		// TODO Auto-generated method stub
		
	}

}
