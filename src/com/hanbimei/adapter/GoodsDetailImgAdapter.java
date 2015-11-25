package com.hanbimei.adapter;

import java.util.List;

import com.hanbimei.R;
import com.hanbimei.utils.InitImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GoodsDetailImgAdapter extends BaseAdapter {
	
	private List<String> data;
	private Context context;
	

	public GoodsDetailImgAdapter(List<String> data, Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data !=null?data.size():0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data !=null?data.get(arg0):null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("InflateParams") @Override
	public View getView(int position, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(contentView == null){
			contentView = LayoutInflater.from(context).inflate(R.layout.img_panel, null);
			holder = new ViewHolder(contentView);
			
			contentView.setTag(holder);
		}else{
			holder = (ViewHolder) contentView.getTag();
		}
		
		Picasso.with(context).load(data.get(position)).into(holder.mImageView);
//		InitImageLoader.loadImage(context, data.get(position), holder.mImageView);
		
		return contentView;
	}
	
	private class ViewHolder{
		public ImageView mImageView;
		
		public ViewHolder(View view){
			mImageView = (ImageView) view.findViewById(R.id.mImageView);
		}
	}

}
