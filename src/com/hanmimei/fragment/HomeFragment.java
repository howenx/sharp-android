package com.hanmimei.fragment;

import java.util.ArrayList;
import java.util.List;
import m.framework.ui.widget.pulltorefresh.Scrollable;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;
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
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Slider;
import com.hanmimei.entity.Theme;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CycleViewPager;
import com.hanmimei.view.CycleViewPager.ImageCycleViewListener;
import com.hanmimei.view.ViewFactory;

@SuppressLint({ "NewApi", "InflateParams" })
public class HomeFragment extends Fragment implements
		OnRefreshListener2<ListView>,OnClickListener ,OnScrollListener{
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
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		back_top = (RelativeLayout) view.findViewById(R.id.back_top);
		back_top.setOnClickListener(this);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.PULL_UP_TO_REFRESH);
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
		LinearLayout header_linear = (LinearLayout) headerView.findViewById(R.id.header_linear);
		// 图片的比例适配
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int width = screenWidth;
		int height = width / 3;
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(width, height);
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
		cycleViewPager.setData(views, dataSliders, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(3000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(Slider slider, int position, View imageView) {
		}

	};

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

	private List<Slider> sliders_temp;

	// 加载网络数据
	private void getNetData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					String result = HttpUtils
							.get(UrlUtil.HOME_LIST_URL + pageIndex);
					List<Theme> list = DataParser.parserHome(result);
					sliders_temp = DataParser.parserSlider(result);
					Message msg;
					if (isUpOrDwom == 0) {
						msg = mHandler.obtainMessage(1);
					} else {
						msg = mHandler.obtainMessage(2);
					}
					msg.obj = list;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				mListView.onRefreshComplete();
				List<Theme> list = (List<Theme>) msg.obj;
				if (list != null && list.size() > 0) {
					sliderDao.deleteAll();
					sliderDao.insertInTx(sliders_temp);
					dataSliders.clear();
					dataSliders.addAll(sliders_temp);
					if (dataSliders != null && dataSliders.size() > 0)
						initHeaderView();
					data.clear();
					data.addAll(list);
					themeDao.deleteAll();
					themeDao.insertInTx(data);
					adapter.notifyDataSetChanged();
				} else {
					ToastUtils.Toast(getActivity(), R.string.error);
				}
				break;
			case 2:
				mListView.onRefreshComplete();
				List<Theme> list_more = (List<Theme>) msg.obj;
				if (list_more != null && list_more.size() > 0) {
					data.addAll(list_more);
					adapter.notifyDataSetChanged();
					Toast.makeText(mContext,
							"小美为您加载了 " + list_more.size() + " 条新数据",
							Toast.LENGTH_SHORT).show();
				} else {
					pageIndex--;
					Toast.makeText(mContext, "暂无更多数据！！！", Toast.LENGTH_SHORT)
							.show();
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

		default:
			break;
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_IDLE:  //屏幕停止滚动
			// 判断滚动到顶部
			if(mListView.getRefreshableView().getFirstVisiblePosition() == 0){
				back_top.setVisibility(View.GONE);
			}else{
				back_top.setVisibility(View.VISIBLE);
			}
			break;
		case SCROLL_STATE_TOUCH_SCROLL:      // 滚动时
			back_top.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
