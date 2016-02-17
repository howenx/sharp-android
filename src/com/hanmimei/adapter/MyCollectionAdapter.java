package com.hanmimei.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.ImageLoaderUtils;

@SuppressLint("InflateParams")
public class MyCollectionAdapter extends BaseAdapter {

	private List<Sku> data;
	private LayoutInflater inflater;

	public MyCollectionAdapter(List<Sku> list, Context mContext) {
		this.data = list;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Sku sku = data.get(position);
		ViewHolder holder = null;	
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.my_collection_item_layout, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoaderUtils.loadImage(sku.getInvImg(), holder.img);
			holder.name.setText(sku.getSkuTitle());
			holder.price.setText("单价：¥" + sku.getPrice());
			return convertView;
	}

	private class ViewHolder {
		private ImageView img;
		private TextView name;
		private TextView price;
	}

}
