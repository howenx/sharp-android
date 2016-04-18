package com.hanmimei.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.OnStateListener;
import com.dd.processbutton.ProcessButton;
import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.GoodsBalance;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Order;
import com.hanmimei.entity.Sku;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.upload.Http3Utils;
import com.hanmimei.utils.upload.MultipartRequestParams;

public class ApplyRefundActivity extends BaseActivity implements
		OnClickListener {
	private Sku sku;
	private EditText discription;
	private ArrayList<String> mSelectPath;
	private String refundType = "deliver";// 退款类型，pin：拼购自动退款，receive：收货后申请退款，deliver：发货前退款

	private Order order;
	private ProcessButton btn_submit;
	private TextView nameView, phoneView,payBackFee;
	private boolean isComplete= false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apply_refund_layout);
		ActionBarUtil.setActionBarStyle(this, "申请退款");
		order = (Order) getIntent().getSerializableExtra("order");
		mSelectPath = new ArrayList<String>();
		findView();
	}

	/**
	 * 初始化获取view
	 */
	private void findView() {
		discription = (EditText) findViewById(R.id.discription);
		btn_submit = (ProcessButton) findViewById(R.id.btn_submit);
		nameView = (TextView) findViewById(R.id.contact_name);
		phoneView = (TextView) findViewById(R.id.contact_phone);
		payBackFee = (TextView) findViewById(R.id.payBackFee);
		btn_submit.setOnClickListener(this);
		btn_submit.setOnStateListener(mOnStateListener);
		
		payBackFee.setText(order.getPayTotal()+"");
	}

	private OnStateListener mOnStateListener = new OnStateListener() {
		
		@Override
		public void onProgress() {
			discription.setEnabled(false);
			nameView.setEnabled(false);
			phoneView.setEnabled(false);
		}
		
		@Override
		public void onError() {
			discription.setEnabled(true);
			nameView.setEnabled(true);
			phoneView.setEnabled(true);
		}
		
		@Override
		public void onComplete() {
			finish();
		}
	};
	
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


	private void submit() {
		if (TextUtils.isEmpty(discription.getText())) {
			ToastUtils.Toast(this, "请填写问题描述");
			return;
		}
		if (!TextUtils.isEmpty(phoneView.getText())
				&& !CommonUtil.isPhoneNum(phoneView.getText().toString())) {
			ToastUtils.Toast(this, "请填写正确的联系方式");
			return;
		}
		submitData();
	}

	private void submitData() {
		getLoading().show();
		Http3Utils.doPostRequestTask(this, getHeaders(),
				UrlUtil.CUSTOMER_SERVICE_APPLY, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
						getLoading().dismiss();
						GoodsBalance b = new Gson().fromJson(result,
								GoodsBalance.class);
						HMessage msg = b.getMessage();
						if (msg.getCode() == 200) {
							btn_submit.setProgress(100);
						} else {
							ToastUtils.Toast(getActivity(), msg.getMessage());
							btn_submit.setProgress(-1);
							CommonUtil.closeBoardIfShow(getActivity());
						}
					}

					@Override
					public void onError() {
						getLoading().dismiss();
						ToastUtils.Toast(getActivity(), R.string.error);
						btn_submit.setProgress(-1);
					}
				}, getParams());
	}

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
