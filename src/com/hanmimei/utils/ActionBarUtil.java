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

	public static void setActionBarStyle(Context context, String tag,int img, Boolean isBack, OnClickListener l) {
		final BaseActivity activity = (BaseActivity) context;
		ActionBar actionbar = activity.getSupportActionBar();
		actionbar.show();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle("");
		actionbar.setDisplayShowCustomEnabled(true);
		actionbar.setCustomView(R.layout.main_header_layout);
		View view = actionbar.getCustomView();
		TextView title = (TextView) view.findViewById(R.id.header);
		ImageView btn_back = (ImageView) view.findViewById(R.id.back);
		ImageView btn_setting = (ImageView) view.findViewById(R.id.setting);
		if(isBack)
			btn_back.setVisibility(View.VISIBLE);
			btn_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.finish();
				}
			});
		title.setText(tag);
		if(img !=0){
			btn_setting.setVisibility(View.VISIBLE);
			btn_setting.setImageResource(img);
		}
		if(l !=null)
			btn_setting.setOnClickListener(l);
	}
	
	
	public static void setActionBarStyle(Context context, String tag){
		setActionBarStyle(context, tag, 0, false, null);
	}
	
	
}
