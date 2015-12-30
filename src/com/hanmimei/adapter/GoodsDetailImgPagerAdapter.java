package com.hanmimei.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.entity.HMMGoods.ImgInfo;
import com.hanmimei.utils.ImageLoaderUtils;

/**
 * 商品详情页 轮播图展示
 * 
 * @author Administrator
 *
 */
public class GoodsDetailImgPagerAdapter extends PagerAdapter {

	private List<View> datas;

	@SuppressLint("InflateParams")
	public GoodsDetailImgPagerAdapter(Context mContext,
			final ArrayList<ImgInfo> imgUrls) {
		datas = new ArrayList<View>();
		View v = null;
		final BaseActivity mActivity = (BaseActivity) mContext;
		LayoutInflater mInflater = LayoutInflater.from(mContext);
		for (int i = 0; i < imgUrls.size(); i++) {
			v = mInflater.inflate(R.layout.good_detail_img_item_layout, null);
			com.hanmimei.view.DragImageView imageView = (com.hanmimei.view.DragImageView) v.findViewById(R.id.mImageView);
			ImageLoaderUtils.loadImage(mContext,imgUrls.get(i).getUrl(), imageView);
			this.datas.add(v);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas != null ? datas.size() : 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(this.datas.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(this.datas.get(position));
		return this.datas.get(position);
	}

}
