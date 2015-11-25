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
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") 
public class LoginActivity extends BaseActivity implements OnClickListener{

	private TextView header;
	private EditText phone_edit;
	private TextView pwd_edit;
	private TextView login;
	private TextView regist;
	private TextView forget;
	private String phone;
	private String pwd;
	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		getActionBar().hide();
		setContentView(R.layout.login_layout);
		initView();
	}
	private void initView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("账号登录");
		phone_edit = (EditText) findViewById(R.id.phone_num);
		pwd_edit = (TextView) findViewById(R.id.pwd);
		forget = (TextView) findViewById(R.id.forget);
		login = (TextView) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		forget.setOnClickListener(this);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget:
			CommonUtil.doJump(this, ForgetPwdActivity.class);
			break;
		case R.id.login:
			checkInput();
			break;
		case R.id.regist:
			CommonUtil.doJump(this, RegistActivity.class);
			break;
		default:
			break;
		}
	}
	private void checkInput() {
		phone = phone_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		if(!CommonUtil.isPhoneNum(phone)){
			Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			return;
		}else if(pwd.equals("")){
			Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
			return;
		}else{
			doLogin();
		}
	}
	private void doLogin() {
		dialog = CommonUtil.dialog(this, "正在登录，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("name", phone));
				params.add(new BasicNameValuePair("password", pwd));
				String result = HttpUtils.postCommon("http://172.28.3.18:9004/api/login_user_name", params);
				Result loginInfo = DataParser.parserLoginResult(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = loginInfo;
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
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else if(result.isSuccess() == false){
					Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(LoginActivity.this, "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
				}
				break;

			default:
				break;
			}
		}
		
	};

}
