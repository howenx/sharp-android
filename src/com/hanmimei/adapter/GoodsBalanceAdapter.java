package com.hanmimei.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Sku;

public class GoodsBalanceAdapter extends BaseAdapter {

	private List<Sku> data;
	private LayoutInflater inflater;
	
	public GoodsBalanceAdapter(List<Sku> data, Context mContext){
		this.data = data;
		inflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Sku sku = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.goods_balance_item_layout, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		return convertView;
	}
	private class ViewHolder{
		private TextView pro_name;
		private TextView pro_nums;
		private TextView pro_price;
		private View area_title;
		private TextView area_name;
		private ImageView pro_icon;
		public ViewHolder(View view) {
			super();
			this.pro_name = (TextView) view.findViewById(R.id.pro_name);
			this.pro_nums = (TextView) view.findViewById(R.id.pro_num);
			this.pro_price = (TextView) view.findViewById(R.id.pro_price);
			this.area_title = (TextView) view.findViewById(R.id.area_title);
			this.area_name = (TextView) view.findViewById(R.id.area_name);
			this.pro_icon = (ImageView) view.findViewById(R.id.pro_img);
		}
	}

}
