package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.ThemeItem;
import com.hanmimei.utils.InitImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ThemeAdapter extends BaseAdapter {
	private List<ThemeItem> data;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;
	private Activity activity;
	private int screenWidth;

	public ThemeAdapter (List<ThemeItem> data, Context mContext){
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
		imageLoader = InitImageLoader.initLoader(mContext);
		imageOptions = InitImageLoader.initOptions();
//		 图片的比例适配
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels/2;
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
		ThemeItem theme = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.themeitem_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.old_price = (TextView) convertView.findViewById(R.id.old_price);
			holder.discount = (TextView) convertView.findViewById(R.id.discount);
			holder.sold_out = (ImageView) convertView.findViewById(R.id.sold_out);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		 LayoutParams params = holder.img.getLayoutParams();
		params.height = screenWidth - 10;
		params.width = screenWidth -10;
		holder.img.setLayoutParams(params);
		imageLoader.displayImage(theme.getItemImg(), holder.img,imageOptions);
		holder.title.setText(theme.getItemTitle());
		holder.price.setText(activity.getResources().getString(R.string.price, theme.getItemPrice()));
		holder.old_price.setText(activity.getResources().getString(R.string.price, theme.getItemSrcPrice()));
		holder.old_price.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.discount.setText(activity.getResources().getString(R.string.discount, theme.getItemDiscount()));
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
		private TextView discount;
	}

}
