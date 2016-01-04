package com.hanmimei.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hanmimei.R;
import com.hanmimei.view.CustomScrollView;

public class PingouDetailActivity extends BaseActivity implements CustomScrollView.OnScrollUpListener{

	private CustomScrollView mScrollView;
	private View header01,header02,buy_hide;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pingou_detail_layout);
		findView();
		
	}
	
	private void findView(){
		mScrollView = (CustomScrollView) findViewById(R.id.mScrollView);
		header01 = findViewById(R.id.header01);
		buy_hide = findViewById(R.id.buy_hide);
		
		mScrollView.setOnScrollUpListener(this);
	}

	@Override
	public void onScroll(int scrollY, boolean scrollDirection) {
		if(scrollY>=header01.getMeasuredHeight() && buy_hide.getVisibility() == View.GONE){
			buy_hide.setVisibility(View.VISIBLE);
		}
		if(scrollY<=header01.getMeasuredHeight() && buy_hide.getVisibility() == View.VISIBLE){
			buy_hide.setVisibility(View.GONE);
		}
	}

}
