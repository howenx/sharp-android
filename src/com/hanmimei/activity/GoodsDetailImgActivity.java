package com.hanmimei.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.hanmimei.R;
import com.hanmimei.adapter.GoodsDetailImgPagerAdapter;
import com.hanmimei.listener.GoodsPageChangeListener;
import com.umeng.analytics.MobclickAgent;

public class GoodsDetailImgActivity extends AppCompatActivity {

	private GalleryViewPager mViewPager;
	private TextView pageNum;

	private List<String> imgUrls;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_detail_img_layout);
		getSupportActionBar().hide();

		imgUrls = getIntent().getStringArrayListExtra("imgUrls");
		position = getIntent().getIntExtra("position", 0);

		mViewPager = (GalleryViewPager) findViewById(R.id.mViewPager);
		pageNum = (TextView) findViewById(R.id.pageNum);

		pageNum.setText((position + 1) + "/" + imgUrls.size());
		mViewPager.setAdapter(new GoodsDetailImgPagerAdapter(
				getSupportFragmentManager(), imgUrls));
		mViewPager.setOffscreenPageLimit(imgUrls.size());
		mViewPager.setCurrentItem(position);
		mViewPager.addOnPageChangeListener(new GoodsPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pageNum.setText((arg0 + 1) + "/" + imgUrls.size());
			}

		});
		
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("GoodsDetailImgActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("GoodsDetailImgActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
															// onPageEnd
															// 在onPause 之前调用,因为
															// onPause
															// 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}
}
