package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.Result;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.YanZhengCodeTextView;
import com.umeng.analytics.MobclickAgent;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
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
public class ForgetPwdActivity extends BaseActivity implements OnClickListener,TimeEndListner{

	private TextView header;
	private ImageView back;
	private EditText phone_edit;
	private EditText code_edit;
	private EditText pwd_edit;
	private YanZhengCodeTextView get_code;
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
//		getActionBar().hide();
		initView();
	}
	private void initView() {
		header = (TextView) findViewById(R.id.header);
		header.setText("忘记密码");
		back = (ImageView) findViewById(R.id.back);
		back.setVisibility(View.VISIBLE);
		phone_edit = (EditText) findViewById(R.id.phone_num);
		code_edit = (EditText) findViewById(R.id.yanzheng);
		get_code = (YanZhengCodeTextView) findViewById(R.id.get_yanzheng);
		pwd_edit = (EditText) findViewById(R.id.pwd);
		send = (TextView) findViewById(R.id.regist);
		new_pwd = (TextView) findViewById(R.id.new_pwd);
		new_pwd.setText("新密码");
		back.setOnClickListener(this);
		get_code.setOnClickListener(this);
		get_code.setTimeEndListner(this);
		send.setOnClickListener(this);
		
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
	
	//获取验证码
	private void getCode() {//验证码倒计时，不可点击
		get_code.setClickable(false);
		Drawable background = getResources().getDrawable(R.drawable.huise_button_bg);
		get_code.setBackground(background);
		get_code.setTimes(60);
		get_code.beginRun();
		//加密
		msg = CommonUtil.md5(phone + "hmm");
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("msg", msg));
				String result = HttpUtils.postCommon(UrlUtil.GET_CODE_URL, params);
				Result issucess = DataParser.parserLoginResult(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = issucess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
	//检查输入
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
	
	//修改密码
	private void doUpPwd() {
		dialog = CommonUtil.dialog(this, "正在修改，请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("code", yanzheng));
				params.add(new BasicNameValuePair("password", pwd));
				String result = HttpUtils.postCommon(UrlUtil.RESET_PWD_URL, params);
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
//					Intent intent = new Intent(ForgetPwdActivity.this, MainActivity.class);
//					startActivity(intent);
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
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("ForgetPwdActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("ForgetPwdActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}
	@Override
	public void isTimeEnd() {
		get_code.setClickable(true);
		Drawable background = getResources().getDrawable(R.drawable.theme_button_bg);
		get_code.setBackground(background);
	}

}
