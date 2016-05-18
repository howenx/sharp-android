package com.hanmimei.activity.goods.detail;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.diegocarloslima.byakugallery.lib.GalleryViewPager;
import com.hanmimei.R;
import com.hanmimei.activity.goods.detail.adapter.GoodsDetailImgPagerAdapter;
import com.hanmimei.override.ViewPageChangeListener;
 /**
  * 
  * @author vince
  *
  */
public class GoodsDetailImgActivity extends AppCompatActivity {

	private GalleryViewPager mViewPager;
	private TextView pageNum;

	private List<String> imgUrls;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.good_detail_img_layout);

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
