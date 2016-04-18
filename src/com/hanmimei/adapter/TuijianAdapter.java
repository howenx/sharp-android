package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.HGoods;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.GlideLoaderUtils;

public class TuijianAdapter extends BaseAdapter {
	private List<HGoods> data;
	private LayoutInflater inflater;
	private Activity activity;

	public TuijianAdapter(List<HGoods> data, Context mContext) {
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
		// 图片的比例适配
	}

	@Override
	public int getCount() {
		return data != null ? data.size() : 0;
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
		HGoods theme = data.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tuijian_item_layout, null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GlideLoaderUtils.loadGoodsImage(activity, theme.getItemImgForImgInfo()
				.getUrl(), holder.img);
		holder.title.setText(theme.getItemTitle());

		if (theme.getItemType().equals("pin")) {
			holder.sold_ready.setVisibility(View.GONE);
			holder.pin_tip.setVisibility(View.VISIBLE);
			holder.price.setText(activity.getResources().getString(
					R.string.price, theme.getItemPrice()));
			if (theme.getState().equals("P")) {
				holder.sold_out.setVisibility(View.GONE);
				holder.timeView.setText(DateUtils.getTimeDiffDesc(theme
						.getStartAt()) + "开售");
			} else if (theme.getState().equals("Y")) {
				holder.sold_out.setVisibility(View.GONE);
				holder.timeView.setText("截止"
						+ DateUtils.getTimeDiffDesc(theme.getEndAt()));
			} else {
				holder.timeView.setText("已结束");
				holder.sold_out.setVisibility(View.VISIBLE);
			}
		} else {
			holder.price.setText(activity.getResources().getString(
					R.string.price, theme.getItemPrice()));
			if (theme.getItemDiscount() > 0) {
				holder.discount.setText(activity.getResources().getString(
						R.string.discount, theme.getItemDiscount()));
				holder.old_price.setText(activity.getResources().getString(
						R.string.price, theme.getItemSrcPrice()));
				holder.old_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			}
			holder.pin_tip.setVisibility(View.GONE);

			if (theme.getState().equals("P")) {
				holder.sold_ready.setVisibility(View.VISIBLE);
				holder.sold_out.setVisibility(View.GONE);
			} else if (theme.getState().equals("Y")) {
				holder.sold_out.setVisibility(View.GONE);
				holder.sold_ready.setVisibility(View.GONE);
			} else {
				holder.sold_ready.setVisibility(View.GONE);
				holder.sold_out.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView img, sold_out;
		private View sold_ready;
		private TextView title;
		private TextView price;
		private TextView old_price;
		private TextView discount;
		private TextView timeView;
		private View pin_tip;

		public ViewHolder(View convertView) {
			super();
			this.img = (ImageView) convertView.findViewById(R.id.img);
			this.title = (TextView) convertView.findViewById(R.id.title);
			this.price = (TextView) convertView.findViewById(R.id.price);
			this.old_price = (TextView) convertView
					.findViewById(R.id.old_price);
			this.discount = (TextView) convertView.findViewById(R.id.discount);
			this.sold_out = (ImageView) convertView.findViewById(R.id.sold_out);
			this.sold_ready = convertView.findViewById(R.id.sold_ready);
			this.timeView = (TextView) convertView.findViewById(R.id.timeView);
			this.pin_tip = convertView.findViewById(R.id.pin_tip);
		}

	}

}
