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
import com.hanmimei.entity.Coupon;

public class TicketAdapter extends BaseAdapter {
	private List<Coupon> data;
	private LayoutInflater inflater;
	private Activity activity;

	public TicketAdapter (List<Coupon> data, Context mContext){
		this.data = data;
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
		Coupon coupon = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.ticket_item, null);
			holder = new ViewHolder();
			holder.bg_img = (ImageView) convertView.findViewById(R.id.bg);
			holder.money = (TextView) convertView.findViewById(R.id.denomination);
			holder.more_pay = (TextView) convertView.findViewById(R.id.limit);
			holder.use_date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(coupon.getState().equals("N")){
			holder.bg_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.youhuiquan_bg));
		}else if(coupon.getState().equals("Y")){
			holder.bg_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.youhuiquan_bg_use));
		}else{
			holder.bg_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.youhuiquan_bg_use));
		}
		holder.money.setText("¥" + coupon.getDenomination());
		holder.more_pay.setText("满" + coupon.getLimitQuota() + "可用");
		holder.use_date.setText("有效期至：" + coupon.getEndAt());
		return convertView;
	}
	private class ViewHolder{
		private TextView money;
		private TextView more_pay;
		private TextView use_date;
		private ImageView bg_img;
	}

}
