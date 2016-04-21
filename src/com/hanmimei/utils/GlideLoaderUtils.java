package com.hanmimei.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hanmimei.R;

public class GlideLoaderUtils {

	/**
	 * imageloader加载图片
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */

	public static void loadCirlceImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url).crossFade().into(imgView);
	}

	public static void loadGoodsImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url).placeholder(R.drawable.hmm_place_holder_z)
				.fitCenter().animate(R.anim.abc_fade_in).into(imgView);
	}

	public static void loadImage(Context context, String url,
			ImageView imgView, int placeholder) {
		Glide.with(context).load(url).placeholder(placeholder).animate(R.anim.abc_fade_in).centerCrop().into(imgView);
	}

	public static void loadThemeImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url).placeholder(R.drawable.hmm_place_holder_z).centerCrop().animate(R.anim.abc_fade_in).into(imgView);
	}

	public static void loadThemeImage(Context mContext, ImageView imgView,
			String url, int w, int h) {
		int screenWidth = CommonUtil.getScreenWidth(mContext);
		android.view.ViewGroup.LayoutParams params;
		params = imgView.getLayoutParams();
		params.height = screenWidth * h / w;
		params.width = screenWidth;
		imgView.setLayoutParams(params);
		loadThemeImage(mContext, url, imgView);
	}

	public static void pauseRequests(Context context) {
		Glide.with(context).pauseRequestsRecursive();
	}

	public static void resumeRequests(Context context) {
		Glide.with(context).resumeRequestsRecursive();
	}

}
