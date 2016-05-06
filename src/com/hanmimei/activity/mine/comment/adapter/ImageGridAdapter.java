/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-25 下午3:59:04 
 **/
package com.hanmimei.activity.mine.comment.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hanmimei.R;
import com.hanmimei.utils.GlideLoaderUtils;
/**
 * @author eric
 *
 */
public class ImageGridAdapter extends BaseAdapter {
	private LayoutInflater inflater; // 视图容器
	private List<String> mSelectPath;
	private Activity activity;
	private boolean isShow;

	public ImageGridAdapter(Context context, List<String> mSelectPath,
			boolean isShow) {
		activity = (Activity) context;
		inflater = LayoutInflater.from(context);
		this.mSelectPath = mSelectPath;
		this.isShow = isShow;
	}

	public int getCount() {
		return (mSelectPath.size() + 1);
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * ListView Item设置
	 */
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.upload_item_published_grida, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
			holder.btn_image_del = (ImageView) convertView.findViewById(R.id.btn_image_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (isShow) {
			if(position < mSelectPath.size()){
			holder.btn_image_del.setVisibility(View.GONE);
			GlideLoaderUtils.loadSquareImage(activity, mSelectPath.get(position), holder.image);
			}else{
				holder.image.setVisibility(View.GONE);
			}
		} else {
			holder.image.setVisibility(View.VISIBLE);
			if (position == mSelectPath.size()) {
				holder.btn_image_del.setVisibility(View.GONE);
				holder.image.setImageResource(R.drawable.hmm_icon_addpic);
				if (position == 5) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.btn_image_del.setVisibility(View.VISIBLE);
				GlideLoaderUtils.loadSquareImage(activity,
						mSelectPath.get(position), holder.image);
				holder.btn_image_del.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mSelectPath.remove(position);
						notifyDataSetChanged();
					}
				});
			}
		}

		return convertView;
	}

	public class ViewHolder {
		public ImageView image, btn_image_del;

	}
}