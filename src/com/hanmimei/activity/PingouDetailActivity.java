package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.astuetz.PagerSlidingTabStrip;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout.OnScrollListener;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.fragment.HotFragment;
import com.hanmimei.activity.fragment.ImgFragment;
import com.hanmimei.activity.fragment.ParamsFragment;
import com.hanmimei.adapter.GoodsDetailPagerAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.entity.PinDetail;
import com.hanmimei.entity.ShareVo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.override.ViewPageChangeListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.NetworkImageHolderView;
import com.hanmimei.view.PushWindow;
import com.hanmimei.view.ShareWindow;
/**
 * 
 * @author vince
 *
 */
public class PingouDetailActivity extends BaseActivity implements
		OnClickListener {

	private ScrollableLayout mScrollLayout;
	private ConvenientBanner<ImgInfo> slider;
	private View back_top;
	private PinDetail detail;
	private ImageView collectionImg;
	private boolean isCollection = false;
	private TextView more_view;

	private String noticeText = "正在加载数据";
	
	private ScrollAbleFragment imgFragment;
	private ScrollAbleFragment parFragment;
	private ScrollAbleFragment gridViewFragment;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "商品详情",
				R.drawable.hmm_icon_share, true, this, this);
		setContentView(R.layout.pingou_detail_layout);
		findView();
		initFragmentPager();
		loadUrl();
		registerReceivers();
	}

	private void loadUrl() {
		if(TextUtils.isEmpty(getIntent().getStringExtra("url")))
			return;
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent()
				.getStringExtra("url"), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				try {
					detail = new Gson().fromJson(result, PinDetail.class);
				} catch (Exception e) {
					ToastUtils.Toast(getActivity(), R.string.error);
					return;
				}

				if (detail.getMessage().getCode() == 200) {
					loadFragmentData();
					initGoodsDetail(detail.getStock());
					mScrollLayout.canScroll();
				} else {
					ToastUtils.Toast(getActivity(), detail.getMessage()
							.getMessage());
				}
				getLoading().dismiss();
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void findView() {
		mScrollLayout = (ScrollableLayout) findViewById(R.id.mScrollLayout);
		slider = (ConvenientBanner<ImgInfo>) findViewById(R.id.slider);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		slider.setLayoutParams(lp);

		back_top = findViewById(R.id.back_top);
		more_view = (TextView) findViewById(R.id.more_view);

		collectionImg = (ImageView) findViewById(R.id.attention);
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
		initSliderImage(stock.getItemPreviewImgsForList());
		TextView pinTitle = (TextView) findViewById(R.id.pinTitle);
		TextView soldAmount = (TextView) findViewById(R.id.soldAmount);
		TextView pin_per_num = (TextView) findViewById(R.id.pin_per_num);
		TextView item_src_price = (TextView) findViewById(R.id.item_src_price);
		TextView pin_price = (TextView) findViewById(R.id.pin_price);

		pinTitle.setText(stock.getPinTitle());
		soldAmount.setText("已售：" + stock.getSoldAmount() + "件");
		if (stock.getInvPrice() != null) {
			item_src_price.setText(stock.getInvPrice() + "元/件");

		}
		pin_price.setText(stock.getFloorPrice().get("price") + "元/件起");
		pin_per_num.setText("最高" + stock.getFloorPrice().get("person_num")
				+ "人团");
		if (stock.getCollectId() != 0) {
			collectionImg.setImageResource(R.drawable.hmm_icon_collect_h);
			isCollection = true;
		} else {
			collectionImg.setImageResource(R.drawable.hmm_icon_collect);
			isCollection = false;
		}
		if (stock.getStatus().equals("Y")) {

		} else if (stock.getStatus().equals("P")) {
			noticeText = "活动尚未开始";
		} else {
			noticeText = "活动已结束";
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
	private void initSliderImage(List<ImgInfo> imgUrls) {
		ArrayList<ImgInfo> networkImages = new ArrayList<ImgInfo>(imgUrls);
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		}, networkImages).setPageIndicator(
				new int[] { R.drawable.page_indicator,
						R.drawable.page_indicator_fcoused });
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
			mScrollLayout.scrollToTop();
			break;
		case R.id.btn_buy_01:
		case R.id.btn_buy_02:
			clickPay();
			break;
		case R.id.btn_pin_01:
		case R.id.btn_pin_02:
			showEasyDialog(detail.getStock());
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
		case R.id.back:
			exitClick();
			break;
		default:
			break;
		}

	}

	private ShareWindow shareWindow;

	// 分享面板
	@SuppressLint("InflateParams")
	private void showShareboard() {
		if (shareWindow == null) {
			ShareVo vo = new ShareVo();
			vo.setContent(detail.getStock().getPinTitle());
			vo.setTitle("韩秘美，只卖韩国正品");
			vo.setImgUrl(detail.getStock().getInvImgForObj().getUrl());
			vo.setTargetUrl("http://style.hanmimei.com" + detail.getStock().getPinRedirectUrl().split("comm")[1]);
			vo.setInfoUrl(detail.getStock().getPinRedirectUrl());
			vo.setType("P");
			shareWindow = new ShareWindow(this, vo);
		}
		shareWindow.show();

	}

	private PushWindow pushWindow;

	@SuppressLint("InflateParams")
	private void showPopupwindow() {
		if (pushWindow == null) {
			pushWindow = new PushWindow(this, detail.getPush());
		}
		pushWindow.show();

	}

	private void collectGoods() {
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			findViewById(R.id.btn_attention).setOnClickListener(null);
			if (isCollection) {
				delCollection();
			} else {
				addCollection(getCollectedGoodsInfo());
			}
		}
	}

	private void delCollection() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.DEL_COLLECTION
				+ detail.getStock().getCollectId(), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				findViewById(R.id.btn_attention).setOnClickListener(
						PingouDetailActivity.this);
				HMessage message = DataParser.paserResultMsg(result);
				if (message.getCode() == 200) {
					isCollection = false;
					detail.getStock().setCollectId(0);
					collectionImg.setImageResource(R.drawable.hmm_icon_collect);
					sendBroadcast(new Intent(
							AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
				} else {
					ToastUtils.Toast(PingouDetailActivity.this, "取消收藏失败");
				}
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				findViewById(R.id.btn_attention).setOnClickListener(
						PingouDetailActivity.this);
				ToastUtils.Toast(PingouDetailActivity.this, "取消收藏失败");
			}
		});
	}

	private String getCollectedGoodsInfo() {
		JSONObject object = new JSONObject();
		try {
			object.put("skuId", detail.getStock().getId());
			object.put("skuType", detail.getStock().getSkuType());
			object.put("skuTypeId", detail.getStock().getSkuTypeId());
		} catch (JSONException e) {
		}
		return object.toString();
	}

	// 添加收藏
	private void addCollection(String cGoods) {
		getLoading().show();
		Http2Utils.doRequestTask2(this, Method.POST, getHeaders(),
				UrlUtil.ADD_COLLECTION, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						findViewById(R.id.btn_attention).setOnClickListener(
								PingouDetailActivity.this);
						HMessage message = DataParser.paserResultMsg(result);
						int collectionId = DataParser.parserCollectId(result);
						if (message.getCode() == 200) {
							isCollection = true;
							detail.getStock().setCollectId(collectionId);
							collectionImg
									.setImageResource(R.drawable.hmm_icon_collect_h);
							sendBroadcast(new Intent(
									AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
						} else {
							ToastUtils.Toast(PingouDetailActivity.this, "收藏失败");
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						findViewById(R.id.btn_attention).setOnClickListener(
								PingouDetailActivity.this);
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}, cGoods);
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
//				setScrollDown(currentY);
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
	
	/**
	 * 加载商品详情数据
	 */
	private void loadFragmentData() {
		if(detail == null || detail.getMain() == null)
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
		List<Customs> list = new ArrayList<Customs>();
		Customs customs = new Customs();

		ShoppingGoods sgoods = null;
		StockVo s = detail.getStock();
		if (s.getStatus().equals("Y")) {
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

	private void showEasyDialog(StockVo stock) {
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
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitClick();
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出函数
	 */
	private void exitClick() {
		if(getIntent().getStringExtra("from") != null){
			startActivity(new Intent(this,MainTestActivity.class));
			finish();
		}else{
			finish();
		}
	}

}
