package com.hanbimei.activity;


import com.hanbimei.R;

import android.annotation.SuppressLint;
import android.os.Bundle;

@SuppressLint("NewApi") 
public class AboutWeActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.about_we_layout);
		getActionBar().hide();
		
	}

}
