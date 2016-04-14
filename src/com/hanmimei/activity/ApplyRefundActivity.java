package com.hanmimei.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.hanmimei.utils.GlideLoaderUtils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.upload.Http3Utils;
import com.hanmimei.utils.upload.MultipartRequestParams;
import com.hanmimei.view.CustomGridView;
import com.hanmimei.view.UDialog;
import com.hanmimei.view.UDialog.CallBack;

public class ApplyRefundActivity extends BaseActivity implements
		OnClickListener {
	private Sku sku;
	private EditText discription;
	private ArrayList<String> mSelectPath;
	private String refundType = "deliver";// 退款类型，pin：拼购自动退款，receive：收货后申请退款，deliver：发货前退款

	private Order order;
	private ProcessButton btn_submit;
	private TextView nameView, phoneView,payBackFee;

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
			nameView.setEnabled(false);
			phoneView.setEnabled(false);
		}
		
		@Override
		public void onError() {
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
		btn_submit.setProgress(50);
		Http3Utils.doPostRequestTask(this, getHeaders(),
				UrlUtil.CUSTOMER_SERVICE_APPLY, new VolleyJsonCallback() {

					@Override
					public void onSuccess(String result) {
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
		} else if (sku != null) {
			params.put("orderId", getIntent().getStringExtra("orderId"));
			params.put("splitOrderId",
					getIntent().getStringExtra("splitOrderId"));
			params.put("skuId", sku.getSkuId());

			params.put("skuType", sku.getSkuType());
			params.put("skuTypeId", sku.getSkuTypeId());
			params.put("refundType", refundType);

			params.put("reason", discription.getText().toString());
			// params.put("contactName", name);
			// params.put("contactTel", phone);
			for (int i = 0; i < mSelectPath.size(); i++) {
				String path = mSelectPath.get(i);
				params.put("refundImg" + i, new File(path));
			}
		}
		return params;
	}
	
}
