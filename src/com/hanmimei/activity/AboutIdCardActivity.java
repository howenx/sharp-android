package com.hanmimei.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.hanmimei.R;
import com.hanmimei.utils.ActionBarUtil;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi") 
public class AboutIdCardActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.about_id_card_layout);
		ActionBarUtil.setActionBarStyle(this, "关于身份证");
	}
	

}
