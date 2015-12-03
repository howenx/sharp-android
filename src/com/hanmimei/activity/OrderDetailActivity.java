package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.hanmimei.R;
import com.hanmimei.adapter.OrderDetailListAdapter;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.Adress;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.CustomListView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("NewApi") 
public class OrderDetailActivity extends BaseActivity implements OnClickListener{

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
	private Adress addressInfo;
	private List<Sku> list;
	private CustomListView listView;
	private OrderDetailListAdapter adapter;
	
	private JSONObject object;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_detail_layout);
//		getActionBar().hide();
		order = new Order();
		addressInfo = new Adress();
		list = new ArrayList<Sku>();
		adapter = new OrderDetailListAdapter(list, this);
		order = (Order) getIntent().getSerializableExtra("order");
		addressInfo = order.getAdress();
		list.addAll(order.getList());
		findView();
		initView();
		adapter.notifyDataSetChanged();
	}
	private void initView() {
		order_code.setText("订单号：" + order.getOrderId());
		if(order.getOrderStatus().equals("S")){
			order_state.setText("订单状态：已支付");
			cancle.setVisibility(View.GONE);
		}else if(order.getOrderStatus().equals("I")){
			order_state.setText("订单状态：待支付");
		}else{
			order_state.setText("订单状态：已取消");
			cancle.setVisibility(View.GONE);
		}
		pay_from.setText("支付方式：" + order.getPayMethod());
		order_date.setText("下单时间：" + order.getOrderCreateAt());
		pay_from.setText("支付方式：" + order.getPayMethod());
		name.setText("收货人：" + addressInfo.getName());
		phone.setText("手机号码：" + addressInfo.getPhone());
		address.setText("收货地址：" + addressInfo.getCity() + addressInfo.getAdress());
		nums.setText("订单总件数：" + list.size());
		int goods_price = 0;
		for(int i = 0; i < list.size(); i ++){
			goods_price = goods_price + list.get(i).getPrice() * list.get(i).getAmount();
		}
		total_price.setText("商品总费用：" + goods_price);
		post_cost.setText("邮费：" + order.getShipFee());
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
		cancle.setOnClickListener(this);
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
				String result = HttpUtils.post("http://172.28.3.18:9003/client/order/state/update", object, "id-token", getUser().getToken());
				Result isSuccess = DataParser.parserResult(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = isSuccess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}
		
	};
}
