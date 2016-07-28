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
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
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
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.h5.Html5LoadActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.ThemeGoodsActivity;
import com.kakao.kakaogift.adapter.CategoryAdapter;
import com.kakao.kakaogift.adapter.HomeAdapter;
import com.kakao.kakaogift.dao.EntryDao;
import com.kakao.kakaogift.dao.SliderDao;
import com.kakao.kakaogift.dao.ThemeDao;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Entry;
import com.kakao.kakaogift.entity.Home;
import com.kakao.kakaogift.entity.Slider;
import com.kakao.kakaogift.entity.Theme;
import com.kakao.kakaogift.manager.MessageMenager;
import com.kakao.kakaogift.utils.GlideLoaderTools;
import com.kakao.kakaogift.utils.HttpUtils;
import com.kakao.kakaogift.utils.PreferenceUtil.IntroConfig;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.CycleViewPager;
import com.kakao.kakaogift.view.IntroMsgDialog;
import com.kakao.kakaogift.view.MyGridView;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.BaseIconFragment;

/**
 * @author eric
 * 
 */
public class HomeFragment extends BaseIconFragment implements
		OnRefreshListener2<ListView>, OnClickListener, OnScrollListener {
	private LayoutInflater inflater;
	private PullToRefreshListView mListView;
	private HomeAdapter adapter;
	private List<Theme> data;
	private List<Slider> dataSliders;
	private Context mContext;
	private int pageIndex = 1;
	private int isUpOrDwom = 0;
	private ThemeDao themeDao;
	private SliderDao sliderDao;
	private EntryDao entryDao;
	private BaseActivity mActivity;
	//
	private View headerView;
	private List<ImageView> views = new ArrayList<ImageView>();
	private CycleViewPager cycleViewPager;
	private View back_top;
	private LinearLayout no_net;
	private TextView reload;

	private int pullNum = 1;
	//
	private List<Entry> catData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		inflater = LayoutInflater.from(mContext);
		mActivity = (BaseActivity) getActivity();
		dataSliders = new ArrayList<Slider>();
		data = new ArrayList<Theme>();
		adapter = new HomeAdapter(data, mContext);
		catData = new ArrayList<Entry>();
		categoryAdapter = new CategoryAdapter(catData, mContext);
		themeDao = mActivity.getDaoSession().getThemeDao();
		sliderDao = mActivity.getDaoSession().getSliderDao();
		entryDao = mActivity.getDaoSession().getEntryDao();
		registerReceivers();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		back_top = view.findViewById(R.id.back_top);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		reload = (TextView) view.findViewById(R.id.reload);
		reload.setOnClickListener(this);
		back_top.setOnClickListener(this);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.PULL_FROM_END);
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
		loadData();
		addHeaderView();
		return view;
	}

	private void IntroMsg() {
		if (IntroConfig.getIntroMsgCfg(getActivity()).equals(
				IntroConfig.INTRO_CONFIG_VALUE_IS)) {
			IntroMsgDialog dialog = new IntroMsgDialog(getActivity());
			dialog.show();
		}
	}

	private View catView;
	private CategoryAdapter categoryAdapter;

	private void findCategory() {
		catView = inflater.inflate(R.layout.home_category_layout, null);
		MyGridView mGridView = (MyGridView) catView.findViewById(R.id.mygrid);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				if (catData.get(position).getTargetType().equals("D")) {
					intent = new Intent(getActivity(), GoodsDetailActivity.class);
				} else if (catData.get(position).getTargetType().equals("T")) {
					intent = new Intent(getActivity(), ThemeGoodsActivity.class);
				} else if (catData.get(position).getTargetType().equals("P")) {
					intent = new Intent(getActivity(), PingouDetailActivity.class);
				} else if(catData.get(position).getTargetType().equals("U")){
					intent = new Intent(getActivity(), Html5LoadActivity.class);
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
		int height = width * 2 / 5;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width,
				height);
		header_linear.setLayoutParams(lp);

		cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
	}

	private void addHeaderView() {
		ListView v = mListView.getRefreshableView();
		// View view =
		// LayoutInflater.from(mActivity).inflate(R.layout.home_category_layout,
		// null);
		v.addHeaderView(headerView);
		v.addHeaderView(catView);
	}

	private void initHeaderView() {
		headerView.setVisibility(View.VISIBLE);
		views.clear();
		// 将最后一个ImageView添加进来
		views.add(getImageView(mContext, dataSliders
				.get(dataSliders.size() - 1).getImgUrl()));
		for (int i = 0; i < dataSliders.size(); i++) {
			views.add(getImageView(mContext, dataSliders.get(i).getImgUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(getImageView(mContext, dataSliders.get(0).getImgUrl()));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, dataSliders, null);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(3000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	// 加载本地数据
	private void loadData() {
		List<Theme> list = themeDao.queryBuilder().build().list();
		List<Slider> list2 = sliderDao.queryBuilder().build().list();
		List<Entry> list3 = entryDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			data.clear();
			data.addAll(themeDao.queryBuilder().build().list());
			adapter.notifyDataSetChanged();
		}
		if (list2 != null && list2.size() > 0) {
			dataSliders.clear();
			dataSliders.addAll(list2);
			initHeaderView();
		}
		if (list3 != null && list3.size() > 0) {
			catData.clear();
			catData.addAll(list3);
			categoryAdapter.notifyDataSetChanged();
		}
		getNetData();
	}

	// 加载网络数据
	private void getNetData() {
		if (isUpOrDwom == 0)
			mActivity.getLoading().show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				if (mActivity.getHeaders() == null) {
					result = HttpUtils.get(UrlUtil.HOME_LIST_URL + pageIndex);
				} else {
					result = HttpUtils.get(UrlUtil.HOME_LIST_URL + pageIndex,
							mActivity.getHeaders().get("id-token"));
				}
				Home home = DataParser.parserHomeData(result);
				Message msg;
				if (isUpOrDwom == 0) {
					msg = mHandler.obtainMessage(1);
				} else {
					msg = mHandler.obtainMessage(2);
				}
				msg.obj = home;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void afterLoadData(Home home, boolean isNew) {
		List<Theme> list = home.getThemes();
		List<Slider> sliders = home.getSliders();
		List<Entry> entries = home.getEntries();
		if ((list != null && list.size() > 0)) {
			if (isNew) {
				data.clear();
				data.addAll(list);
				themeDao.deleteAll();
				themeDao.insertInTx(data);
			} else {
				themeDao.insertInTx(list);
				data.addAll(list);
			}
			adapter.notifyDataSetChanged();
			if (isNew)
				mListView.getRefreshableView().setSelection(0);
		}
		if (sliders != null && sliders.size() > 0) {
			sliderDao.deleteAll();
			sliderDao.insertInTx(sliders);
			dataSliders.clear();
			dataSliders.addAll(sliders);
			initHeaderView();
		}
		if (entries != null && entries.size() > 0) {
			entryDao.deleteAll();
			entryDao.insertInTx(entries);
			catData.clear();
			catData.addAll(entries);
			categoryAdapter.notifyDataSetChanged();
		}
		if (home.getHasMsg() != 0) {
			MessageMenager.getInstance().getListener()
					.onGetMessage(R.drawable.hmm_icon_message_h);
		}
	}

	private Home home;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mActivity.getLoading().dismiss();
				mListView.onRefreshComplete();
				home = (Home) msg.obj;
				if (home.gethMessage() != null) {
					mListView.setVisibility(View.VISIBLE);
					no_net.setVisibility(View.GONE);
					if (home.gethMessage().getCode() == 200) {
						afterLoadData(home, true);
					} else {
						ToastUtils.Toast(mActivity, home.gethMessage()
								.getMessage());
					}
				} else {
					mListView.setVisibility(View.GONE);
					no_net.setVisibility(View.VISIBLE);
				}
				IntroMsg();
				break;
			case 2:
				mListView.onRefreshComplete();
				Home home2 = (Home) msg.obj;
				if (home2.gethMessage() != null) {
					mListView.setVisibility(View.VISIBLE);
					no_net.setVisibility(View.GONE);
					if (home2.gethMessage().getCode() == 200) {
						pullNum = pullNum + 1;
						afterLoadData(home2, false);
						ToastUtils.Toast(mActivity, "小美为您加载了 "
								+ home2.getThemes().size() + " 条新数据");
					} else {
						ToastUtils.Toast(mActivity, home2.gethMessage()
								.getMessage());
					}
				} else {
					mListView.setVisibility(View.GONE);
					no_net.setVisibility(View.VISIBLE);
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		isUpOrDwom = 0;
		getNetData();

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (home.getPage_count() <= pullNum) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mListView.onRefreshComplete();
					ToastUtils.Toast(getActivity(), "暂无更多数据");
				}
			}, 1000);
		} else {
			isUpOrDwom = 1;
			pageIndex++;
			getNetData();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (cycleViewPager != null) {
			FragmentManager f = getActivity().getFragmentManager();
			if (f != null && !f.isDestroyed()) {
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
			mListView.getRefreshableView().setSelection(0);
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
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION)) {
				pageIndex = 1;
				isUpOrDwom = 0;
				pullNum = 1;
				loadData();
			} else if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				pageIndex = 1;
				isUpOrDwom = 0;
				pullNum = 1;
				loadData();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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
		return "韩秘美";
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

}
