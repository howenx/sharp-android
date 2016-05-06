/**
 * @Description: TODO(商品的评价显示) 
 * @author vince.刘奇
 * @date 2016-4-25 下午3:18:18 
 **/
package com.hanmimei.activity.goods.comment.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.RemarkVo;
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.view.CircleImageView;

/**
 * @author vince
 * @param <Remark>
 * 
 */
public class GoodsCommentAdapter extends BaseAdapter {

	private List<RemarkVo> datas;
	private Context mContext;

	public GoodsCommentAdapter(List<RemarkVo> datas, Context mContext) {
		super();
		this.datas = datas;
		this.mContext = mContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return datas.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas.get(position);
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
		ViewHolder mViewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.goods_comment_pager_item_layout, null);
			mViewHolder = new ViewHolder(convertView);

			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		RemarkVo raRemarkVo = datas.get(position);

		mViewHolder.name.setText(raRemarkVo.getUserName());
		GlideLoaderUtils.loadCirlceImage(mContext, raRemarkVo.getUserImg(),
				mViewHolder.header);
		mViewHolder.content.setText(raRemarkVo.getContent());
		mViewHolder.create_time.setText(raRemarkVo.getCreateAt());
		mViewHolder.ratingBar.setImageResource(raRemarkVo.getGradeResource());
		if(raRemarkVo.getSize()!=null){
			mViewHolder.size.setVisibility(View.VISIBLE);
			mViewHolder.size.setText("规格："+raRemarkVo.getSize());
		}else{
			mViewHolder.size.setVisibility(View.GONE);
		}
		mViewHolder.buy_time.setText("购买日期："+raRemarkVo.getBuyAt());
		if (raRemarkVo.getPictureList() != null){
			mViewHolder.images.setAdapter(new GoodsCommentImageAdapter(
					mContext, raRemarkVo.getPictureList()));
		}
			

		return convertView;
	}

	private class ViewHolder {
		TextView name, content, create_time,buy_time,size;
		ImageView ratingBar;
		CircleImageView header;
		GridView images;

		public ViewHolder(View convertView) {
			super();
			this.name = (TextView) convertView.findViewById(R.id.name);
			this.content = (TextView) convertView.findViewById(R.id.content);
			this.create_time = (TextView) convertView
					.findViewById(R.id.create_time);
			this.buy_time = (TextView) convertView
					.findViewById(R.id.buy_time);
			this.size = (TextView) convertView
					.findViewById(R.id.size);
			this.header = (CircleImageView) convertView
					.findViewById(R.id.header);
			this.images = (GridView) convertView.findViewById(R.id.mGridView);
			this.ratingBar = (ImageView) convertView
					.findViewById(R.id.ratingBar);
		}

	}

}
