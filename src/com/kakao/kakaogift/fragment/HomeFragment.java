package com.kakao.kakaogift.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.internal.EndLayout;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.category.CategoryGoodsActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.h5.Html5LoadActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.ThemeGoodsActivity;
import com.kakao.kakaogift.adapter.CategoryAdapter;
import com.kakao.kakaogift.adapter.HomeAdapter;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Entry;
import com.kakao.kakaogift.entity.Home;
import com.kakao.kakaogift.entity.Slider;
import com.kakao.kakaogift.entity.Theme;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.manager.MessageMenager;
import com.kakao.kakaogift.override.OnGetMessageListener;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.CustomGridView;
import com.kakao.kakaogift.view.CycleViewPager;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.BaseIconFragment;

/**
 * @author eric
 * 
 */
public class HomeFragment extends BaseIconFragment implements
		OnRefreshListener2<ListView>, OnClickListener, OnScrollListener,
		OnGetMessageListener {
	private LayoutInflater inflater;
	private PullToRefreshListView mListView;
	private HomeAdapter adapter;
	private List<Theme> data;
	private List<Slider> dataSliders;
	private Context mContext;
	private int pageIndex = 1;
	private int isUpOrDwom = 0;
	private BaseActivity mActivity;
	//
	private View headerView;
	private List<ImageView> views = new ArrayList<ImageView>();
	private CycleViewPager cycleViewPager;
	private View back_top;
	private LinearLayout no_net;
	private TextView reload;

	private ImageView settingView;

	private int pullNum = 1;
	//
	private List<Entry> catData;
	private BaseActivity baseActivity;
	private EndLayout endLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		baseActivity = (BaseActivity) getActivity();
		mContext = getActivity();
		inflater = LayoutInflater.from(mContext);
		mActivity = (BaseActivity) getActivity();
		dataSliders = new ArrayList<Slider>();
		data = new ArrayList<Theme>();
		adapter = new HomeAdapter(data, mContext);
		catData = new ArrayList<Entry>();
		categoryAdapter = new CategoryAdapter(catData, mContext);
		registerReceivers();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list_layout, null);

		settingView = ActionBarUtil.initMainActionBarStyle(baseActivity,view, 0);
		MessageMenager.getInstance().setOnGetMessageListener(this);

		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		back_top = view.findViewById(R.id.back_top);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		reload = (TextView) view.findViewById(R.id.reload);
		reload.setOnClickListener(this);
		back_top.setOnClickListener(this);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				if (position < 3)
					return;
				if (data.get(position - 3).getType() == null)
					return;
				Intent intent = null;
				if (data.get(position - 3).getType().equals("ordinary")) {
					intent = new Intent(mContext, ThemeGoodsActivity.class);
				} else if (data.get(position - 3).getType().equals("h5")) {
					intent = new Intent(mContext, Html5LoadActivity.class);
				} else if (data.get(position - 3).getType().equals("pin")) {
					intent = new Intent(mContext, PingouDetailActivity.class);
				} else if (data.get(position - 3).getType().equals("detail")) {
					intent = new Intent(mContext, GoodsDetailActivity.class);
				}
				intent.putExtra("url", data.get(position - 3).getThemeUrl());
				mContext.startActivity(intent);
			}
		});
		mListView.setOnScrollListener(this);
		findHeaderView();
		findCategory();
		getNetData();
		addHeaderView();
		addFootView(view.getContext());
		return view;
	}
	
	private void addFootView(Context context){
		endLayout = new EndLayout(context);
//		mListView.getRefreshableView().addFooterView(endLayout.getView());
	}

	private View catView;
	private CategoryAdapter categoryAdapter;

	private void findCategory() {
		catView = inflater.inflate(R.layout.home_category_layout, null);
		CustomGridView mGridView = (CustomGridView) catView.findViewById(R.id.mygrid);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				if (catData.get(position).getTargetType().equals("D")) {
					intent = new Intent(getActivity(),
							GoodsDetailActivity.class);
				} else if (catData.get(position).getTargetType().equals("T")) {
					intent = new Intent(getActivity(), ThemeGoodsActivity.class);
				} else if (catData.get(position).getTargetType().equals("P")) {
					intent = new Intent(getActivity(),
							PingouDetailActivity.class);
				} else if (catData.get(position).getTargetType().equals("U")) {
					intent = new Intent(getActivity(), Html5LoadActivity.class);
				}else if (catData.get(position).getTargetType().equals("M")) {
					//////////////////
					intent = new Intent(getActivity(), CategoryGoodsActivity.class);
				}
				intent.putExtra("url", catData.get(position).getItemTarget());
				getActivity().startActivity(intent);

			}
		});
		mGridView.setAdapter(categoryAdapter);
	}

	private void findHeaderView() {
		headerView = inflater.inflate(R.layout.home_header_slider_layout, null);
		headerView.setVisibility(View.GONE);
		LinearLayout header_linear = (LinearLayout) headerView
				.findViewById(R.id.header_linear);
		// 图片的比例适配
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int width = screenWidth;
		int height = width * 1 / 3;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width,
				height);
		header_linear.setLayoutParams(lp);

		cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
	}

	private void addHeaderView() {
		ListView v = mListView.getRefreshableView();
		v.addHeaderView(headerView);
		v.addHeaderView(catView);
	}

	private void initHeaderView() {
		headerView.setVisibility(View.VISIBLE);
		views.clear();
		boolean isCycle = true;
		if(dataSliders.size() > 1){
		// 将最后一个ImageView添加进来
		views.add(getImageView(mContext, dataSliders
				.get(dataSliders.size() - 1).getImgUrl()));
		for (int i = 0; i < dataSliders.size(); i++) {
			views.add(getImageView(mContext, dataSliders.get(i).getImgUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(getImageView(mContext, dataSliders.get(0).getImgUrl()));
		}else{
			isCycle = false;
			for (int i = 0; i < dataSliders.size(); i++) {
				views.add(getImageView(mContext, dataSliders.get(i).getImgUrl()));
			}
		}
		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(isCycle);
		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, dataSliders, null);
		// 设置轮播
		if(isCycle)
			cycleViewPager.setWheel(true);
		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(3000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}


	// 加载网络数据
	private void getNetData() {
		if (isUpOrDwom == 0){
			data.clear();
			dataSliders.clear();
			catData.clear();
			mActivity.getLoading().show();
		}
		
		if (mActivity.getHeaders() == null) {
			VolleyHttp.doGetRequestTask(UrlUtil.HOME_LIST_URL + pageIndex,
					new VolleyJsonCallback() {

						@Override
						public void onSuccess(String result) {
							Log.i("result", result);
							home = DataParser.parserHomeData(result);
							if (isUpOrDwom == 0) {
								upData(home);
							} else {
								dwomData(home);
							}
						}

						@Override
						public void onError() {
							Log.i("result", "error");
							mActivity.getLoading().dismiss();
							mListView.setVisibility(View.GONE);
							no_net.setVisibility(View.VISIBLE);
						}
					});
		} else {
			VolleyHttp.doGetRequestTask(baseActivity.getHeaders(),
					UrlUtil.HOME_LIST_URL + pageIndex,
					new VolleyJsonCallback() {

						@Override
						public void onSuccess(String result) {
							home = DataParser.parserHomeData(result);
							if (isUpOrDwom == 0) {
								upData(home);
							} else {
								dwomData(home);
							}

						}

						@Override
						public void onError() {
							mActivity.getLoading().dismiss();
							mListView.setVisibility(View.GONE);
							no_net.setVisibility(View.VISIBLE);
						}
					});
		}
	}

	private void upData(Home home) {
		mActivity.getLoading().dismiss();
		mListView.onRefreshComplete();
		if (home.gethMessage() != null) {
			mListView.setVisibility(View.VISIBLE);
			no_net.setVisibility(View.GONE);
			if (home.gethMessage().getCode() == 200) {
				afterLoadData(home, true);
			} else {
				ToastUtils.Toast(mActivity, home.gethMessage().getMessage());
			}
		} else {
			mListView.setVisibility(View.GONE);
			no_net.setVisibility(View.VISIBLE);
		}
	}

	private void dwomData(Home home) {
		mListView.onRefreshComplete();
		if (home.gethMessage() != null) {
			mListView.setVisibility(View.VISIBLE);
			no_net.setVisibility(View.GONE);
			if (home.gethMessage().getCode() == 200) {
				pullNum = pullNum + 1;
				afterLoadData(home, false);
			} else {
				ToastUtils.Toast(mActivity, home.gethMessage().getMessage());
			}
		} else {
			mListView.setVisibility(View.GONE);
			no_net.setVisibility(View.VISIBLE);
		}
	};

	private void afterLoadData(Home home, boolean isNew) {
		List<Theme> list = home.getThemes();
		List<Slider> sliders = home.getSliders();
		List<Entry> entries = home.getEntries();
		if ((list != null && list.size() > 0)) {
			if (isNew) {
				data.addAll(list);
			} else {
				data.addAll(list);
			}
			adapter.notifyDataSetChanged();
			if (isNew)
				mListView.getRefreshableView().setSelection(0);
		}
		if (sliders != null && sliders.size() > 0) {
			dataSliders.addAll(sliders);
			initHeaderView();
		}
		if (entries != null && entries.size() > 0) {
			catData.addAll(entries);
			categoryAdapter.notifyDataSetChanged();
		}
		if (home.getHasMsg() != 0) {
			MessageMenager.getInstance().getListener()
					.onGetMessage(R.drawable.hmm_icon_message);
		}
		// TODO Auto-generated method stub
		if(home.getPage_count()<=pageIndex){
			mListView.getRefreshableView().addFooterView(endLayout.getView());
			mListView.setMode(Mode.PULL_FROM_START);
		}else if(pageIndex == 1){
			mListView.setMode(Mode.BOTH);
			mListView.getRefreshableView().removeFooterView(endLayout.getView());
		}
	}

	private Home home;

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex = 1;
		isUpOrDwom = 0;
		pullNum = 1;
		getNetData();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			isUpOrDwom = 1;
			pageIndex++;
			getNetData();
	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (cycleViewPager != null) {
			FragmentManager f = getActivity().getFragmentManager();
			if (f != null) {
				final FragmentTransaction ft = f.beginTransaction();
				if (ft != null) {
					ft.remove((android.app.Fragment) cycleViewPager).commit();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_top:
			mListView.getRefreshableView().smoothScrollToPosition(0);
			back_top.setVisibility(View.GONE);
			break;
		case R.id.reload:
			no_net.setVisibility(View.GONE);
			getNetData();
			break;
		default:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		if (mListView.getRefreshableView().getFirstVisiblePosition() <= 4) {
			if (back_top.getVisibility() == View.VISIBLE)
				back_top.setVisibility(View.GONE);
		} else {
			if (back_top.getVisibility() == View.GONE)
				back_top.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		// if (scrollState == SCROLL_STATE_FLING) {
		// GlideLoaderTools.pauseRequests(getActivity());
		// } else {
		// GlideLoaderTools.resumeRequests(getActivity());
		// }
	}

	private MyBroadCastReceiver myReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		myReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		getActivity().registerReceiver(myReceiver, intentFilter);
	}

	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
//			if (intent.getAction().equals(
//					AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION)) {
//				pageIndex = 1;
//				isUpOrDwom = 0;
//				pullNum = 1;
//				loadData();
//			} else 
				if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				pageIndex = 1;
				isUpOrDwom = 0;
				pullNum = 1;
				getNetData();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MessageMenager.getInstance().setOnGetMessageListener(null);
		getActivity().unregisterReceiver(myReceiver);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("HomeFragment"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("HomeFragment");
	}

	@Override
	public String getTitle() {
		return "首页";
	}

	@Override
	public int getIconId() {
		return R.drawable.tab_home;
	}

	private ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		GlideLoaderTools.loadRectImage(context, url, imageView);
		return imageView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.kakao.kakaogift.override.OnGetMessageListener#onGetMessage(int)
	 */
	@Override
	public void onGetMessage(int resId) {
		settingView.setImageResource(resId);
	}

}
