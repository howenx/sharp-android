package com.hanmimei.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.entity.MessageInfo;
import com.hanmimei.utils.ImageLoaderUtils;
import com.sina.weibo.sdk.utils.ImageUtils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyMsgAdapter extends BaseAdapter {

	private List<MessageInfo> list;
	private LayoutInflater inflater;
	private boolean showImg;
	private Activity mActivity;
	public MyMsgAdapter(List<MessageInfo> list,Context mContext, boolean showImg){
		this.list = list;
		this.inflater = LayoutInflater.from(mContext);
		this.showImg = showImg;
		this.mActivity = (Activity) mContext;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		MessageInfo info = list.get(position);
		ViewHold hold = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.my_msg_item, null);
			hold = new ViewHold();
			hold.time = (TextView) convertView.findViewById(R.id.time);
			hold.msg = (TextView) convertView.findViewById(R.id.msg);
			hold.content = (TextView) convertView.findViewById(R.id.content);
			hold.img = (ImageView) convertView.findViewById(R.id.img);
			if(showImg){
				convertView.findViewById(R.id.img).setVisibility(View.VISIBLE);
			}else{
				convertView.findViewById(R.id.img).setVisibility(View.GONE);
			}
			convertView.setTag(hold);
		} else{
			hold = (ViewHold) convertView.getTag();
		}
		hold.time.setText(info.getCreateAt()+"");
		hold.msg.setText(info.getMsgTitle());
		hold.content.setText(info.getMsgContent());
		if(showImg){
			ImageLoaderUtils.loadImage(mActivity, info.getMsgImg(), hold.img);
		}
		return convertView;
	}
	
	private class ViewHold{
		private ImageView img;
		private TextView time;
		private TextView msg;
		private TextView content;
	}

}
