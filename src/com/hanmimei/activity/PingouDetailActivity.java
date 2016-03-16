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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
import com.hanmimei.adapter.TuijianAdapter;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.entity.MainVo;
import com.hanmimei.entity.PinDetail;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.listener.GoodsPageChangeListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.PopupWindowUtil;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.HorizontalListView;
import com.hanmimei.view.NetworkImageHolderView;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class PingouDetailActivity extends BaseActivity implements
		OnClickListener {

	private ScrollableLayout mScrollLayout;
	private ConvenientBanner<ImgInfo> slider;
	private View back_top;
	private PinDetail detail;
	private ImageView collectionImg;
	private boolean isCollection = false;
	private TextView notice, more_view;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "商品详情", R.drawable.hmm_icon_fenxiang,
				true, null, this);
		setContentView(R.layout.pingou_detail_layout);
		findView();
		loadUrl();
		registerReceivers();
	}

	private void loadUrl() {
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
					initFragmentPager(detail.getMain());
					initGoodsDetail(detail.getStock());
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

		back_top = findViewById(R.id.back_top);
		notice = (TextView) findViewById(R.id.notice);
		more_view = (TextView) findViewById(R.id.more_view);

		View view = findViewById(R.id.viewpager_content);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		view.setLayoutParams(lp);
		collectionImg = (ImageView) findViewById(R.id.attention);
		findViewById(R.id.wanfaView).setOnClickListener(this);
		findViewById(R.id.back_top).setOnClickListener(this);
		findViewById(R.id.btn_attention).setOnClickListener(this);

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
			collectionImg.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_collect));
			isCollection = true;
		} else {
			collectionImg.setImageDrawable(getResources().getDrawable(
					R.drawable.icon_un_collect));
			isCollection = false;
		}
		if (stock.getStatus().equals("Y")) {
			findViewById(R.id.btn_buy_01).setOnClickListener(this);
			findViewById(R.id.btn_buy_02).setOnClickListener(this);
			findViewById(R.id.btn_pin_01).setOnClickListener(this);
			findViewById(R.id.btn_pin_02).setOnClickListener(this);
			notice.setVisibility(View.INVISIBLE);
		} else if (stock.getStatus().equals("P")) {
			notice.setText("该商品尚未开售");
		} else {
			notice.setText("该商品已售罄");
			more_view.setVisibility(View.VISIBLE);
			more_view.setOnClickListener(this);
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
		case R.id.qq:
			shareQQ();
			break;
		case R.id.weixin:
			shareWeiXin();
			break;
		case R.id.weixinq:
			shareCircle();
			break;
		case R.id.copy:
			doCopy();
			shareWindow.dismiss();
			break;
		default:
			break;
		}

	}

	private void doCopy() {
		String code[] = detail.getStock().getPinRedirectUrl().split("detail");
		HMMApplication application = (HMMApplication) getApplication();
		application.setKouling("KAKAO-HMM 复制这条信息,打开👉韩秘美👈即可看到<P>【"
				+ detail.getStock().getPinTitle() + "】," + code[1]
				+ ",－🔑 M令 🔑");
		ToastUtils.Toast(this, "复制成功，快去粘贴吧");
	}

	// 微信朋友圈分享设置
	private void shareCircle() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
				.setCallback(umShareListener)
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withTitle(shareStock.getPinTitle())
				.withText(shareStock.getPinTitle())
				.withTargetUrl("http://www.hanmimei.com/").share();
	}

	// 微信分享设置
	private void shareWeiXin() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.WEIXIN)
				.setCallback(umShareListener)
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withTitle("全球正品，尽在韩秘美").withText(shareStock.getPinTitle())
				.withTargetUrl("http://www.hanmimei.com/").share();
	}

	// QQ分享设置
	private void shareQQ() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.QQ)
				.setCallback(umShareListener)
				.withTitle("全球正品，尽在韩秘美")
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withText(shareStock.getPinTitle())
				.withTargetUrl("http://www.hanmimei.com/").share();
	}

	private StockVo shareStock;
	private PopupWindow shareWindow;

	// 分享面板
	@SuppressLint("InflateParams")
	private void showShareboard() {
		View view = LayoutInflater.from(this).inflate(R.layout.share_layout,
				null);
		shareWindow = PopupWindowUtil.showPopWindow(this, view);
		view.findViewById(R.id.qq).setOnClickListener(this);
		view.findViewById(R.id.weixin).setOnClickListener(this);
		view.findViewById(R.id.weixinq).setOnClickListener(this);
		// view.findViewById(R.id.sina).setOnClickListener(this);
		view.findViewById(R.id.copy).setOnClickListener(this);
		Config.OpenEditor = true;
		shareStock = detail.getStock();

	}

	private PopupWindow tuiWindow;

	@SuppressLint("InflateParams")
	private void showPopupwindow() {
		if (tuiWindow == null) {
			View view = getLayoutInflater().inflate(R.layout.tuijian_layout,
					null);
			HorizontalListView more_grid = (HorizontalListView) view
					.findViewById(R.id.more_grid);
			TextView titleView = (TextView) view.findViewById(R.id.title);
			titleView.setText("该活动已结束，去看看其他拼购吧");
			more_grid.setAdapter(new TuijianAdapter(detail.getPush(), this));
			more_grid.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = null;
					if (detail.getPush().get(arg2).getItemType().equals("pin")) {
						intent = new Intent(getActivity(),
								PingouDetailActivity.class);
					} else {
						intent = new Intent(getActivity(),
								GoodsDetailActivity.class);
					}
					intent.putExtra("url", detail.getPush().get(arg2)
							.getItemUrl());
					startActivityForResult(intent, 1);
				}
			});
			more_grid.setFocusable(false);
			tuiWindow = PopupWindowUtil.showPopWindow(this, view);
			more_view.setOnClickListener(this);
		} else {
			PopupWindowUtil.backgroundAlpha(this, 0.4f);
			tuiWindow.showAtLocation(more_view, Gravity.BOTTOM, 0, 0);
		}

	}

	private void collectGoods() {
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {
			toObject();
			if (isCollection) {
				delCollection();
			} else {
				addCollection();
			}
		}
	}

	private void delCollection() {
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.DEL_COLLECTION
				+ detail.getStock().getCollectId(), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				HMessage message = DataParser.paserResultMsg(result);
				if (message.getCode() == 200) {
					isCollection = false;
					detail.getStock().setCollectId(0);
					collectionImg.setImageDrawable(getResources().getDrawable(
							R.drawable.icon_un_collect));
					ToastUtils.Toast(PingouDetailActivity.this, "取消收藏成功");
					sendBroadcast(new Intent(
							AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
				} else {
					ToastUtils.Toast(PingouDetailActivity.this, "取消收藏失败");
				}
			}

			@Override
			public void onError() {
				ToastUtils.Toast(PingouDetailActivity.this, "取消收藏失败");
			}
		});
	}

	private JSONObject object;

	private void toObject() {
		object = new JSONObject();
		try {
			object.put("skuId", detail.getStock().getId());
			object.put("skuType", detail.getStock().getSkuType());
			object.put("skuTypeId", detail.getStock().getSkuTypeId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 添加收藏
	private void addCollection() {
		Http2Utils.doRequestTask2(this, Method.POST, getHeaders(),
				UrlUtil.ADD_COLLECTION, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						HMessage message = DataParser.paserResultMsg(result);
						int collectionId = DataParser.parserCollectId(result);
						if (message.getCode() == 200) {
							isCollection = true;
							detail.getStock().setCollectId(collectionId);
							collectionImg.setImageDrawable(getResources()
									.getDrawable(R.drawable.icon_collect));
							ToastUtils.Toast(PingouDetailActivity.this, "收藏成功");
							sendBroadcast(new Intent(
									AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
						} else {
							ToastUtils.Toast(PingouDetailActivity.this, "收藏失败");
						}
					}

					@Override
					public void onError() {
						ToastUtils.Toast(PingouDetailActivity.this,
								"收藏失败，请检查您的网络");
					}
				}, object.toString());
	}

	private void initFragmentPager(MainVo main) {
		if (main == null)
			return;
		mScrollLayout.canScroll();
		List<String> titles = new ArrayList<String>();
		titles.add("图文详情");
		titles.add("商品参数");
		titles.add("热门商品");

		final List<ScrollAbleFragment> fragments = new ArrayList<ScrollAbleFragment>();
		ScrollAbleFragment imgFragment = ImgFragment.newInstance(main
				.getItemDetailImgs());
		ScrollAbleFragment parFragment = ParamsFragment.newInstance(main
				.getItemFeaturess());
		ScrollAbleFragment gridViewFragment = HotFragment.newInstance(detail
				.getPush());
		fragments.add(imgFragment);
		fragments.add(parFragment);
		fragments.add(gridViewFragment);

		GoodsDetailPagerAdapter adapter = new GoodsDetailPagerAdapter(
				getSupportFragmentManager(), fragments, titles);
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
			}
		});
		pagerSlidingTabStrip
				.setOnPageChangeListener(new GoodsPageChangeListener() {

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
	 * 点击立即购买按钮的响应事件
	 */
	private void clickPay() {
		// 未登录跳到登陆页面
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
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
			sgoods.setSkuTypeId(s.getPinId());
		} else if (s.getStatus().equals("P")) {
			ToastUtils.Toast(this, "尚未开售");
			return;
		} else {
			ToastUtils.Toast(this, "活动已结束");
			return;
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

	// ---------------友盟-----------------------
	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			shareWindow.dismiss();
			// Toast.makeText(GoodsDetailActivity.this, platform + " 分享成功",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			shareWindow.dismiss();
			// Toast.makeText(GoodsDetailActivity.this,platform + " 分享失败",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			shareWindow.dismiss();
			// Toast.makeText(GoodsDetailActivity.this,platform + " 分享取消",
			// Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}

}
