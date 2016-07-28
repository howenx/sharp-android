package com.kakao.kakaogift.activity.balance;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.balance.adapter.GoodsBalanceCustomAdapter;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.address.MyAddressActivity;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.JSONPaserTool;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.CouponVo;
import com.kakao.kakaogift.entity.CustomsVo;
import com.kakao.kakaogift.entity.FromVo;
import com.kakao.kakaogift.entity.GoodsBalanceVo;
import com.kakao.kakaogift.entity.HAddress;
import com.kakao.kakaogift.entity.OrderInfo;
import com.kakao.kakaogift.entity.OrderSubmitVo;
import com.kakao.kakaogift.entity.ShoppingCar;
import com.kakao.kakaogift.entity.GoodsBalanceVo.Settle;
import com.kakao.kakaogift.entity.GoodsBalanceVo.SingleCustoms;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.override.ViewExpandAnimation;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.AlertDialogUtils;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;

/**
 * 
 * @author vince
 * 
 */
public class GoodsBalanceActivity extends BaseActivity implements
		OnClickListener {

	private RadioGroup  group_send_time, group_coupons; // 支付类型
																		// /发送时间/
																		// 优惠券
	private TextView send_time; // 选中的发送时间 /支付类型
	private ListView mListView;//
	private List<CustomsVo> customslist; // 保税区列表

	private TextView all_price, all_money, youhui, all_portalfee, all_shipfee;
	private TextView name, phone, address, coupon_num, coupon_denomi,notice;

	private long selectedId = Long.valueOf("0"); // 默认地址为0

	private GoodsBalanceCustomAdapter adapter;

	private ShoppingCar car; // 所要显示的商品数据
	private GoodsBalanceVo goodsBalance; // 本页数据集合
	private OrderSubmitVo orderSubmit; // 提交订单数据集合

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//取消滑动退出功能
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
		group_send_time = (RadioGroup) findViewById(R.id.group_send_time);
		mListView = (ListView) findViewById(R.id.mListView);
		group_coupons = (RadioGroup) findViewById(R.id.group_coupons);

		send_time = (TextView) findViewById(R.id.send_time);

		all_price = (TextView) findViewById(R.id.all_price);
		all_money = (TextView) findViewById(R.id.all_money);
		youhui = (TextView) findViewById(R.id.youhui);
		all_portalfee = (TextView) findViewById(R.id.all_portalfee);
		all_shipfee = (TextView) findViewById(R.id.all_shipfee);
		name = (TextView) findViewById(R.id.name);
		phone = (TextView) findViewById(R.id.phone);
		address = (TextView) findViewById(R.id.address);
		coupon_num = (TextView) findViewById(R.id.coupon_num);
		coupon_denomi = (TextView) findViewById(R.id.coupon_denomi);
		notice = (TextView) findViewById(R.id.notice);

		findViewById(R.id.btn_send_time).setOnClickListener(this);
		findViewById(R.id.newAddress).setOnClickListener(this);
		findViewById(R.id.btn_mCoupon).setOnClickListener(this);
		findViewById(R.id.selectAddress).setOnClickListener(this);

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
					car.setDenomination((BigDecimal)btn.getTag(R.id.coupon_de));
					orderSubmit.setCouponId(btn.getTag(R.id.coupon_id)
							.toString());
				}
				youhui.setText(getResources().getString(R.string.price,
						car.getDiscountFee()));
				all_money.setText(getResources().getString(R.string.all_money,
						car.getTotalPayFee()));
				if(car.getTotalPayFee().compareTo(BigDecimal.ONE) <= 0){
					notice.setVisibility(View.VISIBLE);
				}else{
					notice.setVisibility(View.GONE);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_time:
			group_send_time.startAnimation(new ViewExpandAnimation(group_send_time));
			break;
		case R.id.btn_mCoupon:
			group_coupons.startAnimation(new ViewExpandAnimation(group_coupons));
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
		//初始化订单提交所需信息
		JSONArray array = JSONPaserTool.ClientSettlePaser(car);
		orderSubmit.setSettleDtos(array);
		orderSubmit.setAddressId(addressId);
		final JSONObject json = JSONPaserTool.OrderSubmitPaser(orderSubmit);
		//提交订单信息，获取支付数据
		VolleyHttp.doPostRequestTask2(getHeaders(), UrlUtil.POST_CLIENT_SETTLE,
				new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						goodsBalance = new Gson().fromJson(result,GoodsBalanceVo.class);
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
			//初始化商品信息
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
			//存在地址信息
			findViewById(R.id.selectAddress).setVisibility(View.VISIBLE);
			findViewById(R.id.newAddress).setVisibility(View.GONE);

			selectedId = settle.getAddress().getAddId();
			//设置订单提交所需数据
			orderSubmit.setAddressId(selectedId);
			address.setText(getResources().getString(
					R.string.address,
					settle.getAddress().getDeliveryCity()
							+ settle.getAddress().getDeliveryDetail()));
			name.setText(getResources().getString(R.string.name,
					settle.getAddress().getName()));
			phone.setText(getResources().getString(R.string.phone,CommonUtils.phoneNumPaser(settle.getAddress().getTel())));
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
				if( c.getLimitQuota().compareTo(BigDecimal.ZERO)<=0){
					btn.setText(c.getDenomination()+"元，无限制");
				}else{
					btn.setText(c.getDenomination()+"元，满"+c.getLimitQuota()+"元可用");
				}
				
				btn.setTag(R.id.coupon_de, c.getDenomination());
				btn.setTag(R.id.coupon_id, c.getCoupId());
				group_coupons.addView(btn, lp);
			}
			group_coupons.check(R.id.btn_unuse);
		}
	}
	/**
	 * 支付信息
	 */
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
	/**
	 * 获取自定义单选按钮
	 * @return
	 */
	private RadioButton getCustomRadioButton() {
		return (RadioButton) getLayoutInflater().inflate(R.layout.panel_radiobutton, null);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 1) {
			//地址选择返回结果
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
