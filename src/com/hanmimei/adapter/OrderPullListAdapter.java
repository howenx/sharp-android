package com.hanmimei.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import com.hanmimei.R;
import com.hanmimei.activity.BaseActivity;
import com.hanmimei.activity.LogisticsActivity;
import com.hanmimei.activity.OrderDetailActivity;
import com.hanmimei.activity.OrderSubmitActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.KeyWordUtil;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.HorizontalListView;

/**
 * @author eric
 * 
 */
@SuppressLint("InflateParams")
public class OrderPullListAdapter extends BaseAdapter {

	private List<Order> data;
	private LayoutInflater inflater;
	private OrderListAdapter adapter;
	private BaseActivity activity;
	private Order orderT;

	public OrderPullListAdapter(List<Order> data, Context mContext) {
		this.data = data;
		orderT = new Order();
		activity = (BaseActivity) mContext;
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
			holder.date = (TextView) convertView.findViewById(R.id.order_date);
			holder.all_price = (TextView) convertView
					.findViewById(R.id.all_price);
			holder.go_pay = (TextView) convertView.findViewById(R.id.go_pay);
			holder.apply_customer = (TextView) convertView
					.findViewById(R.id.apply_customer);
			holder.listView = (HorizontalListView) convertView
					.findViewById(R.id.my_listview);
			holder.goods_post = (TextView) convertView
					.findViewById(R.id.goods_post);
			holder.bootom = (LinearLayout) convertView
					.findViewById(R.id.bottom);
			holder.do_shou = (TextView) convertView.findViewById(R.id.do_shou);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 点击头部，跳转到订单详情页面
		holder.order_header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doJumpDetail(order);
			}
		});
		// 点击list的子项，跳转到订单详情页面
		holder.listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				doJumpDetail(order);
			}
		});
		// 点击去支付，跳转到收银台界面
		holder.go_pay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getIsTimeOver(order);
				orderT = order;
				// doPay(order);
			}
		});
		// 点击确认收货，弹窗提示是否确定
		holder.do_shou.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				confirmDelivery(order);
			}
		});
		holder.goods_post.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(activity,LogisticsActivity.class);
				intent.putExtra("orderId", order.getOrderId());
				activity.startActivity(intent);
			}
		});
		holder.orderCode.setText("订单号： " + order.getOrderId());
		// 根据订单state判断订单的状态，I:初始化即未支付状态，S:成功，C：取消， F:失败，R:已收货，D:已经发货，J:拒收， T已退款
		if (order.getOrderStatus().equals("I")) {
			holder.all_price.setVisibility(View.VISIBLE);
			holder.all_price.setText("应付金额：¥"
					+ CommonUtil.doubleTrans(order.getPayTotal()));
			holder.state.setText("待支付");
			//颜色
			holder.state.setTextColor(activity.getResources().getColor(R.color.theme));
			holder.goods_post.setVisibility(View.GONE);
			holder.go_pay.setVisibility(View.VISIBLE);
			holder.go_pay.setText("去支付");
			holder.apply_customer.setVisibility(View.GONE);
			holder.bootom.setVisibility(View.VISIBLE);
			holder.do_shou.setVisibility(View.GONE);
		} else if (order.getOrderStatus().equals("S")) {
			holder.bootom.setVisibility(View.GONE);
			//颜色
			holder.state.setTextColor(activity.getResources().getColor(R.color.theme));
			if(order.getRefund() !=null && !order.getRefund().getState().equals("R")){
//					String state = "待发货(已锁定)";
//					KeyWordUtil.setDifrentFontColor12(activity, holder.state, state, 2, state.length());
				holder.state.setText("待发货(已锁定)");
			}else{
				holder.state.setText("待发货");
			}
		} else if (order.getOrderStatus().equals("D")) {
			holder.bootom.setVisibility(View.VISIBLE);
			holder.all_price.setVisibility(View.GONE);
			holder.state.setText("待收货");
			//颜色
			holder.state.setTextColor(activity.getResources().getColor(R.color.theme));
			holder.do_shou.setVisibility(View.VISIBLE);
			holder.go_pay.setVisibility(View.GONE);
			holder.goods_post.setVisibility(View.VISIBLE);
			holder.apply_customer.setVisibility(View.GONE);
		} else if (order.getOrderStatus().equals("C")) {
			holder.state.setText("已取消");
			//颜色
			holder.state.setTextColor(activity.getResources().getColor(R.color.fontcolor));
			holder.bootom.setVisibility(View.GONE);
			holder.apply_customer.setVisibility(View.GONE);
		} else if (order.getOrderStatus().equals("R")) {
			holder.state.setText("已完成");
			//颜色
			holder.state.setTextColor(activity.getResources().getColor(R.color.green));
			holder.bootom.setVisibility(View.VISIBLE);
			holder.do_shou.setVisibility(View.GONE);
			holder.go_pay.setVisibility(View.GONE);
			holder.all_price.setVisibility(View.GONE);
			holder.goods_post.setVisibility(View.VISIBLE);
			holder.apply_customer.setVisibility(View.GONE);
		}else if(order.getOrderStatus().equals("T")){
			holder.state.setText("已退款");
			//颜色
			holder.state.setTextColor(activity.getResources().getColor(R.color.fontcolor));
			holder.bootom.setVisibility(View.GONE);
		}
		holder.date.setText(order.getOrderCreateAt());
		adapter = new OrderListAdapter(order.getList(), activity);
		holder.listView.setAdapter(adapter);
		return convertView;
	}

	private void getIsTimeOver(final Order order) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.get(
						UrlUtil.GET_ORDER_IS_TIME + order.getOrderId(),
						activity.getUser().getToken());
				HMessage hMessage = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hMessage;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				HMessage hMessage = (HMessage) msg.obj;
				if (hMessage.getCode() != null) {
					if (hMessage.getCode() == 200) {
						doPay(orderT);
					} else {
						activity.sendBroadcast(new Intent(
								AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
						Toast.makeText(activity, "订单已经过期，已自动取消",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(activity, "请检查您的网络", Toast.LENGTH_SHORT)
							.show();
				}

				break;

			default:
				break;
			}
		}

	};

	private void doPay(Order order) {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setOrderId(order.getOrderId());
		Intent intent = new Intent(activity, OrderSubmitActivity.class);
		intent.putExtra("orderInfo", orderInfo);
		intent.putExtra("orderType", "item");
		activity.startActivity(intent);
		activity.finish();
	}

	// 跳转到订单详情界面的方法
	private void doJumpDetail(Order order) {
		Intent intent = new Intent(activity, OrderDetailActivity.class);
		intent.putExtra("order", order);
		activity.startActivity(intent);
	}

	// 确认收货操作
	private void confirmDelivery(final Order order) {
		AlertDialogUtils.showDeliveryDialog(activity, new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmDeliveryR(order);
			}
		});
	}

	private void confirmDeliveryR(Order order) {
		activity.getLoading().show();
		Http2Utils.doGetRequestTask(activity, activity.getHeaders(),
				UrlUtil.CONFIRM_DELIVERY + order.getOrderId(),
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						activity.getLoading().dismiss();
						activity.sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
					}

					@Override
					public void onError() {
						activity.getLoading().dismiss();
					}
				});
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
		private TextView apply_customer;
		private LinearLayout bootom;
		private TextView do_shou;
	}

}
