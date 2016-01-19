package com.hanmimei.utils;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;

public class ActionBarUtil {
	/**
	 * 
	 * @param context
	 * @param title  标题
	 */
	public static void setActionBarStyle(Context context, String title) {
		setActionBarStyle(context, title, 0, true, null, null);
	}
	/**
	 * 
	 * @param context
	 * @param title 标题
	 * @param bl 返回按钮的响应事件
	 */
	public static void setActionBarStyle(Context context, String title,OnClickListener bl) {
		setActionBarStyle(context, title, 0, true, bl, null);
	}
	/**
	 * 
	 * @param context
	 * @param title	标题
	 * @param img  右侧按钮的图片
	 * @param isBack 是否显示返回按钮
	 * @param l 右侧按钮的响应事件
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack,OnClickListener l){
		return setActionBarStyle(context, title, img, isBack, null, l);
	}
	public static void setActionBarStyle(Context context, String title,int img,OnClickListener l) {
		setActionBarStyle(context, title, img, true, null, l);
	}
	
	/**
	 * 
	 * @param context
	 * @param title 标题
	 * @param img 右侧按钮图片
	 * @param isBack 是否显示返回按钮
	 * @param bl 返回按钮的响应事件
	 * @param l 右侧按钮的响应事件
	 * @return
	 */
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack,OnClickListener bl, OnClickListener l) {
		final BaseActivity activity = (BaseActivity) context;
		ActionBar actionbar = activity.getSupportActionBar();
		actionbar.show();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.main_header_layout);
		View view = actionbar.getCustomView();
		TextView titleView = (TextView) view.findViewById(R.id.header);
		ImageView btn_back = (ImageView) view.findViewById(R.id.back);
		ImageView btn_setting = (ImageView) view.findViewById(R.id.setting);
		if (isBack) {
			btn_back.setVisibility(View.VISIBLE);
			if(bl !=null){
				btn_back.setOnClickListener(bl);
			}else{
				btn_back.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						activity.finish();
					}
				});
			}
		}
		titleView.setText(title);
		if (img != 0) {
			btn_setting.setVisibility(View.VISIBLE);
			btn_setting.setImageResource(img);
		}
		if (l != null)
			btn_setting.setOnClickListener(l);
		return view;
	}

}
