package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.hanmimei.R;
import com.hanmimei.adapter.MyPagerAdapter;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.BitmapInfo;
import com.hanmimei.entity.Category;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.GoodsDetail.Main;
import com.hanmimei.entity.GoodsDetail.Stock;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.Tag;
import com.hanmimei.entity.User;
import com.hanmimei.fragment.goodstab.ImgFragment;
import com.hanmimei.fragment.goodstab.ParamFragment;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AsyncImageLoader;
import com.hanmimei.utils.AsyncImageLoader.LoadedCallback;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CustomScrollView;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, OnSliderClickListener,
		CustomScrollView.OnScrollUpListener {


	private List<Category> tabs;

	private SliderLayout slider;
	private TextView num_attention, discount, itemTitle, itemSrcPrice,
			itemPrice;
	private TagCloudView tagCloudView;
	private TextView publicity;
	private ViewPager pager;
	private CustomScrollView mScrollView;
	private View indicator_hide;
	private ImageView shoppingCar;

	private View pager_header;
	
	private User user;
	private ShoppingGoodsDao goodsDao;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "商品详情", R.drawable.white_shoppingcar, true, this);
		setContentView(R.layout.goods_detail_layout);
		findView(arg0);
		loadDataByUrl();
//		registerReceivers();
	}


	private void findView(Bundle savedInstanceState) {

		slider = (SliderLayout) findViewById(R.id.slider);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CommonUtil.getScreenWidth(this),CommonUtil.getScreenWidth(this));
		slider.setLayoutParams(lp);
		num_attention = (TextView) findViewById(R.id.num_attention);
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemSrcPrice = (TextView) findViewById(R.id.itemSrcPrice);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		tagCloudView = (TagCloudView) findViewById(R.id.tagCloudView);
		publicity = (TextView) findViewById(R.id.publicity);
		indicator_hide = findViewById(R.id.indicator_hide);
		pager = (ViewPager) findViewById(R.id.pager);
		discount = (TextView) findViewById(R.id.discount);
		mScrollView = (CustomScrollView) findViewById(R.id.mScrollView);
		mScrollView.setOnScrollUpListener(this);
		pager_header = findViewById(R.id.pager_header);
		
		user = getUser();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		goods = new ShoppingGoods();
		findViewById(R.id.btn_attention).setOnClickListener(this);
		findViewById(R.id.btn_share).setOnClickListener(this);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.btn_shopcart).setOnClickListener(this);
		findViewById(R.id.talk_us).setOnClickListener(this);
		findViewById(R.id.like).setOnClickListener(this);
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
		case R.id.btn_pay:
			if(getUser() == null){
				startActivity(new Intent(this, LoginActivity.class));
				return;
			}
			ShoppingCar car = new ShoppingCar();
			List<Customs> list =new ArrayList<Customs>();
			Customs customs = new Customs();
			
			ShoppingGoods sgoods = null;
			for(Stock s :stocks){
				if(s.getOrMasterInv()){
					sgoods = new ShoppingGoods();
					sgoods.setGoodsId(s.getId());
					sgoods.setGoodsImg(s.getInvImg());
					sgoods.setGoodsName(s.getInvTitle());
					sgoods.setGoodsNums(1);
					sgoods.setGoodsPrice(s.getItemPrice().intValue());
					sgoods.setInvArea(s.getInvArea());
					sgoods.setInvCustoms(s.getInvCustom());
					customs.setInvArea(s.getInvArea());
					customs.setInvCustoms(s.getInvCustom());
					customs.addShoppingGoods(sgoods);
				}
			}
			list.add(customs);
			car.setList(list);
			car.setAllPrice(sgoods.getGoodsPrice());
			Intent intent  = new Intent(this, GoodsBalanceActivity.class);
			intent.putExtra("car", car);
			startActivity(intent);
			break;

		case R.id.btn_shopcart:
			for(int i = 0; i < stocks.size(); i ++){
				Stock stock = stocks.get(i);
				if(stock.getOrMasterInv()){
					goods.setGoodsId(stock.getId());
					goods.setGoodsNums(1);
					goods.setState("I");
				}
			}
			if(user != null){
				sendData();
			}else{
				goodsDao.insert(goods);
//				Toast.makeText(GoodsDetailActivity.this, "已加入购物车", Toast.LENGTH_SHORT).show();
				ToastUtils.Toast(this);
				sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_ADD_CAR));
			}
			break;
		case R.id.setting:
			setResult(AppConstant.CAR_TO_GOODS_CODE);
			startActivity(new Intent(this, ShoppingCarActivity.class));
			break;
		case R.id.talk_us:
			   intent= new Intent();
			   intent.setAction("android.intent.action.CALL");
			   intent.setData(Uri.parse("tel:"+ "18511664543"));
			   startActivity(intent);
			break;
		case R.id.like:
			Toast.makeText(this, "您点击了收藏", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private ShoppingGoods goods;
	private void sendData() {
		toObject();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.post("http://172.28.3.18:9003/client/cart", array, "id-token", user.getToken());
				HMessage hm = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = hm;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private JSONArray array;
	private void toObject() {
		try {
			array = new JSONArray();
				JSONObject object = new JSONObject();
				object.put("cartId", 0);
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				array.put(object);
			
		} catch (JSONException e) {
			e.printStackTrace();
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
			case 2:
				HMessage hm = (HMessage) msg.obj;
				if(hm.getCode() == 200){
					ToastUtils.Toast(GoodsDetailActivity.this);
//					Toast.makeText(GoodsDetailActivity.this, "已加入购物车", Toast.LENGTH_SHORT).show();
					sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_ADD_CAR));
				}else{
					Toast.makeText(GoodsDetailActivity.this, hm.getMessage(), Toast.LENGTH_SHORT).show();
				}
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
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void initFragment(List<BitmapInfo> infos) {
		List<Fragment> data = new ArrayList<Fragment>();
		ImgFragment imgFragment = ImgFragment.newInstance(main.getItemNotice(),
				infos);
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
		CommonUtil.resetViewPagerHeight(pager, 0);
		pager.addOnPageChangeListener(new OnPageChangeListener() {

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
		slider.setVisibility(View.INVISIBLE);
//		slider.removeAllSliders();
		List<DefaultSliderView> imageContent = new ArrayList<DefaultSliderView>();
		for (String url : s.getItemPreviewImgs()) {
			DefaultSliderView defaultSliderView = new DefaultSliderView(this);
			// initialize a SliderLayout
			defaultSliderView.image(url)
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(this);

			// add your extra information
			defaultSliderView.getBundle().putString("extra", s.getInvUrl());
			imageContent.add(defaultSliderView);
//			slider.addSlider(defaultSliderView);
		}
		slider.setPresetTransformer(SliderLayout.Transformer.Default);
		slider.addSliderAll(imageContent);
		slider.setVisibility(View.VISIBLE);
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
		
		findViewById(R.id.guid1).setOnClickListener(new txListener(0));
		findViewById(R.id.guid2).setOnClickListener(new txListener(1));
		findViewById(R.id.guid3).setOnClickListener(new txListener(2));
		
		
		
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
	
	private TextView barText,tv_barText;
	/**
	 * 初始化图片的位移像素
	 */
	private void InitTextBar() {
		barText = (TextView) findViewById(R.id.cursor);
		tv_barText = (TextView) findViewById(R.id.tv_cursor);
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
		tv_barText.setLayoutParams(lp);
  
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
	}
		
	private List<BitmapInfo>  infos;
	
		private void getItemDetailImages(){
			final int size = main.getItemDetailImgs().size();
			infos = new ArrayList<BitmapInfo>();
			for (int i = 0; i < size; i++) {
				AsyncImageLoader.getInstance().loadBitmap(this,i, main.getItemDetailImgs().get(i), new LoadedCallback() {

					@Override
					public void imageLoaded(BitmapInfo info) {
						// TODO Auto-generated method stub
						infos.add(info);
						if(infos.size() == main.getItemDetailImgs().size()){
							initFragment(infos);
						}
						
					}
					
					
				});
				
			}
		}
	

}
