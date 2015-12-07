package com.hanmimei.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.hanmimei.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class InitImageLoader {

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

	public static void loadImage(Context mContext, String url, ImageView imgView) {
		initOptions();
		imageLoader = initLoader(mContext);
		imageLoader.displayImage(url, imgView, imageOptions);
	}

	public static void loadImage(Context mContext, String url,
			ImageView imgView, ImageLoadingListener l) {
		initOptions();
		imageLoader = initLoader(mContext);
		imageLoader.displayImage(url, imgView, imageOptions, l);
	}
}
