package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.activity.ThemeGoodsActivity;
import com.hanmimei.adapter.HomeAdapter;
import com.hanmimei.dao.SliderDao;
import com.hanmimei.dao.ThemeDao;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Home;
import com.hanmimei.entity.Slider;
import com.hanmimei.entity.Theme;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CycleViewPager;
import com.hanmimei.view.ViewFactory;
import com.umeng.analytics.MobclickAgent;

@SuppressLint({ "NewApi", "InflateParams" })
public class HomeFragment extends Fragment implements
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
	private BaseActivity mActivity;
	//
	private View headerView;
	private List<ImageView> views = new ArrayList<ImageView>();
	private CycleViewPager cycleViewPager;
	private RelativeLayout back_top;
	private LinearLayout no_net;
	private TextView reload;

	private int pullNum = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		inflater = LayoutInflater.from(mContext);
		mActivity = (BaseActivity) getActivity();
		dataSliders = new ArrayList<Slider>();
		data = new ArrayList<Theme>();
		adapter = new HomeAdapter(data, mContext);
		themeDao = mActivity.getDaoSession().getThemeDao();
		sliderDao = mActivity.getDaoSession().getSliderDao();
		registerReceivers();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		back_top = (RelativeLayout) view.findViewById(R.id.back_top);
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
				Intent intent = new Intent(mContext, ThemeGoodsActivity.class);
				intent.putExtra("url", data.get(position - 2).getThemeUrl());
				mContext.startActivity(intent);
			}
		});
		mListView.setOnScrollListener(this);
		findHeaderView();
		loadData();
		addHeaderView();
		return view;
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
		v.addHeaderView(headerView);
	}

	private void initHeaderView() {
		headerView.setVisibility(View.VISIBLE);
		views.clear();
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(mContext,
				dataSliders.get(dataSliders.size() - 1).getImgUrl()));
		for (int i = 0; i < dataSliders.size(); i++) {
			views.add(ViewFactory.getImageView(mContext, dataSliders.get(i)
					.getImgUrl()));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(mContext, dataSliders.get(0)
				.getImgUrl()));

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
		getNetData();
	}

	// 加载网络数据
	private void getNetData() {
		if (isUpOrDwom == 0)
			mActivity.getLoading().show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					String result = HttpUtils.get(UrlUtil.HOME_LIST_URL
							+ pageIndex);
					Home home = DataParser.parserHomeData(result);
					Message msg;
					if (isUpOrDwom == 0) {
						msg = mHandler.obtainMessage(1);
					} else {
						msg = mHandler.obtainMessage(2);
					}
					msg.obj = home;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void afterLoadData(Home home, boolean isNew) {
		List<Theme> list = home.getThemes();
		List<Slider> sliders = home.getSliders();
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
		if (home.getPage_count() <= pullNum) {
			mListView.setMode(Mode.DISABLED);
		} else {
			mListView.setMode(Mode.PULL_FROM_END);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mActivity.getLoading().dismiss();
				mListView.onRefreshComplete();
				Home home = (Home) msg.obj;
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
		isUpOrDwom = 1;
		pageIndex++;
		getNetData();
	}

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

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_IDLE: // 屏幕停止滚动
			// 判断滚动到顶部
			if (mListView.getRefreshableView().getFirstVisiblePosition() <= 4) {
				back_top.setVisibility(View.GONE);
			} else {
				back_top.setVisibility(View.VISIBLE);
			}
			break;
		case SCROLL_STATE_TOUCH_SCROLL: // 滚动时
			if (mListView.getRefreshableView().getFirstVisiblePosition() <= 4) {
				back_top.setVisibility(View.GONE);
			} else {
				back_top.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	private MyBroadCastReceiver myReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		myReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_UP_HOME_ACTION);
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

}
