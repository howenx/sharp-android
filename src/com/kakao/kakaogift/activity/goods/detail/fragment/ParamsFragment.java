package com.kakao.kakaogift.activity.goods.detail.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.goods.detail.adapter.GoodsDetailParamAdapter;
import com.kakao.kakaogift.entity.ItemFeature;
/**
 * 
 * @author vince
 *
 */
public class ParamsFragment extends ScrollAbleFragment{
	
	private List<ItemFeature> data;
	private GoodsDetailParamAdapter adpater;
	

//	public static ParamsFragment newInstance(String itemFeatures) {
//		ParamsFragment newFragment = new ParamsFragment();
//		Bundle bundle = new Bundle();
//		bundle.putString("itemFeatures", itemFeatures);
//		newFragment.setArguments(bundle);
//		// bundle还可以在每个标签里传送数据
//		return newFragment;
//	}
	private ListView mListView;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.listview_layout, null);
//		String itemFeatures = getArguments().getString("itemFeatures");
		 mListView = (ListView) view.findViewById(R.id.mListView);
		 data = new ArrayList<ItemFeature>();
		 adpater = new GoodsDetailParamAdapter(data, getActivity());
		mListView.setAdapter(adpater);
		return view;
	}
	
	public List<ItemFeature> getItemFeatures(String itemFeatures) {
		List<ItemFeature> list = new ArrayList<ItemFeature>();
		Map<String, String> map = null;
		try {
			map = new Gson().fromJson(itemFeatures,
					new TypeToken<Map<String, String>>() {
					}.getType());
		} catch (Exception e) {
			return null;
		}
		
		ItemFeature f = null;
		for (String key : map.keySet()) {
			f = new ItemFeature();
			f.setKey(key);
			f.setValue(map.get(key));
			list.add(f);
		}
		return list;
	}

	@Override
	public View getScrollableView() {
		// TODO Auto-generated method stub
		return mListView;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "商品参数";
	}

	@Override
	public void showData(Object obj) {
		if(obj == null)
			return;
		data.addAll(getItemFeatures(obj.toString()));
		adpater.notifyDataSetChanged();
	}
	
}
