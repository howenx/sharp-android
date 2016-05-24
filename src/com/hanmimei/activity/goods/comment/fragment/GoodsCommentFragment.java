/**
 * @Description: TODO(商品评价列表) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午5:05:35 
 **/
package com.hanmimei.activity.goods.comment.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.goods.comment.adapter.GoodsCommentAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.entity.RemarkVo;
import com.hanmimei.event.MessageEvent;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.view.waterdrop.WaterDropListView;
import com.view.waterdrop.WaterDropListView.IWaterDropListViewListener;
import com.viewpagerindicator.BaseIconFragment;
import com.ypy.eventbus.EventBus;

/**
 * @author vince
 * @param <Remark>
 * 
 */
public class GoodsCommentFragment extends BaseIconFragment implements
		IWaterDropListViewListener {

	private WaterDropListView mListView;
	private View noSmsData;

	private List<RemarkVo> datas;
	private String loadUrl;
	private EvaType type;
	private GoodsCommentVo vo;
	private boolean isFirstLoad = true;

	private int index = 1;

	private GoodsCommentAdapter mAdapter;

	public enum EvaType {
		all("全部", 0), good("好评", 1), bad("差评", 2);
		private String name;
		private int position;

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		private EvaType(String name, int position) {
			this.name = name;
			this.position = position;
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

	public GoodsCommentFragment(EvaType type, String skuType, String skuTypeId) {
		super();
		this.type = type;
		if (type == EvaType.good) {
			loadUrl = UrlUtil.GOODS_REMARK_GOOD + skuType + "/" + skuTypeId
					+ "/";
		} else if (type == EvaType.bad) {
			loadUrl = UrlUtil.GOODS_REMARK_BAD + skuType + "/" + skuTypeId
					+ "/";
		} else if (type == EvaType.all) {
			loadUrl = UrlUtil.GOODS_REMARK_ALL + skuType + "/" + skuTypeId
					+ "/";
		}
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.goods_comment_pager_layout, null);
		mListView = (WaterDropListView) view.findViewById(R.id.mListView);
		noSmsData = view.findViewById(R.id.noSmsData);
		datas = new ArrayList<>();
		mAdapter = new GoodsCommentAdapter(datas, getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setPullRefreshEnable(false);
		mListView.setWaterDropListViewListener(this);
		mListView.setEmptyView(noSmsData);
		loadGoodsEvaluteData(index);
		return view;
	}

	private void loadGoodsEvaluteData(int index) {
		VolleyHttp.doGetRequestTask(loadUrl + index, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				vo = new Gson().fromJson(result, GoodsCommentVo.class);
				if (vo.getMessage().getCode() == 200) {
					showGoodsComment(vo.getRemarkList());
					notifyTabTitleChange();
				}
				mListView.stopLoadMore();

			}

			@Override
			public void onError() {
				mListView.stopLoadMore();
			}
		});
	}

	private void notifyTabTitleChange() {
		if (isFirstLoad) {
			EventBus.getDefault().post(new MessageEvent(type.getPosition()));
			isFirstLoad = false;
		}
	}

	/**
	 * @param list
	 */
	private void showGoodsComment(List<RemarkVo> list) {
		if (index == 1) {
			datas.clear();
		}
		if (list == null)
			return;
		datas.addAll(list);
		mAdapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.view.waterdrop.WaterDropListView.IWaterDropListViewListener#onRefresh
	 * ()
	 */
	@Override
	public void onRefresh() {
		// index = 1;
		// loadGoodsEvaluteData(index);
		mListView.stopRefresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.view.waterdrop.WaterDropListView.IWaterDropListViewListener#onLoadMore
	 * ()
	 */
	@Override
	public void onLoadMore() {
		if (index >= vo.getPage_count()) {
			mListView.stopLoadMore();
			return;
		}
		index++;
		loadGoodsEvaluteData(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viewpagerindicator.BaseTitleFragment#getTitle()
	 */
	@Override
	public String getTitle() {
		if (vo != null)
			return type.getName() + "\n" + vo.getCount_num();
		return type.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viewpagerindicator.BaseIconFragment#getIconId()
	 */
	@Override
	public int getIconId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
