package com.kakao.kakaogift.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R
;

public class ActionBarUtil {
	
	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 */
	public static void setActionBarStyle(Context context, String title) {
		setActionBarStyle(context, title, 0, true, null, null, 0, false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param bl
	 *            返回按钮的响应事件
	 */
	public static void setActionBarStyle(Context context, String title,
			OnClickListener bl) {
		setActionBarStyle(context, title, 0, true, bl, null, 0, false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param img
	 *            右侧按钮的图片
	 * @param isBack
	 *            是否显示返回按钮
	 * @param l
	 *            右侧按钮的响应事件
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener l) {
		return setActionBarStyle(context, title, img, isBack, null, l, 0, false);
	}
	public static View setMainActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener l) {
		return setActionBarStyle(context, title, img, isBack, null, l, 0, true);
	}
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener l,int color) {
		return setActionBarStyle(context, title, img, isBack, null, l );
	}

	public static View setActionBarStyle(Context context, String title,
			int img, OnClickListener l) {
		return setActionBarStyle(context, title, img, true, null, l, 0,false);
	}
	public static View setActionBarStyle(Context context, String title,
			int img, OnClickListener l,int colorRes) {
		return setActionBarStyle(context, title, img, true, null, l, colorRes,false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param img
	 *            右侧按钮图片
	 * @param isBack
	 *            是否显示返回按钮
	 * @param bl
	 *            返回按钮的响应事件
	 * @param l
	 *            右侧按钮的响应事件
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener bl, OnClickListener l) {
		return setActionBarStyle(context, title, img, isBack, bl, l, 0,false);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param img
	 *            右侧按钮图片
	 * @param isBack
	 *            是否显示返回按钮
	 * @param bl
	 *            返回按钮的响应事件
	 * @param l
	 *            右侧按钮的响应事件
	 * @param colorRes
	 * 			  背景颜色
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack, OnClickListener bl, OnClickListener l,
			int colorRes, boolean isMain) {
		final AppCompatActivity activity = (AppCompatActivity) context;
		ActionBar actionbar = activity.getSupportActionBar();
		actionbar.show();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.actionbar_custom_layout);
		View view = actionbar.getCustomView();
		TextView titleView = (TextView) view.findViewById(R.id.header);
		ImageView btn_back = (ImageView) view.findViewById(R.id.back);
		ImageView btn_setting = (ImageView) view.findViewById(R.id.setting);
		if (colorRes != 0) {
			view.setBackgroundResource(colorRes);
		}
		if (isBack) {
			btn_back.setVisibility(View.VISIBLE);
			if (bl != null) {
				btn_back.setOnClickListener(bl);
			} else {
				btn_back.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						activity.finish();
					}
				});
			}
		}
		titleView.setText(title);
		if(isMain){
			view.findViewById(R.id.logo).setVisibility(View.VISIBLE);
			view.findViewById(R.id.header).setVisibility(View.GONE);
		}else{
			view.findViewById(R.id.logo).setVisibility(View.GONE);
			view.findViewById(R.id.header).setVisibility(View.VISIBLE);
		}
		if (img != 0) {
			btn_setting.setVisibility(View.VISIBLE);
			btn_setting.setImageResource(img);
		}
		if (l != null)
			btn_setting.setOnClickListener(l);
		return view;
	}

}
