package com.hanmimei.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.entity.Goods;
import com.hanmimei.utils.KeyWordUtil;

public class PingouListAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private List<Goods> data;
	private Activity activity;
	
	public PingouListAdapter(Context mContext,List<Goods> data){
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.pingou_list_item_layout, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.zhekou = (TextView) convertView.findViewById(R.id.zhekou);
			holder.iniPrice = (TextView) convertView.findViewById(R.id.shiprice);
			holder.zhePrice = (TextView) convertView.findViewById(R.id.zhekouprice);
			holder.pinNum = (TextView) convertView.findViewById(R.id.pnum);
			holder.pinPrice = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.title.setText("韩国发希fascy陆心媛娃娃护手霜 80ml3支深层滋润保湿五件套");
//		holder.zhekou.setText("20%");
//		holder.iniPrice.setText("市场价：¥600");
//		holder.zhePrice.setText(KeyWordUtil.matcherSearchTitle(activity.getResources().getColor(R.color.theme), "折扣价：¥480", "¥480"));
//		holder.pinNum.setText("（4人拼购价）");
//		holder.pinPrice.setText("¥400");
		return convertView;
	}
	private class ViewHolder{
		private ImageView img;
		private TextView title;
		private TextView zhekou;
		private TextView iniPrice;
		private TextView zhePrice;
		private TextView pinNum;
		private TextView pinPrice;
	}
}
