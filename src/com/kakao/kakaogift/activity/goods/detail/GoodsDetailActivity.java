package com.kakao.kakaogift.activity.goods.detail;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.balance.GoodsBalanceActivity;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.car.ShoppingCarActivity;
import com.kakao.kakaogift.activity.goods.comment.GoodsCommentActivity;
import com.kakao.kakaogift.activity.goods.detail.adapter.GoodsDetailPagerAdapter;
import com.kakao.kakaogift.activity.goods.detail.fragment.HotFragment;
import com.kakao.kakaogift.activity.goods.detail.fragment.ImgFragment;
import com.kakao.kakaogift.activity.goods.detail.fragment.ParamsFragment;
import com.kakao.kakaogift.activity.goods.detail.presenter.GoodsDetailPresenterImpl;
import com.kakao.kakaogift.activity.login.LoginActivity;
import com.kakao.kakaogift.activity.main.HMainActivity;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.entity.CommentVo;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.GoodsDetail;
import com.kakao.kakaogift.entity.ImageVo;
import com.kakao.kakaogift.entity.ShareVo;
import com.kakao.kakaogift.entity.ShoppingCar;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.StockVo;
import com.kakao.kakaogift.entity.TagVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.override.SimpleAnimationListener;
import com.kakao.kakaogift.override.ViewPageChangeListener;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AnimationTools;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.kakao.kakaogift.utils.KeyWordUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.BadgeView;
import com.kakao.kakaogift.view.GoodsPushWindow;
import com.kakao.kakaogift.view.NetworkImageHolderView;
import com.kakao.kakaogift.view.ShareWindow;
import com.kakao.kakaogift.view.TagCloudView;
import com.kakao.kakaogift.view.TagCloudView.OnTagClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
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

	private TextView itemTitle, itemSrcPrice, itemPrice, area, restrictAmount;// 标题、
	// 原价、现价、发货区,税率

	// private TextView num_restrictAmount; // 限购数量
	private ImageView img_hide, collectionImg;
	private TextView more_view;

	private BadgeView goodsNumView;// 显示购买数量的控件
	private ConvenientBanner<ImageVo> slider;

	private ShareWindow shareWindow; // 分享弹出窗
	private GoodsPushWindow pushWindow; // 推荐商品弹出窗
	private View back_top; // 返回顶部按钮
	private ScrollableLayout mScrollLayout; // 滚动

	// private User user;
	private GoodsDetail detail;
	private GoodsDetailPresenterImpl detailPresenterImpl;
	private int num_shopcart;
	private boolean isCollection; // 标志商品是否被收藏
	private boolean isChange; // 判断是否进行过加入购物车操作

	private ScrollAbleFragment imgFragment;
	private ScrollAbleFragment parFragment;
	private ScrollAbleFragment gridViewFragment;

	private ObjectAnimator shopcartAnimator, imgAnimator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "商品详情",
				R.drawable.hmm_icon_share, true, new BackListener(), this);
		setContentView(R.layout.goods_detail_layout);
		//
		findView();
		// 购物车数量view 初始化
		initGoodsNumView();
		// 初始化商品详情布局
		initFragmentPager();
		// 获取购物车数量
		getGoodsNums();
		// 加载数据
		loadDataByUrl();
		// 注册广播接受者
		registerReceivers();
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
		restrictAmount = (TextView) findViewById(R.id.restrictAmount);
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
		goodsNumView.setTextColor(getResources().getColor(R.color.white));
		goodsNumView.setBadgePosition(BadgeView.POSITION_CENTER_HORIZONTAL);
		goodsNumView.setTextSize(10);
		goodsNumView.setBackgroundResource(R.drawable.bg_badgeview);
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
		detailPresenterImpl.getCartNumData(getHeaders(), getDaoSession().getShoppingGoodsDao(),null);
	}

	// =========================================================================
	// ===============================点击 事件====================================
	// =========================================================================

	@Override
	public void onClick(View arg0) {
		if(detail == null)
			return;
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
		// 获取被选中的商品
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
		// 加入购物车
		detailPresenterImpl.addToCart(getHeaders(), getDaoSession().getShoppingGoodsDao(),goods);
		isChange = true;

	}

	/**
	 * 播放加入购物车动画
	 */
	private void displayAnimation() {
		// 加载商品图片
//		if(isDestroyed())
//			return;
		GlideLoaderTools.loadSquareImage(getActivity(), detail.getCurrentStock().getInvImgForObj().getUrl(), img_hide);
		if (shopcartAnimator == null && imgAnimator == null) {
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
		if (shareWindow == null) {
			StockVo shareStock = detail.getCurrentStock();
			if (shareStock == null) {
				ToastUtils.Toast(this, "等待加载数据");
				return;
			}
			ShareVo vo = new ShareVo();
			vo.setContent(shareStock.getInvTitle());
			vo.setTitle("我在KakaoGift发现了一个不错的礼物，赶快来看看吧");
			if (shareStock.getInvImgForObj() != null)
				vo.setImgUrl(shareStock.getInvImgForObj().getUrl());
			if (shareStock.getInvUrl() != null) {
				vo.setTargetUrl(shareStock.getInvUrl().split("comm")[1]);
				vo.setInfoUrl(shareStock.getInvUrl());
			}
			vo.setType("C");
			shareWindow = new ShareWindow(this, vo);
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
		// 获取被选中的商品 ,整理成购物车数据格式
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
		// 跳转到立即支付页面
		Intent intent = new Intent(this, GoodsBalanceActivity.class);
		intent.putExtra("car", car); // 购物车数据格式
		intent.putExtra("orderType", "item"); // 订单类型 item 普通订单 pin 拼购订单
		startActivity(intent);
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
	 */
	public void scrollToTop(View view) {
		mScrollLayout.smoothScrollToTop();
	}

	/**
	 * 收藏商品
	 * 
	 * @param view
	 */
	public void collectGoods(View view) {
		if (getUser() == null) {
			// 未登录情况下，先去登录
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}
		// 商品尚未加载完成，return 操作 避免空指针
		if (detail == null)
			return;
		findViewById(R.id.btn_attention).setEnabled(false);
		if (isCollection) {
			// 已经被收藏，取消收藏
			detailPresenterImpl.cancelCollection(getHeaders(), detail
					.getCurrentStock().getCollectId());
		} else {
			// 尚未被收藏 添加收藏
			detailPresenterImpl.addCollection(getHeaders(),
					detail.getCurrentStock());
		}
	}

	/**
	 * 展示推荐商品
	 */
	private void showPushWindow() {
		if (pushWindow == null) {
			pushWindow = new GoodsPushWindow(this, detail.getPush());
//			pushWindow = new GoodsPushWindow(this, detail.getPush());
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
		// 商品尚未加载完成，return 操作 避免空指针
		if (detail == null)
			return;
		// 商品加载失败
		if (detail.getMessage().getCode() != 200) {
			ToastUtils.Toast(this, detail.getMessage().getMessage());
			return;
		}
		// 子商品信息
		TextView publicity = (TextView) findViewById(R.id.publicity); // 优惠信息
		if (detail.getMain() != null &&
				detail.getMain().getPublicity() !=null) {
			publicity.setVisibility(View.INVISIBLE);
			publicity.setText(detail.getMain().getPublicity());
		}
		// 主详情
		initGoodsInfo();
		// 加载商品详情数据
		loadFragmentData();
		// 数据加载完成，允许滚动
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
		// 注册滚动帮助，避免滚动冲突
		mScrollLayout.getHelper().setCurrentScrollableContainer(
				fragments.get(0));
		pagerSlidingTabStrip.setViewPager(viewPager);
		pagerSlidingTabStrip.setTextColor(getResources().getColor(
				R.color.indicator_font_color_selector));
		// 滚动监听，判断
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
		// 初始化显示数据
		imgFragment.showData(detail.getMain().getItemDetailImgs());
		parFragment.showData(detail.getMain().getItemFeaturess());
		gridViewFragment.showData(detail.getPush());

	}

	/**
	 * 初始化商品详情
	 */
	private void initGoodsInfo() {
		if (detail.getStock() == null)
			return;
		StockVo stock = null;
		List<TagVo> tags = new ArrayList<TagVo>();
		boolean outDate = true;
		// 获取主显示商品信息，并判断是否正常销售
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
			// 确定所有商品未能正常销售
			more_view.setVisibility(View.VISIBLE);
			more_view.setOnClickListener(this);
			findViewById(R.id.btn_pay).setEnabled(false);
			findViewById(R.id.btn_add_shopcart).setEnabled(false);
			TextView view = (TextView) findViewById(R.id.sold_out);
			if(stock.getState().equals("K")){
				view.setText("已抢光");
			}else if(stock.getState().equals("D")){
				view.setText("已下架");
			}else if(stock.getState().equals("P")){
				view.setText("未开售");
			}
			view.setVisibility(View.VISIBLE);
			showPushWindow();
		}
		// 初始化主显示商品信息
		initStocks(stock);
		// 初始化各个商品标签
		initTags(tags);
		// 出事化商品评价
		initGoodsComment(detail.getCommentVo());
	}

	/**
	 * 初始化商品评价
	 * 
	 * @param comm
	 *            评价信息
	 */
	private void initGoodsComment(CommentVo comm) {
		TextView remarkRate = (TextView) findViewById(R.id.remarkRate);
		TextView remarkCount = (TextView) findViewById(R.id.remarkCount);
		if (comm.getRemarkCount() <= 0) {
			// 尚未有评价信息，隐藏评价按钮你
			findViewById(R.id.btn_comment).setVisibility(View.GONE);
		} else {
			// 初始化评价信息
			findViewById(R.id.btn_comment).setOnClickListener(this);
			remarkCount.setText(getResources().getString(R.string.comment,
					comm.getRemarkCount()));
			String str = getResources().getString(R.string.comment_good,
					comm.getRemarkRate());
			KeyWordUtil.setDifrentFontColor(this, remarkRate, str, 3,
					str.length());
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
				//
				detail.getStock().get(oldPostion).setOrMasterInv(false);
				detail.getStock().get(position).setOrMasterInv(true);
				initStocks(detail.getStock().get(position));
				mScrollLayout.smoothScrollToTop();
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
		// 初始化轮播图
		initSliderImage(s);
		String zhe = null;
		if (s.getItemDiscount().floatValue() > 0
				&& s.getItemDiscount().floatValue() < 10) {
			// 标题折扣的获取
			zhe = " " + s.getItemDiscount() + "折 ";
			// 存在折扣 显示原价
			itemSrcPrice.setText(getResources().getString(R.string.price_src,
					s.getItemSrcPrice()));
			itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		// 显示商品标题
		if (zhe != null) {
			KeyWordUtil.setDifferentFontForDetailTitle(this, itemTitle, zhe
					+ "  " + s.getInvTitle(), 0, zhe.length());
		} else {
			itemTitle.setText(s.getInvTitle());
		}
		itemPrice.setText(s.getItemPrice()+"");
		if (s.getRestrictAmount() != null && s.getRestrictAmount() > 0) {
			// 存在限购数量
			restrictAmount.setVisibility(View.VISIBLE);
			restrictAmount.setText(getResources().getString(
					R.string.restrictAmount, s.getRestrictAmount()));
		} else {
			restrictAmount.setVisibility(View.GONE);
		}
		// 邮寄方式
		area.setText(s.getInvAreaNm());
		// 初始化收藏按钮
		if (s.getCollectId() != 0) {
			// 已收藏
			collectionImg.setImageResource(R.drawable.hmm_icon_collect_h);
			isCollection = true;
		} else {
			// 尚未收藏
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
		// 注册接受 1.购物车数量发生变化 2.用户登录成功
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
				// 购物车数量发生变化重新获取购物车数量
				getGoodsNums();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				// 用户登录成功 重新获取商品信息及购物车数量
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
			// 发成过加入购物车操作，通知重新获取购物车数量
			Intent intent = new Intent(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
			intent.putExtra("cartNum", num_shopcart);
			sendBroadcast(intent);
		}
		if(shareWindow !=null)
			shareWindow.dismiss();
		if(pushWindow!=null)
			pushWindow.dismiss();
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

	private class BackListener implements OnClickListener {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#showLoading()
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
	 * @see
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#hideLoading()
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
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#GetGoodsDetailData
	 * (com.kakao.kakaogift.entity.GoodsDetail)
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
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#GetCartNumData
	 * (java .lang.Integer)
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
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#showLoadFaild
	 * (java .lang.String)
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
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#addCollection
	 * (long)
	 */
	@Override
	public void addCollection(long collectId) {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_attention).setEnabled(true);
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
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#cancelCollection
	 * ()
	 */
	@Override
	public void cancelCollection() {
		// TODO Auto-generated method stub
		findViewById(R.id.btn_attention).setEnabled(true);
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
	 * com.kakao.kakaogift.activity.view.gdetail.GoodsDetailView#addToCartWithLogin
	 * ()
	 */
	@Override
	public void addToCartSuccess(ShoppingGoods goods) {
		// 购物车添加成功，显示提示框
		getDaoSession().getShoppingGoodsDao().insertOrReplace(goods);
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
}
