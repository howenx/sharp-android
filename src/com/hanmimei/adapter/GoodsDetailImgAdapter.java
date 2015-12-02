package com.hanmimei.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hanmimei.R;
import com.hanmimei.utils.CommonUtil;

public class GoodsDetailImgAdapter extends BaseAdapter {

	private List<Bitmap> data;
	private Context context;
	private LoadCallback mLoadCallback;

	public GoodsDetailImgAdapter(List<Bitmap> data, Context context,LoadCallback mLoadCallback) {
		super();
		this.data = data;
		this.context = context;
		this.mLoadCallback = mLoadCallback;
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
//		ViewHolder holder = null;
//		Bitmap bitmap = null;
//		if (contentView == null) {
//			contentView = LayoutInflater.from(context).inflate(
//					R.layout.img_panel, null);
//			bitmap = data.get(position);
//			holder = new ViewHolder(contentView);
//			holder.mImageView.setTag(bitmap);
//			contentView.setTag(holder);
//		} else {
//			holder = (ViewHolder) contentView.getTag();
//		}
//		
//		if(holder.mImageView.getTag().equals(bitmap)){
//			 Picasso.with(context).load(url).into(holder.mImageView);
//			AsyncImageLoader.getInstance().loadBitmap(context, holder.mImageView,url, new ImageCallback() {
//				
//				@Override
//				public void imageLoaded(Bitmap imgBitmap, ImageView mImageView,
//						String imageUrl) {
//					// TODO Auto-generated method stub
//						mImageView.setImageBitmap(imgBitmap);
//				}
//			});
//			holder.mImageView.setImageBitmap(bitmap);
//		}
		
		View view =  LayoutInflater.from(context).inflate(
				R.layout.img_panel, null);
		ImageView imgView = (ImageView) view.findViewById(R.id.mImageView);
		Bitmap bm = data.get(position);
		int width = bm.getWidth();
		int screenWidth = CommonUtil.getScreenWidth(context);
		int viewHeight = (int) (bm.getHeight()*(float)screenWidth/(float)width);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,viewHeight);
		imgView.setLayoutParams(lp);
		imgView.setImageBitmap(bm);
		return view;
	}

//	private  class ViewHolder {
//		public ImageView mImageView;
//
//		public ViewHolder(View view) {
//			mImageView = (ImageView) view.findViewById(R.id.mImageView);
//		}
//	}
	
	public interface LoadCallback{
		public void finish();
	};

}
