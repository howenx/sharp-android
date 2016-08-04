/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-4 上午10:45:53 
 **/
package com.kakao.kakaogift.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.adapter.GoodsPushAdapter.PushHolder;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.utils.DateUtils;
import com.kakao.kakaogift.utils.GlideLoaderTools;

/**
 * @author vince
 * 
 */
public class GoodsPushAdapter extends Adapter<PushHolder> {

	private List<HGoodsVo> data;
	private Activity activity;

	public GoodsPushAdapter(Activity activity, List<HGoodsVo> data) {
		super();
		this.data = data;
		this.activity = activity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
	 */
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android
	 * .support.v7.widget.RecyclerView.ViewHolder, int)
	 */
	@Override
	public void onBindViewHolder(PushHolder holder, int position) {
		// TODO Auto-generated method stub
		HGoodsVo theme = data.get(position);
		GlideLoaderTools.loadSquareImage(activity, theme.getItemImgForImgInfo()
				.getUrl(), holder.img);
		holder.title.setText(theme.getItemTitle());
		holder.position = position;
		if (theme.getItemType().equals("pin")) {
			holder.sold_ready.setVisibility(View.GONE);
			holder.pin_tip.setVisibility(View.VISIBLE);
			holder.price.setText(theme.getItemPrice());
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
			holder.price.setText(theme.getItemPrice());
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
		Log.i("push_price", theme.getItemPrice());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android
	 * .view.ViewGroup, int)
	 */
	@Override
	public PushHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		View convertView = LayoutInflater.from(arg0.getContext()).inflate(
				R.layout.goods_push_item_layout, null);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		convertView.setLayoutParams(lp);
		return new PushHolder(convertView);
	}

	class PushHolder extends ViewHolder implements OnClickListener {

		public ImageView img, sold_out;
		public View sold_ready;
		public TextView title;
		public TextView price;
		public TextView old_price;
		public TextView discount;
		public TextView timeView;
		public View pin_tip;
		public int position;

		/**
		 * @param arg0
		 */
		public PushHolder(View convertView) {
			super(convertView);
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
			convertView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			onRecyclerItemClickListener.onItemClick(position);
		}

	}

	public static interface OnRecyclerItemClickListener {
		void onItemClick(int position);
	}

	private OnRecyclerItemClickListener onRecyclerItemClickListener;

	public void setOnRecyclerItemClickListener(
			OnRecyclerItemClickListener onRecyclerItemClickListener) {
		this.onRecyclerItemClickListener = onRecyclerItemClickListener;
	}
}
