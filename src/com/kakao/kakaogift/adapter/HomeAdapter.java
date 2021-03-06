package com.kakao.kakaogift.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.entity.Theme;
import com.kakao.kakaogift.utils.CommonUtils;
import com.squareup.picasso.Picasso;

/**
 * @author eric
 *
 */
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
			holder.diver = convertView.findViewById(R.id.diver);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		int w = CommonUtils.getScreenWidth(activity);
		ViewGroup.LayoutParams lp = holder.img.getLayoutParams();
		lp.width = w;
		lp.height =ViewGroup.LayoutParams.WRAP_CONTENT;
		holder.img.setLayoutParams(lp);
		holder.img.setMaxWidth(w);
		holder.img.setMaxHeight(w); 
		Picasso.with(activity)
	    .load(theme.getThemeImg()).placeholder(R.drawable.hmm_place_holder_j)
	    .into( holder.img);  
		if(position == data.size() - 1){
			holder.diver.setVisibility(View.GONE);
		}else{
			holder.diver.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private View diver;
	}

}
