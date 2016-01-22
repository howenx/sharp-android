package com.hanmimei.activity;

import java.io.File;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanmimei.R;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.upload.Bimp;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.ToastUtils;
import com.hanmimei.utils.Http2Utils.VolleyJsonCallback;
import com.hanmimei.utils.upload.Http3Utils;
import com.hanmimei.utils.upload.MultipartRequestParams;

public class CsutomerServiceSubmitActivity extends BaseActivity {
	
	private TextView nameView,phoneView;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_service_next_layout);
		
		nameView = (TextView) findViewById(R.id.username);
		phoneView = (TextView) findViewById(R.id.phone);
		
		findViewById(R.id.btn_submit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
	}
	
	private void submit(){
		if(TextUtils.isEmpty(nameView.getText())){
			ToastUtils.Toast(this, "请填写联系人姓名");
			return;
		}
		if(TextUtils.isEmpty(phoneView.getText())){
			ToastUtils.Toast(this, "请填写联系方式");
			return;
		}
		if(!CommonUtil.isPhoneNum(phoneView.getText().toString())){
			ToastUtils.Toast(this, "请填写正确的联系方式");
			return;
		}
		
	}
	
//	private void submitData(String name,String phone) {
//		MultipartRequestParams params = new MultipartRequestParams();
//		
//		params.put("orderId", getIntent().getStringExtra("orderId"));
//		params.put("splitOrderId", getIntent().getStringExtra("splitOrderId"));
//		params.put("skuId",sku.getSkuId() + "");
//		params.put("reason", discription.getText().toString());
//		params.put("amount", apply_num.getText().toString());
//		params.put("contactName", name);
//		params.put("contactTel", phone);
//		for (int i=0;i<Bimp.drr.size();i++) {
//			String path = Bimp.drr.get(i);
//			params.put("refundImg"+i, new File(path));
//		}
//		
//		Http3Utils.doPostRequestTask(this, getHeaders(),
//				UrlUtil.CUSTOMER_SERVICE_APPLY, new VolleyJsonCallback() {
//
//					@Override
//					public void onSuccess(String result) {
//						HMessage msg = new Gson().fromJson(result, HMessage.class);
//						if(msg.getCode() == 200){
//							getActivity().finish();
//						}else{
//							
//						}
//					}
//
//					@Override
//					public void onError() {
//						ToastUtils.Toast(getActivity(), R.string.error);
//					}
//				}, params);
//	}

}
