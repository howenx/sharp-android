package com.kakao.kakaogift.activity.goods.theme;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.adapter.ThemeAdapter;
import com.kakao.kakaogift.activity.goods.theme.presenter.HThemeGoodsPresenterImpl;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.entity.HGoodsVo.ImgTag;
import com.kakao.kakaogift.entity.HThemeGoods;
import com.kakao.kakaogift.entity.HThemeGoods.ThemeList;
import com.kakao.kakaogift.entity.ImageVo;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ImageResizer;
import com.kakao.kakaogift.utils.ToastUtils;
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
public class ThemeGoodsActivity extends BaseActivity implements HThemeGoodsView {

	private String Tag = "ThemeGoodsActivity";

	private ThemeAdapter adapter; // 商品适配器
	private List<HGoodsVo> data;// 显示的商品数据

	FrameLayout mframeLayout; // 主推商品容器 添加tag使用
	private PullToRefreshScrollView mScrollView;
	private GridView gridView;
	private HThemeGoodsPresenterImpl iGoodsPresenterImpl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theme_layout);
		ActionBarUtil.setActionBarStyle(this, "商品展示");

		// banner图容器
		mframeLayout = (FrameLayout) findViewById(R.id.mframeLayout);
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.mScrollView);
		gridView = (GridView) findViewById(R.id.my_grid);

		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, this);
		gridView.setAdapter(adapter);
		gridView.setFocusable(false);
		gridView.setOnItemClickListener(turnListener);

		mScrollView.setMode(Mode.PULL_FROM_START);
		mScrollView.setOnRefreshListener(scrollListener);

		// 获取数据
		iGoodsPresenterImpl = new HThemeGoodsPresenterImpl(this);
		iGoodsPresenterImpl.getHThemeGoodsData(getHeaders(), getIntent()
				.getStringExtra("url"), Tag);
	}

	private OnItemClickListener turnListener = new  OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = null; 
			if (data.get(arg2).getItemType().equals("pin")) {
				intent = new Intent(getActivity(), PingouDetailActivity.class);
			} else {
				intent = new Intent(getActivity(), GoodsDetailActivity.class);
			}
			intent.putExtra("url", data.get(arg2).getItemUrl());
			startActivityForResult(intent, 1);
		}
	};

	private OnRefreshListener<ScrollView> scrollListener = new OnRefreshListener<ScrollView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
			iGoodsPresenterImpl.getHThemeGoodsData(getHeaders(), getIntent().getStringExtra("url"), Tag);
		}
	};

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
			ActionBarUtil.setActionBarStyle(this, themeList.getTitle());
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
			Log.i("imginfo", themeImg.getWidth() + ", " + themeImg.getHeight()
					+ "");
			initTagInfo(themeList, width, height);
			int inSampleSize = ImageResizer.calculateInSampleSize(
					themeImg.getWidth(), themeImg.getHeight());
			Glide.with(this).load(themeImg.getUrl())
					.animate(R.anim.abc_fade_in)
					.override(width / inSampleSize, height / inSampleSize)
					.into(img);
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
	protected void onDestroy() {
		super.onDestroy();
		VolleyHttp.parseRequestTask(Tag);
	}

	@Override
	public void showLoading() {
		getLoading().show();
	}

	@Override
	public void hideLoading() {
		getLoading().dismiss();
	}

	@Override
	public void GetHThemeGoodsData(HThemeGoods detail) {
		if(mScrollView.isRefreshing()){
			mScrollView.onRefreshComplete();
		}
		initThemeView(detail);
	}

	@Override
	public void showLoadFaild(String str) {
		ToastUtils.Toast(getActivity(), str);
	}

}
