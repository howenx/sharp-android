/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-26 下午6:06:29 
 **/
package com.kakao.kakaogift.activity.goods.comment.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.goods.comment.GoodsCommentImgActivity;
import com.kakao.kakaogift.entity.GoodsCommentVo;
import com.kakao.kakaogift.entity.RemarkVo;
import com.kakao.kakaogift.utils.GlideLoaderTools;

/**
 * @author vince
 * 
 */
public class GoodsCommentImageAdapter extends BaseAdapter {

	private Context context;
	private RemarkVo remarkVo;
	
	private List<String> images;

	public GoodsCommentImageAdapter(Context context, RemarkVo remarkVo) {
		super();
		this.context = context;
		this.remarkVo = remarkVo;
		this.images = remarkVo.getPictureList();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
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
		GlideLoaderTools.loadSquareImage(context, images.get(position),
				holder.imageView);

		holder.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, GoodsCommentImgActivity.class);
				GoodsCommentVo vo = new GoodsCommentVo();
				 vo.setPosition(position);
				vo.setPics(remarkVo.getRemarkVos());
				vo.setIndex(1);
				vo.setCount_num(images.size());
				intent.putExtra("GoodsCommentVo", vo);
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		public ImageView imageView;
	}

}
