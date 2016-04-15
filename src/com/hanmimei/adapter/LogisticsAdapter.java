/**
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author A18ccms A18ccms_gmail_com   
 * @date 2016-4-15 上午11:06:26 
**/
package com.hanmimei.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.entity.LogisticsData;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author eric
 *
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN) public class LogisticsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<LogisticsData> data;
	private Activity activity;
	public LogisticsAdapter(Context mContext, List<LogisticsData> data){
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return data.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		LogisticsData logisticsData = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.logistics_item_layout, null);
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.xianView = convertView.findViewById(R.id.view_1);
			holder.image = (TextView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(position == 0){
			holder.xianView.setVisibility(View.INVISIBLE);
			holder.image.setBackground(activity.getResources().getDrawable(R.drawable.theme_radio_bg));
		}else{
			holder.xianView.setVisibility(View.VISIBLE);
			holder.image.setBackground(activity.getResources().getDrawable(R.drawable.huise_radio_bg));
		}
		holder.content.setText(logisticsData.getContent());
		holder.time.setText(logisticsData.getTime());
		return convertView;
	}
	private class ViewHolder{
		private TextView content;
		private TextView time;
		private View xianView;
		private TextView image;
	}

}
