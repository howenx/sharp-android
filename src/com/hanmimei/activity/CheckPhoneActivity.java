package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.DoJumpUtils;
import com.hanmimei.utils.HttpUtils;

@SuppressLint("NewApi")
public class CheckPhoneActivity extends BaseActivity implements OnClickListener {

	private TextView next;
	private EditText phone;
	private TextView go_login;
	private ProgressDialog dialog;
	private String phone_num;
	private TextView attention;

	private AlertDialog alertDialog;
	private ImageView clear_phone;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "手机注册");
		setContentView(R.layout.phone_check_layout);
		findView();
		registerReceivers();
	}

	private void findView() {
		next = (TextView) findViewById(R.id.next);
		phone = (EditText) findViewById(R.id.phone);
		go_login = (TextView) findViewById(R.id.go_login);
		attention = (TextView) findViewById(R.id.attention);
		next.setClickable(false);
		go_login.setOnClickListener(this);
		phone.addTextChangedListener(mTextWatcher);
		clear_phone = (ImageView) findViewById(R.id.clear_phone);
		clear_phone.setOnClickListener(this);
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() == 11) {
				next.setBackground(getResources().getDrawable(
						R.drawable.theme_button_bg));
				next.setClickable(true);
				next.setOnClickListener(CheckPhoneActivity.this);
			} else {
				next.setBackground(getResources().getDrawable(
						R.drawable.huise_button_bg));
				next.setClickable(false);
				next.setOnClickListener(null);
			}
			if (s.length() != 0) {
				clear_phone.setVisibility(View.VISIBLE);
			} else {
				clear_phone.setVisibility(View.INVISIBLE);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.go_login:
			finish();
			break;
		case R.id.next:
			//关闭键盘
			CommonUtil.closeBoardIfShow(this);
			phone_num = phone.getText().toString();
			if (!CommonUtil.isPhoneNum(phone_num)) {
				CommonUtil.setAttention(attention,"请填写正确的手机号");
				return;
			} else {
				checkPhone();
			}
			break;
		case R.id.cancle:
			alertDialog.dismiss();
			break;
		case R.id.besure:
			alertDialog.dismiss();
			DoJumpUtils.doJump(this, ForgetPhoneActivity.class);
			finish();
			break;

		// 清空手机号的输入
		case R.id.clear_phone:
			phone.setText("");
			break;
		default:
			break;
		}
	}

	private void checkPhone() {
		dialog = CommonUtil.dialog(this, "请稍后...");
		dialog.show();
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone_num));
				String result = HttpUtils.postCommon(UrlUtil.CHECK_PHONE_REGIST, params);
				HMessage hMessage = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hMessage;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				HMessage hMessage = (HMessage) msg.obj;
				if (hMessage.getCode() != null) {
					if (hMessage.getCode() == 4003) {
						Intent intent = new Intent(CheckPhoneActivity.this,
								RegistActivity.class);
						intent.putExtra("phone", phone_num);
						intent.putExtra("from", "regist");
						startActivity(intent);
					} else if (hMessage.getCode() == 5001) {
						showDialog();
					}
				} else {
					CommonUtil.setAttention(attention, "网络连接异常，请检查网络");
				}
				break;
			default:
				break;
			}
		}
	};

	private void showDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,
				null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(view);
		alertDialog.show();
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView besure = (TextView) view.findViewById(R.id.besure);
		title.setText("该手机号已经注册");
		besure.setText("找回密码");
		view.findViewById(R.id.cancle).setOnClickListener(this);
		view.findViewById(R.id.besure).setOnClickListener(this);
	}
	private MyBroadCastReceiver netReceiver;
	private void registerReceivers() {
		netReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_UPDATE_SHOPPINGCAR);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		intentFilter.addAction(AppConstant.MESSAGE_BROADCAST_QUIT_LOGIN_ACTION);
		getActivity().registerReceiver(netReceiver, intentFilter);
	}
	private class MyBroadCastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				CheckPhoneActivity.this.finish();
			}
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(netReceiver);
	}
}
