package com.hanmimei.activity.balance;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
import com.hanmimei.activity.balance.adapter.GoodsBalanceCustomAdapter;
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.activity.mine.address.MyAddressActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.JSONPaserTool;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.CouponVo;
import com.hanmimei.entity.CustomsVo;
import com.hanmimei.entity.FromVo;
import com.hanmimei.entity.GoodsBalanceVo;
import com.hanmimei.entity.GoodsBalanceVo.Settle;
import com.hanmimei.entity.GoodsBalanceVo.SingleCustoms;
import com.hanmimei.entity.HAddress;
import com.hanmimei.entity.OrderInfo;
import com.hanmimei.entity.OrderSubmitVo;
import com.hanmimei.entity.ShoppingCar;
import com.hanmimei.http.VolleyHttp;
import com.hanmimei.http.VolleyHttp.VolleyJsonCallback;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.AlertDialogUtils;
import com.hanmimei.utils.ToastUtils;

/**
 * 
 * @author vince
 * 
 */
public class GoodsBalanceActivity extends BaseActivity implements
		OnClickListener {

	private RadioGroup group_pay_type, group_send_time, group_coupons; // 支付类型
																		// /发送时间/
																		// 优惠券
	private TextView send_time, pay_type; // 选中的发送时间 /支付类型
	private ListView mListView;//
	private List<CustomsVo> customslist; // 保税区列表

	private TextView all_price, all_money, youhui, all_portalfee, all_shipfee;
	private TextView name, phone, address, idCard, coupon_num, coupon_denomi;

	private long selectedId = Long.valueOf("0"); // 默认地址为0

	private GoodsBalanceCustomAdapter adapter;

	private ShoppingCar car; // 所要显示的商品数据
	private GoodsBalanceVo goodsBalance; // 本页数据集合
	private OrderSubmitVo orderSubmit; // 提交订单数据集合

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setBackEnable(false);
		ActionBarUtil.setActionBarStyle(this, "支付结算", this);
		setContentView(R.layout.goods_balance_layout);
		// 获取要购买的数据
		car = (ShoppingCar) getIntent().getSerializableExtra("car");
		customslist = car.getList();
		orderSubmit = new OrderSubmitVo();
		orderSubmit.setBuyNow(car.getBuyNow());
		orderSubmit.setPinActiveId(getIntent().getStringExtra("pinActiveId"));
		findView();
		adapter = new GoodsBalanceCustomAdapter(customslist, this);
		mListView.setAdapter(adapter);
		mListView.setFocusable(false);
		loadData(selectedId);

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
						orderSubmit.setShipTime(Integer.parseInt(btn.getTag()
								.toString()));
					}
				});

		group_coupons.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 优惠券响应时间
				RadioButton btn = (RadioButton) findViewById(checkedId);
				if (checkedId == R.id.btn_unuse) {
					coupon_denomi.setText(btn.getText());
					car.setDenomination(new BigDecimal(0));
				} else {
					coupon_denomi.setText("-" + btn.getTag(R.id.coupon_de)
							+ "元");
					car.setDenomination(new BigDecimal(btn.getTag(
							R.id.coupon_de).toString()));
					orderSubmit.setCouponId(btn.getTag(R.id.coupon_id)
							.toString());
				}
				youhui.setText(getResources().getString(R.string.price,
						car.getDiscountFee()));
				all_money.setText(getResources().getString(R.string.all_money,
						car.getTotalPayFee()));
			}
		});

	}

	@Override
	public void onClick(View v) {
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
			Intent intnt = new Intent(this, MyAddressActivity.class);
			intnt.putExtra("from", FromVo.GoodsBalanceActivity);
			intnt.putExtra("selectedId", selectedId);
			startActivityForResult(intnt, 1);
			break;
		case R.id.selectAddress:
			intnt = new Intent(this, MyAddressActivity.class);
			intnt.putExtra("from", FromVo.GoodsBalanceActivity);
			intnt.putExtra("selectedId", selectedId);
			Log.i("selectAddress_id", selectedId + "");
			startActivityForResult(intnt, 1);
			break;
		case R.id.btn_pay:
			sendData(orderSubmit);
			break;
		case R.id.back:
			showBackDialog();
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

		getLoading().show();
		JSONArray array = JSONPaserTool.ClientSettlePaser(car);
		orderSubmit.setSettleDtos(array);
		orderSubmit.setAddressId(addressId);
		final JSONObject json = JSONPaserTool.OrderSubmitPaser(orderSubmit);
		VolleyHttp.doPostRequestTask2(getHeaders(), UrlUtil.POST_CLIENT_SETTLE,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						goodsBalance = new Gson().fromJson(result,
								GoodsBalanceVo.class);
						initViewData();
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}, json.toString());
	}

	/**
	 * 提交订单数据
	 * 
	 * @param os
	 *            订单提交信息
	 */
	private void sendData(OrderSubmitVo os) {
		final JSONObject json = JSONPaserTool.OrderSubmitPaser(os);
		getLoading().show();
		VolleyHttp.doPostRequestTask2(getHeaders(),
				UrlUtil.POST_CLIENT_ORDER_SUBMIT, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						OrderInfo info = new Gson().fromJson(result,
								OrderInfo.class);
						if (info.getMessage().getCode() == 200) {
							Intent intent = new Intent(getActivity(),
									OrderSubmitActivity.class);
							intent.putExtra("orderInfo", info);
							intent.putExtra("orderType", getIntent()
									.getStringExtra("orderType"));
							startActivity(intent);
							sendBroadcast(new Intent(
									AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR));
							finish();
						} else {
							ToastUtils.Toast(getActivity(), info.getMessage()
									.getMessage());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}, json.toString());
	}

	/**
	 * 初始化添加view数据
	 */
	private void initViewData() {

		if (goodsBalance == null)
			return;
		if (goodsBalance.getMessage().getCode() == 200) {
			Settle settle = goodsBalance.getSettle();
			initGoodsInfo(settle);
			// 如果有默认地址 则显示地址信息
			initAddressInfo(settle);
			// 如果有优惠券则显示优惠券信息
			initCouponsInfo(settle);
			initBalanceInfo();

		} else {
			ToastUtils.Toast(this, goodsBalance.getMessage().getMessage());
			findViewById(R.id.btn_pay).setBackgroundResource(R.color.unClick);
			findViewById(R.id.selectAddress).setVisibility(View.GONE);
			findViewById(R.id.newAddress).setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 初始化地址信息
	 * 
	 * @param settle
	 */
	private void initAddressInfo(Settle settle) {
		if (settle.getAddress() != null && !settle.getAddress().isEmpty()) {
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
			idCard.setText(getResources().getString(
					R.string.idcard,
					settle.getAddress().getIdCardNum().substring(0, 5)
							+ "********"
							+ settle.getAddress()
									.getIdCardNum()
									.substring(
											14,
											settle.getAddress().getIdCardNum()
													.length())));
			findViewById(R.id.btn_pay).setBackgroundResource(
					R.drawable.btn_buy_selector);
			findViewById(R.id.btn_pay).setOnClickListener(this);

		} else {
			findViewById(R.id.selectAddress).setVisibility(View.GONE);
			findViewById(R.id.newAddress).setVisibility(View.VISIBLE);
			AlertDialogUtils.showAddressDialog(this, new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Intent intnt = new Intent(getActivity(),
							MyAddressActivity.class);
					intnt.putExtra("from", FromVo.GoodsBalanceActivity);
					intnt.putExtra("selectedId", selectedId);
					startActivityForResult(intnt, 1);
				}
			});
		}
	}

	/**
	 * 初始化商品信息
	 * 
	 * @param settle
	 */
	private void initGoodsInfo(Settle settle) {
		// 添加商品 行邮税 运费等信息
		car.setFactPortalFee(settle.getFactPortalFee());
		car.setFactShipFee(settle.getFactShipFee());
		car.setPortalFee(settle.getPortalFee());
		car.setShipFee(settle.getShipFee());
		car.setTotalFee(settle.getTotalFee());
		car.setDiscountFee(settle.getDiscountFee());

		for (CustomsVo cs : car.getList()) {
			for (SingleCustoms scs : settle.getSingleCustoms()) {
				if (cs.getInvCustoms().equals(scs.getInvCustoms())) {
					cs.setFactPortalFeeSingleCustoms(scs
							.getFactPortalFeeSingleCustoms());
					cs.setFactSingleCustomsShipFee(scs
							.getFactSingleCustomsShipFee());
					cs.setPortalSingleCustomsFee(scs
							.getPortalSingleCustomsFee());
					cs.setShipSingleCustomsFee(scs.getShipSingleCustomsFee());
					cs.setPostalStandard(settle.getPostalStandard());
					break;
				}
			}
		}
		// 通知显示
		adapter.notifyDataSetChanged();
	}

	/**
	 * 初始化购物券信息
	 * 
	 * @param settle
	 */
	private void initCouponsInfo(Settle settle) {
		if (settle.getCoupons() != null && settle.getCoupons().size() > 0) {
			List<CouponVo> coupons = settle.getCoupons();
			coupon_num.setText(coupons.size() + "张可用");
			RadioButton btn = null;
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			if (group_coupons.getChildCount() > 1) {
				group_coupons.removeViews(1, group_coupons.getChildCount() - 1);
			}
			for (CouponVo c : coupons) {
				btn = getCustomRadioButton();
				btn.setText("满" + c.getLimitQuota() + "减" + c.getDenomination());
				btn.setTag(R.id.coupon_de, c.getDenomination());
				btn.setTag(R.id.coupon_id, c.getCoupId());
				group_coupons.addView(btn, lp);
			}
			group_coupons.check(R.id.btn_unuse);
		}
	}

	private void initBalanceInfo() {
		all_shipfee.setText(getResources().getString(R.string.price,
				car.getFactShipFee()));
		all_price.setText(getResources().getString(R.string.price,
				car.getTotalFee()));
		all_portalfee.setText(getResources().getString(R.string.price,
				car.getFactPortalFee()));
		youhui.setText(getResources().getString(R.string.price,
				car.getDiscountFee()));
		all_money.setText(getResources().getString(R.string.all_money,
				car.getTotalPayFee()));
	}

	@SuppressLint("InflateParams")
	private RadioButton getCustomRadioButton() {
		return (RadioButton) getLayoutInflater().inflate(
				R.layout.panel_radiobutton, null);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1) {
			HAddress adress = (HAddress) arg2.getSerializableExtra("address");
			selectedId = adress.getAdress_id();
			if (selectedId != 0) {
				findViewById(R.id.newAddress).setVisibility(View.GONE);
				findViewById(R.id.selectAddress).setVisibility(View.VISIBLE);
				address.setText(getResources().getString(R.string.address,
						adress.getCity() + adress.getAdress()));
				name.setText(getResources().getString(R.string.name,
						adress.getName()));
				phone.setText(getResources().getString(R.string.phone,
						adress.getPhone()));
				idCard.setText(getResources().getString(R.string.idcard,
						adress.getIdCardd()));

				if (adress.isDefault()) {
					findViewById(R.id.isDefault).setVisibility(View.VISIBLE);
				} else {
					findViewById(R.id.isDefault).setVisibility(View.GONE);
				}
			} else {
				findViewById(R.id.newAddress).setVisibility(View.VISIBLE);
				findViewById(R.id.selectAddress).setVisibility(View.GONE);
			}
			loadData(selectedId);
			if (goodsBalance != null) {
				findViewById(R.id.btn_pay).setBackgroundResource(
						R.drawable.btn_buy_selector);
				findViewById(R.id.btn_pay).setOnClickListener(this);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 菜单返回按钮点击事件
			showBackDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 显示取消支付窗口
	private void showBackDialog() {
		AlertDialogUtils.showBackDialog(getActivity(), new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
				finish();
			}
		});
	}

}
