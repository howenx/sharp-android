package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
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

public class OrderDetailListAdapter extends BaseAdapter {

	private List<Sku> data;
	private LayoutInflater inflater;
	private Activity activity;
	
	public OrderDetailListAdapter(List<Sku> data, Context mContext){
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
			convertView = inflater.inflate(R.layout.oder_detail_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.order_detail);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.nums = (TextView) convertView.findViewById(R.id.order_nums);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoaderUtils.loadImage(activity, sku.getInvImg(), holder.img);
		holder.title.setText(position + 1 + "." + sku.getSkuTitle());
		holder.nums.setText("数量：" + sku.getAmount());
		holder.price.setText("¥" + sku.getPrice());
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView title;
		private TextView nums;
		private TextView price;
	}
}
