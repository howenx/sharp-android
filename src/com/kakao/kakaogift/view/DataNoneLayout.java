/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-28 下午3:09:39 
 **/
package com.kakao.kakaogift.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.goods.theme.adapter.ThemeAdapter;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.HGoodsVo;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;

/**
 * @author vince
 * 
 */
public class DataNoneLayout {

	private View ll_inner;
	private TextView mTextView;
	private ImageView mImageView;
	private GridView mGridView;
	private PullToRefreshScrollView mScrollView;
	private String url = UrlUtil.GOODS_LIKE;
	private ThemeAdapter adapter; // 商品适配器
	private List<HGoodsVo> data;// 显示的商品数据
	private TextView go_home;
	private Context mContext;

	/**
	 * @param context
	 */
	public DataNoneLayout(Context context, ViewParent parent) {
		initLayout(context, parent);
	}

	private void initLayout(Context context, ViewParent parent) {
		ll_inner = LayoutInflater.from(context).inflate(
				R.layout.notify_null_layout, (ViewGroup) parent);
		mScrollView = (PullToRefreshScrollView) ll_inner.findViewById(R.id.mScrollView);
		mTextView = (TextView) ll_inner.findViewById(R.id.mTextView);
		mImageView = (ImageView) ll_inner.findViewById(R.id.mImageView);
		mGridView = (GridView) ll_inner.findViewById(R.id.mGridView);
		go_home = (TextView) ll_inner.findViewById(R.id.go_home);
		this.mContext = context;
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = null;
				if (data.get(position).getItemType().equals("pin")) {
					intent = new Intent(mContext, PingouDetailActivity.class);
				} else {
					intent = new Intent(mContext, GoodsDetailActivity.class);
				}
				intent.putExtra("url", data.get(position).getItemUrl());
				mContext.startActivity(intent);
			}
		});
		go_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mContext.sendBroadcast(
						new Intent(AppConstant.MESSAGE_BROADCAST_GO_HOME));
			}
		});
		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, context);
		mGridView.setAdapter(adapter);
	}

	public void setGoHome(){
		go_home.setVisibility(View.VISIBLE);
	}
	public View getView() {
		return ll_inner;
	}

	public void setNullImage(int resId) {
		mImageView.setImageResource(resId);
	}

	public void setText(String text) {
		mTextView.setText(text);
	}
	public void setMode(Mode mode){
		mScrollView.setMode(mode);
	}
	public void setNoVisible(){
		mScrollView.setVisibility(View.GONE);
	}
	public void setVisible(){
		mScrollView.setVisibility(View.VISIBLE);
		mScrollView.getRefreshableView().smoothScrollTo(0, 20);
	}
	public void loadData(int pageIndex) {
		VolleyHttp.doGetRequestTask(url + pageIndex, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				LikeGoodsVo vo = new Gson().fromJson(result, LikeGoodsVo.class);
				if (vo.getMessage().getCode() == 200) {
					data.addAll(vo.getThemeItemList());
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onError() {
			}
		});
	}

	class LikeGoodsVo {
		private HMessage message;
		private List<HGoodsVo> themeItemList;

		public HMessage getMessage() {
			return message;
		}

		public void setMessage(HMessage message) {
			this.message = message;
		}

		public List<HGoodsVo> getThemeItemList() {
			return themeItemList;
		}

		public void setThemeItemList(List<HGoodsVo> themeItemList) {
			this.themeItemList = themeItemList;
		}
	}

}
