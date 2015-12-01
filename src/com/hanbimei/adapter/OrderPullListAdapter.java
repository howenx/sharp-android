package com.hanbimei.adapter;

import java.util.List;
import com.hanbimei.R;
import com.hanbimei.activity.OrderDetailActivity;
import com.hanbimei.entity.Order;
import com.hanbimei.view.CustomListView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams") 
public class OrderPullListAdapter extends BaseAdapter{

	private List<Order> data;
	private LayoutInflater inflater;
	private OrderListAdapter adapter;
	private Drawable drawable;
	private Activity activity;
	
	public OrderPullListAdapter(List<Order> data, Context mContext){
		this.data = data;
		activity = (Activity) mContext;
		inflater = LayoutInflater.from(mContext);
		drawable = activity.getResources().getDrawable(R.drawable.icon_jiantou);
		drawable.setBounds(0, 0, 40, 40);
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
			holder.listView = (CustomListView) convertView.findViewById(R.id.my_listview);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.order_header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity,OrderDetailActivity.class);
				intent.putExtra("order", order);
				activity.startActivity(intent);
			}
		});
		holder.go_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(activity, "亲，您需要支付 225¥", Toast.LENGTH_SHORT).show();
			}
		});
		holder.orderCode.setText("订单号： " + order.getOrderId());
		if(order.getOrderStatus().equals("I")){
			holder.all_price.setText("应付金额： ¥" + order.getPayTotal());
			holder.state.setText("待支付");
		}else if(order.getOrderStatus().equals("S")){
			holder.go_pay.setText("确定收货");
			holder.all_price.setText("已付金额： ¥" + order.getPayTotal());
			holder.state.setText("已支付");
		}else{
			holder.all_price.setText("应付金额： ¥ 0.00");
			holder.state.setText("已取消");
			holder.go_pay.setVisibility(View.GONE);
		}
		holder.date.setText(order.getOrderCreateAt());
		adapter = new OrderListAdapter(order.getList(), activity);
		holder.listView.setAdapter(adapter);
		return convertView;
	}
	private class ViewHolder{
		private RelativeLayout order_header;
		private TextView orderCode;
		private TextView state;
		private TextView date;
		private TextView all_price;
		private CustomListView listView;
		private TextView go_pay;
	}

}
