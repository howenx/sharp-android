package com.hanmimei.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoaderUtils {


	/**
	 * imageloader加载图片
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadImage(Context mContext, String url, ImageView imgView) {
		ImageLoader.getInstance().displayImage(url, imgView);
	}
	public static void loadImage(String url, ImageView imgView) {
		ImageLoader.getInstance().displayImage(url, imgView);
	}
	/**
	 * imageloader加载图片
	 * @param mContext
	 * @param url
	 * @param imgView
	 * @param l
	 */
	public static void loadImage(Context mContext, String url,
			ImageView imgView, ImageLoadingListener l) {
		ImageLoader.getInstance().displayImage(url, imgView, l);
	}
	/**
	 * imageloader加载图片  
	 * @param activity
	 * @param imgView
	 * @param url
	 * @param w   宽度
	 * @param h	高度比例
	 */
	public static void loadImage(Context mContext, ImageView imgView, String url,
			int w, int h) {
		// 图片的比例适配
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		android.view.ViewGroup.LayoutParams params;
		params = imgView.getLayoutParams();
		params.height = screenWidth * h / w;
		params.width = screenWidth;
		imgView.setLayoutParams(params);
		loadImage(mContext, url, imgView);
	}
}
