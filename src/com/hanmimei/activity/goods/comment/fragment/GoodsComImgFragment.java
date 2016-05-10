/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-27 上午11:37:51 
 **/
package com.hanmimei.activity.goods.comment.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.activity.goods.comment.GoodsCommentImgActivity;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsCommentVo;
import com.hanmimei.entity.MessageEvent;
import com.hanmimei.entity.RemarkVo;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.GlideLoaderTools;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.GridBottomView;
import com.hanmimei.view.GridBottomView.OnScrollToBottomListener;
import com.viewpagerindicator.BaseIconFragment;
import com.ypy.eventbus.EventBus;

/**
 * @author vince
 * 
 */
public class GoodsComImgFragment extends BaseIconFragment  implements OnScrollToBottomListener{

	private String loadUrl;
	private int index = 1;
	private GoodsCommentVo vo;

	private GridBottomView mGridView;
	private HmmAdapter adapter;
	private List<RemarkVo> pics;
	private boolean isFirstLoad = true;

	public GoodsComImgFragment(String skuType, String skuTypeId) {
		loadUrl = UrlUtil.GOODS_REMARK_PICTURE + skuType + "/" + skuTypeId
				+ "/";
		pics = new ArrayList<RemarkVo>();
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.goods_comment_pager_item_img_layout, null);
		mGridView = (GridBottomView) view.findViewById(R.id.mGridView);
		adapter = new HmmAdapter(getActivity(), pics);
		mGridView.setAdapter(adapter);
		mGridView.setOnScrollToBottomLintener(this);
		loadGoodsEvaluteData(index);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), GoodsCommentImgActivity.class);
				vo.setPosition(position);
				vo.setLoadUrl(loadUrl);
				vo.setPics(pics);
				vo.setIndex(index);
				intent.putExtra("GoodsCommentVo", vo);
				startActivity(intent);
			}
		});
		return view;
	}

	private void loadGoodsEvaluteData(int index) {
		VolleyHttp.doGetRequestTask(loadUrl + index, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				vo = new Gson().fromJson(result, GoodsCommentVo.class);
				showGoodsEvaluteImg(vo.getRemarkList());
				notifyTabTitleChange();
			}

			@Override
			public void onError() {
				ToastUtils.Toast(getActivity(), R.string.error);
			}
		});
	}
	
	private void notifyTabTitleChange() {
		if (isFirstLoad) {
			EventBus.getDefault().post(new MessageEvent(3));
			isFirstLoad = false;
		}
	}
	
	private void showGoodsEvaluteImg(List<RemarkVo> images){
		if(index == 1){
			pics.clear();
		}
		if(images == null)
			return;
		pics.addAll(images);
		adapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viewpagerindicator.BaseIconFragment#getTitle()
	 */
	@Override
	public String getTitle() {
		if(vo !=null && vo.getCount_num() !=null)
			return "晒单"+"\n"+vo.getCount_num();
		return "晒单";
	}



	/* (non-Javadoc)
	 * @see com.hanmimei.view.GridBottomView.OnScrollToBottomListener#onScrollBottomListener()
	 */
	@Override
	public void onScrollBottomListener() {
		if(index>=vo.getPage_count()) {
			return;
		}
		index ++;
		loadGoodsEvaluteData(index);
		
	}
	
	
	
	private class HmmAdapter extends BaseAdapter {

		private Context context;
		private List<RemarkVo> images;

		public HmmAdapter(Context context, List<RemarkVo> images) {
			super();
			this.context = context;
			this.images = images;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return images.size();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return images.get(position);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.img_panel, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView
						.findViewById(R.id.my_image_view);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			GlideLoaderTools.loadSquareImage(context, images.get(position).getPicture(),
					holder.imageView, ScaleType.CENTER_CROP);

			return convertView;
		}

		private class ViewHolder {
			public ImageView imageView;
		}

	}
	
	@Override
	public int getIconId() {
		return 0;
	}

}
