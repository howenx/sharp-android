package com.kakao.kakaogift.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.override.GlideRoundTransform;

public class GlideLoaderTools {

	/**
	 * 加载图片(圆形)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadCirlceImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url).placeholder(R.drawable.hmm_mine_face).animate(R.anim.abc_fade_in).into(imgView);
	}
	/**
	 * 加载图片(正方形，fitCenter)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadSquareImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url) 
				.placeholder(R.drawable.hmm_place_holder_z).fitCenter()
				.animate(R.anim.abc_fade_in).into(imgView);
	}
	
	/**
	 * 加载图片(正方形 scaleType)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadSquareImage(Context context, String url, ImageView imgView,ScaleType scaleType) {
		if(ScaleType.CENTER_CROP == scaleType){
			Glide.with(context).load(url).placeholder(R.drawable.hmm_place_holder_z).centerCrop()
			.animate(R.anim.abc_fade_in).into(imgView);
		}else if(ScaleType.FIT_CENTER == scaleType){
			Glide.with(context).load(url).placeholder(R.drawable.hmm_place_holder_z).fitCenter()
			.animate(R.anim.abc_fade_in).into(imgView);
		}else {
			Glide.with(context).load(url).placeholder(R.drawable.hmm_place_holder_z)
			.animate(R.anim.abc_fade_in).into(imgView);
		}
	}
	
	/**
	 * 加载图片(自定义placeholder)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadImage(Context context, String url,
			ImageView imgView, int placeholder) {
		Glide.with(context).load(url).placeholder(placeholder).fitCenter().animate(R.anim.abc_fade_in).into(imgView);
	}
	/**
	 * 加载图片(矩形，centerCrop)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadRectImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url)
				.placeholder(R.drawable.hmm_place_holder_j).fitCenter()
				.animate(R.anim.abc_fade_in).into(imgView);
	}
	/**
	 * 加载图片(圆角)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadRoundImage(Context context, String url,
			ImageView imgView) {
		Glide.with(context).load(url).transform(new GlideRoundTransform(context, 10))
				.animate(R.anim.abc_fade_in).into(imgView);
	}
	
	/**
	 * 加载图片(矩形)
	 * 
	 * @param mContext
	 * @param url
	 * @param imgView
	 */
	public static void loadCustomImage(Context mContext, ImageView imgView,
			String url, int w, int h) {
		Glide.with(mContext).load(url).placeholder(R.drawable.hmm_place_holder_j).override(w, h).into(imgView);
	}

	public static void pauseRequests(Context context) {
		Glide.with(context).pauseRequestsRecursive();
	}

	public static void resumeRequests(Context context) {
		Glide.with(context).resumeRequestsRecursive();
	}

}
