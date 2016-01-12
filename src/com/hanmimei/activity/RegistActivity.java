package com.hanmimei.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.hanmimei.R;
import com.hanmimei.activity.listener.TimeEndListner;
import com.hanmimei.data.DataParser;
import com.hanmimei.data.UrlUtil;
import com.hanmimei.entity.HMessage;
import com.hanmimei.entity.Result;
import com.hanmimei.utils.ActionBarUtil;
import com.hanmimei.utils.CommonUtil;
import com.hanmimei.utils.HttpUtils;
import com.hanmimei.view.YanZhengCodeTextView;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi") 
public class RegistActivity extends BaseActivity implements OnClickListener,TimeEndListner{

	private TextView phone_TextView;
	private EditText yanzheng_edit;
	private EditText pwd_edit;
	private EditText pwd_agin_edit;
	private YanZhengCodeTextView get_yanzheng;
	private TextView regist;
	private TextView attention;
	private String phone;
	private String yanzheng;
	private String pwd;
	private String pwd_agin;
	private String msg;
	private ProgressDialog dialog;
	
	private boolean isRegist;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.regist_layout);
		if(getIntent().getStringExtra("from").equals("forget")){
			isRegist = false;
			ActionBarUtil.setActionBarStyle(this, "找回密码");
		}else{
			isRegist = true;
			ActionBarUtil.setActionBarStyle(this, "账号注册");
		}
		
		initView();
	}
	private void initView() {
		phone = getIntent().getStringExtra("phone");
		phone_TextView = (TextView) findViewById(R.id.phone);
		yanzheng_edit = (EditText) findViewById(R.id.yanzheng);
		get_yanzheng = (YanZhengCodeTextView) findViewById(R.id.get_yanzheng);
		attention = (TextView) findViewById(R.id.attention);
		get_yanzheng.setOnClickListener(this);
		get_yanzheng.setTimeEndListner(this);
		pwd_edit = (EditText) findViewById(R.id.pwd);
		pwd_agin_edit = (EditText) findViewById(R.id.two_pwd);
		regist = (TextView) findViewById(R.id.regist);
		if(!isRegist)
			regist.setText("重置");
		regist.setOnClickListener(this);
		phone_TextView.setText("已经发送验证码至  " + phone.substring(0, 3) + "****" + phone.substring(8, phone.length()));
		getYanZheng();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_yanzheng:
			getYanZheng();
			break;
		case R.id.regist:
			checkInput();
			break;
		default:
			break;
		}
	}
	private void checkInput() {
		yanzheng = yanzheng_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		pwd_agin = pwd_agin_edit.getText().toString();
		if(yanzheng.length() != 6){
//			Toast.makeText(this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
			setAttention("请输入6位验证码");
			return;
		}else if(pwd.length() < 6 || pwd.length() > 12){
//			Toast.makeText(this, "请输入6-20位密码", Toast.LENGTH_SHORT).show();
			setAttention("请输入6-12位密码");
			return;
		}else if(!CommonUtil.isPassWord(pwd)){
			setAttention("密码必须位数字和字母的组合");
			return;
		}else if(!pwd.equals(pwd_agin)){
			setAttention("两次输入的密码不一致");
		}else{
			doRegist();
		}
	}
	private void doRegist() {
		dialog = CommonUtil.dialog(this, "请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("code", yanzheng));
				params.add(new BasicNameValuePair("password", pwd));
				if(isRegist){
					result = HttpUtils.postCommon(UrlUtil.REGIST_URL, params);
				}else{
					result = HttpUtils.postCommon(UrlUtil.RESET_PWD_URL, params);
				}
				HMessage hMessage = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hMessage;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	private void getYanZheng() {
		//验证码倒计时，不可点击
		get_yanzheng.setClickable(false);
		get_yanzheng.setTextColor(getResources().getColor(R.color.huise));
		get_yanzheng.setTimes(60);
		get_yanzheng.beginRun();
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
	private void setAttention(String att){
		attention.setText(att);
		attention.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
					Message msg = mHandler.obtainMessage(3);
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
				HMessage result = (HMessage) msg.obj;
				if(result.getCode() != null){
					if(result.getCode() == 200){
						finish();
					}else{
						setAttention(result.getMessage());
					}
				}else{
					setAttention("网络连接异常，请检查网络");
				}
				break;
			case 2:
				
				Result code_result = (Result) msg.obj;
				if(code_result.isSuccess() == true){
					setAttention(code_result.getMessage());
				}else if(code_result.isSuccess() == false){
					setAttention(code_result.getMessage());
				}else{
					setAttention("网络连接异常，请检查网络");
				}
				break;
			case 3:
				attention.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}
		
	};
	@Override
	public void isTimeEnd() {
//		倒计时结束，获取验证码可以点击
		get_yanzheng.setClickable(true);
		get_yanzheng.setText("获取验证码");
		get_yanzheng.setTextColor(getResources().getColor(R.color.theme));
	}
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("RegistActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
	    MobclickAgent.onResume(this);          //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("RegistActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
	    MobclickAgent.onPause(this);
	}

}
