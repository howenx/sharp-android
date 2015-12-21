package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.adapter.GoodsBalanceCustomAdapter;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.JSONPaserTool;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Coupon;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.From;
import com.hanmimei.entity.GoodsBalance;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.entity.GoodsBalance.Settle;
import com.hanmimei.entity.GoodsBalance.SingleCustoms;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.OrderSubmit;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;

public class GoodsBalanceActivity extends BaseActivity implements
		OnClickListener {

	private RadioGroup group_pay_type, group_send_time, group_coupons; // 支付类型
																		// /发送时间/
																		// 优惠券
	private TextView send_time, pay_type; // 选中的发送时间 /支付类型
	private ListView mListView;//
	private List<Customs> customslist; // 保税区列表

	private TextView all_price, all_money, youhui, all_portalfee, all_shipfee;
	private TextView name, phone, address, idCard, coupon_num, coupon_denomi;

	private int selectedId = 0; // 默认地址为0

	private GoodsBalanceCustomAdapter adapter;

	private ShoppingCar car; // 所要显示的商品数据
	private GoodsBalance goodsBalance; // 本页数据集合
	private OrderSubmit orderSubmit; //提交订单数据集合

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "支付结算");
		setContentView(R.layout.goods_balance_layout);
		// 获取要购买的数据
		car = (ShoppingCar) getIntent().getSerializableExtra("car");
		customslist = car.getList();
		orderSubmit = new OrderSubmit();
		orderSubmit.setBuyNow(car.getBuyNow());
		findView();
		adapter = new GoodsBalanceCustomAdapter(customslist, this);
		mListView.setAdapter(adapter);
		mListView.setFocusable(false);
		all_price.setText(getResources().getString(R.string.price,
				car.getAllPrice()));
		loadData(Long.valueOf(selectedId));
	}

	/**
	 * 初始化所有view对象
	 */
	private void findView() {
		group_pay_type = (RadioGroup) findViewById(R.id.group_pay_type);
		group_send_time = (RadioGroup) findViewById(R.id.group_send_time);
		mListView = (ListView) findViewById(R.id.mListView);
		group_coupons = (RadioGroup) findViewById(R.id.group_coupons);

		pay_type = (TextView) findViewById(R.id.pay_type);
		send_time = (TextView) findViewById(R.id.send_time);

		all_price = (TextView) findViewById(R.id.all_price);
		all_money = (TextView) findViewById(R.id.all_money);
		youhui = (TextView) findViewById(R.id.youhui);
		all_portalfee = (TextView) findViewById(R.id.all_portalfee);
		all_shipfee = (TextView) findViewById(R.id.all_shipfee);
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		address = (TextView) findViewById(R.id.address);
		idCard = (TextView) findViewById(R.id.idCard);
		coupon_num = (TextView) findViewById(R.id.coupon_num);
		coupon_denomi = (TextView) findViewById(R.id.coupon_denomi);

		findViewById(R.id.btn_pay_type).setOnClickListener(this);
		findViewById(R.id.btn_send_time).setOnClickListener(this);
		findViewById(R.id.newAddress).setOnClickListener(this);
		findViewById(R.id.btn_mCoupon).setOnClickListener(this);
		findViewById(R.id.selectAddress).setOnClickListener(this);

		group_pay_type
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// 显示选中的支付方式
						RadioButton btn = (RadioButton) findViewById(arg1);
						pay_type.setText(btn.getText());
						orderSubmit.setPayMethod(btn.getTag().toString());
					}
				});
		group_send_time
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// 显示选中的送达时间
						RadioButton btn = (RadioButton) findViewById(arg1);
						send_time.setText(btn.getText());
						orderSubmit.setShipTime(Integer.parseInt(btn.getTag().toString()));
					}
				});

		group_coupons.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 优惠券响应时间
				RadioButton btn = (RadioButton) findViewById(checkedId);
				if (checkedId == R.id.btn_unuse) {
					coupon_denomi.setText(btn.getText());
					car.setDenomination(0.00);
				} else {
					coupon_denomi.setText("-" + btn.getTag(R.id.coupon_de)+"元");
					car.setDenomination(Double
							.parseDouble(btn.getTag(R.id.coupon_de).toString()));
					orderSubmit.setCouponId(btn.getTag(R.id.coupon_id).toString());
				}
				youhui.setText(getResources().getString(R.string.price,
						car.getDenomination()));
				all_money.setText(getResources().getString(R.string.all_money,
						car.getAllMoney()));
			}
		});

	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_pay_type:
			if (group_pay_type.getVisibility() == View.VISIBLE) {
				group_pay_type.setVisibility(View.GONE);
			} else {
				group_pay_type.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_send_time:
			if (group_send_time.getVisibility() == View.VISIBLE) {
				group_send_time.setVisibility(View.GONE);
			} else {
				group_send_time.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_mCoupon:
			if (group_coupons.getVisibility() == View.VISIBLE) {
				group_coupons.setVisibility(View.GONE);
			} else {
				group_coupons.setVisibility(View.VISIBLE);
			}
			break;

		case R.id.newAddress:
			Intent intnt = new Intent(this, AdressActivity.class);
			intnt.putExtra("from", From.GoodsBalanceActivity);
			intnt.putExtra("selectedId", selectedId);
			Log.i("newAddress_id", selectedId + "");
			startActivityForResult(intnt, 1);
			break;
		case R.id.selectAddress:
			intnt = new Intent(this, AdressActivity.class);
			intnt.putExtra("from", From.GoodsBalanceActivity);
			intnt.putExtra("selectedId", selectedId);
			Log.i("selectAddress_id", selectedId + "");
			startActivityForResult(intnt, 1);
			break;
		case R.id.btn_pay:
			sendData(orderSubmit);
			break;

		default:
			break;
		}
	}

	/**
	 * 加载网络数据
	 * 
	 * @param addressId
	 *            被选中的地址id
	 */
	private void loadData(Long addressId) {
		JSONArray array = JSONPaserTool.ClientSettlePaser(car, addressId);
		orderSubmit.setSettleDtos(array);
		final JSONObject json = JSONPaserTool.OrderSubmitPaser(orderSubmit);
		submitTask(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.POST_CLIENT_SETTLE,
						json, "id-token", getUser().getToken());
				Message msg = mHandler.obtainMessage(1, result);
				mHandler.sendMessage(msg);
			}
		});
	}
	
	/**
	 * 加载网络数据
	 * 
	 * @param addressId
	 *            被选中的地址id
	 */
	private void sendData(OrderSubmit os) {
		final JSONObject json = JSONPaserTool.OrderSubmitPaser(os);
		submitTask(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.POST_CLIENT_ORDER_SUBMIT,
						json, "id-token", getUser().getToken());
				Message msg = mHandler.obtainMessage(2, result);
				mHandler.sendMessage(msg);
			}
		});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				goodsBalance = new Gson().fromJson(result, GoodsBalance.class);
				initViewData();
				break;
			case 2:
				result = (String) msg.obj;
				OrderInfo info = new Gson().fromJson(result, OrderInfo.class);
				Intent intent = new Intent(getActivity(), OrderSubmitActivity.class);
				intent.putExtra("orderInfo", info);
				startActivity(intent);
				sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
				finish();
				break;

			default:
				break;
			}
		}

	};

	/**
	 * 初始化添加view数据
	 */
	private void initViewData() {

		if (goodsBalance == null)
			return ;
			if (goodsBalance.getMessage().getCode() == 200) {
				Settle settle = goodsBalance.getSettle();
				//添加商品 行邮税 运费等信息
				car.setFactPortalFee(settle.getFactPortalFee());
				car.setFactShipFee(settle.getFactShipFee());
				car.setPortalFee(settle.getPortalFee());
				car.setShipFee(settle.getShipFee());

				for (Customs cs : car.getList()) {
					for (SingleCustoms scs : settle.getSingleCustoms()) {
						if (cs.getInvCustoms().equals(scs.getInvCustoms())) {
							cs.setFactPortalFeeSingleCustoms(scs
									.getFactPortalFeeSingleCustoms());
							cs.setFactSingleCustomsShipFee(scs
									.getFactSingleCustomsShipFee());
							cs.setPortalSingleCustomsFee(scs
									.getPortalSingleCustomsFee());
							cs.setShipSingleCustomsFee(scs
									.getShipSingleCustomsFee());
							break;
						}
					}
				}
				//通知显示
				adapter.notifyDataSetChanged();
				//如果有默认地址 则显示地址信息
				if (!settle.getAddress().isEmpty()) {
					findViewById(R.id.selectAddress).setVisibility(View.VISIBLE);
					findViewById(R.id.newAddress).setVisibility(View.GONE);
					
					selectedId = settle.getAddress().getAddId();
					orderSubmit.setAddressId(selectedId);
					address.setText(getResources().getString(
							R.string.address,
							settle.getAddress().getDeliveryCity()
									+ settle.getAddress().getDeliveryDetail()));
					name.setText(getResources().getString(R.string.name,
							settle.getAddress().getName()));
					phone.setText(getResources().getString(R.string.phone,
							settle.getAddress().getTel()));
					idCard.setText(getResources().getString(R.string.idcard,settle.getAddress().getIdCardNum().substring(0, 5) 
							+ "********" + settle.getAddress().getIdCardNum().substring(14, settle.getAddress().getIdCardNum().length())
							));
					findViewById(R.id.btn_pay).setBackgroundResource(R.color.theme);
					findViewById(R.id.btn_pay).setOnClickListener(this);

				}else{
					findViewById(R.id.selectAddress).setVisibility(View.GONE);
					findViewById(R.id.newAddress).setVisibility(View.VISIBLE);

				}
				//如果有优惠券则显示优惠券信息
				if (settle.getCoupons() != null && settle.getCoupons().size()>0) {
					List<Coupon> coupons = settle.getCoupons();
					coupon_num.setText(coupons.size() + "张可用");
					RadioButton btn = null;
					LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
					for (Coupon c : coupons) {
						btn = getCustomRadioButton();
						btn.setText("满"+c.getLimitQuota()+"减"+c.getDenomination());
						btn.setTag(R.id.coupon_de,c.getDenomination());
						btn.setTag(R.id.coupon_id, c.getCoupId());
						group_coupons.addView(btn,lp);
					}
				}

				all_shipfee.setText(getResources().getString(R.string.price,
						car.getFactShipFeeFormat()));
				all_price.setText(getResources().getString(R.string.price,
						car.getAllPriceFormat()));
				all_portalfee.setText(getResources().getString(R.string.price,
						car.getFactPortalFeeFormat()));
				youhui.setText(getResources().getString(R.string.price,
						car.getDenominationFormat()));
				all_money.setText(getResources().getString(R.string.all_money,
						car.getAllMoneyFormat()));
				
				findViewById(R.id.btn_pay).setOnClickListener(this);
			} else {
				ToastUtils.Toast(this, goodsBalance.getMessage().getMessage());
				findViewById(R.id.btn_pay).setBackgroundColor(
						Color.parseColor("#9F9F9F"));
				findViewById(R.id.selectAddress).setVisibility(View.GONE);
				findViewById(R.id.newAddress).setVisibility(View.VISIBLE);
			}
		
	}
	
	
	private RadioButton getCustomRadioButton() {
		return (RadioButton) getLayoutInflater().inflate(
				R.layout.panel_radiobutton, null);
	}


	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1) {
			findViewById(R.id.newAddress).setVisibility(View.GONE);
			findViewById(R.id.selectAddress).setVisibility(View.VISIBLE);
			HMMAddress adress = (HMMAddress) arg2
					.getSerializableExtra("address");
			address.setText(getResources().getString(R.string.address,
					adress.getCity() + adress.getAdress()));
			name.setText(getResources().getString(R.string.name,
					adress.getName()));
			phone.setText(getResources().getString(R.string.phone,
					adress.getPhone()));
			idCard.setText(getResources().getString(R.string.idcard,
					adress.getIdCard()));
			selectedId = adress.getAdress_id();
			if (adress.isDefault()) {
				findViewById(R.id.isDefault).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.isDefault).setVisibility(View.GONE);
			}
			loadData(Long.valueOf(selectedId));
			if(goodsBalance != null){
				findViewById(R.id.btn_pay).setBackgroundResource(R.color.theme);
				findViewById(R.id.btn_pay).setOnClickListener(this);
			}
		}
	}
	
	
	

}
