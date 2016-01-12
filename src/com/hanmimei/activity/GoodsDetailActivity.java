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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.hanmimei.R;
import com.hanmimei.activity.listener.SimpleAnimationListener;
import com.hanmimei.adapter.GoodsDetailParamAdapter;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.dao.ShoppingGoodsDao.Properties;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.GoodsDetail.Main;
import com.hanmimei.entity.GoodsDetail.Stock;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.Tag;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.PopupWindowUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.hanmimei.view.CustomScrollView;
import com.hanmimei.view.NetworkImageHolderView;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, CustomScrollView.OnScrollUpListener,
		RadioGroup.OnCheckedChangeListener {

	private ConvenientBanner<ImgInfo> slider; // 轮播图控件
	private TextView discount, itemTitle, itemSrcPrice, itemPrice, area; // 商品折扣
																			// 标题
																			// 原价
																			// 现价
																			// 发货区
	private TextView num_restrictAmount;
	private TagCloudView tagCloudView; // 规格标签控件
	private TextView publicity; // 优惠信息 /购物车数量
	private CustomScrollView mScrollView; //
	private ListView content_params, content_hot; // 商品参数／热卖商品
	private RadioGroup indicator_hide, indicator; // 顶部导航栏 中部导航栏
	private ImageView img_hide, shopcart;

	private View pager_header, back_top;

	private BadgeView goodsNumView;// 显示购买数量的控件

	private PopupWindow shareWindow;
	private ImageView btn_collect;

	private User user;
	private ShoppingGoodsDao goodsDao;
	private Main main; // 主信息
	private List<Stock> stocks; // 子商品信息
	private List<Tag> tags; // 规格标签信息

	private int num_shopcart = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "商品详情", R.drawable.fenxiang,
				true, this, this);
		setContentView(R.layout.goods_detail_layout);
		findView();
		initGoodsNumView();
		initAnimatorSetValue();
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
		View view = findViewById(R.id.viewpager_content);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		view.setLayoutParams(lp);
		itemTitle = (TextView) findViewById(R.id.itemTitle);
		itemSrcPrice = (TextView) findViewById(R.id.itemSrcPrice);
		itemPrice = (TextView) findViewById(R.id.itemPrice);
		tagCloudView = (TagCloudView) findViewById(R.id.tagCloudView);
		publicity = (TextView) findViewById(R.id.publicity);
		num_restrictAmount = (TextView) findViewById(R.id.restrictAmount);
		indicator_hide = (RadioGroup) findViewById(R.id.indicator_hide);
		indicator = (RadioGroup) findViewById(R.id.indicator);
		discount = (TextView) findViewById(R.id.discount);
		area = (TextView) findViewById(R.id.area);
		mScrollView = (CustomScrollView) findViewById(R.id.mScrollView);
		mScrollView.setOnScrollUpListener(this);
		pager_header = findViewById(R.id.pager_header);
		shopcart = (ImageView) findViewById(R.id.shopcart);

		img_hide = (ImageView) findViewById(R.id.img_hide);
		back_top = findViewById(R.id.back_top);
		btn_collect = (ImageView) findViewById(R.id.btn_collect);

		content_params = (ListView) findViewById(R.id.content_params);
		FrameLayout.LayoutParams lpm = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenHeight(this) - getResources().getDimensionPixelSize(R.dimen.actionbar_size)-indicator.getMeasuredHeight()
						-getResources().getDimensionPixelSize(R.dimen.navigationbar_size)
						- CommonUtil.getStatusBarHeight(this));
		content_params.setLayoutParams(lpm);
		content_params.setFocusable(false);
		content_hot = (ListView) findViewById(R.id.content_hot);
		content_hot.setLayoutParams(lpm);
		content_hot.setFocusable(false);

		user = getUser();
		goodsDao = getDaoSession().getShoppingGoodsDao();

		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.btn_shopcart).setOnClickListener(this);
		shopcart.setOnClickListener(this);
		findViewById(R.id.btn_portalFee).setOnClickListener(this);
		findViewById(R.id.back_top).setOnClickListener(this);
		findViewById(R.id.reload).setOnClickListener(this);
		indicator.setOnCheckedChangeListener(this);
		btn_collect.setOnClickListener(this);

	}

	private void initGoodsNumView() {
		goodsNumView = new BadgeView(this, shopcart);
		goodsNumView.setTextColor(Color.WHITE);
		goodsNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		goodsNumView.setTextSize(10);
		goodsNumView.setBackgroundResource(R.drawable.bg_badgeview);
	}

	private void showGoodsNums() {
		int num = 0;
		if (getUser() == null) {
			List<ShoppingGoods> goods = goodsDao.queryBuilder().list();
			for (ShoppingGoods sg : goods) {
				num += sg.getGoodsNums();
			}
		} else {
			num = num_shopcart;
		}

		if (num > 0) {
			if(num <=99){
				goodsNumView.setText(num + "");
			}else{
				goodsNumView.setText("...");
			}
			goodsNumView.show(true);
		} else {
			goodsNumView.hide(true);
		}

	}

	// =========================================================================
	// ===============================网络 请求==================================
	// =========================================================================
	/**
	 * 加载数据
	 */
	private GoodsDetail detail;

	private void loadDataByUrl() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent()
				.getStringExtra("url"), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				detail = DataParser.parserGoodsDetail(result);
				initGoodsDetail(detail);
			}

			@Override
			public void onError() {
				getLoading().dismiss();
				findViewById(R.id.no_net).setVisibility(View.VISIBLE);
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}

	private HMessage msg;

	/**
	 * 发送商品信息添加到购物车
	 */
	private void sendData(ShoppingGoods goods) {
		if (msg != null) {
			ToastUtils.Toast(getActivity(), msg.getMessage());
			return;
		}
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
//							ToastUtils.Toast(GoodsDetailActivity.this,hm.getMessage());
							num_shopcart++;
							showGoodsNums();
						} else if (hm.getCode() == 3001 || hm.getCode() == 2001) {
							// 提示添加失败原因
							msg = hm;
							ToastUtils.Toast(getActivity(), msg.getMessage());
						} else {
							ToastUtils.Toast(getActivity(), msg.getMessage());
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
			msg = null;
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
								ToastUtils.Toast(getActivity(),
										msg.getMessage());
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
	// ===============================点击事件==================================
	// =========================================================================

	@Override
	public void onClick(View arg0) {
		if (main == null) {
			ToastUtils.Toast(this, "正在加载数据");
			return;
		}
		switch (arg0.getId()) {
		case R.id.shopcart:
			setResult(AppConstant.CAR_TO_GOODS_CODE);
			startActivity(new Intent(this, ShoppingCarActivity.class));
			break;
		case R.id.btn_portalFee:
			showPortalFeeInfo();
			break;
		case R.id.btn_pay:
			clickPay();
			break;
		case R.id.btn_collect:
			collectGoods();
			break;
		case R.id.btn_shopcart:
			addToShoppingCart();
			break;
		case R.id.setting:
			showShareboard();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.back_top:
			mScrollView.fullScroll(ScrollView.FOCUS_UP);
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
		case R.id.sina:
			// shareSina();
			Toast.makeText(this, "新浪微博分享，等待微博审核才可以分享", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.reload:
			loadDataByUrl();
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
		ShoppingGoods goods = null;
		for (Stock stock : stocks) {
			if (stock.getOrMasterInv() && stock.getState().equals("Y")) {
				goods = new ShoppingGoods();
				goods.setGoodsId(stock.getId());
				goods.setGoodsNums(1);
				break;
			}
		}
		if (goods == null) {
			ToastUtils.Toast(this, "请选择商品");
			return;
		}
		
		if (getUser() != null) {
			sendData(goods);
		} else {
			addShoppingCartCheck(goods);
		}
	}
	/**
	 * 加入购物车验证
	 * @author vince
	 */
	ShoppingGoods goods2;
	private void addShoppingCartCheck(ShoppingGoods goods) {
		 goods2 = goodsDao.queryBuilder().where(Properties.GoodsId.eq(goods.getGoodsId())).unique();
		if(goods2 == null){
			goods2 = new ShoppingGoods();
			goods2.setGoodsId(goods.getGoodsId());
			goods2.setGoodsNums(0);
		}
		getLoading().show();
		Http2Utils.doGetRequestTask(this, UrlUtil.SEND_CAR_TO_SERVER_UN
				+ goods2.getGoodsId() + "/" + (goods2.getGoodsNums()+1),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						HMessage hm = DataParser.paserResultMsg(result);
						if (hm.getCode() == 200) {
							// 购物车添加成功，显示提示框
//							ToastUtils.Toast(GoodsDetailActivity.this,hm.getMessage());
							displayAnimation();
							goods2.setGoodsNums(goods2.getGoodsNums()+1);
							goodsDao.insertOrReplace(goods2);
							showGoodsNums();
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
	private void initAnimatorSetValue(){
		int translationX  = CommonUtil.getScreenWidth(this)*4/11;
		ObjectAnimator animX = ObjectAnimator.ofFloat(img_hide, "translationX", 0, -translationX);
		ObjectAnimator animY = ObjectAnimator.ofFloat(img_hide, "translationY", 0,-250,50);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(img_hide, "scaleX", 1f, 0.3f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(img_hide, "scaleY", 1f, 0.3f);
		set = new AnimatorSet();
		set.playTogether(animX,animY,scaleX,scaleY);
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
	private Stock shareStock;

	// 新浪微博分享设置
	private void shareSina() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.SINA)
				.setCallback(umShareListener)
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withTitle("全球正品，尽在韩秘美").withText(shareStock.getInvTitle())
				.withTargetUrl("http://www.hanmimei.com/").share();
	}

	// 微信朋友圈分享设置
	private void shareCircle() {
		new ShareAction(this)
				.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
				.setCallback(umShareListener)
				.withMedia(
						new UMImage(this, shareStock.getInvImgForObj().getUrl()))
				.withTitle(shareStock.getInvTitle())
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
		view.findViewById(R.id.sina).setOnClickListener(this);
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
		for (Stock s : stocks) {
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
		startActivity(intent);
	}

	PopupWindow window = null;

	/**
	 * 弹出显示税费的提醒框
	 */
	private void showPortalFeeInfo() {
		// TODO Auto-generated method stub
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
			array.put(object);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return array;
	}

	private void collectGoods() {
		if (getUser() == null) {
			startActivity(new Intent(this, LoginActivity.class));
		} else {

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
	private void initGoodsDetail(GoodsDetail detail) {
		if (detail.getMessage().getCode() != 200) {
			findViewById(R.id.no_net).setVisibility(View.VISIBLE);
			ToastUtils.Toast(this, detail.getMessage().getMessage());
			return;
		}
		findViewById(R.id.no_net).setVisibility(View.GONE);
		// 主详情
		main = detail.getMain();
		// 子商品信息
		stocks = detail.getStock();

		publicity.setText(main.getPublicity());
		tags = new ArrayList<Tag>();
		// 初始化子商品信息 （-1表示默认选中位置 ）
		initGoodsInfo();
		// 初始化规格显示
		tagCloudView.removeAllViews();
		tagCloudView.setTags(tags);
		// 规格标签的点击事件
		tagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int oldPostion, int position, Tag tag) {
				stocks.get(oldPostion).setOrMasterInv(false);
				stocks.get(position).setOrMasterInv(true);
				initStocks(stocks.get(position));
			}
		});

		// 初始化选项卡显示内容
		WebView mWebView = (WebView) findViewById(R.id.mWebView);
		mWebView.loadData(main.getItemDetailImgss(), "text/html", "UTF-8");
		mWebView.setFocusable(false);
		TextView notice = (TextView) findViewById(R.id.notice);
		if (main.getItemNotice() != null) {
			notice.setVisibility(View.VISIBLE);
			notice.setText(main.getItemNotice());
		}
		content_params.setAdapter(new GoodsDetailParamAdapter(main
				.getItemFeatures(), this));
		content_hot.setAdapter(new GoodsDetailParamAdapter(main
				.getItemFeatures(), this));
		initShopcartNum();

	}

	private int curPostalTaxRate; // 当前商品税率
	private double curItemPrice; // 当前商品价格
	private int postalStandard;// 关税收费标准

	private void initGoodsInfo() {
		Stock stock = null;
		for (Stock s : stocks) {
			tags.add(new Tag(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));
			if (s.getOrMasterInv())
				stock = s;
		}
		initStocks(stock);
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

	private void initStocks(Stock s) {
		initSliderImage(s);
		if (s.getItemDiscount().floatValue() > 0) {
			discount.setText("[" + s.getItemDiscount() + "折]");
			itemSrcPrice.setText(getResources().getString(R.string.price,
					s.getItemSrcPrice()));
			itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}
		itemTitle.setText(s.getInvTitle());
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
		curPostalTaxRate = s.getPostalTaxRate();
		curItemPrice = s.getItemPrice().doubleValue();
		postalStandard = s.getPostalStandard();
		area.setText(s.getInvAreaNm());
	}

	/**
	 * 初始化轮播图
	 * 
	 * @param s
	 *            当前选中子商品
	 */
	private void initSliderImage(Stock s) {
		ArrayList<ImgInfo> networkImages = new ArrayList<ImgInfo>(
				s.getItemPreviewImgsForList());
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
			@Override
			public NetworkImageHolderView createHolder() {
				return new NetworkImageHolderView();
			}
		}, networkImages).setPageIndicator(
				new int[] { R.drawable.page_indicator,
						R.drawable.page_indicator_fcoused });
	}

	/**
	 * 选项卡选中改变事件
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.guid1:
		case R.id.guid1_hide:
			findViewById(R.id.content_img).setVisibility(View.VISIBLE);
			content_params.setVisibility(View.GONE);
			content_hot.setVisibility(View.GONE);
			if (indicator_hide.getVisibility() == View.GONE) {
				indicator_hide.check(R.id.guid1_hide);
			} else {
				indicator.check(R.id.guid1);
			}
			break;
		case R.id.guid2:
		case R.id.guid2_hide:
			findViewById(R.id.content_img).setVisibility(View.GONE);
			content_params.setVisibility(View.VISIBLE);
			content_hot.setVisibility(View.GONE);
			if (indicator_hide.getVisibility() == View.GONE) {
				indicator_hide.check(R.id.guid2_hide);
			} else {
				indicator.check(R.id.guid2);
				content_params.requestFocusFromTouch();
				content_params.setSelection(0);
			}
			break;
		case R.id.guid3:
		case R.id.guid3_hide:
			findViewById(R.id.content_img).setVisibility(View.GONE);
			content_params.setVisibility(View.GONE);
			content_hot.setVisibility(View.VISIBLE);
			if (indicator_hide.getVisibility() == View.GONE) {
				indicator_hide.check(R.id.guid3_hide);
			} else {
				indicator.check(R.id.guid3);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onScroll(int scrollY, boolean scrollDirection) {
		// 如果滚动值
		if (scrollY >= pager_header.getMeasuredHeight()
				&& indicator_hide.getVisibility() == View.GONE) {
			indicator_hide.setVisibility(View.VISIBLE);
			indicator.setOnCheckedChangeListener(null);
			indicator_hide.setOnCheckedChangeListener(this);
			return;
		}

		if (scrollY < pager_header.getMeasuredHeight()
				&& indicator_hide.getVisibility() == View.VISIBLE) {
			indicator_hide.setVisibility(View.GONE);
			indicator.setOnCheckedChangeListener(this);
			indicator_hide.setOnCheckedChangeListener(null);
			return;
		}

		if (scrollY > 0 && back_top.getVisibility() == View.GONE) {
			back_top.setVisibility(View.VISIBLE);
		}

		if (scrollY <= 0 && back_top.getVisibility() == View.VISIBLE) {
			back_top.setVisibility(View.GONE);
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
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_ADD_CAR);
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
				getGoodsNums();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
		sendBroadcast(new Intent(
				AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("GoodsDetailActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		ToastUtils.cancel();
		MobclickAgent.onPageEnd("GoodsDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
														// onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

}
