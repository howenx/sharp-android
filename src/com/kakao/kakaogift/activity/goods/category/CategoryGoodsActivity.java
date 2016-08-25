/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-25 上午10:12:32 
 **/
package com.kakao.kakaogift.activity.goods.category;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.category.presenter.CategoryGoodsPresenter;
import com.kakao.kakaogift.activity.goods.category.presenter.CategoryGoodsPresenterImpl;
import com.kakao.kakaogift.activity.goods.theme.adapter.ThemeAdapter;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * @author vince
 * 
 */
public class CategoryGoodsActivity extends BaseActivity implements
		OnRefreshListener2<GridView>, CategoryGoodsView {

	private PullToRefreshGridView mGridView;
	private ThemeAdapter adapter;
	private List<HGoodsVo> data;
	private int pageNo = 1;

	private String url;
	private CategoryGoodsPresenter presenter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "KakaoGift");
		setContentView(R.layout.goods_category_layout);

		url = getIntent().getStringExtra("url");
		mGridView = (PullToRefreshGridView) findViewById(R.id.mGridView);

		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, this);
		mGridView.setMode(Mode.BOTH);
		mGridView.setAdapter(adapter);
		mGridView.setOnRefreshListener(this);
		presenter = new CategoryGoodsPresenterImpl(this);
		presenter.getCategoryGoodsList(url, pageNo);
	}
	
	private void notifyDataUpdate(List<HGoodsVo> list){
		if(pageNo==1){
			data.clear();
		}
		data.addAll(list);
		adapter.notifyDataSetChanged();
	}


	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		pageNo =1;
		presenter.getCategoryGoodsList(url, pageNo);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
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
	public void CategoryGoodsData(List<HGoodsVo> data) {
		if(mGridView.isRefreshing()){
			mGridView.onRefreshComplete();
		}
		notifyDataUpdate(data);
	}

	@Override
	public void showLoadFaild(String str) {
		if(mGridView.isRefreshing()){
			mGridView.onRefreshComplete();
		}
		ToastUtils.Toast(this, str);
	}

}
