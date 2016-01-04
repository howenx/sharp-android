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

	public static void setActionBarStyle(Context context, String title) {
		setActionBarStyle(context, title, 0, true, null, null);
	}
	public static void setActionBarStyle(Context context, String title,OnClickListener bl) {
		setActionBarStyle(context, title, 0, true, bl, null);
	}
	public static View setActionBarStyle(Context context, String title,
			int img, Boolean isBack,OnClickListener l){
		return setActionBarStyle(context, title, img, isBack, null, l);
	}

}
