package com.hanmimei.utils;

import android.content.Context;
import android.widget.ImageView;

import com.hanmimei.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class InitImageLoader {
	private static ImageLoader imageLoader;
	private static DisplayImageOptions imageOptions;
	public static ImageLoader initLoader(Context mContext){
		imageLoader = ImageLoader.getInstance();
		// 初始化imageloader、displayImageOptions
		imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
		return imageLoader;
	}
	@SuppressWarnings("deprecation")
	public static DisplayImageOptions initOptions(){
		imageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.showStubImage(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisc(true).build();
		return imageOptions;
	}
	
	public static void loadImage(Context mContext,String url ,ImageView imgView){
		initOptions();
		imageLoader = initLoader(mContext);
		imageLoader.displayImage(url, imgView,imageOptions);
	}
}
