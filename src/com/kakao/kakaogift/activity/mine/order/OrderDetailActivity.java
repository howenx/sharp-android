package com.kakao.kakaogift.activity.mine.order;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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

import com.flyco.dialog.widget.NormalDialog;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.apply.ApplyRefundActivity;
import com.kakao.kakaogift.activity.balance.OrderSubmitActivity;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.goods.detail.GoodsDetailActivity;
import com.kakao.kakaogift.activity.goods.pin.PingouDetailActivity;
import com.kakao.kakaogift.activity.mine.comment.CommentGoodsActivity;
import com.kakao.kakaogift.activity.mine.order.adapter.OrderDetailListAdapter;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.HAddress;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.Order;
import com.kakao.kakaogift.entity.OrderInfo;
import com.kakao.kakaogift.entity.OrderList;
import com.kakao.kakaogift.entity.Result;
import com.kakao.kakaogift.entity.Sku;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.override.TimeEndListner;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.HttpUtils;
import com.kakao.kakaogift.utils.KeyWordUtil;
import com.kakao.kakaogift.utils.ToastUtils;
import com.kakao.kakaogift.view.CustomListView;
import com.kakao.kakaogift.view.TimerTextView;

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
//	private TextView idcard;
	private TextView tax;
	private TimerTextView attention;
	private TextView item_order_id;
	private TextView go_wuliu;
	private TextView go_comment;

	private TextView payBackFee;
	private TextView reason;
	private TextView refund_state;
	private TextView contactTel;
	private TextView rejectReason;
	
	private HAddress addressInfo;
	private List<Sku> list;
	private CustomListView listView;
	private OrderDetailListAdapter adapter;
	private NormalDialog dialog;
	private JSONObject object;
	private ProgressDialog progressDialog;
	private boolean isShow = false;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_detail_layout);
		order = new Order();
		addressInfo = new HAddress();
		list = new ArrayList<Sku>();
		order = (Order) getIntent().getSerializableExtra("order");
		if(order.getOrderStatus().equals("R")){
			isShow = true;
		}
		adapter = new OrderDetailListAdapter(list, this, isShow);
		findView();
		//取消，退款可以删除 || order.getOrderStatus().equals("T")
		if(order.getOrderStatus().equals("C") || order.getOrderStatus().equals("F")){
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
		VolleyHttp.doGetRequestTask( getHeaders(), UrlUtil.GET_ORDER_LIST_URL+"/" + order.getOrderId(), new VolleyJsonCallback() {
			
			@Override
			public void onSuccess(String result) {
				OrderList orderList = DataParser.parserOrder(result);
				getLoading().dismiss();
				if(orderList.getList().size() > 0 && orderList.getList() != null){
					order = orderList.getList().get(0);
					addressInfo = order.getAdress();
					list.clear();
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
//		I:初始化即未支付状态，S:成功，C：取消， F:失败，R:已收货，D:已经发货，J:拒收， T已退款 ，F支付失败
		if(order.getOrderStatus().equals("S")){
			if(order.getRefund() != null){
				findViewById(R.id.bottom).setVisibility(View.GONE);
				if(order.getRefund().getState().equals("R")){
					order_state.setText("订单状态：待发货");
				}else{
					String state = "订单状态：待发货(已锁定)";
					KeyWordUtil.setDifrentFontColor12(this, order_state, state, 7, state.length());
				}
				findViewById(R.id.go_money).setVisibility(View.GONE);
				findViewById(R.id.linear_refund).setVisibility(View.VISIBLE);
				if(order.getRefund().getPayBackFee() !=null){
					payBackFee.setText("退款金额：" + order.getRefund().getPayBackFee());
				}else{
					payBackFee.setVisibility(View.GONE);
				}
				reason.setText("退款原因：" + order.getRefund().getReason());
				if(order.getRefund().getState() !=null){
					KeyWordUtil.setDifferentFontColor(this, refund_state,order.getRefund().getStateText(), 5, order.getRefund().getStateText().length());
				}else{
					refund_state.setVisibility(View.GONE);
				}
				if(order.getRefund().getContactTel() !=null){
					contactTel.setText("联系电话：" + order.getRefund().getContactTel());
				}else{
					contactTel.setVisibility(View.GONE);
				}
				if(order.getRefund().getState().equals("R")){
					rejectReason.setText("拒绝原因：" + order.getRefund().getRejectReason());
				}else{
					rejectReason.setVisibility(View.GONE);
				}
			}else {
				order_state.setText("订单状态：待发货");
				findViewById(R.id.go_money).setVisibility(View.VISIBLE);
			}
		}else if(order.getOrderStatus().equals("I")){
			attention.setVisibility(View.VISIBLE);
			attention.setTimes(CommonUtils.getTimer(order.getCountDown()/1000 - 10),"订单超过24小时，已经过期");
			attention.beginRun();
			order_state.setText("订单状态：待支付");
			cancle.setVisibility(View.VISIBLE);
			go_pay.setVisibility(View.VISIBLE);
		}else if(order.getOrderStatus().equals("C")){
			findViewById(R.id.bottom).setVisibility(View.GONE);
			order_state.setText("订单状态：已取消");
		}else if(order.getOrderStatus().equals("D")){
			findViewById(R.id.bottom).setVisibility(View.GONE);
			order_state.setText("订单状态：待收货");
		}else if(order.getOrderStatus().equals("T")){
			findViewById(R.id.linear_refund).setVisibility(View.VISIBLE);
			if(order.getRefund().getPayBackFee() !=null){
				payBackFee.setText("退款金额：" + order.getRefund().getPayBackFee());
			}else{
				payBackFee.setVisibility(View.GONE);
			}
			reason.setText("退款原因：" + order.getRefund().getReason());
			if(order.getRefund().getState() !=null){
				KeyWordUtil.setDifferentFontColor(this, refund_state,order.getRefund().getStateText(), 5, order.getRefund().getStateText().length());
			}else{
				refund_state.setVisibility(View.GONE);
			}
			if(order.getRefund().getContactTel() !=null){
				contactTel.setText("联系电话：" + order.getRefund().getContactTel());
			}else{
				contactTel.setVisibility(View.GONE);
			}
			if(order.getRefund().getState().equals("R")){
				rejectReason.setText("拒绝原因：" + order.getRefund().getRejectReason());
			}else{
				rejectReason.setVisibility(View.GONE);
			}
			order_state.setText("订单状态：已退款");
			findViewById(R.id.bottom).setVisibility(View.GONE);
		}else if(order.getOrderStatus().equals("R")){
			findViewById(R.id.bottom).setVisibility(View.VISIBLE);
			go_wuliu.setVisibility(View.VISIBLE);
//			if(order.getRemark().equals("N")){
			go_comment.setVisibility(View.VISIBLE);
//			}else{
//				go_comment.setVisibility(View.GONE);
//			}
			
			order_state.setText("订单状态：已完成");
		}else if(order.getOrderStatus().equals("F")){
			findViewById(R.id.bottom).setVisibility(View.GONE);
			findViewById(R.id.linear_att).setVisibility(View.VISIBLE);
			order_state.setText("订单状态：交易失败");
		}
		if(!order.getOrderSplitId().equals("null") && !order.getOrderSplitId().equals("")){
			item_order_id.setVisibility(View.VISIBLE);
			item_order_id.setText("子订单号：" + order.getOrderSplitId());
		}
		String payMethod = "在线支付";
		if(order.getPayMethod().equals("JD")){
			payMethod = "京东支付";
		}else if(order.getPayMethod().equals("ALIPAY")){
			payMethod = "支付宝支付";
		}else if(order.getPayMethod().equals("WEIXIN")){
			payMethod = "微信支付";
		}
		pay_from.setText("支付方式：" + payMethod);
		order_date.setText("下单时间：" + order.getOrderCreateAt());
		name.setText("收货人：" + addressInfo.getName());
		phone.setText("手机号码：" + addressInfo.getPhone().substring(0, 3) + "****" + addressInfo.getPhone().substring(7, addressInfo.getPhone().length()));
//		idcard.setText("身份证：" + addressInfo.getIdCard().substring(0, 5) + "********" + addressInfo.getIdCard().substring(14, addressInfo.getIdCard().length()));
		address.setText("收货地址：" + addressInfo.getCity() + addressInfo.getAdress());
		nums.setText("订单总件数：" + list.size());
		total_price.setText("商品总费用：" + CommonUtils.doubleTrans(order.getTotalFee()));
		post_cost.setText("邮费：" + CommonUtils.doubleTrans(order.getShipFee()));
		tax.setText("行邮税：" + CommonUtils.doubleTrans(order.getPostalFee()));
		cut_price.setText("已优惠金额：" + CommonUtils.doubleTrans(order.getDiscount()));
		order_price.setText("订单应付金额：" + CommonUtils.doubleTrans(order.getPayTotal()));
		
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
//		idcard = (TextView) findViewById(R.id.idcard);
		tax = (TextView) findViewById(R.id.tax);
		attention = (TimerTextView) findViewById(R.id.attention);
		attention.setTimeEndListner(this);
		item_order_id = (TextView) findViewById(R.id.item_order);
		cancle.setOnClickListener(this);
		go_pay.setOnClickListener(this);
		findViewById(R.id.go_money).setOnClickListener(this);
		go_wuliu = (TextView) findViewById(R.id.go_wuliu);
		go_comment = (TextView) findViewById(R.id.go_comment);
		go_wuliu.setOnClickListener(this);
		go_comment.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = null;
				if(list.get(arg2).getSkuType().equals("pin")){
					intent = new Intent(OrderDetailActivity.this,
							PingouDetailActivity.class);
				}else{
					intent = new Intent(OrderDetailActivity.this,
							GoodsDetailActivity.class);
				}
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
			intent.putExtra("orderType", "item");
			startActivity(intent);
			finish();
			break;
		case R.id.setting:
			showDelDialog();
			break;
		case R.id.go_money:
			Intent intent1 = new Intent(this, ApplyRefundActivity.class);
			intent1.putExtra("order", order);
			startActivityForResult(intent1,1);
			break;
		case R.id.go_comment:
			Intent intentComment = new Intent(this, CommentGoodsActivity.class);
			intentComment.putExtra("orderId", order.getOrderId());
			startActivity(intentComment);
			break;
		case R.id.go_wuliu:
			Intent intentPost = new Intent(this, LogisticsActivity.class);
			intentPost.putExtra("orderId", order.getOrderId());
			startActivity(intentPost);
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
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg0 == 1){
			loadData();
		}
	}
	
	
	
	
}
