/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午3:06:12 
 **/
package com.hanmimei.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Comment;
import com.hanmimei.gallery.MultiImageSelectorActivity;
import com.hanmimei.view.CustomGridView;

/**
 * @author eric
 * 暂未使用
 * 
 */
@SuppressLint("ViewHolder")
public class PubCommentAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Comment> data;
	private Context mContext;
	private static final int REQUEST_IMAGE = 2;
	private static final int IMAGE_MAX_NUM = 3;

	public PubCommentAdapter(Context mContext, List<Comment> data) {
		inflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final int index = position;
		final Comment comment = data.get(position);
		convertView = inflater.inflate(R.layout.pub_comment_item, null);
		ViewHold hold = new ViewHold();
		hold.img = (ImageView) convertView.findViewById(R.id.img);
		hold.content = (TextView) convertView.findViewById(R.id.content);
		hold.mGridView = (CustomGridView) convertView
				.findViewById(R.id.mGridView);
		hold.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
		if (comment.getComment() != null && !comment.getComment().equals("")) {
			hold.content.setText(comment.getComment());
		}
		if (comment.getScore() != 0) {
			hold.ratingBar.setRating(comment.getScore());
		}
//		ImageGridAdapter adapter = new ImageGridAdapter(mContext,
//				comment.getPhotoList());
//		hold.mGridView.setAdapter(adapter);
		hold.content.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				comment.setComment(s.toString());
			}
		});
		hold.mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == comment.getPhotoList().size()) {
					Intent intent = new Intent(mContext,
							MultiImageSelectorActivity.class);
					// 最大可选择图片数量
					intent.putExtra(
							MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
							IMAGE_MAX_NUM);
					intent.putExtra("position", index);
					// 默认选择
					if (comment.getPhotoList() != null
							&& comment.getPhotoList().size() > 0) {
						intent.putExtra(
								MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
								comment.getPhotoList());
					}
					((Activity) mContext).startActivityForResult(intent,
							REQUEST_IMAGE);
				}
			}
		});
		hold.ratingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						comment.setScore(rating);
					}
				});
		return convertView;
	}

	private class ViewHold {
		private ImageView img;
		private TextView content;
		private RatingBar ratingBar;
		private CustomGridView mGridView;
	}

}
