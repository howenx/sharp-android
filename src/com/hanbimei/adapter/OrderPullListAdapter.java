package com.hanbimei.adapter;

import java.util.ArrayList;
import java.util.List;

import com.hanbimei.R;
import com.hanbimei.activity.OrderDetailActivity;
import com.hanbimei.entity.Goods;
import com.hanbimei.entity.Order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OrderPullListAdapter extends BaseAdapter{

	private List<Order> data;
	private LayoutInflater inflater;
	private List<Goods> goods;
	private OrderListAdapter adapter;
	private Drawable drawable;
	private Activity activity;
	
	public OrderPullListAdapter(List<Order> data, Context mContext){
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
		goods = new ArrayList<Goods>();
		adapter = new OrderListAdapter(goods, mContext);
		drawable = activity.getResources().getDrawable(R.drawable.icon_jiantou);
		drawable.setBounds(0, 0, 40, 40);
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		final Order order = data.get(position);
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.order_list_layout, null);
			holder.order_header = (RelativeLayout) convertView.findViewById(R.id.order_header);
			holder.orderCode = (TextView) convertView.findViewById(R.id.order_nums);
			holder.state = (TextView) convertView.findViewById(R.id.order_state);
			holder.state.setCompoundDrawables(null, null, drawable, null);
			holder.date = (TextView) convertView.findViewById(R.id.order_date);
			holder.all_price = (TextView) convertView.findViewById(R.id.all_price);
			holder.go_pay = (TextView) convertView.findViewById(R.id.go_pay);
			holder.listView = (ListView) convertView.findViewById(R.id.my_listview);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.order_header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,OrderDetailActivity.class);
				intent.putExtra("orderId", order.getOrderId());
				activity.startActivity(intent);
			}
		});
		holder.orderCode.setText("订单号： " + order.getOrderNums());
		holder.state.setText(order.getState());
		holder.date.setText(order.getOrderTime());
		double allPrice = 0;
		for(int i = 0; i < order.getList().size(); i ++){
			allPrice = allPrice + Double.parseDouble(order.getList().get(i).getPrice());
		}
		holder.all_price.setText("应付金额： ¥" + allPrice);
		holder.listView.setAdapter(adapter);
		goods.clear();
		goods.addAll(order.getList());
		adapter.notifyDataSetChanged();
		return convertView;
	}
	private class ViewHolder{
		private RelativeLayout order_header;
		private TextView orderCode;
		private TextView state;
		private TextView date;
		private TextView all_price;
		private ListView listView;
		private TextView go_pay;
	}

}
