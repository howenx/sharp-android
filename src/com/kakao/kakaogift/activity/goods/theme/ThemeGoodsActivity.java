package com.kakao.kakaogift.activity.goods.theme;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.car.ShoppingCarActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.adapter.ThemeAdapter;
import com.kakao.kakaogift.activity.goods.theme.presenter.HThemeGoodsPresenterImpl;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.entity.HThemeGoods;
import com.kakao.kakaogift.entity.ImageVo;
import com.kakao.kakaogift.entity.HGoodsVo.ImgTag;
import com.kakao.kakaogift.entity.HThemeGoods.ThemeList;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.kakao.kakaogift.utils.ImageResizer;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.BadgeView;
import com.kakao.kakaogift.view.CustomScrollView;
import com.kakao.kakaogift.view.CustomScrollView.OnScrollUpListener;
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
	private GridView gridView;
	private Drawable backgroundDrawable;
	private HThemeGoodsPresenterImpl iGoodsPresenterImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		initView();
		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, this);
		gridView = (GridView) findViewById(R.id.my_grid);
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
		actionbarView = ActionBarUtil.setActionBarStyle(this, "商品展示",
				R.drawable.che, this);
		// 购物车
		View cartView = actionbarView.findViewById(R.id.setting);
		bView = new BadgeView(this, cartView);
		bView.setBackgroundResource(R.drawable.bg_badgeview);
		bView.setBadgePosition(BadgeView.POSITION_CENTER);
		bView.setTextSize(10);
		bView.setTextColor(Color.parseColor("#FFFFFF"));
		// banner图容器
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
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
		if (cartNum == null)
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
		if (themeList.getTitle() != null) {
			TextView titleView = (TextView) actionbarView
					.findViewById(R.id.header);
			titleView.setText(themeList.getTitle());
		}
		ImageVo themeImg = themeList.getThemeImg();
		if (themeImg != null) {
			int width = CommonUtils.getScreenWidth(this);
			int height = CommonUtils.getScreenWidth(this)
					* themeImg.getHeight() / themeImg.getWidth();
			mframeLayout.setLayoutParams(new LinearLayout.LayoutParams(width,
					height));
			ImageView img = (ImageView) findViewById(R.id.img); // 主推商品图片
			// 初始化标签信息
			Log.i("imginfo", themeImg.getWidth()+", "+ themeImg.getHeight()+"");
			initTagInfo(themeList, width, height);
			int inSampleSize  = ImageResizer.calculateInSampleSize(themeImg.getWidth(), themeImg.getHeight());
			Glide.with(this).load(themeImg.getUrl()).animate(R.anim.abc_fade_in)
						.override(width / inSampleSize, height / inSampleSize).into(img);
		}
		if (themeList.getThemeItemList() != null
				&& themeList.getThemeItemList().size() > 0) {
			data.clear();
			data.addAll(themeList.getThemeItemList());
			adapter.notifyDataSetChanged();
		} else {
			gridView.setVisibility(View.GONE);
		}
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
	 * @see
	 * com.kakao.kakaogift.activity.goods.theme.view.HThemeGoodsView#showLoading
	 * ()
	 */
	@Override
	public void showLoading() {
		// TODO Auto-generated method stub
		getLoading().show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.kakao.kakaogift.activity.goods.theme.view.HThemeGoodsView#hideLoading
	 * ()
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
	 * com.kakao.kakaogift.activity.goods.theme.view.HThemeGoodsView#GetData
	 * (com.kakao.kakaogift .entity.HThemeGoods)
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
	 * com.kakao.kakaogift.activity.goods.theme.view.HThemeGoodsView#showLoadFaild
	 * ( java.lang.String)
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
	 * com.kakao.kakaogift.activity.view.theme.HThemeGoodsView#GetCartNumData
	 * (java. lang.Integer)
	 */
	@Override
	public void GetCartNumData(Integer cartNum) {
		// TODO Auto-generated method stub
		showCartNum(cartNum);
	}

	private class OnActionbarScrollListener implements OnScrollUpListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.kakao.kakaogift.view.CustomScrollView.OnScrollUpListener#onScroll
		 * (int, boolean)
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
