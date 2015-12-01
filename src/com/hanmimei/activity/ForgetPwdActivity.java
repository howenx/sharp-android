package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.hanmimei.R;
import com.hanmimei.data.DataParser;
import com.hanmimei.entity.Result;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;

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
public class ForgetPwdActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private ImageView back;
	private EditText phone_edit;
	private EditText code_edit;
	private EditText pwd_edit;
	private TextView get_code;
	private TextView send;
	private TextView new_pwd;
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
		header.setText("忘记密码");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		phone_edit = (EditText) findViewById(R.id.phone_num);
		code_edit = (EditText) findViewById(R.id.yanzheng);
		get_code = (TextView) findViewById(R.id.get_yanzheng);
		pwd_edit = (EditText) findViewById(R.id.pwd);
		send = (TextView) findViewById(R.id.regist);
		new_pwd = (TextView) findViewById(R.id.new_pwd);
		new_pwd.setText("新密码");
		back.setOnClickListener(this);
		get_code.setOnClickListener(this);
		send.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.get_yanzheng:
			if(!CommonUtil.isPhoneNum(phone)){
				Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			}else{
				getCode();
			}
			break;
		case R.id.regist:
			checkInput();
			break;
		default:
			break;
		}
	}
	private void getCode() {
		msg = CommonUtil.md5(phone + "hmm");
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("msg", msg));
				String result = HttpUtils.postCommon("http://172.28.3.18:9004/api/send_code", params);
				Result issucess = DataParser.parserLoginResult(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = issucess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void checkInput() {
		phone = phone_edit.getText().toString();
		yanzheng = code_edit.getText().toString();
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
			doUpPwd();
		}
	}
	private void doUpPwd() {
		dialog = CommonUtil.dialog(this, "正在注册，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("code", yanzheng));
				params.add(new BasicNameValuePair("password", pwd));
				String result = HttpUtils.postCommon("http://172.28.3.18:9004/api/reset_password", params);
				Result issucess = DataParser.parserLoginResult(result);
				Message msg = mHandler.obtainMessage(1);
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
					Intent intent = new Intent(ForgetPwdActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else if(result.isSuccess() == false){
					Toast.makeText(ForgetPwdActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ForgetPwdActivity.this, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2:
				Result code_result = (Result) msg.obj;
				if(code_result.isSuccess() == true){
					Toast.makeText(ForgetPwdActivity.this, code_result.getMessage(), Toast.LENGTH_SHORT).show();
				}else if(code_result.isSuccess() == false){
					Toast.makeText(ForgetPwdActivity.this, code_result.getMessage(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(ForgetPwdActivity.this, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
			}
		}
		
	};

}
