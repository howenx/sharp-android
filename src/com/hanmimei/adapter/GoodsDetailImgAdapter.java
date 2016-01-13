package com.hanmimei.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hanmimei.R;
import com.hanmimei.activity.GoodsDetailImgActivity;
import com.hanmimei.utils.ImageLoaderUtils;

/**
 * 设配器
 */
public class GoodsDetailImgAdapter extends BaseAdapter{
	
	private ArrayList<String> urls; 
	private Context context;
	

    public GoodsDetailImgAdapter(ArrayList<String> urls, Context context) {
		super();
		this.urls = urls;
		this.context = context;
	}

	@Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_detail_img_item_layout, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.mImageView);
        ImageLoaderUtils.loadImage(context, urls.get(position%urls.size()), imageView);
        imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, GoodsDetailImgActivity.class);
				intent.putStringArrayListExtra("imgUrls", urls);
				context.startActivity(intent);
			}
		});
        return convertView;
    }
}
