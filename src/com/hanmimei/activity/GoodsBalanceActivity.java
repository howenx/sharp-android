package com.hanmimei.activity;

import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.adapter.GoodsBalanceCustomAdapter;
import com.hanmimei.data.JSONPaserTool;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Coupon;
import com.hanmimei.entity.Customs;
import com.hanmimei.entity.From;
import com.hanmimei.entity.GoodsBalance;
import com.hanmimei.entity.GoodsBalance.Settle;
import com.hanmimei.entity.GoodsBalance.SingleCustoms;
import com.hanmimei.entity.HMMAddress;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.utils.ToastUtils;

public class GoodsBalanceActivity extends BaseActivity implements
		OnClickListener {

	private RadioGroup group_pay_type, group_send_time, group_coupons;
	private TextView send_time, pay_type;
	private ListView mListView, mCouponView;
	private ShoppingCar car;
	private List<Customs> customslist; // 保税区列表

	private TextView all_price, all_money, youhui, all_portalfee, all_shipfee;
	private TextView name, phone, address, idCard,coupon_num, coupon_denomi;

	private int selectedId = 0;

	private GoodsBalanceCustomAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "支付结算");
		setContentView(R.layout.goods_balance_layout);

		car = (ShoppingCar) getIntent().getSerializableExtra("car");
		customslist = car.getList();
		findView();
		adapter = new GoodsBalanceCustomAdapter(customslist, this);
		mListView.setAdapter(adapter);
		mListView.setFocusable(false);
		all_price.setText(getResources().getString(R.string.price,
				car.getAllPrice()));
		// initViews();
		loadData(Long.valueOf(selectedId));
	}

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
						// TODO Auto-generated method stub
						RadioButton btn = (RadioButton) findViewById(arg1);
						pay_type.setText(btn.getText());
					}
				});
		group_send_time
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						RadioButton btn = (RadioButton) findViewById(arg1);
						send_time.setText(btn.getText());
					}
				});

		group_coupons.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton btn = (RadioButton) findViewById(checkedId);
				if (checkedId == R.id.btn_unuse) {
					coupon_denomi.setText(btn.getText());
					car.setDenomination(0.00);
				} else {
					coupon_denomi.setText("-" + btn.getText());
					car.setDenomination(Double
							.valueOf(btn.getText().toString()));
				}
				youhui.setText(getResources().getString(R.string.price,
						car.getDenomination()));
				all_money.setText(getResources().getString(R.string.all_money,
						car.getAllMoney()));

			}
		});

	}

	private void initViews() {

		if (goodsBalance != null) {
			if (goodsBalance.getMessage().getCode() == 200) {
				Settle settle = goodsBalance.getSettle();

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
				adapter.notifyDataSetChanged();

				if (settle.getAddress() != null) {
					findViewById(R.id.selectAddress).setVisibility(View.VISIBLE);
					findViewById(R.id.newAddress).setVisibility(View.GONE);
					selectedId = settle.getAddress().getAddId();
					address.setText(getResources().getString(R.string.address, settle.getAddress().getDeliveryCity()+ settle.getAddress().getDeliveryDetail()));
					name.setText(getResources().getString(R.string.name,settle.getAddress().getName()));
					phone.setText(getResources().getString(R.string.phone,settle.getAddress().getTel()));
					idCard.setText(getResources().getString(R.string.idcard,settle.getAddress().getIdCardNum()));
					findViewById(R.id.isDefault).setVisibility(View.VISIBLE);
				}
				if (settle.getCoupons() != null) {
					List<Coupon> coupons = settle.getCoupons();
					coupon_num.setText(coupons.size() + "张可用");
					RadioButton btn = null;
					for (Coupon c : coupons) {
						btn = getCustomRadioButton();
						btn.setText(c.getDenomination() + "");
						group_coupons.addView(btn);
					}
				}

				all_shipfee.setText(getResources().getString(R.string.price,
						car.getFactShipFee()));
				all_price.setText(getResources().getString(R.string.price,
						car.getAllPrice()));
				all_portalfee.setText(getResources().getString(R.string.price,
						car.getFactPortalFee()));
				youhui.setText(getResources().getString(R.string.price,
						car.getDenomination()));
				all_money.setText(getResources().getString(R.string.all_money,
						car.getAllMoney()));

				findViewById(R.id.btn_pay).setOnClickListener(this);
			} else {
				ToastUtils.Toast(this, goodsBalance.getMessage().getMessage());
				findViewById(R.id.btn_pay).setBackgroundColor(
						Color.parseColor("#9F9F9F"));
				findViewById(R.id.selectAddress).setVisibility(View.GONE);
				findViewById(R.id.newAddress).setVisibility(View.VISIBLE);
			}
		}
	}

	private RadioButton getCustomRadioButton() {
		return (RadioButton) getLayoutInflater().inflate(
				R.layout.panel_radiobutton, null);
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

		default:
			break;
		}
	}

	// 网络请求数据
	private void loadData(Long addressId) {
		final JSONArray array = JSONPaserTool.ClientSettlePaser(car, addressId);
		submitTask(new Runnable() {

			@Override
			public void run() {
				String result = HttpUtils.post(UrlUtil.POST_CLIENT_SETTLE,
						array, "id-token", getUser().getToken());
				Message msg = mHandler.obtainMessage(1, result);
				mHandler.sendMessage(msg);
			}
		});
	}

	private GoodsBalance goodsBalance;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 1) {
				String result = (String) msg.obj;
				goodsBalance = new Gson().fromJson(result, GoodsBalance.class);
				initViews();
			}
		}

	};

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1) {
			findViewById(R.id.newAddress).setVisibility(View.GONE);
			findViewById(R.id.selectAddress).setVisibility(View.VISIBLE);
			HMMAddress adress = (HMMAddress) arg2
					.getSerializableExtra("address");
			address.setText(getResources().getString(R.string.address,adress.getCity() + adress.getAdress() ));
			name.setText(getResources().getString(R.string.name,adress.getName()));
			phone.setText(getResources().getString(R.string.phone,adress.getPhone()));
			idCard.setText(getResources().getString(R.string.idcard,adress.getIdCard()));
			selectedId = adress.getAdress_id();
			if (adress.isDefault()) {
				findViewById(R.id.isDefault).setVisibility(View.VISIBLE);
			} else {
				findViewById(R.id.isDefault).setVisibility(View.GONE);
			}
			loadData(Long.valueOf(selectedId));
		}
	}

}
