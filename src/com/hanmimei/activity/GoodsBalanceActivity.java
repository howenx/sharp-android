package com.hanmimei.activity;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.hanmimei.entity.GoodsBalance;
import com.hanmimei.entity.GoodsBalance.Settle;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.entity.ShoppingGoods;
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

	private TextView all_price, all_money;
	private TextView name, phone, address, coupon_num;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		ActionBarUtil.setActionBarStyle(this, "支付结算");
		setContentView(R.layout.goods_balance_layout);

		car = (ShoppingCar) getIntent().getSerializableExtra("car");
		customslist = car.getList();
		findView();
		// initViews();
		loadData();
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
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		address = (TextView) findViewById(R.id.address);
		coupon_num = (TextView) findViewById(R.id.coupon_num);

		findViewById(R.id.btn_pay_type).setOnClickListener(this);
		findViewById(R.id.btn_send_time).setOnClickListener(this);
		findViewById(R.id.newAddress).setOnClickListener(this);
		findViewById(R.id.group_coupons).setOnClickListener(this);

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

	}

	private void initViews() {
		mListView.setAdapter(new GoodsBalanceCustomAdapter(customslist, this));
		all_price.setText(car.getAllPrice() + "");
		if (goodsBalance != null) {
			if (goodsBalance.getMessage().getCode() == 200) {
				Settle settle = goodsBalance.getSettle();
				if (settle.getAddress() != null) {
					findViewById(R.id.newAddress).setVisibility(View.GONE);
					address.setText(settle.getAddress().getDeliveryCity());
					name.setText(settle.getAddress().getName());
					phone.setText(settle.getAddress().getTel());
				}
				if (settle.getCoupons() != null) {
					List<Coupon> coupons = settle.getCoupons();
					coupon_num.setText(coupons.size() + "张可用");
					for (Coupon c : coupons) {
						RadioButton btn = new RadioButton(this);
						btn.setText(c.getDenomination());
						group_coupons.addView(btn);
					}
				}

			}
		}
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

		default:
			break;
		}
	}

	// 网络请求数据
	private void loadData() {
		final JSONArray array = JSONPaserTool.ClientSettlePaser(car);
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
				Gson gson = new Gson();
				goodsBalance = gson.fromJson(result, GoodsBalance.class);
				initViews();
				ToastUtils.Toast(getActivity(), result);
			}
		}

	};

}
