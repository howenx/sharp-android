/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午10:12:32 
 **/
package com.kakao.kakaogift.activity.goods.category;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.extras.GridViewWithHeaderAndFooter;
import com.handmark.pulltorefresh.library.extras.PullToRefreshHeaderAndFooterGridView;
import com.handmark.pulltorefresh.library.internal.EndLayout;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.category.presenter.CategoryGoodsPresenter;
import com.kakao.kakaogift.activity.goods.category.presenter.CategoryGoodsPresenterImpl;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.adapter.ThemeAdapter;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author vince
 * 
 */
public class CategoryGoodsActivity extends BaseActivity implements
		OnRefreshListener2<GridViewWithHeaderAndFooter>, CategoryGoodsView {

	private PullToRefreshHeaderAndFooterGridView mGridView;
	private ThemeAdapter adapter;
	private List<HGoodsVo> data;
	private int pageNo = 1;

	private String url;
	private CategoryGoodsPresenter presenter;
	private EndLayout endLayout;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "KakaoGift");
		setContentView(R.layout.goods_category_layout);

		url = getIntent().getStringExtra("url");
		mGridView = (PullToRefreshHeaderAndFooterGridView) findViewById(R.id.mGridView);
		initFooterView();
		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, this);
		mGridView.setAdapter(adapter);
		mGridView.setMode(Mode.BOTH);
		mGridView.setOnRefreshListener(this);
		presenter = new CategoryGoodsPresenterImpl(this);
		mGridView.setOnItemClickListener(clickListener);
		
		presenter.getCategoryGoodsList(url, pageNo);
	}
	
	private void initFooterView(){
		endLayout = new EndLayout(this);
		mGridView.getRefreshableView().addFooterView(endLayout.getLayoutHolder());
		endLayout.hide();
	}
	
	
	private OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = null; 
			if (data.get(position).getItemType().equals("pin")) {
				intent = new Intent(getActivity(), PingouDetailActivity.class);
			} else {
				intent = new Intent(getActivity(), GoodsDetailActivity.class);
			}
			intent.putExtra("url", data.get(position).getItemUrl());
			startActivityForResult(intent, 1);
		}
	};
	
	private void notifyDataUpdate(List<HGoodsVo> list,int page_count){
		if(pageNo==1){
			mGridView.setMode(Mode.BOTH);
			data.clear();
			endLayout.hide();
		}
		data.addAll(list);
		adapter.notifyDataSetChanged();
		if(pageNo >= page_count){
			mGridView.setMode(Mode.PULL_FROM_START);
			endLayout.show();
		}
	}


	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridViewWithHeaderAndFooter> refreshView) {
		pageNo =1;
		presenter.getCategoryGoodsList(url, pageNo);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridViewWithHeaderAndFooter> refreshView) {
		pageNo++;
		presenter.getCategoryGoodsList(url, pageNo);
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
	public void CategoryGoodsData(List<HGoodsVo> data, int page_count) {
		if(mGridView.isRefreshing()){
			mGridView.onRefreshComplete();
		}
		notifyDataUpdate(data,page_count);
	}

	@Override
	public void showLoadFaild(String str) {
		if(mGridView.isRefreshing()){
			mGridView.onRefreshComplete();
		}
		ToastUtils.Toast(this, str);
	}

	@Override
	public void title(String title) {
		ActionBarUtil.setActionBarStyle(this, title);
	}

}
