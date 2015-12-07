package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hanmimei.R;
import com.hanmimei.entity.BitmapInfo;
import com.squareup.picasso.Picasso;

public class GoodsDetailImgAdapter extends BaseAdapter {

	private List<BitmapInfo> data;
	private Context context;
	private LoadCallback mLoadCallback;
	int screenWidth;
	int screenHeight;

	public GoodsDetailImgAdapter(List<BitmapInfo> data, Context context,int screenHeight,int screenWidth) {
		super();
		this.data = data;
		this.context = context;
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data != null ? data.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data != null ? data.get(arg0) : null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		BitmapInfo info = null;
		if (contentView == null) {
			contentView = LayoutInflater.from(context).inflate(
					R.layout.img_panel, null);
			info = data.get(position);
			holder = new ViewHolder(contentView);
			
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		
		info = data.get(position);
		int viewHeight = (int) (screenWidth/info.getScaleSize());
		if(viewHeight>screenHeight)
			viewHeight = screenHeight;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,viewHeight);
		holder.mImageView.setLayoutParams(lp);
		
		Picasso.with(context).load(info.getUrl()).into(holder.mImageView);
		return contentView;
	}

	private  class ViewHolder {
		public ImageView mImageView;

		public ViewHolder(View view) {
			mImageView = (ImageView) view.findViewById(R.id.my_image_view);
		}
	}
	
	public interface LoadCallback{
		public void finish();
	};

}
