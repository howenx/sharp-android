package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.adapter.OrderDetailListAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.CustomListView;
import com.hanmimei.view.TimerTextView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") 
public class OrderDetailActivity extends BaseActivity implements OnClickListener, TimeEndListner{

	private TextView header;
	private ImageView back;
	private Order order;
	private TextView order_code;
	private TextView order_state;
	private TextView pay_from;
	private TextView order_date;
	private TextView name;
	private TextView phone;
	private TextView address;
	private TextView nums;
	private TextView total_price;
	private TextView post_cost;
	private TextView cut_price;
	private TextView order_price;
	private TextView cancle;
	private TextView go_pay;
	private TextView idcard;
	private TextView tax;
	private TimerTextView attention;
	private TextView item_order_id;
	private HMMAddress addressInfo;
	private List<Sku> list;
	private CustomListView listView;
	private OrderDetailListAdapter adapter;
	
	private JSONObject object;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_detail_layout);
		order = new Order();
		addressInfo = new HMMAddress();
		list = new ArrayList<Sku>();
		adapter = new OrderDetailListAdapter(list, this);
		order = (Order) getIntent().getSerializableExtra("order");
		
		findView();
		if(order.getOrderStatus().equals("I")){
			loadData();
		}else{
			addressInfo = order.getAdress();
			list.addAll(order.getList());
			initView();
			adapter.notifyDataSetChanged();
		}
	}
	//当订单的状态为i时，加载详情数据
	private void loadData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = HttpUtils.getToken(UrlUtil.GET_ORDER_LIST_URL+"/" + order.getOrderId(),
						"id-token", OrderDetailActivity.this.getUser().getToken());
				List<Order> list = DataParser.parserOrder(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = list;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void initView() {
		order_code.setText("订单号：" + order.getOrderId());
//		I:初始化即未支付状态，S:成功，C：取消， F:失败，R:已收货，D:已经发货，J:拒收
		if(order.getOrderStatus().equals("S")){
			order_state.setText("订单状态：待发货");
		}else if(order.getOrderStatus().equals("I")){
			attention.setVisibility(View.VISIBLE);
			attention.setTimes(CommonUtil.getTimer(24 * 60 * 60 - order.getCountDown()/1000 - 300));
			attention.beginRun();
			order_state.setText("订单状态：待支付");
			cancle.setVisibility(View.VISIBLE);
			go_pay.setVisibility(View.VISIBLE);
		}else if(order.getOrderStatus().equals("C")){
			order_state.setText("订单状态：已取消");
			cancle.setVisibility(View.GONE);
		}else if(order.getOrderStatus().equals("D")){
			order_state.setText("订单状态：待收货");
			cancle.setVisibility(View.GONE);
		}else{
			order_state.setText("订单状态：已完成");
			cancle.setVisibility(View.GONE);
		}
		if(!order.getOrderSplitId().equals("null") && !order.getOrderSplitId().equals("")){
			item_order_id.setVisibility(View.VISIBLE);
			item_order_id.setText("子订单号：" + order.getOrderSplitId());
		}
		String payMethod = "";
		if(order.getPayMethod().equals("JD")){
			payMethod = "京东支付";
		}else if(order.getPayMethod().equals("APAY")){
			payMethod = "支付宝支付";
		}else if(order.getPayMethod().equals("WEIXIN")){
			payMethod = "微信支付";
		}
		pay_from.setText("支付方式：" + payMethod);
		order_date.setText("下单时间：" + order.getOrderCreateAt());
		name.setText("收货人：" + addressInfo.getName());
		phone.setText("手机号码：" + addressInfo.getPhone());
		idcard.setText("身份证：" + addressInfo.getIdCard());
		address.setText("收货地址：" + addressInfo.getCity() + addressInfo.getAdress());
		nums.setText("订单总件数：" + list.size());
		total_price.setText("商品总费用：" + order.getTotalFee());
		post_cost.setText("邮费：" + order.getShipFee());
		tax.setText("行邮税：" + order.getPostalFee());
		cut_price.setText("已优惠金额：" + order.getDiscount());
		order_price.setText("订单应付金额：" + order.getPayTotal());
	}
	private void findView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("订单详情");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		order_code = (TextView) findViewById(R.id.order_code);
		order_state = (TextView) findViewById(R.id.order_state);
		order_date = (TextView) findViewById(R.id.order_date);
		pay_from = (TextView) findViewById(R.id.pay_from);
		listView = (CustomListView) findViewById(R.id.sku_list);
		listView.setAdapter(adapter);
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		address = (TextView) findViewById(R.id.address);
		nums = (TextView) findViewById(R.id.nums);
		total_price = (TextView) findViewById(R.id.price);
		post_cost = (TextView) findViewById(R.id.post_cost);
		cut_price = (TextView) findViewById(R.id.cost_price);
		order_price = (TextView) findViewById(R.id.order_price);
		cancle = (TextView) findViewById(R.id.send);
		go_pay = (TextView) findViewById(R.id.go_pay);
		idcard = (TextView) findViewById(R.id.idcard);
		tax = (TextView) findViewById(R.id.tax);
		attention = (TimerTextView) findViewById(R.id.attention);
		attention.setTimeEndListner(this);
		item_order_id = (TextView) findViewById(R.id.item_order);
		cancle.setOnClickListener(this);
		go_pay.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.send:
			toObject();
			cancleOrder();
			break;
		case R.id.go_pay:
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrder(order);
			Intent intent = new Intent(this, OrderSubmitActivity.class);
			intent.putExtra("orderInfo", orderInfo);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	private void toObject() {
		object = new JSONObject();
		try {
			object.put("orderId", order.getOrderId());
			object.put("status", "C");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	private void cancleOrder() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String result = HttpUtils.get(UrlUtil.CANCLE_ORDER_URL + order.getOrderId(), getUser().getToken());
				Result isSuccess = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = isSuccess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@SuppressLint("HandlerLeak") 
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Result result = (Result) msg.obj;
				if(result.getCode() == 200){
					sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
					finish();
				}else{
					Toast.makeText(OrderDetailActivity.this, "取消订单失败！", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				List<Order> orders = (List<Order>) msg.obj;
				if(orders.size() > 0 && orders != null){
					order = orders.get(0);
					addressInfo = order.getAdress();
					list.addAll(order.getList());
					initView();
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		}		
	};
	@Override
	public void isTimeEnd() {
		sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
		cancle.setVisibility(View.GONE);
		go_pay.setVisibility(View.GONE);
	}
}
