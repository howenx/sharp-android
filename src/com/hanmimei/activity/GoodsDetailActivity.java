package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.entity.Tag;
import com.hanmimei.entity.User;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.InitImageLoader;
import com.hanmimei.utils.PopupWindowUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.hanmimei.view.CustomScrollView;
import com.hanmimei.view.NetworkImageHolderView;
import com.hanmimei.view.TagCloudView;
import com.hanmimei.view.TagCloudView.OnTagClickListener;

@SuppressLint("NewApi")
public class GoodsDetailActivity extends BaseActivity implements
		OnClickListener, CustomScrollView.OnScrollUpListener,
		RadioGroup.OnCheckedChangeListener {

	private ConvenientBanner<String> slider; // 轮播图控件
	private TextView discount, itemTitle, itemSrcPrice, itemPrice, area; // 商品折扣
																			// 标题
																			// 原价
																			// 现价
																			// 发货区
	private TextView num_restrictAmount;
	private TagCloudView tagCloudView; // 规格标签控件
	private TextView publicity; // 优惠信息 /购物车数量å
	private CustomScrollView mScrollView; //
	private ListView content_params, content_hot; // 商品参数／热卖商品
	private RadioGroup indicator_hide, indicator; // 顶部导航栏 中部导航栏
	private ImageView img_hide,shopcart;

	private View pager_header,back_top;
	
	private BadgeView buyNumView;//显示购买数量的控件

	private User user;
	private ShoppingGoodsDao goodsDao;
	private Main main; // 主信息
	private List<Stock> stocks; // 子商品信息
	private List<Tag> tags; // 规格标签信息
	
	private int num_shopcart=0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "商品详情", R.drawable.fenxiang,
				true, new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		setContentView(R.layout.goods_detail_layout);
		findView();
		loadDataByUrl();
		// registerReceivers();
	}
	/**
	 * 初始化所有view
	 * @param savedInstanceState
	 */
	private void findView() {

		slider = (ConvenientBanner<String>)findViewById(R.id.slider);
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
		buyNumView = new BadgeView(this, shopcart);
		buyNumView.setTextColor(Color.WHITE);
		buyNumView.setTextSize(10);
		buyNumView.setBackgroundResource(R.drawable.bg_badgeview);
		img_hide = (ImageView) findViewById(R.id.img_hide);
		back_top =  findViewById(R.id.back_top);

		content_params = (ListView) findViewById(R.id.content_params);
		FrameLayout.LayoutParams lpm = (FrameLayout.LayoutParams)new FrameLayout.LayoutParams(
				CommonUtil.getScreenWidth(this),
				CommonUtil.getScreenHeight(this)-CommonUtil.dip2px(129)-CommonUtil.getStatusBarHeight(this));
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

		indicator.setOnCheckedChangeListener(this);

	}
	
	//=========================================================================
	//===============================网络     请求==================================
	//=========================================================================
	/**
	 * 加载数据
	 */
	private void loadDataByUrl() {
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent().getStringExtra("url"), new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				GoodsDetail detail = DataParser.parserGoodsDetail(result);
				initGoodsDetail(detail);
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}
	
	/**
	 * 发送商品信息添加到购物车
	 */
	private void sendData(ShoppingGoods goods) {
		final JSONArray array = toJSONArray(goods);
		Http2Utils.doPostRequestTask2(this,  getHeaders(), UrlUtil.GET_CAR_LIST_URL, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				HMessage hm = DataParser.paserResultMsg(result);
				if (hm.getCode() == 200) {
					//购物车添加成功，显示提示框
					ToastUtils.Toast(GoodsDetailActivity.this);
					//发送广播 提示购物车更新数据
					sendBroadcast(new Intent(
							AppConstant.MESSAGE_BROADCAST_ADD_CAR));
				} else {
					//提示添加失败原因
					ToastUtils.Toast(getActivity(), hm.getMessage());
				}
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		}, array.toString());
	}

	//=========================================================================
		//===============================点击事件==================================
		//=========================================================================

	@Override
	public void onClick(View arg0) {
		if(main == null){
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

		case R.id.btn_shopcart:
			
			ShoppingGoods goods = new ShoppingGoods();
			for (int i = 0; i < stocks.size(); i++) {
				Stock stock = stocks.get(i);
				if (stock.getOrMasterInv()) {
					if(stock.getState().equals("Y")){
						goods.setGoodsId(stock.getId());
						goods.setGoodsNums(1);
						goods.setState("I");
					}else{
						ToastUtils.Toast(this, "请选择商品");
						return;
					}
				}
			}
			if (user != null) {
				sendData(goods);
			} else {
				ShoppingGoods goods2 = goodsDao.queryBuilder().where(Properties.GoodsId.eq(goods.getGoodsId())).build().unique();
				if(goods2 != null){
					goodsDao.delete(goods2);
					goods.setGoodsNums(goods2.getGoodsNums() + 1);
					goodsDao.insert(goods);
				}else{
					goodsDao.insert(goods);
				}
				sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_ADD_CAR));
			}
			Animation anim = AnimationUtils.loadAnimation(this, R.anim.shopcart_anim);
			img_hide.startAnimation(anim);
			// 动画监听事件
			anim.setAnimationListener(new SimpleAnimationListener() {

				// 动画的结束
				@Override
				public void onAnimationEnd(Animation animation) {
					num_shopcart++;
					buyNumView.setText(num_shopcart+ "");//
					buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
					buyNumView.show();
				}
			});
			break;
		case R.id.setting:

			break;
		case R.id.back_top:
			mScrollView.fullScroll(ScrollView.FOCUS_UP);
			break;
		case R.id.btn_cancel:
			window.dismiss();
			break;
		default:
			break;
		}
	}
	
	
	//=========================================================================
		//===============================响应方法   ==================================
		//=========================================================================
	/**
	 * 点击立即购买按钮的响应事件
	 */
	private void clickPay(){
		//未登录跳到登陆页面
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
				if(s.getState().equals("Y")){
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
					break;
				}else{
					ToastUtils.Toast(this, "请选择商品");
					return;
				}
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
		
		if (postalFee <= 50){
			num_portalfee.setText(getResources().getString(R.string.price,
					CommonUtil.DecimalFormat(postalFee)));
			num_portalfee.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			postalFee = curItemPrice *100/(100 - curPostalTaxRate) - curItemPrice;
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

	
	//=========================================================================
		//===============================初始化方法==================================
		//=========================================================================
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
		initGoodsInfo();
		// 初始化规格显示
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
			notice.setText(main.getItemNotice());
		}
		content_params.setAdapter(new GoodsDetailParamAdapter(main
				.getItemFeatures(), this));
		content_hot.setAdapter(new GoodsDetailParamAdapter(main
				.getItemFeatures(), this));
		
		if(getUser() == null){
			List<ShoppingGoods> goods = goodsDao.queryBuilder().list();
			for(ShoppingGoods sg :goods){
				num_shopcart += sg.getGoodsNums();
			}
		}else{
			if(detail.getCartNum() !=null)
				num_shopcart = detail.getCartNum();
		}
		if(num_shopcart >0){
			buyNumView.setText(num_shopcart+"");
			buyNumView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			buyNumView.show();
		}
	}

	private int curPostalTaxRate; // 当前商品税率
	private double curItemPrice; // 当前商品价格
	private int postalStandard;// 关税收费标准
	
	private void initGoodsInfo(){
		Stock stock = null;
		for (Stock s : stocks) {
			tags.add(new Tag(s.getItemColor() + " " + s.getItemSize(), s
					.getState(), s.getOrMasterInv()));
			if (s.getOrMasterInv()) 
				stock = s;
			}
		initStocks(stock);
	}
	

	/**
	 * 初始化子商品信息
	 * 
	 * @param position
	 *            选中商品位置
	 */
	
	private void initStocks(Stock s) {
				initSliderImage(s);
				discount.setText("[" + s.getItemDiscount() + "折]");
				itemTitle.setText(s.getInvTitle());
				itemSrcPrice.setText(getResources().getString(R.string.price,
						s.getItemSrcPrice()));
				itemSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				itemPrice.setText(getResources().getString(R.string.price,
						s.getItemPriceFormat()));
				if (s.getRestrictAmount() > 0) {
					num_restrictAmount.setVisibility(View.VISIBLE);
					num_restrictAmount.setText(getResources().getString(
							R.string.restrictAmount, s.getRestrictAmount()));
				} else {
					num_restrictAmount.setVisibility(View.GONE);
				}
				InitImageLoader.loadImage(this, s.getInvImg(), img_hide);
				curPostalTaxRate = s.getPostalTaxRate();
				curItemPrice = s.getItemPrice();
				postalStandard = s.getPostalStandard();
				area.setText(s.getInvArea());
	}

	/**
	 * 初始化轮播图
	 * 
	 * @param s
	 *            当前选中子商品
	 */
	private void initSliderImage(Stock s) {
		ArrayList<String> networkImages = new ArrayList<String>(s.getItemPreviewImgs());
		slider.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        },networkImages).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused});
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
		
		if(scrollY>0 && back_top.getVisibility() == View.GONE){
			back_top.setVisibility(View.VISIBLE);
		}

		if(scrollY<=0 && back_top.getVisibility() == View.VISIBLE){
			back_top.setVisibility(View.GONE);
		}
		
	}

}
