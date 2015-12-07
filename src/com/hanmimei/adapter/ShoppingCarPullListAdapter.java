package com.hanmimei.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.entity.Customs;
import com.hanmimei.view.CustomListView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShoppingCarPullListAdapter extends BaseAdapter {

	private List<Customs> data;
	private LayoutInflater inflater;
	private ShoppingCarAdapter adapter;
	private Activity activity;
	
	public ShoppingCarPullListAdapter(List<Customs> data, Context mContext){
		this.data = data;
		inflater = LayoutInflater.from(mContext);
		activity = (Activity) mContext;
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		Customs custom = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.shoppingcar_pull_item, null);
			holder = new ViewHolder();
			holder.area = (TextView) convertView.findViewById(R.id.area);
			holder.listView = (CustomListView) convertView.findViewById(R.id.my_listview);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.area.setText(custom.getInvArea());
		adapter = new ShoppingCarAdapter(custom.getList(), activity);
		holder.listView.setAdapter(adapter);
		return convertView;
	}
	private class ViewHolder{
		private TextView area;
		private CustomListView listView;
	}

}
