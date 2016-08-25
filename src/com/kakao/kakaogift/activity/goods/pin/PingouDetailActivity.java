package com.kakao.kakaogift.activity.goods.pin;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bumptech.glide.Glide;
import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout.OnScrollListener;
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.balance.GoodsBalanceActivity;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.adapter.GoodsDetailPagerAdapter;
import com.kakao.kakaogift.activity.goods.detail.fragment.HotFragment;
import com.kakao.kakaogift.activity.goods.detail.fragment.ImgFragment;
import com.kakao.kakaogift.activity.goods.detail.fragment.ParamsFragment;
import com.kakao.kakaogift.activity.goods.pin.presenter.PinDetailPresenter;
import com.kakao.kakaogift.activity.goods.pin.presenter.PinDetailPresenterImpl;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.activity.main.HMainActivity;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.entity.CommentVo;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.ImageVo;
import com.kakao.kakaogift.entity.PinDetail;
import com.kakao.kakaogift.entity.ShareVo;
import com.kakao.kakaogift.entity.ShoppingCar;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.StockVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.override.ViewPageChangeListener;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.GoodsPushWindow;
import com.kakao.kakaogift.view.NetworkImageHolderView;
import com.kakao.kakaogift.view.ShareWindow;

/**
 * 
 * @author vince
 * 
 */
public class PingouDetailActivity extends BaseActivity implements
		OnClickListener, PinDetailView {

	private static final String Tag = "PingouDetailActivity";

	private ScrollableLayout mScrollLayout;  //滚动
	private ConvenientBanner<ImageVo> slider; //轮播
	private View back_top;	//返回顶部按钮
	private ImageView collectionImg; //收藏按钮
	private ImageView wanfaView; //
	private TextView more_view; //查看更多
	private ShareWindow shareWindow;

	private ScrollAbleFragment imgFragment;
	private ScrollAbleFragment parFragment;
	private ScrollAbleFragment gridViewFragment;

	private PinDetailPresenter mPinDetailPresenter;

	private boolean isCollection = false; //是否收藏
	private PinDetail detail; //拼购详情信息

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "商品详情",R.drawable.hmm_icon_share, true, new BackListener(), this);
		setContentView(R.layout.pingou_detail_layout);
		initView();
		initFragmentPager();
		loadUrl();
		registerReceivers();
	}

	// ------------------------------------------------------------------------------------------------------------------------------
	// 网络请求
	// ------------------------------------------------------------------------------------------------------------------------------
	/**
	 * 加载数据
	 */
	private void loadUrl() {
		if (TextUtils.isEmpty(getIntent().getStringExtra("url")))
			return;
		//获取拼购信息
		mPinDetailPresenter.getPinDetail(getHeaders(), getIntent().getStringExtra("url"), Tag);
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		mPinDetailPresenter = new PinDetailPresenterImpl(this);

		mScrollLayout = (ScrollableLayout) findViewById(R.id.mScrollLayout);
		slider = (ConvenientBanner<ImageVo>) findViewById(R.id.slider);

		back_top = findViewById(R.id.back_top);
		more_view = (TextView) findViewById(R.id.more_view);

		collectionImg = (ImageView) findViewById(R.id.attention);
		wanfaView = (ImageView) findViewById(R.id.wanfaView);
		
		Drawable able = getResources().getDrawable(R.drawable.pingou_wanfa);
		int height = able.getIntrinsicHeight();
		int width = able.getIntrinsicWidth();
		int faceWidth = CommonUtils.getScreenWidth(getActivity());
		int factHeight = faceWidth*height/width;
		Glide.with(getActivity()).load(R.drawable.pingou_wanfa).override(faceWidth, factHeight).into(wanfaView);

		findViewById(R.id.wanfaView).setOnClickListener(this);
		findViewById(R.id.back_top).setOnClickListener(this);
		findViewById(R.id.btn_attention).setOnClickListener(this);
		findViewById(R.id.btn_buy_01).setOnClickListener(this);
		findViewById(R.id.btn_buy_02).setOnClickListener(this);
		findViewById(R.id.btn_pin_01).setOnClickListener(this);
		findViewById(R.id.btn_pin_02).setOnClickListener(this);

	}

	private void initGoodsDetail(StockVo stock) {
		if (stock == null)
			return;
		//初始化 轮播图
		initSliderImage(stock.getItemPreviewImgsForList());
		//
		TextView pinTitle = (TextView) findViewById(R.id.pinTitle);
		TextView soldAmount = (TextView) findViewById(R.id.soldAmount);
		TextView pin_per_num = (TextView) findViewById(R.id.pin_per_num);
		TextView item_src_price = (TextView) findViewById(R.id.item_src_price);
		TextView pin_price = (TextView) findViewById(R.id.pin_price);
		//初始化商品基础信息
		pinTitle.setText(stock.getPinTitle());
		soldAmount.setText("已售：" + stock.getSoldAmount() + "件");
		if (stock.getInvPrice() != null) {
			item_src_price.setText(stock.getInvPrice() + "元/件");

		}
		if (stock.getPinTieredPrices().size() > 2) {
			pin_price.setText(stock.getFloorPrice().get("price") + "元/件起");
			pin_per_num.setText("最高" + stock.getFloorPrice().get("person_num")
					+ "人团");
		} else {
			pin_price.setText(stock.getFloorPrice().get("price") + "元/件");
			pin_per_num.setText(stock.getFloorPrice().get("person_num") + "人拼团");
		}

		if (stock.getCollectId() != 0) {
			//收藏
			collectionImg.setImageResource(R.drawable.hmm_icon_collect_h);
			isCollection = true;
		} else {
			//尚未收藏
			collectionImg.setImageResource(R.drawable.hmm_icon_collect);
			isCollection = false;
		}
		if (stock.getStatus().equals("Y")) {

		} else {
			//商品未能正常销售
			more_view.setVisibility(View.VISIBLE);
			more_view.setOnClickListener(this);
			findViewById(R.id.btn_buy_01).setEnabled(false);
			findViewById(R.id.btn_buy_02).setEnabled(false);
			findViewById(R.id.btn_pin_01).setEnabled(false);
			findViewById(R.id.btn_pin_02).setEnabled(false);
			showPopupwindow();
		}
	}

	/**
	 * 初始化轮播图
	 * 
	 * @param s
	 *            当前选中子商品
	 */
	private void initSliderImage(List<ImageVo> imgUrls) {
		ArrayList<ImageVo> networkImages = new ArrayList<ImageVo>(imgUrls);
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		}, networkImages).setPageIndicator(
				new int[] { R.drawable.page_indicator,
						R.drawable.page_indicator_fcoused });
	}
	//
	private void initGoodsComment(CommentVo comm) {
		if (comm.getRemarkCount() <= 0) {
			findViewById(R.id.btn_comment).setVisibility(View.GONE);
		} else {
			findViewById(R.id.btn_comment).setOnClickListener(this);
			TextView remarkRate = (TextView) findViewById(R.id.remarkRate);
			TextView remarkCount = (TextView) findViewById(R.id.remarkCount);
			remarkCount.setText(getResources().getString(R.string.comment,
					comm.getRemarkCount()));
			remarkRate.setText(getResources().getString(R.string.comment_good,
					comm.getRemarkRate()));
		}
	}

	private void initFragmentPager() {
		final List<ScrollAbleFragment> fragments = new ArrayList<ScrollAbleFragment>();
		imgFragment = new ImgFragment();
		parFragment = new ParamsFragment();
		gridViewFragment = new HotFragment();

		fragments.add(imgFragment);
		fragments.add(parFragment);
		fragments.add(gridViewFragment);

		GoodsDetailPagerAdapter adapter = new GoodsDetailPagerAdapter(
				getSupportFragmentManager(), fragments);
		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		PagerSlidingTabStrip pagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

		viewPager.setAdapter(adapter);
		viewPager.setOffscreenPageLimit(3);
		mScrollLayout.getHelper().setCurrentScrollableContainer(
				fragments.get(0));
		pagerSlidingTabStrip.setViewPager(viewPager);
		mScrollLayout.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(int currentY, int maxY) {
				if (currentY > 1 && back_top.getVisibility() == View.GONE) {
					back_top.setVisibility(View.VISIBLE);
				}
				if (currentY <= 1 && back_top.getVisibility() == View.VISIBLE) {
					back_top.setVisibility(View.GONE);
				}
				// setScrollDown(currentY);
			}

		});
		pagerSlidingTabStrip
				.setOnPageChangeListener(new ViewPageChangeListener() {

					@Override
					public void onPageSelected(int i) {
						/** 标注当前页面 **/
						mScrollLayout
								.getHelper()
								.setCurrentScrollableContainer(fragments.get(i));
					}

				});
		viewPager.setCurrentItem(0);
	}

	// 分享面板
	@SuppressLint("InflateParams")
	private void showShareboard() {
		if (shareWindow == null) {
			ShareVo vo = new ShareVo();
			vo.setContent(detail.getStock().getPinTitle());
			vo.setTitle("我在KakaoGift发现了一个不错的礼物，赶快来看看吧");
			if (detail.getStock() == null) {
				ToastUtils.Toast(this, "等待加载数据");
				return;
			}
			vo.setImgUrl(detail.getStock().getInvImgForObj().getUrl());
			vo.setTargetUrl("http://style.hanmimei.com"
					+ detail.getStock().getPinRedirectUrl().split("comm")[1]);
			vo.setInfoUrl(detail.getStock().getPinRedirectUrl());
			vo.setType("P");
			shareWindow = new ShareWindow(this, vo);
		}
		shareWindow.show();

	}

	private GoodsPushWindow pushWindow;
	/**
	 * 推荐商品弹出窗
	 */
	private void showPopupwindow() {
		if (pushWindow == null) {
			pushWindow = new GoodsPushWindow(this, detail.getPush());
		}
		pushWindow.show();

	}
	/**
	 * 收藏商品操作
	 */
	private void collectGoods() {
		if (getUser() == null) {
			//未登录 先去登录
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		findViewById(R.id.btn_attention).setOnClickListener(null);
		if (isCollection) {
			mPinDetailPresenter.cancelCollection(getHeaders(), detail.getStock().getCollectId());
		} else {
			mPinDetailPresenter.addCollection(getHeaders(), detail.getStock());
		}
	}

	/**
	 * 加载商品详情数据
	 */
	private void loadFragmentData() {
		if (detail == null || detail.getMain() == null)
			return;
		imgFragment.showData(detail.getMain().getItemDetailImgs());
		parFragment.showData(detail.getMain().getItemFeaturess());
		gridViewFragment.showData(detail.getPush());

	}

	/**
	 * 点击立即购买按钮的响应事件
	 */
	private void clickPay() {
		// 未登录跳到登陆页面
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		if (!detail.getStock().getStatus().equals("Y")) {
			return;
		}
		ShoppingCar car = new ShoppingCar();
		List<CustomsVo> list = new ArrayList<CustomsVo>();
		CustomsVo customs = new CustomsVo();
		//判断商品是否能正常销售,组装购物车数据结构
		ShoppingGoods sgoods = null;
		StockVo s = detail.getStock();
		if (s.getStatus().equals("Y")) {
			//商品正常 获取商品信息
			sgoods = new ShoppingGoods();
			sgoods.setGoodsId(s.getId() + "");
			sgoods.setGoodsImg(s.getInvImgForObj().getUrl());
			sgoods.setGoodsName(s.getPinTitle());
			sgoods.setGoodsNums(1);
			sgoods.setGoodsPrice(s.getInvPrice().doubleValue());
			sgoods.setInvArea(s.getInvArea());
			sgoods.setInvAreaNm(s.getInvAreaNm());
			sgoods.setInvCustoms(s.getInvCustoms());
			sgoods.setPostalTaxRate(s.getPostalTaxRate());
			sgoods.setPostalStandard(s.getPostalStandard());
			sgoods.setSkuType("item");
			sgoods.setSkuTypeId(s.getId());
		}
		customs.addShoppingGoods(sgoods);
		customs.setInvArea(sgoods.getInvArea());
		customs.setInvAreaNm(sgoods.getInvAreaNm());
		customs.setInvCustoms(sgoods.getInvCustoms());
		list.add(customs);
		car.setList(list);
		Intent intent = new Intent(this, GoodsBalanceActivity.class);
		intent.putExtra("car", car);
		intent.putExtra("orderType", "item");
		startActivity(intent);
	}
	/**
	 * 跳转拼购选择页
	 * @param stock
	 */
	private void turnToPingouDetailSelActivity(StockVo stock) {
		if (!detail.getStock().getStatus().equals("Y")) {
			return;
		}
		Intent intent = new Intent(this, PingouDetailSelActivity.class);
		intent.putExtra("stock", stock);
		startActivity(intent);
	}

	private MyBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				loadUrl();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
		VolleyHttp.parseRequestTask(Tag);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitClick();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			exitClick();
			
		}
		
	}

	/**
	 * 退出函数
	 */
	private void exitClick() {
		if (getIntent().getStringExtra("from") != null) {
			startActivity(new Intent(this, HMainActivity.class));
			finish();
		} else {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		if (detail == null)
			return;
		switch (v.getId()) {
		case R.id.wanfaView:
			startActivity(new Intent(this, PingouLiuChengActivity.class));
			break;
		case R.id.back_top:
			mScrollLayout.smoothScrollToTop();
			break;
		case R.id.btn_buy_01:
		case R.id.btn_buy_02:
			clickPay();
			break;
		case R.id.btn_pin_01:
		case R.id.btn_pin_02:
			turnToPingouDetailSelActivity(detail.getStock());
			break;
		case R.id.btn_attention:
			collectGoods();
			break;
		case R.id.more_view:
			showPopupwindow();
			break;
		case R.id.setting:
			showShareboard();
			break;
		default:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kakao.kakaogift.activity.view.pdetail.PinDetailView#showLoading()
	 */
	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		getLoading().show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kakao.kakaogift.activity.view.pdetail.PinDetailView#hideLoading()
	 */
	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		getLoading().dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.view.pdetail.PinDetailView#loadPinDetailData(com
	 * .hanmimei.entity.PinDetail)
	 */
	@Override
	public void loadPinDetailData(PinDetail detail) {
		// TODO Auto-generated method stub
		this.detail = detail;
		loadFragmentData();
		initGoodsDetail(detail.getStock());
		initGoodsComment(detail.getComment());
		mScrollLayout.canScroll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.view.pdetail.PinDetailView#addCollectionSuccess
	 * (long)
	 */
	@Override
	public void addCollectionSuccess(long collectId) {
		// TODO Auto-generated method stub
		isCollection = true;
		detail.getStock().setCollectId(collectId);
		collectionImg.setImageResource(R.drawable.hmm_icon_collect_h);
		sendBroadcast(new Intent(
				AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
		findViewById(R.id.btn_attention).setOnClickListener(this);
		ToastUtils.Toast(this, "收藏成功");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.view.pdetail.PinDetailView#cancelCollectionSuccess
	 * ()
	 */
	@Override
	public void cancelCollectionSuccess() {
		// TODO Auto-generated method stub
		isCollection = false;
		detail.getStock().setCollectId(0);
		collectionImg.setImageResource(R.drawable.hmm_icon_collect);
		sendBroadcast(new Intent(
				AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
		findViewById(R.id.btn_attention).setOnClickListener(this);
		ToastUtils.Toast(this, "已取消收藏");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.view.pdetail.PinDetailView#showLoadFaild(java.lang
	 * .String)
	 */
	@Override
	public void showLoadFaild(String str) {
		// TODO Auto-generated method stub
		ToastUtils.Toast(this, str);
	}

}
