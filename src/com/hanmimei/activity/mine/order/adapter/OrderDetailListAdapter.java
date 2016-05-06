package com.hanmimei.activity.mine.order.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.apply.CustomerServiceActivity;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.GlideLoaderUtils;
/**
 * @author eric
 *
 */
public class OrderDetailListAdapter extends BaseAdapter {

	private List<Sku> data;
	private LayoutInflater inflater;
	private Activity activity;
	private boolean isShow;
	private String orderId;
	private String childId;
	
	public OrderDetailListAdapter(List<Sku> data, Context mContext, boolean isShow){
		this.data = data;
		this.isShow = isShow;
		activity = (Activity) mContext;
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
		final Sku sku = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.oder_detail_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.order_detail);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.nums = (TextView) convertView.findViewById(R.id.order_nums);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.btn_apply_service = (TextView) convertView.findViewById(R.id.btn_apply_service);
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		GlideLoaderUtils.loadGoodsImage(activity,sku.getInvImg(), holder.img);
		holder.title.setText(position + 1 + "." + sku.getSkuTitle());
		holder.nums.setText("数量：" + sku.getAmount());
		holder.price.setText("¥" + sku.getPrice());
		if(isShow){
			holder.btn_apply_service.setVisibility(View.VISIBLE);
		}else{
			holder.btn_apply_service.setVisibility(View.INVISIBLE);
		}
		holder.btn_apply_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity, CustomerServiceActivity.class);
				intent.putExtra("sku", sku);
				intent.putExtra("orderId", orderId);
				intent.putExtra("splitOrderId", childId);
				activity.startActivity(intent);
			}
		});
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView title;
		private TextView nums;
		private TextView price;
		private TextView btn_apply_service;
	}
	public void setOrderId(String orderId,String childId){
		this.orderId = orderId;
		this.childId = childId;
	}
}
