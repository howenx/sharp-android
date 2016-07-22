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
import android.widget.ListView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.goods.comment.adapter.GoodsCommentAdapter;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.entity.RemarkVo;
import com.hanmimei.event.MessageEvent;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.view.ListBottomView;
import com.hanmimei.view.ListBottomView.OnScrollToBottomListener;
import com.view.waterdrop.WaterDropListView.IWaterDropListViewListener;
import com.viewpagerindicator.BaseIconFragment;
import com.ypy.eventbus.EventBus;

/**
 * @author vince
 * @param <Remark>
 * 
 */
public class GoodsCommentFragment extends BaseIconFragment  implements OnScrollToBottomListener{

	private ListBottomView mListView;
	private View noSmsData;	//空列表视图

	private List<RemarkVo> datas;
	private String loadUrl; //请求api地址
	private EvaType type;
	private GoodsCommentVo vo; 
	private boolean isFirstLoad = true; //标志 是否第一次加载

	private int index = 1;

	private GoodsCommentAdapter mAdapter;
	public enum EvaType {
		all("全部", 0), good("好评", 1), bad("差评", 2);
		private String name; //标签名称
		private int position;	//标签位置

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
		//初始化评价商品接口地址
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
		mListView = (ListBottomView) view.findViewById(R.id.mListView);
		noSmsData = view.findViewById(R.id.noSmsData);
		datas = new ArrayList<>();
		mAdapter = new GoodsCommentAdapter(datas, getActivity());
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(noSmsData);
		mListView.setOnScrollToBottomLintener(this);
		loadGoodsEvaluteData(index);
		return view;
	}
	/**
	 * 请求数据
	 * @param index
	 */
	private void loadGoodsEvaluteData(int index) {
		VolleyHttp.doGetRequestTask(loadUrl + index, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				vo = new Gson().fromJson(result, GoodsCommentVo.class);
				if (vo.getMessage().getCode() == 200) {
					showGoodsComment(vo.getRemarkList());
					notifyTabTitleChange();
				}

			}

			@Override
			public void onError() {
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

	/* (non-Javadoc)
	 * @see com.hanmimei.view.ListBottomView.OnScrollToBottomListener#onScrollBottomListener()
	 */
	@Override
	public void onScrollBottomListener() {
		// TODO Auto-generated method stub
		if (index >= vo.getPage_count()) {
			return;
		}
		index++;
		loadGoodsEvaluteData(index);
	}

}
