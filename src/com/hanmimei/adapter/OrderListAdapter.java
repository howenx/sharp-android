package com.hanmimei.adapter;

import java.util.List;

import android.annotation.SuppressLint;
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

@SuppressLint("InflateParams")
public class OrderListAdapter extends BaseAdapter {

	private List<Sku> data;
	private LayoutInflater inflater;
	private Activity activity;

	public OrderListAdapter(List<Sku> list, Context mContext) {
		this.data = list;
		activity = (Activity) mContext;
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
		ViewHolderMore holderMore = null;
		// 判断数据条数，订单数据为一条的时候，使用order_list_item布局，多余一条则使用order_list_item_img布局
		if (data.size() == 1) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.order_list_item, null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.price = (TextView) convertView.findViewById(R.id.price);
				holder.nums = (TextView) convertView.findViewById(R.id.nums);
				holder.size = (TextView) convertView.findViewById(R.id.size);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageLoaderUtils.loadImage(activity, sku.getInvImg(), holder.img);
			holder.name.setText(sku.getSkuTitle());
			holder.price.setText("单价： ¥" + sku.getPrice());
			holder.nums.setText("x" + sku.getAmount());
			holder.size.setText(sku.getItemColor() + "  " + sku.getItemSize());
			return convertView;
		} else {
			if (convertView == null) {
				holderMore = new ViewHolderMore();
				convertView = inflater.inflate(R.layout.order_list_item_img,
						null);
				holderMore.img = (ImageView) convertView.findViewById(R.id.img);
				convertView.setTag(holder);
			} else {
				holderMore = (ViewHolderMore) convertView.getTag();
			}
			ImageLoaderUtils.loadImage(activity, sku.getInvImg(), holderMore.img);
			return convertView;
		}
	}

	private class ViewHolder {
		private ImageView img;
		private TextView name;
		private TextView price;
		private TextView nums;
		private TextView size;
	}

	private class ViewHolderMore {
		private ImageView img;
	}

}
