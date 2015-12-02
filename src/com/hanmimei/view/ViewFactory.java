package com.hanmimei.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.hanmimei.R;
import com.hanmimei.utils.InitImageLoader;
/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param text
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		InitImageLoader.initLoader(context).displayImage(url, imageView, InitImageLoader.initOptions());
		return imageView;
	}
}
