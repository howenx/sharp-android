package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.adapter.OrderDetailListAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.entity.Result;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.view.CustomListView;
import com.hanmimei.view.TimerTextView;

/**
 * @author eric
 *
 */
@SuppressLint({ "NewApi", "InflateParams" }) 
public class OrderDetailActivity extends BaseActivity implements OnClickListener, TimeEndListner{
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
	
	private TextView payBackFee;
	private TextView reason;
	private TextView refund_state;
	private TextView contactTel;
	private TextView rejectReason;
	
	private HMMAddress addressInfo;
	private List<Sku> list;
	private CustomListView listView;
	private OrderDetailListAdapter adapter;
	private AlertDialog dialog;
	private JSONObject object;
	private ProgressDialog progressDialog;
	private boolean isShow = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_detail_layout);
		order = new Order();
		addressInfo = new HMMAddress();
		list = new ArrayList<Sku>();
		order = (Order) getIntent().getSerializableExtra("order");
		if(order.getOrderStatus().equals("R")){
			isShow = true;
		}
		adapter = new OrderDetailListAdapter(list, this, isShow);
		findView();
//		order.getOrderStatus().equals("R") ||
		if(order.getOrderStatus().equals("C")){
			ActionBarUtil.setActionBarStyle(this, "订单详情", R.drawable.hmm_edit_delete, true, this);
		}else{
			ActionBarUtil.setActionBarStyle(this, "订单详情");
		}
		if(order.getOrderStatus().equals("I")){
			loadData();
		}else{
			addressInfo = order.getAdress();
			list.addAll(order.getList());
			initView();
			adapter.setOrderId(order.getOrderId(), order.getOrderSplitId());
			adapter.notifyDataSetChanged();
		}
	}
	//当订单的状态为i时，加载详情数据
	private void loadData() {
		getLoading().show();
		Http2Utils.doGetRequestTask(this, getHeaders(), UrlUtil.GET_ORDER_LIST_URL+"/" + order.getOrderId(), new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				List<Order> orders = DataParser.parserOrder(result);
				getLoading().dismiss();
				if(orders.size() > 0 && orders != null){
					order = orders.get(0);
					addressInfo = order.getAdress();
					list.addAll(order.getList());
					initView();
					adapter.notifyDataSetChanged();
					adapter.setOrderId(order.getOrderId(), order.getOrderSplitId());
				}else{
					ToastUtils.Toast(OrderDetailActivity.this, "加载数据失败");
				}
			}
			
			@Override
			public void onError() {
				ToastUtils.Toast(OrderDetailActivity.this, "加载数据失败,请检查您的网络");
			}
		});
		
	};
	private void initView() {
		order_code.setText("订单号：" + order.getOrderId());
//		I:初始化即未支付状态，S:成功，C：取消， F:失败，R:已收货，D:已经发货，J:拒收
		if(order.getOrderStatus().equals("S")){
			order_state.setText("订单状态：待发货");
			findViewById(R.id.go_money).setVisibility(View.VISIBLE);
		}else if(order.getOrderStatus().equals("I")){
			attention.setVisibility(View.VISIBLE);
			attention.setTimes(CommonUtil.getTimer(order.getCountDown()/1000 - 300),"订单超过24小时，已经过期");
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
		phone.setText("手机号码：" + addressInfo.getPhone().substring(0, 3) + "****" + addressInfo.getPhone().substring(7, addressInfo.getPhone().length()));
		idcard.setText("身份证：" + addressInfo.getIdCard().substring(0, 5) + "********" + addressInfo.getIdCard().substring(14, addressInfo.getIdCard().length()));
		address.setText("收货地址：" + addressInfo.getCity() + addressInfo.getAdress());
		nums.setText("订单总件数：" + list.size());
		total_price.setText("商品总费用：" + CommonUtil.doubleTrans(order.getTotalFee()));
		post_cost.setText("邮费：" + CommonUtil.doubleTrans(order.getShipFee()));
		tax.setText("行邮税：" + CommonUtil.doubleTrans(order.getPostalFee()));
		cut_price.setText("已优惠金额：" + CommonUtil.doubleTrans(order.getDiscount()));
		order_price.setText("订单应付金额：" + CommonUtil.doubleTrans(order.getPayTotal()));
		if(order.getRefund() != null){
			findViewById(R.id.linear_refund).setVisibility(View.VISIBLE);
			payBackFee.setText("退款金额：" + order.getRefund().getPayBackFee());
			reason.setText("退款原因：" + order.getRefund().getReason());
			refund_state.setText("退款状态：" + order.getRefund().getState());
			contactTel.setText("联系电话：" + order.getRefund().getContactTel());
			if(order.getRefund().getState().equals("R")){
				rejectReason.setText("拒绝原因：" + order.getRefund().getRejectReason());
			}else{
				rejectReason.setVisibility(View.GONE);
			}
		}
	}
	private void findView() {
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
		findViewById(R.id.go_money).setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(OrderDetailActivity.this,
						GoodsDetailActivity.class);
				intent.putExtra("url", list.get(arg2).getInvUrl());
				startActivity(intent);
			}
		});
		
		payBackFee = (TextView) findViewById(R.id.refund_money);
		reason = (TextView) findViewById(R.id.reason);
		refund_state = (TextView) findViewById(R.id.refund_state);
		contactTel = (TextView) findViewById(R.id.contactTel);
		rejectReason = (TextView) findViewById(R.id.rejectReason);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			showDialog();
			break;
		case R.id.go_pay:
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setOrderId(order.getOrderId());
			Intent intent = new Intent(this, OrderSubmitActivity.class);
			intent.putExtra("orderInfo", orderInfo);
			startActivity(intent);
			finish();
			break;
		case R.id.setting:
			showDelDialog();
			break;
		case R.id.go_money:
			Intent intent1 = new Intent(this, CustomerServiceActivity.class);
			intent1.putExtra("order", order);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}
	private void showDelDialog() {
		dialog = AlertDialogUtils.showDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				delOrder();
			}
		});
	}
	private void delOrder() {
		dialog.dismiss();
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("正在删除...");
		progressDialog.show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String result = HttpUtils.get(UrlUtil.DEL_ORDER + order.getOrderId(), getUser().getToken());
				HMessage hMessage = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(3);
				msg.obj = hMessage;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void showDialog(){
		dialog = AlertDialogUtils.showCancelDialog(this, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				toObject();
				cancleOrder();
			}
		});
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
		getLoading().show();
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
				getLoading().dismiss();
				Result result = (Result) msg.obj;
				if(result.getCode() == 200){
					sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
					finish();
				}else{
					Toast.makeText(OrderDetailActivity.this, "取消订单失败！", Toast.LENGTH_SHORT).show();
				}
				break;
//			case 2:
//				getLoading().dismiss();
//				List<Order> orders = (List<Order>) msg.obj;
//				if(orders.size() > 0 && orders != null){
//					order = orders.get(0);
//					addressInfo = order.getAdress();
//					list.addAll(order.getList());
//					initView();
//					adapter.notifyDataSetChanged();
//				}
//				break;
			case 3:
				progressDialog.dismiss();
				HMessage hMessage = (HMessage) msg.obj;
				if(hMessage.getCode() != null){
					if(hMessage.getCode() == 200){
						OrderDetailActivity.this.sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
						finish();
					}else{
						Toast.makeText(OrderDetailActivity.this, "删除失败，请检查您的网络", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(OrderDetailActivity.this, "删除失败，请检查您的网络", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}		
	};
	private boolean isSendBroad = true;
	@Override
	public void isTimeEnd() {
		if(isSendBroad){
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_CANCLE_ORDER));
			cancle.setVisibility(View.GONE);
			go_pay.setVisibility(View.GONE);
			isSendBroad = false;
		}
	}
	
}
