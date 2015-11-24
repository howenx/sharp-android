package com.hanbimei.activity;

import com.hanbimei.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
@SuppressLint("NewApi") 
public class GoodsDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getActionBar().hide();
		
		setContentView(R.layout.goods_detail_layout);
		
		
	}
	
}
