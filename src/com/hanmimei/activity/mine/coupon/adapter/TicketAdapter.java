package com.hanmimei.activity.mine.coupon.adapter;

import java.math.BigDecimal;
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
import com.hanmimei.entity.CouponVo;

/**
 * @author eric
 *
 */
public class TicketAdapter extends BaseAdapter {
	private List<CouponVo> data;
	private LayoutInflater inflater;
	private Activity activity;

	public TicketAdapter (List<CouponVo> data, Context mContext){
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
		CouponVo coupon = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.ticket_item, null);
			holder = new ViewHolder();
			holder.bg_img = (ImageView) convertView.findViewById(R.id.bg);
			holder.money = (TextView) convertView.findViewById(R.id.denomination);
			holder.more_pay = (TextView) convertView.findViewById(R.id.limit);
			holder.use_date = (TextView) convertView.findViewById(R.id.date);
			holder.cat = (TextView) convertView.findViewById(R.id.cat);
			holder.biaoshi = (TextView) convertView.findViewById(R.id.biaoshi);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(coupon.getState().equals("N")){
			holder.money.setTextColor(activity.getResources().getColor(R.color.huang));
			holder.biaoshi.setTextColor(activity.getResources().getColor(R.color.huang));
			holder.bg_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.hmm_bg_youhuiquan));
		}else if(coupon.getState().equals("Y")){
			holder.biaoshi.setTextColor(activity.getResources().getColor(R.color.fontcolor));
			holder.money.setTextColor(activity.getResources().getColor(R.color.fontcolor));
			holder.bg_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.hmm_bg_hui_youhuiquan));
		}else{
			holder.biaoshi.setTextColor(activity.getResources().getColor(R.color.fontcolor));
			holder.money.setTextColor(activity.getResources().getColor(R.color.fontcolor));
			holder.bg_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.hmm_bg_hui_youhuiquan));
		}
		holder.money.setText(coupon.getDenomination() + "");
		if(coupon.getLimitQuota().compareTo(BigDecimal.valueOf(0)) == 0){
			holder.more_pay.setText("无限制");
		}else{
			holder.more_pay.setText("满" + coupon.getLimitQuota() + "可用");
		}
		holder.use_date.setText("有效期至：" + coupon.getEndAt());
		holder.cat.setText(coupon.getCateNm());
		return convertView;
	}
	private class ViewHolder{
		private TextView money;
		private TextView more_pay;
		private TextView use_date;
		private ImageView bg_img;
		private TextView cat;
		private TextView biaoshi;
	}

}
