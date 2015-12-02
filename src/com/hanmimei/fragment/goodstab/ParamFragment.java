package com.hanmimei.fragment.goodstab;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.GoodsDetail.ItemFeature;

public class ParamFragment extends Fragment{

	private ListView mListView;
	private List<ItemFeature> datas;
	private View view;
	
	public static ParamFragment newInstance(List<ItemFeature> datas) {
		ParamFragment fragment = new ParamFragment();
		fragment.initFragment(datas);
		return fragment;
	}
	

	public ParamFragment() {
		super();
	}

	public void initFragment(List<ItemFeature> datas) {
		this.datas = datas;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.goods_detail_param_layout, null);
			mListView = (ListView) view.findViewById(R.id.mListView);
			mListView.setAdapter(new CustomAdapter());
		}
		
		return view;
	}
	
	
	
	private class CustomAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if(arg1 == null){
				arg1 = getActivity().getLayoutInflater().inflate(R.layout.goods_detail_param_item_layout, null);
				holder  = new ViewHolder(arg1);
				
				arg1.setTag(holder);
			}else{
				holder = (ViewHolder) arg1.getTag();
			}
			
			ItemFeature f = datas.get(arg0);
			holder.key.setText(f.getKey());
			holder.value.setText(f.getValue());
			
			return arg1;
		}
		
		private class ViewHolder {
			public TextView key,value;

			public ViewHolder(View view) {
				super();
				this.key = (TextView) view.findViewById(R.id.key);
				this.value = (TextView) view.findViewById(R.id.value);
			}
			
		}
		
	}

}
