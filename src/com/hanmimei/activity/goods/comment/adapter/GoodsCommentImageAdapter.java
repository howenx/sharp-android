/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午6:06:29 
 **/
package com.hanmimei.activity.goods.comment.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.utils.GlideLoaderUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * @author vince
 * 
 */
public class GoodsCommentImageAdapter extends BaseAdapter {

	private Context context;
	private List<String> images;

	public GoodsCommentImageAdapter(Context context, List<String> images) {
		super();
		this.context = context;
		this.images = images;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return images.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return images.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.img_panel, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.my_image_view);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		GlideLoaderUtils.loadImage(context, images.get(position),
				holder.imageView);

		return convertView;
	}

	private class ViewHolder {
		public ImageView imageView;
	}

}
