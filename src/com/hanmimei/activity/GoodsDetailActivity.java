package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.hanmimei.R;
import com.hanmimei.adapter.GoodsDetailParamAdapter;
import com.hanmimei.dao.ShoppingGoodsDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.GoodsDetail;
import com.hanmimei.entity.GoodsDetail.Main;
import com.hanmimei.entity.GoodsDetail.Stock;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.Tag;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.PopupWindowUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CustomScrollView;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, CustomScrollView.OnScrollUpListener,
		RadioGroup.OnCheckedChangeListener {

	private SliderLayout slider; // 轮播图控件
	private TextView discount, itemTitle, itemSrcPrice, itemPrice, area; // 商品折扣
																			// 标题
																			// 原价
																			// 现价
																			// 发货区
	private TextView num_restrictAmount;
	private TagCloudView tagCloudView; // 规格标签控件
	private TextView publicity; // 优惠信息
	private CustomScrollView mScrollView; //
	private ListView content_params, content_hot; // 商品参数／热卖商品
	private RadioGroup indicator_hide, indicator; // 顶部导航栏 中部导航栏

	private View pager_header;

	private User user;
	private ShoppingGoodsDao goodsDao;
	private Main main; // 主信息
	private List<Stock> stocks; // 子商品信息
	private List<Tag> tags; // 规格标签信息

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "商品详情", R.drawable.fenxiang,
				true, this);
		setContentView(R.layout.goods_detail_layout);
		findView(arg0);
		loadDataByUrl();
		// registerReceivers();
	}

	private void findView(Bundle savedInstanceState) {

		slider = (SliderLayout) findViewById(R.id.slider);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenWidth(this));
		slider.setLayoutParams(lp);
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

		content_params = (ListView) findViewById(R.id.content_params);
		content_hot = (ListView) findViewById(R.id.content_hot);

		user = getUser();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		goods = new ShoppingGoods();
		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.btn_shopcart).setOnClickListener(this);
		findViewById(R.id.like).setOnClickListener(this);
		findViewById(R.id.car).setOnClickListener(this);
		findViewById(R.id.btn_portalFee).setOnClickListener(this);

		indicator.setOnCheckedChangeListener(this);

	}

	/**
	 * 加载数据
	 * 
	 */
	private void loadDataByUrl() {
		Http2Utils.doGetRequestTask(this, getIntent().getStringExtra("url"),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						GoodsDetail detail = DataParser
								.parserGoodsDetail(result);
						initGoodsDetail(detail);
					}

					@Override
					public void onError() {
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.car:
			setResult(AppConstant.CAR_TO_GOODS_CODE);
			startActivity(new Intent(this, ShoppingCarActivity.class));
			break;
		case R.id.btn_portalFee:
			showPortalFeeInfo();
			break;
		case R.id.btn_pay:
			if (getUser() == null) {
				startActivity(new Intent(this, LoginActivity.class));
				sendBroadcast(new Intent(
						AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
				return;
			}
			ShoppingCar car = new ShoppingCar();
			List<Customs> list = new ArrayList<Customs>();
			Customs customs = new Customs();

			ShoppingGoods sgoods = null;
			for (Stock s : stocks) {
				if (s.getOrMasterInv()) {
					sgoods = new ShoppingGoods();
					sgoods.setGoodsId(s.getId());
					sgoods.setGoodsImg(s.getInvImg());
					sgoods.setGoodsName(s.getInvTitle());
					sgoods.setGoodsNums(1);
					sgoods.setGoodsPrice(s.getItemPrice());
					sgoods.setInvArea(s.getInvArea());
					sgoods.setInvCustoms(s.getInvCustoms());
					sgoods.setPostalTaxRate(s.getPostalTaxRate());
					sgoods.setPostalStandard(s.getPostalStandard());
				}
			}
			customs.addShoppingGoods(sgoods);
			customs.setInvArea(sgoods.getInvArea());
			customs.setInvCustoms(sgoods.getInvCustoms());
			list.add(customs);
			car.setList(list);
			Intent intent = new Intent(this, GoodsBalanceActivity.class);
			intent.putExtra("car", car);
			startActivity(intent);
			break;

		case R.id.btn_shopcart:
			for (int i = 0; i < stocks.size(); i++) {
				Stock stock = stocks.get(i);
				if (stock.getOrMasterInv()) {
					goods.setGoodsId(stock.getId());
					goods.setGoodsNums(1);
					goods.setState("I");
				}
			}
			if (user != null) {
				sendData();
			} else {
				goodsDao.insert(goods);
				ToastUtils.Toast(this);
				sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_ADD_CAR));
			}
			break;
		case R.id.setting:

			break;
		case R.id.like:
			ToastUtils.Toast(this, "您点击了收藏");
			break;
		case R.id.btn_cancel:
			window.dismiss();
			break;
		default:
			break;
		}
	}

	PopupWindow window = null;

	private void showPortalFeeInfo() {
		// TODO Auto-generated method stub
		View view = getLayoutInflater().inflate(R.layout.panel_portalfee, null);
		TextView num_portalfee = (TextView) view
				.findViewById(R.id.num_portalfee);
		TextView prompt = (TextView) view.findViewById(R.id.prompt);
		Double postalFee = curPostalTaxRate * curItemPrice / 100;
		num_portalfee.setText(getResources().getString(R.string.price,
				CommonUtil.DecimalFormat(postalFee)));
		prompt.setText(getResources().getString(R.string.portalfee_biaozhun,
				curPostalTaxRate, postalStandard));
		if (postalFee <= 50)
			num_portalfee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		window = PopupWindowUtil.showPopWindow(this, view);
	}

	private ShoppingGoods goods;

	private void sendData() {
		toObject();
		submitTask(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL, array,
						"id-token", user.getToken());
				HMessage hm = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hm;
				mHandler.sendMessage(msg);
			}
		});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				HMessage hm = (HMessage) msg.obj;
				if (hm.getCode() == 200) {
					ToastUtils.Toast(GoodsDetailActivity.this);
					sendBroadcast(new Intent(
							AppConstant.MESSAGE_BROADCAST_ADD_CAR));
				} else {
					Toast.makeText(GoodsDetailActivity.this, hm.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	};

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

	/**
	 * 初始化显示商品信息
	 * 
	 * @param detail
	 *            商品总详情数据
	 */
	private void initGoodsDetail(GoodsDetail detail) {
		if (detail.getMessage().getCode() != 200) {
			ToastUtils.Toast(this, detail.getMessage().getMessage());
			return;
		}
		// 主详情
		main = detail.getMain();
		// 子商品信息
		stocks = detail.getStock();

		publicity.setText(main.getPublicity());
		tags = new ArrayList<Tag>();
		// 初始化子商品信息 （-1表示默认选中位置 ）
		initStocks(-1);
		// 初始化规格显示
		tagCloudView.setTags(tags);
		// 规格标签的点击事件
		tagCloudView.setOnTagClickListener(new OnTagClickListener() {

			@Override
			public void onTagClick(int oldPostion, int position, Tag tag) {
				// TODO Auto-generated method stub
				stocks.get(oldPostion).setOrMasterInv(false);
				stocks.get(position).setOrMasterInv(true);
				initStocks(position);
			}
		});

		// 初始化选项卡显示内容
		WebView mWebView = (WebView) findViewById(R.id.mWebView);
		mWebView.loadData(main.getItemDetailImgss(), "text/html", "UTF-8");
		mWebView.setFocusable(false);
		TextView notice = (TextView) findViewById(R.id.notice);
		if (main.getItemNotice() != null) {
			notice.setText(main.getItemNotice());
		}
		content_params.setAdapter(new GoodsDetailParamAdapter(main
				.getItemFeatures(), this));
		content_hot.setAdapter(new GoodsDetailParamAdapter(main
				.getItemFeatures(), this));
	}

	private int curPostalTaxRate; // 当前商品税率
	private double curItemPrice; // 当前商品价格
	private int postalStandard;// 关税收费标准

	/**
	 * 初始化子商品信息
	 * 
	 * @param position
	 *            选中商品位置
	 */
	private void initStocks(int position) {
		for (int index = 0; index < stocks.size(); index++) {
			Stock s = stocks.get(index);
			tags.add(new Tag(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));

			if (s.getOrMasterInv()) {
				initSliderImage(s);
				discount.setText("[" + s.getItemDiscount() + "折]");
				itemTitle.setText(s.getInvTitle());
				itemSrcPrice.setText(getResources().getString(R.string.price,
						s.getItemSrcPrice()));
				itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				itemPrice.setText(getResources().getString(R.string.price,
						s.getItemPrice()));
				if (s.getRestrictAmount() > 0) {
					num_restrictAmount.setVisibility(View.VISIBLE);
					num_restrictAmount.setText(getResources().getString(
							R.string.restrictAmount, s.getRestrictAmount()));
				} else {
					num_restrictAmount.setVisibility(View.GONE);
				}

				curPostalTaxRate = s.getPostalTaxRate();
				curItemPrice = s.getItemPrice();
				postalStandard = s.getPostalStandard();
				area.setText(s.getInvArea());
			}
		}
	}

	/**
	 * 初始化轮播图
	 * 
	 * @param s
	 *            当前选中子商品
	 */
	private void initSliderImage(Stock s) {
		List<DefaultSliderView> imageContent = new ArrayList<DefaultSliderView>();
		for (String url : s.getItemPreviewImgs()) {
			DefaultSliderView defaultSliderView = new DefaultSliderView(this);
			defaultSliderView.image(url)
					.setScaleType(BaseSliderView.ScaleType.Fit)
					.setOnSliderClickListener(new OnSliderClickListener() {

						@Override
						public void onSliderClick(BaseSliderView slider) {
							// 轮播图的点击事件
							
						}
					});
			// add your extra information
			defaultSliderView.getBundle().putString("extra", s.getInvUrl());
			imageContent.add(defaultSliderView);
		}
		slider.setAdapter(imageContent);
		slider.startAutoCycle();
	}

	/**
	 * 选项卡选中改变事件
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
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
				indicator_hide.setVisibility(View.GONE);
				indicator.setOnCheckedChangeListener(this);
				indicator_hide.setOnCheckedChangeListener(null);
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
				indicator_hide.setVisibility(View.GONE);
				indicator.setOnCheckedChangeListener(this);
				indicator_hide.setOnCheckedChangeListener(null);
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

	}

}
