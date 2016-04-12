package com.hanmimei.activity;

import java.util.List;

import android.os.Bundle;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.hanmimei.R;
import com.hanmimei.adapter.GoodsDetailImgPagerAdapter;
import com.hanmimei.override.ViewPageChangeListener;
import com.sliding.finish.SwipeBackActivity;

public class GoodsDetailImgActivity extends SwipeBackActivity {

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
		mViewPager.addOnPageChangeListener(new ViewPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pageNum.setText((arg0 + 1) + "/" + imgUrls.size());
			}

		});
		
	}

}
