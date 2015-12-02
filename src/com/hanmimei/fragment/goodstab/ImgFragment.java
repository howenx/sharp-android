package com.hanmimei.fragment.goodstab;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.GoodsDetailImgAdapter;
import com.hanmimei.adapter.GoodsDetailImgAdapter.LoadCallback;

public class ImgFragment extends Fragment {

	public static final String TAG = ImgFragment.class.getSimpleName();

	private List<Bitmap> datas;
	private String itemNotice;
	private ListView mListView;
	private GoodsDetailImgAdapter adapter;
	private View view;

	public static ImgFragment newInstance(String itemNotice, List<Bitmap> datas) {
		ImgFragment fragment = new ImgFragment();
		fragment.initFragment(itemNotice, datas);
		return fragment;
	}

	public ImgFragment() {
		super();
	}

	public void initFragment(String itemNotice, List<Bitmap> datas) {
		this.datas = datas;
		this.itemNotice = itemNotice;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(view == null){
			view = inflater.inflate(R.layout.goods_detail_img_layout, null);
			mListView = (ListView) view.findViewById(R.id.mListView);
			setAdapter();
		}
		return view;
	}

	public void setAdapter() {
		if (getActivity() == null)
			return;
		if (TextUtils.isEmpty(itemNotice)) {
			View view = getActivity().getLayoutInflater().inflate(
					R.layout.text_panel, null);
			TextView textView = (TextView) view
					.findViewById(R.id.itemNoticeView);
			textView.setText(itemNotice);
			mListView.addHeaderView(textView);
		}

		 adapter = new GoodsDetailImgAdapter(datas,
				getContext(),null);
		mListView.setAdapter(adapter);
		mListView.setFocusable(false);
	}
	
	

}
