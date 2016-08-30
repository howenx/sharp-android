package com.kakao.kakaogift.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.entity.Entry;
import com.kakao.kakaogift.utils.GlideLoaderTools;

/**
 * @author eric
 *
 */
public class CategoryAdapter extends BaseAdapter {
	private List<Entry> data;
	private LayoutInflater inflater;
	private Activity activity;

	public CategoryAdapter (List<Entry> data, Context mContext){
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
	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Entry entry = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.category_item_layout, null);
			holder = new ViewHolder();
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		GlideLoaderTools.loadSquareImage(activity, entry.getImgUrl(), holder.img);
		holder.name.setText(entry.getNavText());
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView name;
	}

}
