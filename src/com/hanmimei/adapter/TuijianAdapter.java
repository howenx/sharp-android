package com.hanmimei.adapter;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hanmimei.R;
import com.hanmimei.entity.HMMGoods;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.ImageLoaderUtils;

public class TuijianAdapter extends BaseAdapter {
	private List<HMMGoods> data;
	private LayoutInflater inflater;
	private Activity activity;

	public TuijianAdapter(List<HMMGoods> data, Context mContext) {
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
		HMMGoods theme = data.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tuijian_item_layout, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.old_price = (TextView) convertView
					.findViewById(R.id.old_price);
			holder.discount = (TextView) convertView
					.findViewById(R.id.discount);
			holder.sold_out = (ImageView) convertView
					.findViewById(R.id.sold_out);
			holder.timeView = (TextView) convertView
					.findViewById(R.id.timeView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoaderUtils.loadImage(activity, theme.getItemImgForImgInfo()
				.getUrl(), holder.img);
		holder.title.setText(theme.getItemTitle());
//		if (theme.getItemDiscount() > 0) {
//			holder.discount.setText(activity.getResources().getString(
//					R.string.discount, theme.getItemDiscount()));
//			holder.old_price.setText(activity.getResources().getString(
//					R.string.price, theme.getItemSrcPrice()));
//			holder.old_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//		}
//		holder.price.setText(activity.getResources().getString(R.string.price,
//				theme.getItemPrice()));
		 if (theme.getItemType().equals("pin")) {
			holder.sold_out.setVisibility(View.GONE);
			holder.timeView.setVisibility(View.VISIBLE);
			if (theme.getState().equals("P")) {
				holder.sold_out.setVisibility(View.GONE);
				holder.timeView.setText(DateUtils.getTimeDiffDesc(theme.getStartAt())+"开售");
			} else if(theme.getState().equals("Y")){
				holder.sold_out.setVisibility(View.GONE);
				holder.timeView.setText("截止"+ DateUtils.getTimeDiffDesc(theme.getEndAt()));
			}else{
				holder.timeView.setText("已结束");
			}
		}else {
			holder.timeView.setVisibility(View.GONE);
			if (theme.getState().equals("Y")) {
				holder.sold_out.setVisibility(View.GONE);
			} else {
				holder.sold_out.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView img, sold_out;
		private TextView title;
		private TextView price;
		private TextView old_price;
		private TextView discount;
		private TextView timeView;
	}
	
}