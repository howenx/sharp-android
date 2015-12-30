package com.hanmimei.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.hanmimei.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class ImageLoaderUtils {

	private static ImageLoader imageLoader;
	private static DisplayImageOptions imageOptions;
	private static ImageLoaderConfiguration config;

	public static ImageLoader initLoader(Context mContext) {
		if (imageLoader == null) {
			imageLoader = ImageLoader.getInstance();
			// 初始化imageloader、displayImageOptions
			imageLoader.init(initImageLoaderConfiguration(mContext));
		}
		return imageLoader;
	}

	public static DisplayImageOptions initOptions() {
		if (imageOptions == null) {
			imageOptions = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.ic_launcher)
					.decodingOptions(initImageLoaderDecodingOptions())
					.cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
					.cacheInMemory(true).build();
		}
		return imageOptions;
	}

	public static BitmapFactory.Options initImageLoaderDecodingOptions() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 4;
		return options;
	}

	public static ImageLoaderConfiguration initImageLoaderConfiguration(
			Context context) {
		config = new ImageLoaderConfiguration.Builder(context).threadPoolSize(2)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCacheSize(2 * 1024 * 1024)
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
				.diskCacheFileCount(100) // 可以缓存的文件数量
				.build();

		return config;
	}
	/**
	 * imageloader加载图片
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadImage(Context mContext, String url, ImageView imgView) {
		initOptions();
		imageLoader = initLoader(mContext);
		imageLoader.displayImage(url, imgView, imageOptions);
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
		initOptions();
		imageLoader = initLoader(mContext);
		imageLoader.displayImage(url, imgView, imageOptions, l);
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
