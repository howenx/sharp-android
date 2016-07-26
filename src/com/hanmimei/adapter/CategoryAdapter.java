package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.Theme;
import com.hanmimei.utils.GlideLoaderTools;

/**
 * @author eric
 *
 */
public class CategoryAdapter extends BaseAdapter {
	private List<Category> data;
	private LayoutInflater inflater;
	private Activity activity;

	public CategoryAdapter (List<Category> data, Context mContext){
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
		Category category = data.get(position);
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
		
		GlideLoaderTools.loadCirlceImage(activity, "", holder.img);
		holder.name.setText("美装");
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView name;
	}

}
