package com.hanmimei.adapter;

import java.util.List;

import com.hanmimei.R;
import com.hanmimei.activity.OrderDetailActivity;
import com.hanmimei.activity.OrderSubmitActivity;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.CustomListView;
import com.hanmimei.view.HorizontalListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class OrderPullListAdapter extends BaseAdapter {

	private List<Order> data;
	private LayoutInflater inflater;
	private OrderListAdapter adapter;
	private Drawable drawable;
	private Activity activity;

	public OrderPullListAdapter(List<Order> data, Context mContext) {
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.order_list_layout, null);
			holder.order_header = (RelativeLayout) convertView
					.findViewById(R.id.order_header);
			holder.orderCode = (TextView) convertView
					.findViewById(R.id.order_nums);
			holder.state = (TextView) convertView
					.findViewById(R.id.order_state);
			holder.state.setCompoundDrawables(null, null, drawable, null);
			holder.date = (TextView) convertView.findViewById(R.id.order_date);
			holder.all_price = (TextView) convertView
					.findViewById(R.id.all_price);
			holder.go_pay = (TextView) convertView.findViewById(R.id.go_pay);
			holder.listView = (HorizontalListView) convertView
					.findViewById(R.id.my_listview);
			holder.goods_post = (TextView) convertView.findViewById(R.id.goods_post);
			holder.bootom = (LinearLayout) convertView.findViewById(R.id.bottom);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//点击头部，跳转到订单详情页面
		holder.order_header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doJumpDetail(order);
			}
		});
//		点击list的子项，跳转到订单详情页面
		holder.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				doJumpDetail(order);
			}
		});
//		点击去支付，跳转到收银台界面
		holder.go_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setOrder(order);
				Intent intent = new Intent(activity, OrderSubmitActivity.class);
				intent.putExtra("orderInfo", orderInfo);
				activity.startActivity(intent);
				activity.finish();
			}
		});
		holder.orderCode.setText("订单号： " + order.getOrderId());
//		根据订单state判断订单的状态，I:初始化即未支付状态，S:成功，C：取消， F:失败，R:已收货，D:已经发货，J:拒收
		if (order.getOrderStatus().equals("I")) {
			holder.all_price.setVisibility(View.VISIBLE);
			holder.all_price.setText("应付金额： ¥" + order.getPayTotal());
			holder.state.setText("待支付");
			holder.goods_post.setVisibility(View.GONE);
			holder.go_pay.setVisibility(View.VISIBLE);
			holder.bootom.setVisibility(View.VISIBLE);
		} else if (order.getOrderStatus().equals("S")) {
			//holder.all_price.setText("已付金额： ¥" + order.getPayTotal());
			holder.bootom.setVisibility(View.GONE);
			holder.state.setText("待发货");
		} else if(order.getOrderStatus().equals("D")){
			holder.bootom.setVisibility(View.VISIBLE);
			holder.all_price.setVisibility(View.GONE);
			holder.state.setText("待收货");
			holder.go_pay.setVisibility(View.GONE);
			holder.goods_post.setVisibility(View.VISIBLE);
		}else if(order.getOrderStatus().equals("C")){
			holder.state.setText("已取消");
			holder.bootom.setVisibility(View.GONE);
		}else if(order.getOrderStatus().equals("R")){
			holder.state.setText("已完成");
			holder.bootom.setVisibility(View.GONE);
		}
		holder.date.setText(order.getOrderCreateAt());
		adapter = new OrderListAdapter(order.getList(), activity);
		holder.listView.setAdapter(adapter);
		return convertView;
	}

	//跳转到订单详情界面的方法
	private void doJumpDetail(Order order) {
		Intent intent = new Intent(activity, OrderDetailActivity.class);
		intent.putExtra("order", order);
		activity.startActivity(intent);
	}

	private class ViewHolder {
		private RelativeLayout order_header;
		private TextView orderCode;
		private TextView state;
		private TextView date;
		private TextView all_price;
		private HorizontalListView listView;
		private TextView go_pay;
		private TextView goods_post;
		private LinearLayout bootom;
	}

}
