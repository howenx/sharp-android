package com.hanmimei.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
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
		Glide.with(context).load(url).placeholder(R.drawable.hmm_avatar_w).animate(R.anim.abc_fade_in).into(imgView);
	}

	public static void loadThemeImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url).placeholder(R.drawable.hmm_avatar_f_w).animate(R.anim.abc_fade_in).into(imgView);
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

}
