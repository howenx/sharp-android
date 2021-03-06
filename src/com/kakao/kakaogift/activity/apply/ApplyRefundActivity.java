package com.kakao.kakaogift.activity.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.GoodsBalanceVo;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.Order;
import com.kakao.kakaogift.http.MultipartRequestParams;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.ToastUtils;
/**
 * 
 * @author vince
 *
 */
public class ApplyRefundActivity extends BaseActivity implements
		OnClickListener {
	private EditText discription;
	private String refundType = "deliver";// 退款类型，pin：拼购自动退款，receive：收货后申请退款，deliver：发货前退款

	private Order order;
	private View btn_submit;
	private TextView nameView, phoneView,payBackFee;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_refund_layout);
		ActionBarUtil.setActionBarStyle(this, "申请退款");
		order = (Order) getIntent().getSerializableExtra("order");
		findView();
	}

	/**
	 * 初始化获取view
	 */
	private void findView() {
		discription = (EditText) findViewById(R.id.discription);
		btn_submit =  findViewById(R.id.btn_submit);
		nameView = (TextView) findViewById(R.id.contact_name);
		phoneView = (TextView) findViewById(R.id.contact_phone);
		payBackFee = (TextView) findViewById(R.id.payBackFee);
		btn_submit.setOnClickListener(this);
		
		payBackFee.setText(order.getPayTotal()+"");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
				submit();
			break;
		default:
			break;
		}
	}

	/**
	 * 提交申请信息
	 */
	private void submit() {
		if (TextUtils.isEmpty(discription.getText())) {
			ToastUtils.Toast(this, "请填写问题描述");
			return;
		}
		if (!TextUtils.isEmpty(phoneView.getText())
				&& !CommonUtils.isPhoneNum(phoneView.getText().toString())) {
			ToastUtils.Toast(this, "请填写正确的联系方式");
			return;
		}
		submitData();
	}
	/**
	 * 提交数据
	 */
	private void submitData() {
		getLoading().show();
		VolleyHttp.doPostRequestTask3( getHeaders(),
				UrlUtil.CUSTOMER_SERVICE_APPLY, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						GoodsBalanceVo b = new Gson().fromJson(result,
								GoodsBalanceVo.class);
						HMessage msg = b.getMessage();
						if (msg.getCode() == 200) {
							finish();
						} else {
							ToastUtils.Toast(getActivity(), msg.getMessage());
							CommonUtils.closeBoardIfShow(getActivity());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
					}
				}, getParams());
	}
	/**
	 * 提交的数据结构
	 * @return
	 */
	private MultipartRequestParams getParams() {
		MultipartRequestParams params = new MultipartRequestParams();

		if (order != null) {
			params.put("orderId", order.getOrderId());
			params.put("splitOrderId", order.getOrderSplitId());
			params.put("refundType", refundType);
			params.put("payBackFee", order.getPayTotal() + "");
			params.put("reason", discription.getText().toString());
			if (TextUtils.isEmpty(nameView.getText()))
				params.put("contactName", nameView.getText().toString().trim());
			if (TextUtils.isEmpty(phoneView.getText()))
				params.put("contactTel", phoneView.getText().toString().trim());
		} 
		return params;
	}
	
}
