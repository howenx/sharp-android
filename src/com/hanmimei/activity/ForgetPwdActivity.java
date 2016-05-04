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
import android.graphics.Bitmap;
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
import com.hanmimei.activity.base.BaseActivity;
import com.hanmimei.data.AppConstant;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.override.HTextWatcher;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;

/**
 * @author eric
 *
 */
@SuppressLint("NewApi")
public class ForgetPwdActivity extends BaseActivity implements
		OnClickListener {
	private TextView next;
	private EditText phone;
	private TextView go_login;
	private ProgressDialog dialog;
	private String phone_num;
	private TextView attention;

	private AlertDialog alertDialog;

	private ImageView jiaoyanImg;
	private EditText codeEditText;
	private AlertDialog imgDialog;
	private ImageView clear_phone;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBarUtil.setActionBarStyle(this, "找回密码");
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
		attention.setVisibility(View.INVISIBLE);
		findViewById(R.id.bom_re).setVisibility(View.GONE);
		clear_phone = (ImageView) findViewById(R.id.clear_phone);
		clear_phone.setOnClickListener(this);
	}

	TextWatcher mTextWatcher = new HTextWatcher(){

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() == 11) {
				next.setBackgroundResource(R.drawable.btn_theme_radius_selector);
				next.setClickable(true);
				next.setOnClickListener(ForgetPwdActivity.this);
			} else {
				next.setBackgroundResource(R.drawable.huise_button_bg);
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
				showCodeDialog();
				loadImg();
			}
			break;
		case R.id.cancel:
			alertDialog.dismiss();
			break;
		case R.id.confirm:
			alertDialog.dismiss();
			
			Intent intent = new Intent(ForgetPwdActivity.this,
					RegistActivity.class);
			intent.putExtra("phone", phone_num);
			intent.putExtra("from", "regist");
			startActivity(intent);
			
			break;

		// 清空手机号的输入
		case R.id.clear_phone:
			phone.setText("");
			break;
		default:
			break;
		}
	}

	private void loadImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = HttpUtils
						.getImg(UrlUtil.GET_IMG_CODE+ Math.round(Math.random() * 1000000));
				Message msg = mHandler.obtainMessage(3);
				msg.obj = bitmap;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void checkPhone() {
		dialog = CommonUtil.dialog(this, "请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone_num));
				params.add(new BasicNameValuePair("code", code));
				result = HttpUtils.postCommon(UrlUtil.CHECK_PHONE_FORGET, params);
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
					// 尚未注册
					if (hMessage.getCode() == 4003) {
						showDialog();
					} else if (hMessage.getCode() == 5001) {
						Intent intent = new Intent(ForgetPwdActivity.this,
								RegistActivity.class);
						intent.putExtra("phone", phone_num);
						intent.putExtra("from", "forget");
						startActivity(intent);
					} else {
						CommonUtil.setAttention(attention,hMessage.getMessage());
					}
				} else {
					CommonUtil.setAttention(attention,"网络连接异常，请检查网络");
				}
				break;
			case 3:
				Bitmap bitmap = (Bitmap) msg.obj;
				setDialogImg(bitmap);
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
		TextView besure = (TextView) view.findViewById(R.id.confirm);
		title.setText("该手机号尚未注册");
		besure.setText("立即注册");
		view.findViewById(R.id.cancel).setOnClickListener(this);
		view.findViewById(R.id.confirm).setOnClickListener(this);
	}

	private void setDialogImg(Bitmap bitmap) {
		jiaoyanImg.setImageBitmap(bitmap);
	}

	private String code;
	private TextView code_attention;

	private void showCodeDialog() {
		View view = LayoutInflater.from(this).inflate(R.layout.img_save_layout,
				null);
		imgDialog = new AlertDialog.Builder(this).create();
		imgDialog.setView(view);
		imgDialog.show();
		TextView title = (TextView) view.findViewById(R.id.title);
		jiaoyanImg = (ImageView) view.findViewById(R.id.img);
		codeEditText = (EditText) view.findViewById(R.id.code);
		code_attention = (TextView) view.findViewById(R.id.attention);
		title.setText("安全校验");
		view.findViewById(R.id.cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						imgDialog.dismiss();
					}
				});
		view.findViewById(R.id.besure).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						CommonUtil.closeBoard(ForgetPwdActivity.this);
						code_attention.setVisibility(View.GONE);
						code = codeEditText.getText().toString();
						if (code.length() != 4 || !CommonUtil.isJiaoYan(code)) {
							code_attention.setText("验证码格式不正确");
							code_attention.setVisibility(View.VISIBLE);
							return;
						} else {
							imgDialog.dismiss();
							checkPhone();
						}
					}
				});
		view.findViewById(R.id.refresh).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						loadImg();
					}
				});
	}

	
	private MyBroadCastReceiver netReceiver;

	// 广播接收者 注册
	private void registerReceivers() {
		netReceiver = new MyBroadCastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION);
		registerReceiver(netReceiver, intentFilter);
	}
	private class MyBroadCastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION)) {
				ForgetPwdActivity.this.finish();
			} 
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netReceiver);
	}
}
