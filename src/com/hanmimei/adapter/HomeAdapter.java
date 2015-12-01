package com.hanmimei.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.entity.Theme;
import com.hanmimei.utils.InitImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HomeAdapter extends BaseAdapter {
	private List<Theme> data;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions imageOptions;

	public HomeAdapter (List<Theme> data, Context mContext){
		this.data = data;
		inflater = LayoutInflater.from(mContext);
		imageLoader = InitImageLoader.initLoader(mContext);
		imageOptions = InitImageLoader.initOptions();
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
		imageLoader.displayImage(theme.getThemeImg(), holder.img,imageOptions);
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
	}

}
