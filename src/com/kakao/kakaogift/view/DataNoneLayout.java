/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-8-28 下午3:09:39 
 **/
package com.kakao.kakaogift.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.goods.theme.adapter.ThemeAdapter;
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
	private String url = UrlUtil.GOODS_LIKE;
	private ThemeAdapter adapter; // 商品适配器
	private List<HGoodsVo> data;// 显示的商品数据

	/**
	 * @param context
	 */
	public DataNoneLayout(Context context, ViewParent parent) {
		initLayout(context, parent);
	}

	private void initLayout(Context context, ViewParent parent) {
		ll_inner = LayoutInflater.from(context).inflate(
				R.layout.notify_null_layout, (ViewGroup) parent);
		mTextView = (TextView) ll_inner.findViewById(R.id.mTextView);
		mImageView = (ImageView) ll_inner.findViewById(R.id.mImageView);
		mGridView = (GridView) ll_inner.findViewById(R.id.mGridView);

		data = new ArrayList<HGoodsVo>();
		adapter = new ThemeAdapter(data, context);
		mGridView.setAdapter(adapter);
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

	public void loadData() {
		VolleyHttp.doGetRequestTask(url, new VolleyJsonCallback() {

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
