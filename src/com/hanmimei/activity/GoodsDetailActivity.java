package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
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
import com.hanmimei.activity.listener.SimpleAnimationListener;
import com.hanmimei.adapter.GoodsDetailPagerAdapter;
import com.hanmimei.dao.ShoppingGoodsDao.Properties;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.entity.MainVo;
import com.hanmimei.entity.ShareVo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.entity.Tag;
import com.hanmimei.override.ViewPageChangeListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.hanmimei.view.NetworkImageHolderView;
import com.hanmimei.view.PushWindow;
import com.hanmimei.view.ShareWindow;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.umeng.socialize.UMShareAPI;

public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener {

	private TextView itemTitle, itemSrcPrice, itemPrice, area;// 标题、 原价、现价、发货区
	private TextView num_restrictAmount; // 限购数量
	private ImageView img_hide, collectionImg;
	private TextView more_view;

	private BadgeView goodsNumView;// 显示购买数量的控件
	private ConvenientBanner<ImgInfo> slider;

	private ShareWindow shareWindow;
	private AlertDialog postDialog;

	private View back_top;
	private ScrollableLayout mScrollLayout;
	private GoodsDetailPagerAdapter adapter;

	// private User user;
	private int num_shopcart;
	private HMessage msg;
	private boolean isCollection;
	private boolean isChange;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "商品详情",
				R.drawable.hmm_icon_share, true, null, this);
		setContentView(R.layout.goods_detail_layout);
		findView();
		initGoodsNumView();
		initAnimatorSetValue();
		getGoodsNums();
		loadDataByUrl();
		registerReceivers();
	}

	/**
	 * 初始化所有view
	 * 
	 * @param savedInstanceState
	 */
	private void findView() {
		slider = (ConvenientBanner<ImgInfo>) findViewById(R.id.slider);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		slider.setLayoutParams(lp);
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemSrcPrice = (TextView) findViewById(R.id.itemSrcPrice);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		more_view = (TextView) findViewById(R.id.more_view);

		num_restrictAmount = (TextView) findViewById(R.id.restrictAmount);
		area = (TextView) findViewById(R.id.area);
		back_top = findViewById(R.id.back_top);

		img_hide = (ImageView) findViewById(R.id.img_hide);
		mScrollLayout = (ScrollableLayout) findViewById(R.id.mScrollLayout);
		collectionImg = (ImageView) findViewById(R.id.attention);
		findViewById(R.id.btn_attention).setOnClickListener(this);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.btn_shopcart).setOnClickListener(this);
		findViewById(R.id.btn_add_shopcart).setOnClickListener(this);
		findViewById(R.id.btn_portalFee).setOnClickListener(this);
		findViewById(R.id.back_top).setOnClickListener(this);
	}

	/**
	 * 购物车数量view 初始化
	 */
	private void initGoodsNumView() {
		goodsNumView = new BadgeView(this, findViewById(R.id.shopcart));
		goodsNumView.setTextColor(Color.WHITE);
		goodsNumView.setBadgePosition(BadgeView.POSITION_CENTER_HORIZONTAL);
		goodsNumView.setTextSize(10);
		goodsNumView.setBackgroundResource(R.drawable.bg_badgeview);
	}

	private void showGoodsNums() {
		int num = 0;
		if (getUser() == null) {
			List<ShoppingGoods> goods = getDaoSession().getShoppingGoodsDao()
					.queryBuilder().list();
			for (ShoppingGoods sg : goods) {
				num += sg.getGoodsNums();
			}
		} else {
			num = num_shopcart;
		}

		if (num > 0) {
			if (num <= 99) {
				goodsNumView.setText(num + "");
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
	private GoodsDetail detail;

	private void loadDataByUrl() {
		getLoading().show();
		Log.i("detailUrl", getIntent().getStringExtra("url"));
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent()
				.getStringExtra("url"), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				try {
					detail = new Gson().fromJson(result, GoodsDetail.class);
				} catch (Exception e) {
					ToastUtils.Toast(getActivity(), R.string.error);
					getLoading().dismiss();
					return;
				}
				initGoodsDetail();
				getLoading().dismiss();
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}

	/**
	 * 发送商品信息添加到购物车
	 */
	private void sendData(ShoppingGoods goods) {
		JSONArray array = toJSONArray(goods);
		getLoading().show();
		Http2Utils.doPostRequestTask2(this, getHeaders(),
				UrlUtil.GET_CAR_LIST_URL, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						HMessage hm = DataParser.paserResultMsg(result);
						if (hm.getCode() == 200) {
							// 购物车添加成功，显示提示框
							displayAnimation();
							num_shopcart++;
							showGoodsNums();
						} else if (hm.getCode() == 3001 || hm.getCode() == 2001) {
							// 提示添加失败原因
							msg = hm;
							ToastUtils.Toast(getActivity(), hm.getMessage());
						} else {
							ToastUtils.Toast(getActivity(), hm.getMessage());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}, array.toString());
	}

	private void getGoodsNums() {
		if (getUser() == null) {
			showGoodsNums();
		} else {
			Http2Utils.doGetRequestTask(this, getHeaders(),
					UrlUtil.GET_CART_NUM_URL, new VolleyJsonCallback() {

						@Override
						public void onSuccess(String result) {
							GoodsDetail numDetail;
							try {
								numDetail = new Gson().fromJson(result,
										GoodsDetail.class);
							} catch (Exception e) {
								ToastUtils.Toast(getActivity(), R.string.error);
								return;
							}
							if (numDetail == null)
								return;
							if (numDetail.getMessage().getCode() == 200) {
								if (numDetail.getCartNum() != null) {
									num_shopcart = numDetail.getCartNum();
								} else {
									num_shopcart = 0;
								}
								showGoodsNums();
							} else {
								ToastUtils.Toast(getActivity(), numDetail
										.getMessage().getMessage());
							}
						}

						@Override
						public void onError() {
							ToastUtils.Toast(getActivity(), R.string.error);
						}
					});
		}
	}

	// =========================================================================
	// ===============================点击 事件==================================
	// =========================================================================

	@Override
	public void onClick(View arg0) {
		if (detail != null && detail.getMain() == null) {
			ToastUtils.Toast(this, "正在加载数据");
			return;
		}
		switch (arg0.getId()) {
		case R.id.btn_shopcart:
			setResult(AppConstant.CAR_TO_GOODS_CODE);
			startActivity(new Intent(this, ShoppingCarActivity.class));
			break;
		case R.id.btn_portalFee:
			showPortalFeeInfo();
			break;
		case R.id.btn_pay:
			clickPay();
			break;
		case R.id.btn_attention:
			collectGoods();
			break;
		case R.id.btn_add_shopcart:
			addToShoppingCart();
			break;
		case R.id.setting:
			showShareboard();
			break;
		case R.id.back_top:
			mScrollLayout.scrollToTop();
			break;
		case R.id.reload:
			loadDataByUrl();
			break;
		case R.id.more_view:
			showPopupwindow();
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
	private void addToShoppingCart() {
		if (msg != null) {
			ToastUtils.Toast(this, msg.getMessage());
			return;
		}
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
			ToastUtils.Toast(this, "商品已售罄");
			return;
		}

		if (getUser() != null) {
//			登录状态下加入购物车
			sendData(goods);
		} else {
//			未登录状态下加入购物车
			addShoppingCartCheck(goods);
		}
		isChange = true;
	}

	/**
	 * 加入购物车验证
	 * 
	 * @author vince
	 */
	ShoppingGoods goods2;

	private void addShoppingCartCheck(ShoppingGoods goods) {
		goods2 = getDaoSession().getShoppingGoodsDao().queryBuilder()
				.where(Properties.GoodsId.eq(goods.getGoodsId()),
						Properties.SkuType.eq(goods.getSkuType()),
						Properties.SkuTypeId.eq(goods.getSkuTypeId())).unique();
		if (goods2 == null || goods2.getId() == null) {
			goods2 = new ShoppingGoods();
			goods2.setGoodsId(goods.getGoodsId());
			goods2.setGoodsNums(0);
			goods2.setSkuType(goods.getSkuType());
			goods2.setSkuTypeId(goods.getSkuTypeId());
		}
		getLoading().show();
		Http2Utils.doPostRequestTask2(this, UrlUtil.POST_ADD_CART,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						HMessage hm = DataParser.paserResultMsg(result);
						if (hm.getCode() == 200) {
							// 购物车添加成功，显示提示框
							// ToastUtils.Toast(GoodsDetailActivity.this,hm.getMessage());
							displayAnimation();
							goods2.setGoodsNums(goods2.getGoodsNums()+1);
							getDaoSession().getShoppingGoodsDao().insertOrReplace(goods2);
							showGoodsNums();
						} else if (hm.getCode() == 3001 || hm.getCode() == 2001) {
							// 提示添加失败原因
							msg = hm;
							ToastUtils.Toast(getActivity(), hm.getMessage());
						} else {
							ToastUtils.Toast(getActivity(), hm.getMessage());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				},toJSONObject(goods2).toString());
	}

	private ObjectAnimator objectAnimator;

	private void initAnimatorSetValue() {
		int translationX = CommonUtil.getScreenWidth(this) * 4 / 11;

		PropertyValuesHolder pvhSX = PropertyValuesHolder.ofFloat("scaleX", 1f,
				0.3f);
		PropertyValuesHolder pvhSY = PropertyValuesHolder.ofFloat("scaleY", 1f,
				0.3f);
		PropertyValuesHolder pvhTY = PropertyValuesHolder.ofFloat(
				"translationY", 0, -250, 50);
		PropertyValuesHolder pvhTX = PropertyValuesHolder.ofFloat(
				"translationX", 0, -translationX);
		objectAnimator = ObjectAnimator.ofPropertyValuesHolder(img_hide, pvhSX,
				pvhSY, pvhTY, pvhTX).setDuration(1200);
		objectAnimator.setInterpolator(new DecelerateInterpolator());
		objectAnimator.addListener(new SimpleAnimationListener() {

			@Override
			public void onAnimationStart(Animator arg0) {
				img_hide.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				img_hide.setVisibility(View.GONE);
			}
		});

	}

	private void displayAnimation() {
		objectAnimator.start();
	}

	// 当前的商品
	private StockVo shareStock;

	// 分享面板
	private void showShareboard() {

		if (shareWindow == null) {
			shareStock = detail.getCurrentStock();
			ShareVo vo = new ShareVo();
			vo.setContent(shareStock.getInvTitle());
			vo.setTitle("全球正品，尽在韩秘美");
			vo.setImgUrl(shareStock.getInvImgForObj().getUrl());
			vo.setTargetUrl("http://www.hanmimei.com/");
			vo.setInfoUrl(shareStock.getInvUrl());
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
					ToastUtils.Toast(this, "商品已售罄");
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
	private void showPortalFeeInfo() {
		// TODO Auto-generated method stub
		if (postDialog == null) {
			postDialog = AlertDialogUtils.showPostDialog(this, curItemPrice,
					curPostalTaxRate, postalStandard);
		} else {
			postDialog.show();
		}

	}

	/**
	 * 登录状态下拼接商品信息
	 */
	private JSONArray toJSONArray(ShoppingGoods goods) {
		JSONArray array = null;
		try {
			array = new JSONArray();
			JSONObject object = new JSONObject();
			object.put("cartId", 0);
			object.put("skuId", goods.getGoodsId());
			object.put("amount", goods.getGoodsNums());
			object.put("state", goods.getState());
			object.put("skuType", goods.getSkuType());
			object.put("skuTypeId", goods.getSkuTypeId());
			array.put(object);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}
	/**
	 * 未登录状态下拼接商品信息
	 */
	private JSONObject toJSONObject(ShoppingGoods goods) {
			JSONObject object = new JSONObject();
			try {
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums()+1);
				object.put("skuType", goods.getSkuType());
				object.put("skuTypeId", goods.getSkuTypeId());
			} catch (JSONException e) {
			}
			
		return object;
	}

	// 收藏商品
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

	private String getCollectedGoodsInfo() {
		JSONObject object = new JSONObject();
		try {
			object.put("skuId", detail.getCurrentStock().getId());
			object.put("skuType", detail.getCurrentStock().getSkuType());
			object.put("skuTypeId", detail.getCurrentStock().getSkuTypeId());
		} catch (JSONException e) {
		}
		return object.toString();
	}

	// 添加收藏
	private void addCollection(String collected) {
		getLoading().show();
		Http2Utils.doRequestTask2(this, Method.POST, getHeaders(),
				UrlUtil.ADD_COLLECTION, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						findViewById(R.id.btn_attention).setOnClickListener(
								GoodsDetailActivity.this);
						HMessage message = DataParser.paserResultMsg(result);
						int collectionId = DataParser.parserCollectId(result);
						if (message.getCode() == 200) {
							isCollection = true;
							detail.getCurrentStock().setCollectId(collectionId);
							collectionImg
									.setImageResource(R.drawable.hmm_icon_collect_h);
							sendBroadcast(new Intent(
									AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
						} else {
							ToastUtils.Toast(GoodsDetailActivity.this, "收藏失败");
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						findViewById(R.id.btn_attention).setOnClickListener(
								GoodsDetailActivity.this);
						ToastUtils.Toast(GoodsDetailActivity.this,
								"收藏失败，请检查您的网络");
					}
				}, collected);
	}

	private void delCollection() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.DEL_COLLECTION
				+ detail.getCurrentStock().getCollectId(),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						findViewById(R.id.btn_attention).setOnClickListener(
								GoodsDetailActivity.this);
						getLoading().dismiss();
						HMessage message = DataParser.paserResultMsg(result);
						if (message.getCode() == 200) {
							isCollection = false;
							detail.getCurrentStock().setCollectId(0);
							collectionImg
									.setImageResource(R.drawable.hmm_icon_collect);
							sendBroadcast(new Intent(
									AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
						} else {
							ToastUtils
									.Toast(GoodsDetailActivity.this, "取消收藏失败");
						}
					}

					@Override
					public void onError() {
						findViewById(R.id.btn_attention).setOnClickListener(
								GoodsDetailActivity.this);
						getLoading().dismiss();
						ToastUtils.Toast(GoodsDetailActivity.this, "取消收藏失败");
					}
				});
	}

	private PushWindow pushWindow;

	private void showPopupwindow() {
		if (pushWindow == null) {
			pushWindow = new PushWindow(this, detail.getPush());
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
		initFragmentPager(detail.getMain());
		mScrollLayout.canScroll();
	}

	private void initFragmentPager(MainVo main) {
		if (main == null)
			return;
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
		if (adapter == null) {
			adapter = new GoodsDetailPagerAdapter(getSupportFragmentManager(),
					fragments, titles);
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
					if (currentY <= 1
							&& back_top.getVisibility() == View.VISIBLE) {
						back_top.setVisibility(View.GONE);
					}
					setScrollDown(currentY);
				}
			});
			pagerSlidingTabStrip
					.setOnPageChangeListener(new ViewPageChangeListener() {

						@Override
						public void onPageSelected(int i) {
							/** 标注当前页面 **/
							mScrollLayout.getHelper()
									.setCurrentScrollableContainer(
											fragments.get(i));
							viewPager.setCurrentItem(i);
						}
					});
		}
	}

	private int curPostalTaxRate; // 当前商品税率
	private double curItemPrice; // 当前商品价格
	private int postalStandard;// 关税收费标准

	private void initGoodsInfo() {
		if (detail.getStock() == null)
			return;
		StockVo stock = null;
		List<Tag> tags = new ArrayList<Tag>();
		boolean outDate = false;
		for (StockVo s : detail.getStock()) {
			tags.add(new Tag(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));
			if (s.getOrMasterInv())
				stock = s;
			if (!s.getState().equals("Y"))
				outDate = true;
		}
		if (outDate) {
			more_view.setVisibility(View.VISIBLE);
			more_view.setOnClickListener(this);
			showPopupwindow();
		}
		initStocks(stock);
		initTags(tags);
	}

	private void initTags(List<Tag> tags) {
		if (tags.size() <= 0)
			return;
		TagCloudView tagCloudView = (TagCloudView) findViewById(R.id.tagCloudView);// 规格标签控件
		// 初始化规格显示
		tagCloudView.removeAllViews();
		tagCloudView.setTags(tags);
		// 规格标签的点击事件
		tagCloudView.setOnTagClickListener(new OnTagClickListener() {
			@Override
			public void onTagClick(int oldPostion, int position, Tag tag) {
				detail.getStock().get(oldPostion).setOrMasterInv(false);
				detail.getStock().get(position).setOrMasterInv(true);
				initStocks(detail.getStock().get(position));
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
		GlideLoaderUtils.loadGoodsImage(getActivity(), s.getInvImgForObj()
				.getUrl(), img_hide);
		if (s.getPostalTaxRate() != null)
			curPostalTaxRate = s.getPostalTaxRate();
		curItemPrice = s.getItemPrice().doubleValue();
		postalStandard = s.getPostalStandard();
		area.setText(s.getInvAreaNm());
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
	@SuppressWarnings("unchecked")
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
				msg = null;
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
		if (isChange)
			sendBroadcast(new Intent(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
	}

}
