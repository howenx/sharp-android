package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hanmimei.R;
import com.hanmimei.entity.Theme;
import com.hanmimei.utils.ImageLoaderUtils;

public class HomeAdapter extends BaseAdapter {
	private List<Theme> data;
	private LayoutInflater inflater;
	private Activity activity;

	public HomeAdapter (List<Theme> data, Context mContext){
		activity = (Activity) mContext;
		this.data = data;
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
		Theme theme = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.home_list_item, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoaderUtils.loadImage(activity, holder.img, theme.getThemeImg(), 2, 1);
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
	}

}
