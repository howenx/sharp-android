/**
 * @Description: TODO(商品评价列表) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午5:05:35 
 **/
package com.hanmimei.activity.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.adapter.GoodsEvaluateAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.entity.RemarkVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.viewpagerindicator.BaseIconFragment;

/**
 * @author vince
 * @param <Remark>
 * 
 */
public class GoodsCommentFragment extends BaseIconFragment implements OnRefreshListener2<ListView>{

	private PullToRefreshListView mListView;
	
	private List<RemarkVo> datas;
	private String loadUrl;
	private EvaType type;
	private GoodsCommentVo vo;
	
	private int index = 1;

	private GoodsEvaluateAdapter mAdapter;
	
	public enum EvaType {
		all("全部"), good("好评"), bad("差评"),picture("图片");
		private String name;

		private EvaType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public GoodsCommentFragment() {
		super();
	}

	public GoodsCommentFragment(EvaType type,String skuType,String skuTypeId) {
		super();
		this.type = type;
		if (type == EvaType.good) {
			loadUrl = UrlUtil.GOODS_REMARK_GOOD+skuType+"/"+skuTypeId+"/";
		} else if (type == EvaType.picture) {
			loadUrl = UrlUtil.GOODS_REMARK_PICTURE+skuType+"/"+skuTypeId+"/";
		} else if (type == EvaType.bad) {
			loadUrl = UrlUtil.GOODS_REMARK_BAD+skuType+"/"+skuTypeId+"/";
		} else if (type == EvaType.all) {
			loadUrl = UrlUtil.GOODS_REMARK_ALL+skuType+"/"+skuTypeId+"/";
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.goods_evaluate_pager_layout, null);
		mListView = (PullToRefreshListView) view.findViewById(R.id.mListView);
		datas = new ArrayList<>();
		mAdapter = new GoodsEvaluateAdapter(datas, getActivity());
		mListView.setAdapter(mAdapter);
		loadGoodsEvaluteData(index);
		

		mListView.setOnRefreshListener(this);
		return view;
	}

	private void loadGoodsEvaluteData(int index) {
		VolleyHttp.doGetRequestTask(loadUrl+index, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				vo = new Gson().fromJson(result,GoodsCommentVo.class);
				if(vo.getMessage().getCode() == 200){
					showGoodsComment(vo.getRemarkList());
				}else{
					ToastUtils.Toast(getActivity(), vo.getMessage().getMessage());
				}
				if(mListView.isRefreshing()){
					mListView.onRefreshComplete();
				}
			}

			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), R.string.error);
				if(mListView.isRefreshing()){
					mListView.onRefreshComplete();
				}
			}
		});
	}
	
	
	

	/**
	 * @param list
	 */
	private void showGoodsComment(List<RemarkVo> list) {
		if(index == 1){
			datas.clear();
		}
		if(list == null)
			return;
		datas.addAll(list);
		mAdapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viewpagerindicator.BaseIconFragment#getTitle()
	 */
	@Override
	public String getTitle() {
		return type.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viewpagerindicator.BaseIconFragment#getIconId()
	 */
	@Override
	public int getIconId() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2#onPullDownToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		index = 1;
		loadGoodsEvaluteData(index);
	}

	/* (non-Javadoc)
	 * @see com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2#onPullUpToRefresh(com.handmark.pulltorefresh.library.PullToRefreshBase)
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		if(index>=vo.getPage_count()) 
			return;
		index++;
		loadGoodsEvaluteData(index);
	}

}
