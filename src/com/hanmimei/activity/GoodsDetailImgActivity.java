package com.hanmimei.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.adapter.GoodsDetailImgPagerAdapter;
import com.hanmimei.listener.GoodsPageChangeListener;

public class GoodsDetailImgActivity extends BaseActivity implements OnClickListener {

	private ViewPager mViewPager;
	private TextView pageNum;

	private ArrayList<String> imgUrls;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.good_detail_img_layout);

		imgUrls = getIntent().getStringArrayListExtra("imgUrls");
		position = getIntent().getIntExtra("position", 0);

		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		findViewById(R.id.btn_back).setOnClickListener(this);
		pageNum = (TextView) findViewById(R.id.pageNum);

		pageNum.setText((position+1) + "/" + imgUrls.size());
		mViewPager.setAdapter(new GoodsDetailImgPagerAdapter(this, imgUrls));
		mViewPager.setCurrentItem(position);
		mViewPager.addOnPageChangeListener(new GoodsPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				pageNum.setText((arg0 + 1) + "/" + imgUrls.size());
			}

		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}

	}
}
