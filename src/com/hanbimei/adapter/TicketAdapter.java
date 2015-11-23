package com.hanbimei.adapter;

import java.util.List;
import com.hanbimei.R;
import com.hanbimei.entity.Ticket;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TicketAdapter extends BaseAdapter {
	private List<Ticket> data;
	private LayoutInflater inflater;

	public TicketAdapter (List<Ticket> data, Context mContext){
		this.data = data;
		inflater = LayoutInflater.from(mContext);
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
		Ticket ticket = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.ticket_item, null);
			holder = new ViewHolder();
			holder.money = (TextView) convertView.findViewById(R.id.money);
			holder.more_pay = (TextView) convertView.findViewById(R.id.more_pay);
			holder.use_date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.money.setText(ticket.getCut_price() + "");
		holder.more_pay.setText("全场满" + ticket.getMore_price() + "可用");
		holder.use_date.setText("有效期至：" + ticket.getUse_date());
		return convertView;
	}
	private class ViewHolder{
		private TextView money;
		private TextView more_pay;
		private TextView use_date;
	}

}
