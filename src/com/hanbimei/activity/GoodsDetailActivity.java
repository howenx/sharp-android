package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
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
import com.hanbimei.entity.Tag;
import com.hanbimei.fragment.goodstab.ImgFragment;
import com.hanbimei.fragment.goodstab.ParamFragment;
import com.hanbimei.utils.AsyncImageLoader;
import com.hanbimei.utils.AsyncImageLoader.LoadedCallback;
import com.hanbimei.utils.CommonUtil;
import com.hanbimei.utils.HttpUtils;
import com.hanbimei.view.CustomScrollView;
import com.hanbimei.view.TagCloudView;
import com.hanbimei.view.TagCloudView.OnTagClickListener;
import com.viewpagerindicator.TabPageIndicator;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, OnSliderClickListener,
		CustomScrollView.OnScrollUpListener {

	private static final String DETAIL_HEADER_TRANSLATION_Y = "detail_header_translation_y";
	private static final String PAGER_HEADER_TRANSLATION_Y = "pager_header_translation_y";

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
	private ViewPager pager;
	private CustomScrollView mScrollView;
	private TabPageIndicator indicator;
	private View indicator_hide;

	private View pager_header;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getActionBar().hide();
		setContentView(R.layout.goods_detail_layout);
		findView(arg0);
		initTab();
		loadDataByUrl();
//		registerReceivers();
	}

	private void findView(Bundle savedInstanceState) {
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
		indicator_hide = findViewById(R.id.indicator_hide);
		pager = (ViewPager) findViewById(R.id.pager);
		discount = (TextView) findViewById(R.id.discount);
		mScrollView = (CustomScrollView) findViewById(R.id.mScrollView);
		mScrollView.setOnScrollUpListener(this);
		pager_header = findViewById(R.id.pager_header);

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
	
	public ViewPager getViewPager (){
		return pager;
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
	private List<Stock> stocks;
	private List<Tag> tags;

	private void initGoodsDetail(GoodsDetail detail) {
		main = detail.getMain();
		stocks = detail.getStocks();

		num_attention.setText("(" + main.getCollectCount() + ")");
		publicity.setText(main.getPublicity());

		tags = new ArrayList<Tag>();
		initStocks(-1);

		tagCloudView.setTags(tags);
		tagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int oldPostion, int position, Tag tag) {
				// TODO Auto-generated method stub
				stocks.get(oldPostion).setOrMasterInv(false);
				stocks.get(position).setOrMasterInv(true);
				initStocks(position);
			}
		});
		getItemDetailImages();
//		initFragment();
	}
	
	
	
	private void initFragment(List<Bitmap> bitmaps) {
		List<Fragment> data = new ArrayList<Fragment>();
		ImgFragment imgFragment = ImgFragment.newInstance(main.getItemNotice(),
				bitmaps);
		ParamFragment pFragment = ParamFragment.newInstance(main
				.getItemFeatures());
		ParamFragment ppFragment = ParamFragment.newInstance(main
				.getItemFeatures());
		data.add(imgFragment);
		data.add(pFragment);
		data.add(ppFragment);

		
		pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), data,
				tabs));
		pager.setOffscreenPageLimit(3);
		indicator.setViewPager(pager);
		indicator.setVisibility(View.VISIBLE);
		CommonUtil.resetViewPagerHeight(pager, 0);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				CommonUtil.resetViewPagerHeight(pager, arg0);
				indicator_hide.setVisibility(View.GONE);
				currIndex = arg0 ;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				// 取得该控件的实例
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) barText
						.getLayoutParams();

				if (currIndex == arg0) {
					ll.leftMargin = (int) (currIndex * barText.getWidth() + arg1
							* barText.getWidth());
				} else if (currIndex > arg0) {
					ll.leftMargin = (int) (currIndex * barText.getWidth() - (1 - arg1)
							* barText.getWidth());
				}
				barText.setLayoutParams(ll);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				

			}
		});
		InitTextView();
		InitTextBar();

	}

	private void initStocks(int position) {
		for (int index = 0; index < stocks.size(); index++) {
			Stock s = stocks.get(index);
			tags.add(new Tag(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));

			if (s.getOrMasterInv()) {
				initSliderImage(s);
				discount.setText("[" + s.getItemDiscount() + "折]");
				itemTitle.setText(s.getInvTitle());
				itemSrcPrice.setText(s.getItemSrcPrice() + "");
				itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				itemPrice.setText(s.getItemPrice() + "");
			}
		}
	}

	private void initSliderImage(Stock s) {
		slider.removeAllSliders();
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
	
	private int currIndex;// 当前页卡编号
	/**
	 * 初始化标签名
	 */
	private void InitTextView() {
		findViewById(R.id.tv_guid1).setOnClickListener(new txListener(0));
		findViewById(R.id.tv_guid2).setOnClickListener(new txListener(1));
		findViewById(R.id.tv_guid3).setOnClickListener(new txListener(2));
		
	}

	public class txListener implements View.OnClickListener {
		private int index = 0;

		public txListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);
		}
	}
	
	private TextView barText;
	/**
	 * 初始化图片的位移像素
	 */
	private void InitTextBar() {
		barText = (TextView) findViewById(R.id.cursor);
		Display display = getWindow().getWindowManager()
				.getDefaultDisplay();
		// 得到显示屏宽度
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		// 1/3屏幕宽度
		int tabLineLength = metrics.widthPixels / 3;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) barText.getLayoutParams();
		lp.width = tabLineLength;
		barText.setLayoutParams(lp);

	}

	@Override
	public void onScroll(int scrollY, boolean scrollDirection) {
		// 如果滚动值
		if (scrollY >= pager_header.getMeasuredHeight()
				&& indicator_hide.getVisibility() == View.GONE) {
			indicator_hide.setVisibility(View.VISIBLE);
			return;
		}

		if (scrollY < pager_header.getMeasuredHeight()
				&& indicator_hide.getVisibility() == View.VISIBLE) {
			indicator_hide.setVisibility(View.GONE);
			return;
		}

	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AsyncImageLoader.getInstance().clearCache();
	}
	
	// ********************************************************************
			// **********************广播接收的初始化**********************************
			// ********************************************************************
//	GoodsDetailBroadCastReceiver receiver = null;
//			/**
//			 * 广播接受者 注册
//			 */
//			private void registerReceivers() {
//				receiver = new GoodsDetailBroadCastReceiver();
//				IntentFilter intentFilter = new IntentFilter("MESSAGE_BROADCAST_CART_CLEAR_ACTION");
//				registerReceiver(receiver, intentFilter);
//			}
//
//			/**
//			 * 通知重新初始化数据
//			 * 
//			 * @author Administrator
//			 *
//			 */
//			private class GoodsDetailBroadCastReceiver extends BroadcastReceiver {
//				@Override
//				public void onReceive(Context context, Intent intent) {
//					if (intent.getAction().equals("MESSAGE_BROADCAST_CART_CLEAR_ACTION")) {
//						initFragment();
//					}
//				}
//
//			}
	
	
		private void getItemDetailImages(){
			int size = main.getItemDetailImgs().size();
			for(String imageUrl : main.getItemDetailImgs()){
				AsyncImageLoader.getInstance().loadBitmap(this,size, imageUrl, new LoadedCallback() {
					
					@Override
					public void imageLoaded(List<Bitmap> bitmaps) {
						// TODO Auto-generated method stub
						initFragment(bitmaps);
					}
				});
				
			}
		}
	

}
