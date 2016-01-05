package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hanmimei.R;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.view.CustomScrollView;
import com.hanmimei.view.NetworkImageHolderView;

public class PingouDetailActivity extends BaseActivity implements CustomScrollView.OnScrollUpListener,OnClickListener{

	private CustomScrollView mScrollView;
	private View header01,header02,buy_hide,indicator_hide;
	private ConvenientBanner<ImgInfo> slider;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "商品详情");
		setContentView(R.layout.pingou_detail_layout);
		findView();
		
	}
	
	@SuppressWarnings("unchecked")
	private void findView(){
		mScrollView = (CustomScrollView) findViewById(R.id.mScrollView);
		slider = (ConvenientBanner<ImgInfo>) findViewById(R.id.slider);
		View view = findViewById(R.id.viewpager_content);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		view.setLayoutParams(lp);
		header01 = findViewById(R.id.header01);
		header02 = findViewById(R.id.header02);
		buy_hide = findViewById(R.id.buy_hide);
		indicator_hide = findViewById(R.id.indicator_hide);
		
		mScrollView.setOnScrollUpListener(this);
		findViewById(R.id.wanfaView).setOnClickListener(this);
		
		initView();
	}
	
	private void initView(){
		List<ImgInfo> imgUrls = new ArrayList<ImgInfo>();
		imgUrls.add(new ImgInfo("http://172.28.3.18:3008/uploads/minify/c9c26b40ccb04b37be67a42817257af61445397942627.jpg"));
		imgUrls.add(new ImgInfo("http://172.28.3.18:3008/uploads/minify/c9c26b40ccb04b37be67a42817257af61445397942627.jpg"));
		imgUrls.add(new ImgInfo("http://172.28.3.18:3008/uploads/minify/c9c26b40ccb04b37be67a42817257af61445397942627.jpg"));
		initSliderImage(imgUrls);
	}
	
	/**
	 * 初始化轮播图
	 * 
	 * @param s
	 *            当前选中子商品
	 */
	private void initSliderImage(List<ImgInfo> imgUrls) {
		ArrayList<ImgInfo> networkImages = new ArrayList<ImgInfo>(imgUrls);
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages).setPageIndicator(new int[]{R.drawable.page_indicator, R.drawable.page_indicator_fcoused});
	}
	

	@Override
	public void onScroll(int scrollY, boolean scrollDirection) {
		if(scrollY>=header01.getMeasuredHeight() && buy_hide.getVisibility() == View.GONE){
			buy_hide.setVisibility(View.VISIBLE);
		}
		if(scrollY<=header01.getMeasuredHeight() && buy_hide.getVisibility() == View.VISIBLE){
			buy_hide.setVisibility(View.GONE);
		}
		if(scrollY>=header02.getMeasuredHeight()-buy_hide.getMeasuredHeight() && indicator_hide.getVisibility() == View.GONE){
			indicator_hide.setVisibility(View.VISIBLE);
		}
		if(scrollY<=header02.getMeasuredHeight()-buy_hide.getMeasuredHeight() && indicator_hide.getVisibility() == View.VISIBLE){
			indicator_hide.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wanfaView:
			startActivity(new Intent(this, PingouLiuChengActivity.class));
			break;

		default:
			break;
		}
		
	}

}
