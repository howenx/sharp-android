package com.hanmimei.utils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.hanmimei.manager.ThreadPoolManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 异步加载图片
 */
public class AsyncImageLoader {

	private static class AsyncImageLoaderHolder {
		public static final AsyncImageLoader instance = new AsyncImageLoader();
	}

	public static AsyncImageLoader getInstance() {
		return AsyncImageLoaderHolder.instance;
	}

	// 软引用，使用内存做临时缓存 （程序退出，或内存不够则清除软引用）
	private HashMap<String, SoftReference<Bitmap>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
		bitmaps = new ArrayList<Bitmap>();
	}

	/**
	 * 定义回调接口
	 */
	public interface ImageCallback {
		public void imageLoaded(Bitmap imgBitmap, ImageView mImageView,
				String imageUrl);
	}

	/**
	 * 定义回调接口
	 */
	public interface LoadedCallback {
		public void imageLoaded(Bitmap bitmap, int position);
	}

	/**
	 * 创建子线程加载图片 子线程加载完图片交给handler处理（子线程不能更新ui，而handler处在主线程，可以更新ui）
	 * handler又交给imageCallback，imageCallback须要自己来实现，在这里可以对回调参数进行处理
	 * 
	 * @param imageUrl
	 *            ：须要加载的图片url
	 * @param imageCallback
	 *            ：
	 * @return
	 */
	public Bitmap loadBitmap(final Context context, final ImageView mImageView,
			final String imageUrl, final ImageCallback imageCallback) {

		// 如果缓存中存在图片 ，则首先使用缓存
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl
					.toString());
			Bitmap mBitmap = softReference.get();
			if (mBitmap != null) {
				imageCallback.imageLoaded(mBitmap, mImageView, imageUrl);// 执行回调
				return mBitmap;
			}
		}

		/**
		 * 在主线程里执行回调，更新视图
		 */
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, mImageView,
						imageUrl);
			}
		};

		/**
		 * 创建子线程访问网络并加载图片 ，把结果交给handler处理
		 */

		ThreadPoolManager.getInstance().getExecutorService()
				.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bitmap mBitmap = CommonUtil.returnBitMap(context,
								imageUrl);
						// 下载完的图片放到缓存里
						imageCache.put(imageUrl, new SoftReference<Bitmap>(
								mBitmap));
						Message message = handler.obtainMessage(0, mBitmap);
						handler.sendMessage(message);
					}
				});

		return null;
	}

	/**
	 * 创建子线程加载图片 子线程加载完图片交给handler处理（子线程不能更新ui，而handler处在主线程，可以更新ui）
	 * handler又交给imageCallback，imageCallback须要自己来实现，在这里可以对回调参数进行处理
	 * 
	 * @param imageUrl
	 *            ：须要加载的图片url
	 * @param imageCallback
	 *            ：
	 * @return
	 */

	private List<Bitmap> bitmaps;

	public void loadBitmap(final Context context, final int position,
			final String imageUrl, final LoadedCallback loadedCallback) {

//		// 如果缓存中存在图片 ，则首先使用缓存
//		if (imageCache.containsKey(imageUrl)) {
//			SoftReference<Bitmap> softReference = imageCache.get(imageUrl
//					.toString());
//			Bitmap mBitmap = softReference.get();
//			if (mBitmap != null) {
//				loadedCallback.imageLoaded(mBitmap, position);// 执行回调
//				return;
//			}
//		}

		/**
		 * 在主线程里执行回调，更新视图
		 */
		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				loadedCallback.imageLoaded((Bitmap) message.obj, position);
			}
		};

		/**
		 * 创建子线程访问网络并加载图片 ，把结果交给handler处理
		 */
		ThreadPoolManager.getInstance().getExecutorService()
				.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Bitmap mBitmap = CommonUtil.returnBitMap(context,
								imageUrl);
//						// 下载完的图片放到缓存里
//						imageCache.put(imageUrl, new SoftReference<Bitmap>(
//								mBitmap));
						Message message = handler.obtainMessage(0, mBitmap);
						handler.sendMessage(message);
					}
				});

		return;
	}

	// 清除缓存
	public void clearCache() {

		if (imageCache.size() > 0) {
			imageCache.clear();
		}

	}

}
