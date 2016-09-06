package com.kakao.kakaogift.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.h5.Html5LoadActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.ThemeGoodsActivity;
import com.kakao.kakaogift.adapter.HomeAdapter;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.Home;
import com.kakao.kakaogift.entity.Theme;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.BaseIconFragment;

/**
 * @author eric
 * 
 */
public class MainPinFragment extends BaseIconFragment implements
OnRefreshListener2<ListView>, OnClickListener, OnScrollListener{
	private Context mContext;
	private PullToRefreshListView mListView;
	private HomeAdapter adapter;
	private List<Theme> data;
	private View back_top;
	private LinearLayout no_net;
	private TextView reload;
	private BaseActivity mActivity;
	private int pageIndex = 1;
	private int pullNum = 1;
	private boolean isNew = true;
	private Home home;
	private EndLayout endLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data = new ArrayList<Theme>();
		adapter = new HomeAdapter(data, mContext);
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mContext = context;
		mActivity = (BaseActivity) context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list_layout, null);
		ActionBarUtil.initMainActionBarStyle(mActivity,view, 1);
		back_top = view.findViewById(R.id.back_top);
		no_net = (LinearLayout) view.findViewById(R.id.no_net);
		reload = (TextView) view.findViewById(R.id.reload);
		reload.setOnClickListener(this);
		back_top.setOnClickListener(this);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mylist);
		mListView.getRefreshableView().setCacheColorHint(Color.TRANSPARENT);
		mListView.setAdapter(adapter);
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(this);
		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = null;
				if (data.get(position - 1).getType().equals("ordinary")) {
					intent = new Intent(mContext, ThemeGoodsActivity.class);
				} else if (data.get(position - 1).getType().equals("h5")) {
					intent = new Intent(mContext, Html5LoadActivity.class);
				} else if (data.get(position - 1).getType().equals("pin")) {
					intent = new Intent(mContext, PingouDetailActivity.class);
				} else if (data.get(position - 1).getType().equals("detail")) {
					intent = new Intent(mContext, GoodsDetailActivity.class);
				}
				intent.putExtra("url", data.get(position - 1).getThemeUrl());
				mContext.startActivity(intent);
			}
		});
		loadData();
		addFooterView(mContext);
		return view;
	}
	
	private void addFooterView(Context context){
		endLayout = new EndLayout(context);
		mListView.getRefreshableView().addFooterView(endLayout.getLayoutHolder());
		endLayout.hide();
	}


	/**
	 * 
	 */
	private void loadData() {
		if(isNew){
			mActivity.getLoading().show();
		}
		VolleyHttp.doGetRequestTask(UrlUtil.PINGOU_URL + pageIndex, new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				mListView.onRefreshComplete();
				mActivity.getLoading().dismiss();
				home = DataParser.parserHomeData(result);
				if(home.gethMessage() != null){
					if(home.gethMessage().getCode() == 200){
						// TODO Auto-generated method stub
						
						if(isNew){
							refreshData(home.getThemes());
						}else{
							moreData(home.getThemes());
						}
						if(home.getPage_count() <= pageIndex){
//							mListView.getRefreshableView().addFooterView(endLayout.getView());
							endLayout.show();
							mListView.setMode(Mode.PULL_FROM_START);
						}else if(pageIndex == 1){
							mListView.setMode(Mode.BOTH);
//							mListView.getRefreshableView().removeFooterView(endLayout.getView());
							endLayout.hide();
						}
					}else{
						ToastUtils.Toast(mActivity, home.gethMessage().getMessage());
					}
				}else{
					mListView.setVisibility(View.GONE);
					no_net.setVisibility(View.VISIBLE);
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


	/**
	 * @param themes
	 */
	private void moreData(List<Theme> themes) {
		if(themes != null && themes.size() > 0){
			pullNum = pullNum + 1;
			data.addAll(themes);
			adapter.notifyDataSetChanged();
		}else{
			ToastUtils.Toast(mContext,  "暂无更多数据");
		}
		
	}

	/**
	 * @param themes
	 */
	private void refreshData(List<Theme> themes) {
		if(themes != null && themes.size() > 0){
			data.clear();
			data.addAll(themes);
			adapter.notifyDataSetChanged();
		}else{
			ToastUtils.Toast(mContext,  "暂无数据");
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
			isNew = true;
			loadData();
			break;
		default:
			break;
		}
	}
	/* (non-Javadoc)
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
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

	/* (non-Javadoc)
	 * @see com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2#onPullDownToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex = 1;
		isNew = true;
		loadData();
	}

	/* (non-Javadoc)
	 * @see com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2#onPullUpToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex++;
		isNew = false;
		loadData();
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainPinFragment"); // 统计页面，"MainPinFragment"为页面名称，可自定义
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainPinFragment");
	}

	@Override
	public String getTitle() {
		return "拼购";
	}

	@Override
	public int getIconId() {
		return R.drawable.tab_pin;
	}

}
