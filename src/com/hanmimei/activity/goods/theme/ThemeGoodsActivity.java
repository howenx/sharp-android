package com.hanmimei.activity.goods.theme;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.car.ShoppingCarActivity;
import com.hanmimei.activity.goods.detail.GoodsDetailActivity;
import com.hanmimei.activity.goods.pin.PingouDetailActivity;
import com.hanmimei.activity.goods.theme.adapter.ThemeAdapter;
import com.hanmimei.activity.presenter.theme.HThemeGoodsPresenterImpl;
import com.hanmimei.activity.view.theme.HThemeGoodsView;
import com.hanmimei.data.AppConstant;
import com.hanmimei.entity.HGoodsVo;
import com.hanmimei.entity.HGoodsVo.ImgTag;
import com.hanmimei.entity.HThemeGoods;
import com.hanmimei.entity.HThemeGoods.ThemeList;
import com.hanmimei.entity.ImageVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtils;
import com.hanmimei.utils.GlideLoaderTools;
import com.hanmimei.utils.StatusBarCompat;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.hanmimei.view.CustomScrollView;
import com.hanmimei.view.CustomScrollView.OnScrollUpListener;
import com.ui.tag.TagInfo;
import com.ui.tag.TagInfo.Type;
import com.ui.tag.TagView;
import com.ui.tag.TagView.TagViewListener;
import com.ui.tag.TagViewLeft;
import com.ui.tag.TagViewRight;

/**
 * @Author vince.liu
 * @Description 主题商品的二级界面 请求商品列表
 * 
 */
@SuppressLint("NewApi")
public class ThemeGoodsActivity extends BaseActivity implements
		OnClickListener, HThemeGoodsView {

	private String Tag = "ThemeGoodsActivity";

	private ThemeAdapter adapter; // 商品适配器
	private List<HGoodsVo> data;// 显示的商品数据
	private BadgeView bView;
	private View actionbarView;

	FrameLayout mframeLayout; // 主推商品容器 添加tag使用
	private CustomScrollView mScrollView;
	private Drawable backgroundDrawable;
	private HThemeGoodsPresenterImpl iGoodsPresenterImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		initView();
		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, this);
		GridView gridView = (GridView) findViewById(R.id.my_grid);
		gridView.setAdapter(adapter);
		gridView.setFocusable(false);
		// 获取数据
		loadUrl();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				if (data.get(arg2).getItemType().equals("pin")) {
					intent = new Intent(getActivity(),
							PingouDetailActivity.class);
				} else {
					intent = new Intent(getActivity(),
							GoodsDetailActivity.class);
				}
				intent.putExtra("url", data.get(arg2).getItemUrl());
				startActivityForResult(intent, 1);
			}
		});

		registerReceivers();
	}

	/**
	 * 初始化view对象
	 */
	private void initView() {
		// actionbar
//		View actionbarView = findViewById(R.id.actionbarView);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			actionbarView.setPadding(0,
//					StatusBarCompat.getStatusBarHeight(this), 0, 0);
//		}
//		actionbarView.setBackgroundResource(R.color.theme);
//		backgroundDrawable = actionbarView.getBackground();
//		backgroundDrawable.setAlpha(0);
		actionbarView = ActionBarUtil.setActionBarStyle(this, "商品展示", R.drawable.white_shoppingcar, this);
		// 购物车
		View cartView = actionbarView.findViewById(R.id.setting);
		bView = new BadgeView(this, cartView);
		bView.setBackgroundResource(R.drawable.bg_badgeview2);
		bView.setBadgePosition(BadgeView.POSITION_CENTER);
		bView.setTextSize(10);
		bView.setTextColor(Color.parseColor("#F9616A"));
		// banner图容器
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
		// 滚动视图
//		mScrollView = (CustomScrollView) findViewById(R.id.mScrollView);
		// mScrollView.setBackgroundColor(Color.parseColor("#b6d4df"));
//		mScrollView.setOnScrollUpListener(new OnActionbarScrollListener());
		// 添加监听
		findViewById(R.id.reload).setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		// 初始化presenter
		iGoodsPresenterImpl = new HThemeGoodsPresenterImpl(this);
	}

	// 获取显示数据
	private void loadUrl() {
		iGoodsPresenterImpl.getHThemeGoodsData(getHeaders(), getIntent()
				.getStringExtra("url"), Tag);
	}

	/**
	 * 获取购物车数量 并显示
	 */
	private void getCartNum() {
		iGoodsPresenterImpl.getCartNumData(getHeaders(), null);
	}

	private void showCartNum(Integer cartNum) {
		if(cartNum == null)
			return;
		if (cartNum > 0) {
			if (cartNum <= 99) {
				bView.setText(cartNum + "");
			} else {
				bView.setText("...");
			}
			bView.show(true);
		} else {
			bView.hide(true);
		}
	}

	/**
	 * 初始化主推商品显示
	 * 
	 * @param detail
	 */
	private void initThemeView(HThemeGoods detail) {
		
		ThemeList themeList = detail.getThemeList();
		if (themeList == null)
			return;
		if(themeList.getTitle()!=null){
			TextView titleView = (TextView) actionbarView.findViewById(R.id.header);
			titleView.setText(themeList.getTitle());
		}
		ImageVo themeImg = themeList.getThemeImg();
		int width = CommonUtils.getScreenWidth(this);
		int height = CommonUtils.getScreenWidth(this) * themeImg.getHeight()
				/ themeImg.getWidth();
		mframeLayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		ImageView img = (ImageView) findViewById(R.id.img); // 主推商品图片
		// 初始化标签信息
		initTagInfo(themeList, width, height);

		GlideLoaderTools.loadRectImage(this, themeImg.getUrl(), img);

		data.clear();
		data.addAll(themeList.getThemeItemList());
		adapter.notifyDataSetChanged();
	}

	/**
	 * 初始化标签参数
	 * 
	 * @param themeList
	 * @param width
	 * @param height
	 */
	private void initTagInfo(ThemeList themeList, final int width,
			final int height) {
		// 获取标签信息
		final List<ImgTag> tags = themeList.getMasterItemTag();
		if (tags == null || tags.size() <= 0)
			return;
		submitTask(new RunContext(this, tags, width, height));

	}

	private class RunContext implements Runnable {

		private Context context;
		private List<ImgTag> tags;
		private int width;
		private int height;

		private RunContext(Context context, List<ImgTag> tags, int width,
				int height) {
			super();
			this.context = context;
			this.tags = tags;
			this.width = width;
			this.height = height;
		}
		@Override
		public void run() {
			for (ImgTag tag : tags) {

				final TagInfo tagInfo = new TagInfo();
				tagInfo.bid = 2L;
				tagInfo.bname = tag.getName();
				tagInfo.direct = tag.getAngle() > 90 ? com.ui.tag.TagInfo.Direction.Right
						: com.ui.tag.TagInfo.Direction.Left;
				tagInfo.type = Type.OfficalPoint;
				tagInfo.targetUrl = tag.getUrl();
				tagInfo.description = tag.getType();

				if (tagInfo.direct == com.ui.tag.TagInfo.Direction.Right) {
					tagInfo.leftMargin = 0;
					tagInfo.topMargin = (int) (height * tag.getTop());
					tagInfo.rightMargin = (int) (width - width * tag.getLeft() - tagInfo
							.getTagWidth());
					Log.i("width", tagInfo.rightMargin + "");
					tagInfo.bottomMargin = 0;
				} else {
					tagInfo.leftMargin = (int) (width * tag.getLeft() - tagInfo
							.getTagWidth()); //
					tagInfo.topMargin = (int) (height * tag.getTop() - tagInfo
							.getTagHeight());
					tagInfo.rightMargin = 0;
					tagInfo.bottomMargin = 0;
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// 更新UI
						TagView tagView = tagInfo.direct == com.ui.tag.TagInfo.Direction.Right ? new TagViewRight(
								context) : new TagViewLeft(context);
						tagView.setData(tagInfo);
						tagView.setTagViewListener(new TagViewListener() {

							@Override
							public void onTagViewClicked(View view, TagInfo info) {
								Intent intent = null;
								if (info.description.equals("pin")) {
									intent = new Intent(getActivity(),
											PingouDetailActivity.class);
								} else {
									intent = new Intent(getActivity(),
											GoodsDetailActivity.class);
								}
								intent.putExtra("url", info.targetUrl);
								startActivityForResult(intent, 1);
							}
						});
						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);

						lp.setMargins(tagInfo.leftMargin, tagInfo.topMargin,
								tagInfo.rightMargin, tagInfo.bottomMargin);
						mframeLayout.addView(tagView, lp);
					}
				});
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting:
			startActivity(new Intent(this, ShoppingCarActivity.class));
			break;
		case R.id.reload:
			loadUrl();
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	private CarBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new CarBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}

	private class CarBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR)) {
				getCartNum();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
		VolleyHttp.parseRequestTask(Tag);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hanmimei.activity.goods.theme.view.HThemeGoodsView#showLoading()
	 */
	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		getLoading().show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hanmimei.activity.goods.theme.view.HThemeGoodsView#hideLoading()
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
	 * com.hanmimei.activity.goods.theme.view.HThemeGoodsView#GetData(com.hanmimei
	 * .entity.HThemeGoods)
	 */
	@Override
	public void GetHThemeGoodsData(HThemeGoods detail) {
		// TODO Auto-generated method stub
		initThemeView(detail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.goods.theme.view.HThemeGoodsView#showLoadFaild(
	 * java.lang.String)
	 */
	@Override
	public void showLoadFaild(String str) {
		// TODO Auto-generated method stub
		ToastUtils.Toast(getActivity(), str);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hanmimei.activity.view.theme.HThemeGoodsView#GetCartNumData(java.
	 * lang.Integer)
	 */
	@Override
	public void GetCartNumData(Integer cartNum) {
		// TODO Auto-generated method stub
		showCartNum(cartNum);
	}
	
	private class OnActionbarScrollListener implements OnScrollUpListener{

		/* (non-Javadoc)
		 * @see com.hanmimei.view.CustomScrollView.OnScrollUpListener#onScroll(int, boolean)
		 */
		@Override
		public void onScroll(int scrollY, boolean scrollDirection) {
			// TODO Auto-generated method stub
			if (mframeLayout.getMeasuredHeight() > 0) {
				if (scrollY <= 0) {
					backgroundDrawable.setAlpha(0);
				} else if (scrollY <= mframeLayout.getMeasuredHeight()
						&& scrollY > 0) {
					backgroundDrawable.setAlpha(scrollY * 255
							/ mframeLayout.getMeasuredHeight());
				} else if (scrollY > mframeLayout.getMeasuredHeight()) {
					backgroundDrawable.setAlpha(255);
				}
			}
		}
		
	}
}
