package com.kakao.kakaogift.activity.goods.theme.adapter;

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

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.utils.DateUtils;
import com.kakao.kakaogift.utils.GlideLoaderTools;

public class ThemeAdapter extends BaseAdapter {
	private List<HGoodsVo> data;
	private LayoutInflater inflater;
	private Activity activity;

	public ThemeAdapter(List<HGoodsVo> data, Context mContext) {
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
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
		HGoodsVo theme = data.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.theme_item_layout, null);
			holder = new ViewHolder(convertView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		GlideLoaderTools.loadSquareImage(activity, theme.getItemImgForImgInfo()
				.getUrl(), holder.img);
		holder.title.setText(theme.getItemTitle());

		if (theme.getItemType().equals("pin")) {
			holder.sold_ready.setVisibility(View.GONE);
			holder.pin_tip.setVisibility(View.VISIBLE);
			holder.price.setText(theme.getItemPrice());
			holder.discount.setText("低至"+activity.getResources().getString(
					R.string.discount, theme.getItemDiscount()));
			holder.old_price.setText("最低");
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
				holder.sold_out.setText("已结束");
			}
		} else {
			holder.price.setText(theme.getItemPrice());
			if (theme.getItemDiscount() > 0) {
				holder.discount.setText(activity.getResources().getString(
						R.string.discount, theme.getItemDiscount()));
				holder.old_price.setText(activity.getResources().getString(
						R.string.price_src, theme.getItemSrcPrice()));
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
				holder.sold_out.setText("已抢光");
			}
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView img;
		private View sold_ready;
		private TextView title,sold_out;
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
			this.sold_out = (TextView) convertView.findViewById(R.id.sold_out);
			this.sold_ready = convertView.findViewById(R.id.sold_ready);
			this.timeView = (TextView) convertView.findViewById(R.id.timeView);
			this.pin_tip = convertView.findViewById(R.id.pin_tip);
		}

	}

}
