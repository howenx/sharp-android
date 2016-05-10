/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-5-9 下午5:30:03 
**/
package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.hanmimei.R;
import com.hanmimei.entity.HGoodsVo;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.GlideLoaderTools;

/**
 * @author vince
 *
 */
public class PushAdapter extends RecyclerView.Adapter<PushAdapter.ViewHolder> {
	
	private List<HGoodsVo> data;
	private  Context mContext;

	public PushAdapter(List<HGoodsVo> data, Context mContext) {
		this.data = data;
		this.mContext =  mContext;
		// 图片的比例适配
	}

	
	public class ViewHolder extends RecyclerView.ViewHolder{
		private ImageView img, sold_out;
		private View sold_ready;
		private TextView title;
		private TextView price;
		private TextView old_price;
		private TextView discount;
		private TextView timeView;
		private View pin_tip;

		public ViewHolder(View convertView) {
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
		}
		
	}

	/* (non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#getItemCount()
	 */
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data != null ? data.size() : 0;
	}

	/* (non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)
	 */
	@Override
	public void onBindViewHolder(PushAdapter.ViewHolder arg0, int position) {
		HGoodsVo theme = data.get(position);
		ViewHolder holder = arg0;

		GlideLoaderTools.loadSquareImage(mContext, theme.getItemImgForImgInfo()
				.getUrl(), holder.img);
		holder.title.setText(theme.getItemTitle());

		if (theme.getItemType().equals("pin")) {
			holder.sold_ready.setVisibility(View.GONE);
			holder.pin_tip.setVisibility(View.VISIBLE);
			holder.price.setText(mContext.getResources().getString(
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
			holder.price.setText(mContext.getResources().getString(
					R.string.price, theme.getItemPrice()));
			if (theme.getItemDiscount() > 0) {
				holder.discount.setText(mContext.getResources().getString(
						R.string.discount, theme.getItemDiscount()));
				holder.old_price.setText(mContext.getResources().getString(
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
		if(mOnItemClickListener !=null)
			mOnItemClickListener.onItemClick( null, holder.itemView, position, holder.itemView.getId());
	}

	/* (non-Javadoc)
	 * @see android.support.v7.widget.RecyclerView.Adapter#onCreateViewHolder(android.view.ViewGroup, int)
	 */
	@Override
	public PushAdapter.ViewHolder onCreateViewHolder(ViewGroup arg0,
			int arg1) {
		View view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.tuijian_item_layout, arg0,false);
		return new ViewHolder(view);
	}
	
	public OnItemClickListener mOnItemClickListener;
	public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
		this.mOnItemClickListener = mOnItemClickListener;
	}
}
