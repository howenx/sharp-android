package com.hanmimei.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.entity.MessageInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyMsgAdapter extends BaseAdapter {

	private List<MessageInfo> list;
	private LayoutInflater inflater;
	public MyMsgAdapter(List<MessageInfo> list,Context mContext){
		this.list = list;
		this.inflater = LayoutInflater.from(mContext);
		
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
			convertView.setTag(hold);
		} else{
			hold = (ViewHold) convertView.getTag();
		}
		hold.time.setText(info.getCreateAt()+"");
		hold.msg.setText(info.getMsgTitle());
		hold.content.setText(info.getMsgContent());
		return convertView;
	}
	
	private class ViewHold{
		private TextView time;
		private TextView msg;
		private TextView content;
	}

}
