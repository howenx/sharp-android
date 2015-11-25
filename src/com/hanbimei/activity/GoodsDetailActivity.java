package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.hanbimei.R;
import com.hanbimei.adapter.MyPagerAdapter;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.Category;
import com.hanbimei.entity.GoodsDetail;
import com.hanbimei.entity.GoodsDetail.Main;
import com.hanbimei.entity.GoodsDetail.Stock;
import com.hanbimei.fragment.goodstab.HotFragment;
import com.hanbimei.fragment.goodstab.ImgFragment;
import com.hanbimei.fragment.goodstab.ParamFragment;
import com.hanbimei.listener.DataLoadListener;
import com.hanbimei.utils.HttpUtils;
import com.hanbimei.view.TagCloudView;
import com.viewpagerindicator.TabPageIndicator;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, OnSliderClickListener {

	private static final String TAB_IMG_ID = "tab_img";
	private static final String TAB_PARAM_ID = "tab_param";
	private static final String TAB_HOT_ID = "tab_hot";

	private static final String TAB_IMG = "图文详情";
	private static final String TAB_PARAM = "商品参数";
	private static final String TAB_HOT = "热卖商品";

	private List<Category> tabs;

	private SliderLayout slider;
	private TextView num_attention, discount, itemTitle, itemSrcPrice,
			itemPrice;
	private TagCloudView tagCloudView;
	private TextView publicity;
	private TabPageIndicator indicator;
	private ViewPager pager;
	
	private DataLoadListener imgDataLoadListener,hotDataLoadListener,paramDataLoadListener;
	private void  setImgDataLoadListener ( DataLoadListener mDataLoadListener){
		this.imgDataLoadListener = mDataLoadListener;
	}
	private void  setHotDataLoadListener ( DataLoadListener mDataLoadListener){
		this.hotDataLoadListener = mDataLoadListener;
	}
	private void  setParamDataLoadListener ( DataLoadListener mDataLoadListener){
		this.paramDataLoadListener = mDataLoadListener;
	}
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getActionBar().hide();

		setContentView(R.layout.goods_detail_layout);
		findView();
		initTab();
		initFragment();
		loadDataByUrl();
	}

	private void findView() {
		TextView header = (TextView) findViewById(R.id.header);
		header.setText("商品详情");

		slider = (SliderLayout) findViewById(R.id.slider);
		num_attention = (TextView) findViewById(R.id.num_attention);
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemSrcPrice = (TextView) findViewById(R.id.itemSrcPrice);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		tagCloudView = (TagCloudView) findViewById(R.id.tagCloudView);
		publicity = (TextView) findViewById(R.id.publicity);
		indicator = (TabPageIndicator) findViewById(R.id.indicator);
		pager = (ViewPager) findViewById(R.id.pager);
		discount = (TextView) findViewById(R.id.discount);

		findViewById(R.id.btn_attention).setOnClickListener(this);
		findViewById(R.id.btn_share).setOnClickListener(this);
		findViewById(R.id.back).setVisibility(View.VISIBLE);
		findViewById(R.id.back).setOnClickListener(this);
	}

	private void initTab() {
		tabs = new ArrayList<Category>();
		tabs.add(new Category(TAB_IMG_ID, TAB_IMG));
		tabs.add(new Category(TAB_PARAM_ID, TAB_PARAM));
		tabs.add(new Category(TAB_HOT_ID, TAB_HOT));
	}
	
	private void initFragment() {
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		
		ImgFragment imgFragment = new ImgFragment();
		setImgDataLoadListener(imgFragment);
		fragmentList.add(imgFragment);
		
		ParamFragment paramFragment = new ParamFragment();
		setParamDataLoadListener(paramFragment);
		fragmentList.add(paramFragment);
		
		
		HotFragment hotFragment = new HotFragment();
		setHotDataLoadListener(hotFragment);
		fragmentList.add(hotFragment);
		
		
		MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, tabs);
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		indicator.setVisibility(View.VISIBLE);
		
	}

	private void loadDataByUrl() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = HttpUtils
						.get(getIntent().getStringExtra("url"));
				Message msg = mHandler.obtainMessage(1);
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.btn_attention:
			break;
		case R.id.btn_share:
			break;

		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				GoodsDetail detail = DataParser.parserGoodsDetail(msg.obj
						.toString());
				initGoodsDetail(detail);
				break;

			default:
				break;
			}
		}

	};
	
	private Main main;
	
	private void initGoodsDetail(GoodsDetail detail) {
		main = detail.getMain();
		List<Stock> stocks = detail.getStocks();

		num_attention.setText("(" + main.getCollectCount() + ")");
		publicity.setText(main.getPublicity());

		List<String> tags = new ArrayList<String>();
		

		for (Stock s : stocks) {
			tags.add(s.getItemColor() + " " + s.getItemSize());
			if (s.getOrMasterInv()) {
				initSliderImage(s);
				discount.setText("[" + s.getItemDiscount() + "折]");
				itemTitle.setText(s.getInvTitle());
				itemSrcPrice.setText(s.getItemSrcPrice() + "");
				itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				itemPrice.setText(s.getItemPrice() + "");
			}
		}
		tagCloudView.setTags(tags);
		
		imgDataLoadListener.dataLoad(main.getItemDetailImgs());
		paramDataLoadListener.dataLoad(main.getItemFeatures());
	}

	private void initSliderImage(Stock s) {
		for (String url : s.getItemPreviewImgs()) {
			DefaultSliderView defaultSliderView = new DefaultSliderView(this);
			// initialize a SliderLayout
			defaultSliderView.image(url)
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);

			// add your extra information
			defaultSliderView.getBundle().putString("extra", s.getInvUrl());

			slider.addSlider(defaultSliderView);
		}
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		// TODO Auto-generated method stub

	}

}
