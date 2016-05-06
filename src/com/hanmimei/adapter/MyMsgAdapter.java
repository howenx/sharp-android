package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.MessageInfo;
import com.hanmimei.utils.DateUtils;
import com.hanmimei.utils.GlideLoaderUtils;

/**
 * @author eric
 *
 */
public class MyMsgAdapter extends BaseAdapter {

	private List<MessageInfo> list;
	private LayoutInflater inflater;
	private String type;
	private Activity mActivity;
	public MyMsgAdapter(List<MessageInfo> list,Context mContext, String type){
		this.list = list;
		this.inflater = LayoutInflater.from(mContext);
		this.type = type;
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
			hold.go = (TextView) convertView.findViewById(R.id.go);
			if(type.equals("goods") || type.equals("discount")){
				convertView.findViewById(R.id.img).setVisibility(View.VISIBLE);
			}else{
				convertView.findViewById(R.id.img).setVisibility(View.GONE);
			}
			convertView.setTag(hold);
		} else{
			hold = (ViewHold) convertView.getTag();
		}
		hold.time.setText(DateUtils.getTimeDiffDesc(DateUtils.getDate(info.getCreateAt())));
		hold.msg.setText(info.getMsgTitle());
		hold.content.setText(info.getMsgContent());
		if(type.equals("goods") || type.equals("discount")){
			GlideLoaderUtils.loadRectImage(mActivity, hold.img, info.getMsgImg(), 2, 1);
		}
		hold.go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		return convertView;
	}
	
	private class ViewHold{
		private ImageView img;
		private TextView time;
		private TextView msg;
		private TextView content;
		private TextView go;
	}

}
