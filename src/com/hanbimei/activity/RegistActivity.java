package com.hanbimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.hanbimei.R;
import com.hanbimei.data.DataParser;
import com.hanbimei.entity.Result;
import com.hanbimei.utils.CommonUtil;
import com.hanbimei.utils.HttpUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") 
public class RegistActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private ImageView back;
	private EditText phone_edit;
	private EditText yanzheng_edit;
	private EditText pwd_edit;
	private TextView get_yanzheng;
	private TextView regist;
	
	private String phone;
	private String yanzheng;
	private String pwd;
	private String msg;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.regist_layout);
		getActionBar().hide();
		initView();
	}
	private void initView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("账号注册");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		phone_edit = (EditText) findViewById(R.id.phone_num);
		yanzheng_edit = (EditText) findViewById(R.id.yanzheng);
		get_yanzheng = (TextView) findViewById(R.id.get_yanzheng);
		get_yanzheng.setOnClickListener(this);
		pwd_edit = (EditText) findViewById(R.id.pwd);
		regist = (TextView) findViewById(R.id.regist);
		regist.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.get_yanzheng:
			phone = phone_edit.getText().toString();
			if(!CommonUtil.isPhoneNum(phone)){
				Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			}else{
				getYanZheng();
			}
			break;
		case R.id.regist:
			checkInput();
			break;
		default:
			break;
		}
	}
	private void checkInput() {
		phone = phone_edit.getText().toString();
		yanzheng = yanzheng_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		if(!CommonUtil.isPhoneNum(phone)){
			Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}else if(yanzheng.length() != 6){
			Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
			return;
		}else if(pwd.length() < 6 || pwd.length() > 20){
			Toast.makeText(this, "请输入6-20位密码", Toast.LENGTH_SHORT).show();
			return;
		}else{
			doRegist();
		}
	}
	private void doRegist() {
		dialog = CommonUtil.dialog(this, "正在注册，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("code", yanzheng));
				params.add(new BasicNameValuePair("password", pwd));
				String result = HttpUtils.postCommon("http://172.28.3.51:9004/api/reg", params);
				Result issucess = DataParser.parserLoginResult(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = issucess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void getYanZheng() {
		msg = CommonUtil.md5(phone + "hmm");
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("msg", msg));
				String result = HttpUtils.postCommon("http://172.28.3.51:9004/api/send_code", params);
				Result issucess = DataParser.parserLoginResult(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = issucess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				Result result = (Result) msg.obj;
				if(result.isSuccess() == true){
					Intent intent = new Intent(RegistActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else if(result.isSuccess() == false){
					Toast.makeText(RegistActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(RegistActivity.this, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				Result code_result = (Result) msg.obj;
				if(code_result.isSuccess() == true){
					Toast.makeText(RegistActivity.this, code_result.getMessage(), Toast.LENGTH_SHORT).show();
				}else if(code_result.isSuccess() == false){
					Toast.makeText(RegistActivity.this, code_result.getMessage(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(RegistActivity.this, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
		
	};

}
