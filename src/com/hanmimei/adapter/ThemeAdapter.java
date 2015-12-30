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
import com.squareup.picasso.Picasso;

public class ThemeAdapter extends BaseAdapter {
	private List<HMMGoods> data;
	private LayoutInflater inflater;
	private Activity activity;
	private int viewWidth;

	public ThemeAdapter (List<HMMGoods> data, Context mContext){
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
//		图片的比例适配
		viewWidth = CommonUtil.getScreenWidth(mContext)/2;
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
		if(convertView == null){
			convertView = inflater.inflate(R.layout.themeitem_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.old_price = (TextView) convertView.findViewById(R.id.old_price);
			holder.area = (TextView) convertView.findViewById(R.id.area);
//			holder.discount = (TextView) convertView.findViewById(R.id.discount);
			holder.sold_out = (ImageView) convertView.findViewById(R.id.sold_out);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		 LayoutParams params = holder.img.getLayoutParams();
		params.height = viewWidth - 10;
		params.width = viewWidth -10;
		holder.img.setLayoutParams(params);
		Picasso.with(activity).load(theme.getItemImgForImgInfo().getUrl()).into(holder.img);
		holder.title.setText(theme.getItemTitle());
		holder.area.setText(theme.getInvAreaNm());
		holder.price.setText(activity.getResources().getString(R.string.price, theme.getItemPrice()));
		holder.old_price.setText(activity.getResources().getString(R.string.price, theme.getItemSrcPrice()));
		holder.old_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		if(theme.getState().equals("Y")){
			holder.sold_out.setVisibility(View.GONE);
		}else{
			holder.sold_out.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	private class ViewHolder{
		private ImageView img,sold_out;
		private TextView title;
		private TextView price;
		private TextView old_price;
//		private TextView discount;
		private TextView area;
	}

}
