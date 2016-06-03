package com.hanmimei.activity.goods.detail;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout.OnScrollListener;
import com.hanmimei.R;
import com.hanmimei.activity.HMainActivity;
import com.hanmimei.activity.balance.GoodsBalanceActivity;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.car.ShoppingCarActivity;
import com.hanmimei.activity.goods.comment.GoodsCommentActivity;
import com.hanmimei.activity.goods.detail.adapter.GoodsDetailPagerAdapter;
import com.hanmimei.activity.goods.detail.fragment.HotFragment;
import com.hanmimei.activity.goods.detail.fragment.ImgFragment;
import com.hanmimei.activity.goods.detail.fragment.ParamsFragment;
import com.hanmimei.activity.login.LoginActivity;
import com.hanmimei.activity.presenter.gdetail.GoodsDetailPresenterImpl;
import com.hanmimei.activity.view.gdetail.GoodsDetailView;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.CommentVo;
import com.hanmimei.entity.CustomsVo;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.ImageVo;
import com.hanmimei.entity.ShareVo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.entity.TagVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.override.SimpleAnimationListener;
import com.hanmimei.override.ViewPageChangeListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.AnimationTools;
import com.hanmimei.utils.GlideLoaderTools;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.hanmimei.view.GoodsPushWindow;
import com.hanmimei.view.NetworkImageHolderView;
import com.hanmimei.view.ShareWindow;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.umeng.socialize.UMShareAPI;

/**
 * 
 * @author vince
 * 
 * 
 */
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, GoodsDetailView, IWeiboHandler.Response {

	private static final String Tag = "GoodsDetailActivity";

	private TextView itemTitle;
	private TextView itemSrcPrice;
	private TextView itemPrice;
	private TextView area;// 标题、 原价、现价、发货区

	private TextView num_restrictAmount; // 限购数量
	private ImageView img_hide, collectionImg;
	private TextView more_view;

	private BadgeView goodsNumView;// 显示购买数量的控件
	private ConvenientBanner<ImageVo> slider;

	private ShareWindow shareWindow;
	private View back_top;
	private ScrollableLayout mScrollLayout;

	// private User user;
	private GoodsDetail detail;
	private GoodsDetailPresenterImpl detailPresenterImpl;
	private int num_shopcart;
	private boolean isCollection;
	private boolean isChange;

	private ScrollAbleFragment imgFragment;
	private ScrollAbleFragment parFragment;
	private ScrollAbleFragment gridViewFragment;

	private ObjectAnimator shopcartAnimator, imgAnimator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "商品详情",
				R.drawable.hmm_icon_share, true, this, this);
		setContentView(R.layout.goods_detail_layout);
		findView();
		initGoodsNumView();
		initFragmentPager();
		getGoodsNums();
		loadDataByUrl();
		registerReceivers();
		if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
	}

	/**
	 * 初始化所有view
	 * 
	 * @param savedInstanceState
	 */
	@SuppressWarnings("unchecked")
	private void findView() {
		detailPresenterImpl = new GoodsDetailPresenterImpl(this);

		slider = (ConvenientBanner<ImageVo>) findViewById(R.id.slider);
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemSrcPrice = (TextView) findViewById(R.id.itemSrcPrice);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		more_view = (TextView) findViewById(R.id.more_view);
		num_restrictAmount = (TextView) findViewById(R.id.restrictAmount);
		area = (TextView) findViewById(R.id.area);
		img_hide = (ImageView) findViewById(R.id.img_hide);
		mScrollLayout = (ScrollableLayout) findViewById(R.id.mScrollLayout);
		collectionImg = (ImageView) findViewById(R.id.attention);
		back_top = findViewById(R.id.back_top);

		findViewById(R.id.btn_comment).setOnClickListener(this);

	}

	/**
	 * 购物车数量view 初始化
	 */
	private void initGoodsNumView() {
		goodsNumView = new BadgeView(this, findViewById(R.id.shopcart));
		goodsNumView.setTextColor(getResources().getColor(R.color.theme));
		goodsNumView.setBadgePosition(BadgeView.POSITION_CENTER_HORIZONTAL);
		goodsNumView.setTextSize(10);
		goodsNumView.setBackgroundResource(R.drawable.bg_badgeview2);
	}

	private void showGoodsNums() {
		if (num_shopcart > 0) {
			if (num_shopcart <= 99) {
				goodsNumView.setText(num_shopcart + "");
			} else {
				goodsNumView.setText("...");
			}
			goodsNumView.show(true);
		} else {
			goodsNumView.hide(true);
		}
	}

	// =========================================================================
	// ===============================网络 请求===================================
	// =========================================================================
	/**
	 * 加载数据
	 */

	private void loadDataByUrl() {
		if (TextUtils.isEmpty(getIntent().getStringExtra("url")))
			return;
		detailPresenterImpl.getGoodsDetailData(getHeaders(), getIntent()
				.getStringExtra("url"), Tag);
	}

	private void getGoodsNums() {
		detailPresenterImpl.getCartNumData(getHeaders(), null);
	}

	// =========================================================================
	// ===============================点击 事件====================================
	// =========================================================================

	@Override
	public void onClick(View arg0) {
		if (detail == null) {
			ToastUtils.Toast(this, "正在加载数据");
			return;
		}
		switch (arg0.getId()) {
		case R.id.setting:
			showShareboard();
			break;
		case R.id.more_view:
			showPushWindow();
			break;
		case R.id.btn_comment:
			turnToGoodsCommentActivity();
			break;
		case R.id.back:
			exitClick();
			break;
		default:
			break;
		}
	}

	/**
	 * 加入购物车
	 * 
	 * @author vince
	 */
	public void addToShoppingCart(View view) {
		if (detail == null)
			return;
		ShoppingGoods goods = null;
		for (StockVo stock : detail.getStock()) {
			if (stock.getOrMasterInv() && stock.getState().equals("Y")) {
				goods = new ShoppingGoods();
				goods.setGoodsId(stock.getId());
				goods.setGoodsNums(1);
				goods.setSkuType(stock.getSkuType());
				goods.setSkuTypeId(stock.getSkuTypeId());
				break;
			}
		}
		if (goods == null) {
			ToastUtils.Toast(this, "请选择商品");
			return;
		}
		findViewById(R.id.btn_add_shopcart).setClickable(false);
		detailPresenterImpl.addToCart(getHeaders(), goods);
		isChange = true;

	}

	private void displayAnimation() {
		GlideLoaderTools.loadSquareImage(getActivity(), detail
				.getCurrentStock().getInvImgForObj().getUrl(), img_hide);
		if (shopcartAnimator == null || imgAnimator == null) {
			shopcartAnimator = AnimationTools.nope(findViewById(R.id.shopcart));
			imgAnimator = AnimationTools.initAnimatorSetValue(this, img_hide,
					new SimpleAnimationListener() {

						@Override
						public void onAnimationStart(Animator arg0) {
							img_hide.setVisibility(View.VISIBLE);
						}

						@Override
						public void onAnimationEnd(Animator arg0) {
							img_hide.setVisibility(View.GONE);
							num_shopcart++;
							showGoodsNums();
							findViewById(R.id.btn_add_shopcart).setClickable(
									true);
							shopcartAnimator.start();
						}
					});
		}
		imgAnimator.start();
	}

	// 分享面板
	private void showShareboard() {
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, AppConstant.WEIBO_APPKEY);
		if (shareWindow == null) {
			StockVo shareStock = detail.getCurrentStock();
			if (shareStock == null) {
				ToastUtils.Toast(this, "等待加载数据");
				return;
			}
			ShareVo vo = new ShareVo();
			vo.setContent(shareStock.getInvTitle());
			vo.setTitle("韩秘美，只卖韩国正品");
			if (shareStock.getInvImgForObj() != null)
				vo.setImgUrl(shareStock.getInvImgForObj().getUrl());
			if (shareStock.getInvUrl() != null) {
				vo.setTargetUrl("http://style.hanmimei.com"
						+ shareStock.getInvUrl().split("comm")[1]);
				vo.setInfoUrl(shareStock.getInvUrl());
			}
			vo.setType("C");
			shareWindow = new ShareWindow(this, vo, mWeiboShareAPI);
		}
		shareWindow.show();
	}

	// =========================================================================
	// ===============================响应方法 ==================================
	// =========================================================================
	/**
	 * 点击立即购买按钮的响应事件
	 */
	public void clickPay(View view) {
		// 未登录跳到登陆页面
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		if (detail == null)
			return;
		ShoppingCar car = new ShoppingCar();
		List<CustomsVo> list = new ArrayList<CustomsVo>();
		CustomsVo customs = new CustomsVo();

		ShoppingGoods sgoods = null;
		for (StockVo s : detail.getStock()) {
			if (s.getOrMasterInv()) {
				if (s.getState().equals("Y")) {
					sgoods = new ShoppingGoods();
					sgoods.setGoodsId(s.getId());
					sgoods.setGoodsImg(s.getInvImgForObj().getUrl());
					sgoods.setGoodsName(s.getInvTitle());
					sgoods.setGoodsNums(1);
					sgoods.setGoodsPrice(s.getItemPrice().doubleValue());
					sgoods.setInvArea(s.getInvArea());
					sgoods.setInvAreaNm(s.getInvAreaNm());
					sgoods.setInvCustoms(s.getInvCustoms());
					sgoods.setPostalTaxRate(s.getPostalTaxRate());
					sgoods.setPostalStandard(s.getPostalStandard());
					sgoods.setSkuType(s.getSkuType());
					sgoods.setSkuTypeId(s.getSkuTypeId());
					// sgoods.setPinTieredPriceId(s.getPinTieredPrices());
					break;
				} else {
					ToastUtils.Toast(this, "请选择商品");
					return;
				}
			}
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
	 * 弹出显示税费的提醒框
	 */
	public void showPortalFeeInfo(View view) {
		// TODO 弹出显示税费的提醒框
		AlertDialogUtils.showPostDialog(this, curItemPrice, curPostalTaxRate,
				postalStandard);
	}

	/**
	 * 跳转到购物车页面
	 * 
	 * @param view
	 */
	public void turnToShoppingCarActivity(View view) {
		startActivity(new Intent(this, ShoppingCarActivity.class));
	}

	/**
	 * 跳转到商品评价页面
	 * 
	 * @param view
	 */
	public void turnToGoodsCommentActivity() {
		if (detail == null)
			return;
		Intent intent = new Intent(this, GoodsCommentActivity.class);
		intent.putExtra("skuType", detail.getCurrentStock().getSkuType());
		intent.putExtra("skuTypeId", detail.getCurrentStock().getSkuTypeId());
		startActivity(intent);
	}

	/**
	 * 返回顶部
	 * 
	 * @param view
	 */
	public void scrollToTop(View view) {
		mScrollLayout.scrollToTop();
	}

	// 收藏商品
	public void collectGoods(View view) {
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		if (detail == null)
			return;
		findViewById(R.id.btn_attention).setClickable(false);
		if (isCollection) {
			detailPresenterImpl.cancelCollection(getHeaders(), detail
					.getCurrentStock().getCollectId());
		} else {
			detailPresenterImpl.addCollection(getHeaders(),
					detail.getCurrentStock());
		}
	}

	private GoodsPushWindow pushWindow;

	private void showPushWindow() {
		if (pushWindow == null) {
			pushWindow = new GoodsPushWindow(this, detail.getPush());
		}
		pushWindow.show();
	}

	// =========================================================================
	// ===============================初始化方法==================================
	// =========================================================================
	/**
	 * 初始化显示商品信息
	 * 
	 * @param detail
	 *            商品总详情数据
	 */
	private void initGoodsDetail() {
		if (detail == null)
			return;
		if (detail.getMessage().getCode() != 200) {
			ToastUtils.Toast(this, detail.getMessage().getMessage());
			return;
		}

		// 子商品信息
		TextView publicity = (TextView) findViewById(R.id.publicity); // 优惠信息
																		// /购物车数量
		if (detail.getMain() != null) {
			publicity.setText(detail.getMain().getPublicity());
		}
		// 主详情
		initGoodsInfo();
		loadFragmentData();
		mScrollLayout.canScroll();
	}

	/**
	 * 初始化商品详情布局
	 */
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
		final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
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
						viewPager.setCurrentItem(i);
					}
				});
	}

	/**
	 * 加载商品详情数据
	 */
	private void loadFragmentData() {
		if (detail.getMain() == null)
			return;
		imgFragment.showData(detail.getMain().getItemDetailImgs());
		parFragment.showData(detail.getMain().getItemFeaturess());
		gridViewFragment.showData(detail.getPush());

	}

	private double curPostalTaxRate; // 当前商品税率
	private double curItemPrice; // 当前商品价格
	private int postalStandard;// 关税收费标准

	private void initGoodsInfo() {
		if (detail.getStock() == null)
			return;
		StockVo stock = null;
		List<TagVo> tags = new ArrayList<TagVo>();
		boolean outDate = true;
		for (StockVo s : detail.getStock()) {
			tags.add(new TagVo(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));
			if (s.getOrMasterInv()) {
				stock = s;
			}
			if (s.getState().equals("Y"))
				outDate = false;
		}
		if (outDate) {
			more_view.setVisibility(View.VISIBLE);
			more_view.setOnClickListener(this);
			findViewById(R.id.btn_pay).setEnabled(false);
			findViewById(R.id.btn_add_shopcart).setEnabled(false);
			showPushWindow();
		}
		initStocks(stock);
		initTags(tags);
		initGoodsComment(detail.getCommentVo());
	}

	private void initGoodsComment(CommentVo comm) {
		TextView remarkRate = (TextView) findViewById(R.id.remarkRate);
		TextView remarkCount = (TextView) findViewById(R.id.remarkCount);
		if (comm.getRemarkCount() <= 0) {
			findViewById(R.id.btn_comment).setVisibility(View.GONE);
		} else {
			findViewById(R.id.btn_comment).setOnClickListener(this);
			remarkCount.setText(getResources().getString(R.string.comment,
					comm.getRemarkCount()));
			remarkRate.setText(getResources().getString(R.string.comment_good,
					comm.getRemarkRate()));
		}
	}

	private void initTags(List<TagVo> tags) {
		if (tags.size() <= 0)
			return;
		TagCloudView tagCloudView = (TagCloudView) findViewById(R.id.tagCloudView);// 规格标签控件
		// 初始化规格显示
		tagCloudView.removeAllViews();
		tagCloudView.setTags(tags);
		// 规格标签的点击事件
		tagCloudView.setOnTagClickListener(new OnTagClickListener() {
			@Override
			public void onTagClick(int oldPostion, int position, TagVo tag) {
				detail.getStock().get(oldPostion).setOrMasterInv(false);
				detail.getStock().get(position).setOrMasterInv(true);
				initStocks(detail.getStock().get(position));
				mScrollLayout.scrollToTop();
				if (more_view.getVisibility() == View.VISIBLE) {
					more_view.setVisibility(View.GONE);
					more_view.setOnClickListener(null);
				}
			}
		});
	}

	/**
	 * 初始化子商品信息
	 * 
	 * @param position
	 *            选中商品位置
	 */

	private void initStocks(StockVo s) {
		if (s == null)
			return;
		initSliderImage(s);
		String zhe = "";
		if (s.getItemDiscount().floatValue() > 0) {
			zhe += "[" + s.getItemDiscount() + "折]";
			itemSrcPrice.setText(getResources().getString(R.string.price,
					s.getItemSrcPrice()));
			itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		KeyWordUtil.setDifferentFontColor(this, itemTitle,
				zhe + s.getInvTitle(), 0, zhe.length());
		itemPrice.setText(getResources().getString(R.string.price,
				s.getItemPrice()));
		if (s.getRestrictAmount() != null && s.getRestrictAmount() > 0) {
			num_restrictAmount.setVisibility(View.VISIBLE);
			num_restrictAmount.setText(getResources().getString(
					R.string.restrictAmount, s.getRestrictAmount()));
		} else {
			num_restrictAmount.setVisibility(View.GONE);
		}
		if (s.getPostalTaxRate() != null)
			curPostalTaxRate = s.getPostalTaxRate_();
		curItemPrice = s.getItemPrice().doubleValue();
		postalStandard = s.getPostalStandard();
		area.setText("邮寄方式：" + s.getInvAreaNm());
		if (s.getCollectId() != 0) {
			collectionImg.setImageResource(R.drawable.hmm_icon_collect_h);
			isCollection = true;
		} else {
			collectionImg.setImageResource(R.drawable.hmm_icon_collect);
			isCollection = false;
		}
	}

	/**
	 * 初始化轮播图
	 * 
	 * @param s
	 *            当前选中子商品
	 */
	private void initSliderImage(StockVo s) {
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		}, s.getItemPreviewImgsForList()).setPageIndicator(
				new int[] { R.drawable.page_indicator,
						R.drawable.page_indicator_fcoused });
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this **/
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}

	private CarBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new CarBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
				getGoodsNums();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				loadDataByUrl();
				getGoodsNums();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
		if (isChange) {
			Intent intent = new Intent(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
			intent.putExtra("cartNum", num_shopcart);
			sendBroadcast(intent);
		}
		VolleyHttp.parseRequestTask(Tag);
	}

	// 主界面返回之后在后台运行
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
		if (getIntent().getStringExtra("from") != null) {
			startActivity(new Intent(this, HMainActivity.class));
			finish();
		} else {
			finish();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hanmimei.activity.view.gdetail.GoodsDetailView#showLoading()
	 */
	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		Log.i("showLoading", getLoading().isShowing() + "");
		if (!getLoading().isShowing())
			getLoading().show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hanmimei.activity.view.gdetail.GoodsDetailView#hideLoading()
	 */
	@Override
	public void hideLoading() {
		// TODO Auto-generated method stub
		Log.i("hideLoading", getLoading().isShowing() + "");
		if (getLoading().isShowing())
			getLoading().dismiss();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.gdetail.GoodsDetailView#GetGoodsDetailData
	 * (com.hanmimei.entity.GoodsDetail)
	 */
	@Override
	public void GetGoodsDetailData(GoodsDetail detail) {
		// TODO Auto-generated method stub
		this.detail = detail;
		initGoodsDetail();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.gdetail.GoodsDetailView#GetCartNumData(java
	 * .lang.Integer)
	 */
	@Override
	public void GetCartNumData(Integer cartNum) {
		// TODO Auto-generated method stub
		if (cartNum == null)
			return;
		this.num_shopcart = cartNum;
		showGoodsNums();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.gdetail.GoodsDetailView#showLoadFaild(java
	 * .lang.String)
	 */
	@Override
	public void showLoadFaild(String str) {
		// TODO Auto-generated method stub
		ToastUtils.Toast(this, str);
		findViewById(R.id.btn_add_shopcart).setClickable(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.gdetail.GoodsDetailView#addCollection(long)
	 */
	@Override
	public void addCollection(long collectId) {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_attention).setClickable(true);
		isCollection = true;
		detail.getCurrentStock().setCollectId(collectId);
		collectionImg.setImageResource(R.drawable.hmm_icon_collect_h);
		sendBroadcast(new Intent(
				AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
		ToastUtils.Toast(this, "收藏成功");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.gdetail.GoodsDetailView#cancelCollection()
	 */
	@Override
	public void cancelCollection() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_attention).setClickable(true);
		isCollection = false;
		detail.getCurrentStock().setCollectId(0);
		collectionImg.setImageResource(R.drawable.hmm_icon_collect);
		sendBroadcast(new Intent(
				AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
		ToastUtils.Toast(this, "已取消收藏");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.gdetail.GoodsDetailView#addToCartWithLogin()
	 */
	@Override
	public void addToCartSuccess() {
		// 购物车添加成功，显示提示框
		displayAnimation();
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		switch (baseResp.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			Toast.makeText(this, R.string.weibosdk_demo_toast_share_success,
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_CANCEL:
			Toast.makeText(this, R.string.weibosdk_demo_toast_share_canceled,
					Toast.LENGTH_LONG).show();
			break;
		case WBConstants.ErrorCode.ERR_FAIL:
			Toast.makeText(
					this,
					getString(R.string.weibosdk_demo_toast_share_failed)
							+ "Error Message: " + baseResp.errMsg,
					Toast.LENGTH_LONG).show();
			break;
		}
	}
	private IWeiboShareAPI mWeiboShareAPI ;
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

}
