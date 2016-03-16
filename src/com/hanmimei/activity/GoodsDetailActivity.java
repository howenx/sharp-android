package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.astuetz.PagerSlidingTabStrip;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.cpoopc.scrollablelayoutlib.ScrollAbleFragment;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout.OnScrollListener;
import com.hanmimei.R;
import com.hanmimei.activity.fragment.HotFragment;
import com.hanmimei.activity.fragment.ImgFragment;
import com.hanmimei.activity.fragment.ParamsFragment;
import com.hanmimei.activity.listener.SimpleAnimationListener;
import com.hanmimei.adapter.GoodsDetailPagerAdapter;
import com.hanmimei.adapter.TuijianAdapter;
import com.hanmimei.application.HMMApplication;
import com.hanmimei.dao.ShoppingGoodsDao.Properties;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.entity.MainVo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.StockVo;
import com.hanmimei.entity.Tag;
import com.hanmimei.listener.GoodsPageChangeListener;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.PopupWindowUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.hanmimei.view.HorizontalListView;
import com.hanmimei.view.NetworkImageHolderView;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener {

	private TextView itemTitle, itemSrcPrice, itemPrice, area;// 标题、 原价、现价、发货区
	private TextView num_restrictAmount; // 限购数量
	private ImageView img_hide, collectionImg;
	private TextView notice,more_view;

	private BadgeView goodsNumView;// 显示购买数量的控件

	private PopupWindow shareWindow;

	private View back_top;
	private ScrollableLayout mScrollLayout;

	// private User user;
	private int num_shopcart = 0;
	private HMessage msg = null;
	private boolean isCollection = false;
	private boolean isChange = false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "商品详情", R.drawable.hmm_icon_fenxiang,true, null, this);
		setContentView(R.layout.goods_detail_layout);
		findView();
		initGoodsNumView();
		initAnimatorSetValue();
		loadDataByUrl();
		getGoodsNums();
		registerReceivers();
	}

	/**
	 * 初始化所有view
	 * 
	 * @param savedInstanceState
	 */
	private void findView() {

		View view = findViewById(R.id.viewpager_content);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		view.setLayoutParams(lp);
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemSrcPrice = (TextView) findViewById(R.id.itemSrcPrice);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		notice = (TextView) findViewById(R.id.notice);
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

		findViewById(R.id.reload).setOnClickListener(this);
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
	// ===============================网络   请求===================================
	// =========================================================================
	/**
	 * 加载数据
	 */
	private GoodsDetail detail;

	private void loadDataByUrl() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent().getStringExtra("url"), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				detail = DataParser.parserGoodsDetail(result);
				initGoodsDetail();
				getLoading().dismiss();
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				findViewById(R.id.no_net).setVisibility(View.VISIBLE);
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
						findViewById(R.id.no_net).setVisibility(View.GONE);
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
							GoodsDetail detail = DataParser
									.parserGoodsDetail(result);
							if (detail.getMessage().getCode() == 200) {
								if (detail.getCartNum() != null) {
									num_shopcart = detail.getCartNum();
								} else {
									num_shopcart = 0;
								}
								showGoodsNums();
							} else {
								ToastUtils.Toast(getActivity(), detail
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
	// ===============================点击     事件==================================
	// =========================================================================

	@Override
	public void onClick(View arg0) {
		if (detail !=null &&detail.getMain() == null) {
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
		case R.id.btn_cancel:
			window.dismiss();
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
		case R.id.reload:
			loadDataByUrl();
			break;
		case R.id.copy:
			doCopy();
			shareWindow.dismiss();
			break;
		case R.id.more_view:
			showPopupwindow();
			break;
		default:
			break;
		}
	}

	private void doCopy() {
		String code[] = detail.getCurrentStock().getInvUrl().split("detail");
		HMMApplication application = (HMMApplication) getApplication();
		application.setKouling("KAKAO-HMM 复制这条信息,打开👉韩秘美👈即可看到<C>【"
				+ detail.getCurrentStock().getInvTitle() + "】," + code[1]
				+ ",－🔑 M令 🔑");
		ToastUtils.Toast(this, "复制成功，快去粘贴吧");
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
			sendData(goods);
		} else {
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
				.where(Properties.GoodsId.eq(goods.getGoodsId()),Properties.SkuType.eq(goods.getSkuType())
						,Properties.SkuTypeId.eq(goods.getSkuTypeId())).unique();
		if (goods2 == null) {
			goods2 = new ShoppingGoods();
			goods2.setGoodsId(goods.getGoodsId());
			goods2.setGoodsNums(0);
			goods2.setSkuType(goods.getSkuType());
			goods2.setSkuTypeId(goods.getSkuTypeId());
		}
		getLoading().show();
		Http2Utils.doGetRequestTask(this, UrlUtil.SEND_CAR_TO_SERVER_UN
				+ goods2.getGoodsId() + "/" + (goods2.getGoodsNums() + 1),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						HMessage hm = DataParser.paserResultMsg(result);
						if (hm.getCode() == 200) {
							// 购物车添加成功，显示提示框
							// ToastUtils.Toast(GoodsDetailActivity.this,hm.getMessage());
							displayAnimation();
							goods2.setGoodsNums(goods2.getGoodsNums() + 1);
							getDaoSession().getShoppingGoodsDao()
									.insertOrReplace(goods2);
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
				});
	}

	private AnimatorSet set;

	private void initAnimatorSetValue() {
		int translationX = CommonUtil.getScreenWidth(this) * 4 / 11;
		ObjectAnimator animX = ObjectAnimator.ofFloat(img_hide, "translationX",
				0, -translationX);
		ObjectAnimator animY = ObjectAnimator.ofFloat(img_hide, "translationY",
				0, -250, 50);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(img_hide, "scaleX", 1f,
				0.3f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(img_hide, "scaleY", 1f,
				0.3f);
		set = new AnimatorSet();
		set.playTogether(animX, animY, scaleX, scaleY);
		set.setDuration(1200);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new SimpleAnimationListener() {

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
		set.start();
	}

	// 当前的商品
	private StockVo shareStock;

	// 新浪微博分享设置
//	private void shareSina() {
//		new ShareAction(this)
//				.setPlatform(SHARE_MEDIA.SINA)
//				.setCallback(umShareListener)
//				.withMedia(
//						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
//				.withTitle("全球正品，尽在韩秘美").withText(shareStock.getInvTitle())
//				.withTargetUrl("http://www.hanmimei.com/").share();
//	}

	// 微信朋友圈分享设置
	private void shareCircle() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
				.setCallback(umShareListener)
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withTitle(shareStock.getInvTitle())
				.withText(shareStock.getInvTitle())
				.withTargetUrl("http://www.hanmimei.com/").share();
	}

	// 微信分享设置
	private void shareWeiXin() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.WEIXIN)
				.setCallback(umShareListener)
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withTitle("全球正品，尽在韩秘美").withText(shareStock.getInvTitle())
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
				.withText(shareStock.getInvTitle())
				.withTargetUrl("http://www.hanmimei.com/").share();
	}

	// 分享面板
	private void showShareboard() {
		View view = LayoutInflater.from(this).inflate(R.layout.share_layout,
				null);
		shareWindow = PopupWindowUtil.showPopWindow(this, view);
		view.findViewById(R.id.qq).setOnClickListener(this);
		view.findViewById(R.id.weixin).setOnClickListener(this);
		view.findViewById(R.id.weixinq).setOnClickListener(this);
//		view.findViewById(R.id.sina).setOnClickListener(this);
		view.findViewById(R.id.copy).setOnClickListener(this);
		Config.OpenEditor = true;
		shareStock = detail.getCurrentStock();

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
//					sgoods.setPinTieredPriceId(s.getPinTieredPrices());
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

	PopupWindow window = null;

	/**
	 * 弹出显示税费的提醒框
	 */
	private void showPortalFeeInfo() {
		// TODO Auto-generated method stub
		if(window == null){
		View view = getLayoutInflater().inflate(R.layout.panel_portalfee, null);
		TextView num_portalfee = (TextView) view
				.findViewById(R.id.num_portalfee);
		TextView prompt = (TextView) view.findViewById(R.id.prompt);
		Double postalFee = curPostalTaxRate * curItemPrice / 100;

		prompt.setText(getResources().getString(R.string.portalfee_biaozhun,
				curPostalTaxRate, postalStandard));

		if (postalFee <= 50) {
			num_portalfee.setText(getResources().getString(R.string.price,
					CommonUtil.DecimalFormat(postalFee)));
			num_portalfee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			postalFee = curItemPrice * 100 / (100 - curPostalTaxRate)
					- curItemPrice;
			num_portalfee.setText(getResources().getString(R.string.price,
					CommonUtil.DecimalFormat(postalFee)));
		}

		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		window = PopupWindowUtil.showPopWindow(this, view);
		}else{
			window.showAtLocation(itemTitle, Gravity.BOTTOM, 0, 0);
		}
		
	}

	/**
	 * 拼接商品信息
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

	// 收藏商品
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

	private JSONObject object;

	private void toObject() {
		object = new JSONObject();
		try {
			object.put("skuId", detail.getCurrentStock().getId());
			object.put("skuType", detail.getCurrentStock().getSkuType());
			object.put("skuTypeId", detail.getCurrentStock().getSkuTypeId());
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
							detail.getCurrentStock().setCollectId(collectionId);
							collectionImg.setImageDrawable(getResources()
									.getDrawable(R.drawable.icon_collect));
							ToastUtils.Toast(GoodsDetailActivity.this, "收藏成功");
							sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
						} else {
							ToastUtils.Toast(GoodsDetailActivity.this, "收藏失败");
						}
					}
					@Override
					public void onError() {
						ToastUtils.Toast(GoodsDetailActivity.this,
								"收藏失败，请检查您的网络");
					}
				}, object.toString());
	}

	private void delCollection() {
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.DEL_COLLECTION
				+ detail.getCurrentStock().getCollectId(), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				HMessage message = DataParser.paserResultMsg(result);
				if (message.getCode() == 200) {
					isCollection = false;
					detail.getCurrentStock().setCollectId(0);
					collectionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_un_collect));
					ToastUtils.Toast(GoodsDetailActivity.this, "取消收藏成功");
					sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_COLLECTION_ACTION));
				}else{
					ToastUtils.Toast(GoodsDetailActivity.this, "取消收藏失败");
				}
			}
			@Override
			public void onError() {
				ToastUtils.Toast(GoodsDetailActivity.this, "取消收藏失败");
			}
		});
	}
	private PopupWindow tuiWindow;
	private void showPopupwindow() {
		if(tuiWindow == null){
		View view = getLayoutInflater().inflate(R.layout.tuijian_layout, null);
		HorizontalListView more_grid = (HorizontalListView) view.findViewById(R.id.more_grid);
		TextView titleView = (TextView) view.findViewById(R.id.title);
		titleView.setText("该商品已卖完，去看看其他商品吧");
		more_grid.setAdapter(new TuijianAdapter(detail.getPush(), this));
		more_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				if (detail.getPush().get(arg2).getItemType().equals("pin")) {
					intent = new Intent(getActivity(),PingouDetailActivity.class);
				} else {
					intent = new Intent(getActivity(),GoodsDetailActivity.class);
				}
				intent.putExtra("url", detail.getPush().get(arg2).getItemUrl());
				startActivityForResult(intent, 1);
			}
		});
		more_grid.setFocusable(false);
		tuiWindow = PopupWindowUtil.showPopWindow(this, view);
		more_view.setOnClickListener(this);
		}else{
			PopupWindowUtil.backgroundAlpha(this, 0.4f);
			tuiWindow.showAtLocation(more_view, Gravity.BOTTOM, 0, 0);
		}
		
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
		if(detail == null)
			return;
		if (detail.getMessage().getCode() != 200) {
			findViewById(R.id.no_net).setVisibility(View.VISIBLE);
			ToastUtils.Toast(this, detail.getMessage().getMessage());
			return;
		}
		findViewById(R.id.no_net).setVisibility(View.GONE);
		// 主详情
		// 子商品信息
		TextView publicity = (TextView) findViewById(R.id.publicity); // 优惠信息
																		// /购物车数量
		if (detail.getMain() != null) {
			publicity.setText(detail.getMain().getPublicity());
			mScrollLayout.canScroll();
		}
		initGoodsInfo();
//		initShopcartNum();
		initFragmentPager(detail.getMain());
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
		ScrollAbleFragment gridViewFragment = HotFragment.newInstance(detail.getPush());
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

	private int curPostalTaxRate; // 当前商品税率
	private double curItemPrice; // 当前商品价格
	private int postalStandard;// 关税收费标准

	private void initGoodsInfo() {
		if (detail.getStock() == null)
			return;
		StockVo stock = null;
		List<Tag> tags = new ArrayList<Tag>();
		for (StockVo s : detail.getStock()) {
			tags.add(new Tag(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));
			if (s.getOrMasterInv()){
				stock = s;
				if(!s.getState().equals("Y")){
					
					showPopupwindow();
				}
			}
				
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
			}
		});
	}

	private void initShopcartNum() {
		if (detail.getCartNum() != null) {
			num_shopcart = detail.getCartNum();
		}
		showGoodsNums();
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
		if (s.getRestrictAmount() > 0) {
			num_restrictAmount.setVisibility(View.VISIBLE);
			num_restrictAmount.setText(getResources().getString(
					R.string.restrictAmount, s.getRestrictAmount()));
		} else {
			num_restrictAmount.setVisibility(View.GONE);
		}
		ImageLoaderUtils
				.loadImage(this, s.getInvImgForObj().getUrl(), img_hide);
		if (s.getPostalTaxRate() != null)
			curPostalTaxRate = s.getPostalTaxRate();
		curItemPrice = s.getItemPrice().doubleValue();
		postalStandard = s.getPostalStandard();
		area.setText(s.getInvAreaNm());
		if(s.getCollectId() != 0){
			collectionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_collect));
			isCollection = true;
		}else{
			collectionImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_un_collect));
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
		ConvenientBanner<ImgInfo> slider = (ConvenientBanner<ImgInfo>) findViewById(R.id.slider);
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		}, s.getItemPreviewImgsForList()).setPageIndicator(
				new int[] { R.drawable.page_indicator,
						R.drawable.page_indicator_fcoused });
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
		intentFilter
		.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
				msg = null;
				getGoodsNums();
			}else if(intent.getAction().equals(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)){
				loadDataByUrl();
				getGoodsNums();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
		if(isChange)
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
	}


}
