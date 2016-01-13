package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.ItemFeature;

public class GoodsDetailParamAdapter extends BaseAdapter{
	
	private List<ItemFeature> datas;
	private Context context;
	
	
	public GoodsDetailParamAdapter(List<ItemFeature> datas, Context context) {
		super();
		this.datas = datas;
		this.context = context;
	}

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
			arg1 = LayoutInflater.from(context).inflate(R.layout.goods_detail_param_item_layout, null);
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
