package com.hanbimei.fragment;

import java.util.ArrayList;
import java.util.List;
import com.hanbimei.R;
import com.hanbimei.activity.BaseActivity;
import com.hanbimei.activity.ThemeDetailActivity;
import com.hanbimei.adapter.HomeAdapter;
import com.hanbimei.dao.SliderDao;
import com.hanbimei.dao.ThemeDao;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.Slider;
import com.hanbimei.entity.Theme;
import com.hanbimei.utils.HttpUtils;
import com.hanbimei.view.CycleViewPager;
import com.hanbimei.view.ViewFactory;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "InflateParams" })
public class HomeFragment extends Fragment implements
		OnRefreshListener2<ListView> {
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
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = new Intent(mContext, ThemeDetailActivity.class);
				intent.putExtra("url", data.get(position - 2).getThemeUrl());
				mContext.startActivity(intent);
			}
		});
		findHeaderView();
		loadData();
		addHeaderView();
		return view;
	}

	private void findHeaderView() {
		headerView = inflater.inflate(R.layout.home_header_slider_layout, null);
		cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
				.findFragmentById(R.id.fragment_cycle_viewpager_content);
	}

	private void addHeaderView() {
		ListView v = mListView.getRefreshableView();
		v.addHeaderView(headerView);
	}

	private void initHeaderView() {
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(mContext,
				dataSliders.get(dataSliders.size() - 1).getUrl()));
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
		cycleViewPager.setTime(2000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();
	}

	// 加载本地数据
	private void loadData() {
		List<Theme> list = themeDao.queryBuilder().build().list();
		List<Slider> list2 = sliderDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			data.clear();
			data.addAll(list);
			adapter.notifyDataSetChanged();
			dataSliders.clear();
			dataSliders.addAll(list2);
			initHeaderView();
		} else {
			getNetData();
		}

	}

	// 加载网络数据
	private void getNetData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
					String result = HttpUtils
							.get("http://172.28.3.18:9001/index/" + pageIndex);
					List<Theme> list = DataParser.parserHome(result);
					dataSliders.clear();
					dataSliders = DataParser.parserSlider(result);
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
					sliderDao.insertInTx(dataSliders);
					initHeaderView();
					themeDao.deleteAll();
					data.clear();
					data.addAll(list);
					themeDao.insertInTx(data);
					adapter.notifyDataSetChanged();
					Toast.makeText(mContext, "加载数据成功！！！", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(mContext, "加载数据成功！！！", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case 2:
				mListView.onRefreshComplete();
				List<Theme> list_more = (List<Theme>) msg.obj;
				if (list_more != null && list_more.size() > 0) {
					data.addAll(list_more);
					adapter.notifyDataSetChanged();
				} else {
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

}
