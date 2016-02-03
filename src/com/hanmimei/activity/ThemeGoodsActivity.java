package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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

import com.hanmimei.R;
import com.hanmimei.adapter.ThemeAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMMGoods;
import com.hanmimei.entity.HMMGoods.ImgTag;
import com.hanmimei.entity.HMMThemeGoods;
import com.hanmimei.entity.HMMThemeGoods.ThemeList;
import com.hanmimei.entity.ImgInfo;
import com.hanmimei.entity.ShoppingGoods;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ImageLoaderUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.BadgeView;
import com.ui.tag.TagInfo;
import com.ui.tag.TagInfo.Type;
import com.ui.tag.TagView;
import com.ui.tag.TagView.TagViewListener;
import com.ui.tag.TagViewLeft;
import com.ui.tag.TagViewRight;
import com.umeng.analytics.MobclickAgent;

/**
 * @author vince 主题商品的二级界面。
 */
public class ThemeGoodsActivity extends BaseActivity implements OnClickListener {

	private ThemeAdapter adapter; // 商品适配器
	private List<HMMGoods> data;// 显示的商品数据
	private BadgeView bView;

	FrameLayout mframeLayout; // 主推商品容器 添加tag使用

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		View view = ActionBarUtil.setActionBarStyle(this, "商品展示",
				R.drawable.white_shoppingcar, true, this);
		View cartView = view.findViewById(R.id.setting);

		data = new ArrayList<HMMGoods>();
		adapter = new ThemeAdapter(data, this);
		findView(cartView);
		GridView gridView = (GridView) findViewById(R.id.my_grid);
		gridView.setAdapter(adapter);
		gridView.setFocusable(false);
		// 获取数据
		loadUrl();

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (data.get(arg2).getState().equals("Y")|| data.get(arg2).getState().equals("P")) {
					Intent intent = null;
					if (data.get(arg2).getItemType().equals("pin")) {
						intent = new Intent(getActivity(),PingouDetailActivity.class);
					} else {
						intent = new Intent(getActivity(),GoodsDetailActivity.class);
					}
					intent.putExtra("url", data.get(arg2).getItemUrl());
					startActivityForResult(intent, 1);
				}
			}
		});

		registerReceivers();
	}

	// 初始化view对象
	private void findView(View cartView) {
		bView = new BadgeView(this, cartView);
		bView.setBackgroundResource(R.drawable.bg_badgeview2);
		bView.setBadgePosition(BadgeView.POSITION_CENTER);
		bView.setTextSize(10);
		bView.setTextColor(Color.parseColor("#F9616A"));
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
		findViewById(R.id.reload).setOnClickListener(this);

	}

	// 获取显示数据
	private void loadUrl() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), getIntent()
				.getStringExtra("url"), new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				getLoading().dismiss();
				findViewById(R.id.no_net).setVisibility(View.GONE);
				// TODO Auto-generated method stub
				HMMThemeGoods detail = DataParser.parserThemeItem(result);
				if (detail.getMessage().getCode() == 200) {
					initShopCartView(detail);
					initThemeView(detail);
				} else {
					findViewById(R.id.no_net).setVisibility(View.VISIBLE);
					ToastUtils.Toast(getActivity(), detail.getMessage()
							.getMessage());
				}
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
	 * 获取购物车数量 并显示
	 */
	private void getCartNum() {

		if (getUser() == null) {
			List<ShoppingGoods> goods = getDaoSession().getShoppingGoodsDao()
					.queryBuilder().list();
			int num = 0;
			for (ShoppingGoods sg : goods) {
				num += sg.getGoodsNums();
			}
			if (num <= 0) {
				bView.hide(true);
			} else {
				bView.setText(num + "");
				bView.show(true);
			}
		} else {
			Http2Utils.doGetRequestTask(this, getHeaders(),
					UrlUtil.GET_CART_NUM_URL, new VolleyJsonCallback() {

						@Override
						public void onSuccess(String result) {
							HMMThemeGoods detail = DataParser
									.parserThemeItem(result);
							if (detail.getMessage().getCode() == 200) {
								if (detail.getCartNum() != null) {
									bView.setText(detail.getCartNum() + "");
									bView.show();
								} else {
									bView.hide();
								}
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

	private void initShopCartView(HMMThemeGoods detail) {
		if (getUser() == null) {
			List<ShoppingGoods> goods = getDaoSession().getShoppingGoodsDao()
					.queryBuilder().list();
			int num = 0;
			for (ShoppingGoods sg : goods) {
				num += sg.getGoodsNums();
			}
			if (num <= 0) {
				bView.hide(true);
			} else {
				bView.setText(num + "");
				bView.show(true);
			}
		} else {
			if (detail.getCartNum() != null) {
				if (detail.getCartNum() <= 99) {
					bView.setText(detail.getCartNum() + "");
				} else {
					bView.setText("...");
				}
				bView.show(true);
			} else {
				bView.hide(true);
			}
		}
	}

	// 初始化主推商品显示
	private void initThemeView(HMMThemeGoods detail) {
		ThemeList themeList = detail.getThemeList();
		if (themeList == null)
			return;
		ImgInfo themeImg = themeList.getThemeImg();
		int width = CommonUtil.getScreenWidth(this);
		int height = CommonUtil.getScreenWidth(this) * themeImg.getHeight()
				/ themeImg.getWidth();
		ImageView img = (ImageView) findViewById(R.id.img); // 主推商品图片
		LayoutParams params = mframeLayout.getLayoutParams();
		params.height = height;
		params.width = width;
		mframeLayout.setLayoutParams(params);
		ImageLoaderUtils.loadImage(this, themeImg.getUrl(), img);
		 //获取标签信息
		List<ImgTag> tags = themeList.getMasterItemTag();

		for (ImgTag tag : tags) {

			TagInfo tagInfo = new TagInfo();
			tagInfo.bid = 2L;
			tagInfo.bname = tag.getName();
			tagInfo.direct = tag.getAngle() > 90 ? com.ui.tag.TagInfo.Direction.Right
					: com.ui.tag.TagInfo.Direction.Left;
			tagInfo.type = Type.OfficalPoint;
			tagInfo.targetUrl = tag.getUrl();

			final TagView tagView = tagInfo.direct == com.ui.tag.TagInfo.Direction.Right ? new TagViewRight(
					this, null) : new TagViewLeft(this, null);
			tagView.setData(tagInfo);
			tagView.setTagViewListener(new TagViewListener() {

				@Override
				public void onTagViewClicked(View view, TagInfo info) {
					Intent intent = new Intent(getActivity(),
							GoodsDetailActivity.class);
					intent.putExtra("url", info.targetUrl);
					startActivityForResult(intent, 1);
				}
			});

			if (tagInfo.direct == com.ui.tag.TagInfo.Direction.Right) {
				tagInfo.leftMargin = 0;
				tagInfo.topMargin = (int) (height * tag.getTop());
				tagInfo.rightMargin = (int) (width - width * tag.getLeft());
				Log.i("width", tagInfo.rightMargin + "");
				tagInfo.bottomMargin = 0;
			} else {
				tagInfo.leftMargin = (int) (width * tag.getLeft()); // 根据屏幕密度计算使动画中心在点击点，15dp是margin
				tagInfo.topMargin = (int) (height * tag.getTop());
				tagInfo.rightMargin = 0;
				tagInfo.bottomMargin = 0;
			}
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			lp.setMargins(tagInfo.leftMargin, tagInfo.topMargin,
					tagInfo.rightMargin, tagInfo.bottomMargin);
			mframeLayout.addView(tagView, lp);

		}
		data.clear();
		data.addAll(themeList.getThemeItemList());
		adapter.notifyDataSetChanged();

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
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("ThemeGoodsActivity"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("ThemeGoodsActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		MobclickAgent.onPause(this);
	}
}
