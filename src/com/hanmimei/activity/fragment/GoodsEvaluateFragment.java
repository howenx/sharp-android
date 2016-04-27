/**
 * @Description: TODO(商品评价列表) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午5:05:35 
 **/
package com.hanmimei.activity.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.viewpagerindicator.BaseIconFragment;

/**
 * @author vince
 * 
 */
public class GoodsEvaluateFragment extends BaseIconFragment {

	private PullToRefreshListView mListView;
	private List<GoodsCommentVo> datas;
	private String loadUrl;
	private int index = 0;
	
	public enum EvaType{
		good,middle,bad;
	}
	
	

	public GoodsEvaluateFragment() {
		super();
	}
	public GoodsEvaluateFragment(EvaType type) {
		super();
		if(type == EvaType.good){
			loadUrl = "";
		}else if(type == EvaType.middle){
			loadUrl = "";
		}else if(type == EvaType.bad){
			loadUrl = "";
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
		loadGoodsEvaluteData();

		return view;
	}

	private void loadGoodsEvaluteData() {
		VolleyHttp.doGetRequestTask(loadUrl,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						GoodsCommentVo vo = new Gson().fromJson(result,
								GoodsCommentVo.class);

					}

					@Override
					public void onError() {

					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viewpagerindicator.BaseIconFragment#getTitle()
	 */
	@Override
	public String getTitle() {
		return null;
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

}
