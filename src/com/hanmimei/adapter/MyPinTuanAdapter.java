package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Goods;
import com.hanmimei.utils.ImageLoaderUtils;

public class MyPinTuanAdapter extends BaseAdapter {

	private List<Goods> data;
	private LayoutInflater inflater;
	public MyPinTuanAdapter(Context mContext, List<Goods> data){
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
		Goods goods = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.my_pintuan_item_layout, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.state = (TextView) convertView.findViewById(R.id.state);
			holder.check_tuan = (TextView) convertView.findViewById(R.id.see_detail);
			holder.check_order = (TextView) convertView.findViewById(R.id.see_order);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoaderUtils.loadImage(goods.getImgUrl(), holder.img);
		holder.title.setText(goods.getTitle());
		holder.price.setText("成团价：" + goods.getPrice());
		holder.state.setText("拼团成功");
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView img;
		private TextView title;
		private TextView price;
		private TextView state;
		private TextView check_tuan;
		private TextView check_order;
	}
      
}
