package com.kakao.kakaogift.activity.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kakao.kakaogift.R
;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.activity.mine.config.HmmServiceActivity;
import com.kakao.kakaogift.application.KKApplication;
import com.kakao.kakaogift.dao.ShoppingGoodsDao;
import com.kakao.kakaogift.dao.UserDao;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.override.TimeEndListner;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.DateUtils;
import com.kakao.kakaogift.utils.HttpUtils;
import com.kakao.kakaogift.utils.KeyWordUtil;
import com.kakao.kakaogift.view.YanZhengCodeTextView;

/**
 * @author eric
 *注册页面
 */
@SuppressLint("NewApi")
public class RegistActivity extends BaseActivity implements OnClickListener,
		TimeEndListner {

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
	private TextView agree_us;
	private boolean isRegist;
	private ImageView clear_pwd;
	private ImageView clear_pwd2;
	private ImageView show_pwd;
	private ImageView show_pwd2;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.regist_layout);
		if (getIntent().getStringExtra("from").equals("forget")) {
			isRegist = false;
			ActionBarUtil.setActionBarStyle(this, "找回密码");
		} else {
			isRegist = true;
			ActionBarUtil.setActionBarStyle(this, "账号注册");
		}
		initView();
	}
	/*
	 * 初始化控件
	 */
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
		clear_pwd = (ImageView) findViewById(R.id.clear_pwd);
		clear_pwd2 = (ImageView) findViewById(R.id.clear_pwd2);
		show_pwd = (ImageView) findViewById(R.id.show_pwd);
		show_pwd2 = (ImageView) findViewById(R.id.show_pwd2);
		agree_us = (TextView) findViewById(R.id.agree_us);
		clear_pwd.setOnClickListener(this);
		clear_pwd2.setOnClickListener(this);
		show_pwd.setOnClickListener(this);
		show_pwd2.setOnClickListener(this);
		if (!isRegist) {
			regist.setText("重置");
		} else {
			agree_us.setText(KeyWordUtil.matcherSearchTitle(getResources()
					.getColor(R.color.yellow),
					getResources().getString(R.string.regist_agree_us),
					getResources().getString(R.string.regist_agree_us_key)));
			agree_us.setOnClickListener(this);
			agree_us.setVisibility(View.VISIBLE);
		}
		regist.setOnClickListener(this);
		phone_TextView.setText("已经发送验证码至  " + phone.substring(0, 3) + "****"
				+ phone.substring(7, phone.length()));
		getYanZheng();
		userDao = getDaoSession().getUserDao();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		pwd_edit.addTextChangedListener(pwdWatcher);
		pwd_agin_edit.addTextChangedListener(pwd2Watcher);
	}

	// 密码输入的监听
	TextWatcher pwdWatcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() != 0) {
				clear_pwd.setVisibility(View.VISIBLE);
			} else {
				clear_pwd.setVisibility(View.INVISIBLE);
			}
		}
	};
	// 密码输入的监听
	TextWatcher pwd2Watcher = new TextWatcher() {
		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (s.length() != 0) {
				clear_pwd2.setVisibility(View.VISIBLE);
			} else {
				clear_pwd2.setVisibility(View.INVISIBLE);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_yanzheng:
			get_yanzheng.stopRun();
			getYanZheng();
			break;
		case R.id.regist:
			// 关闭键盘
			CommonUtils.closeBoardIfShow(this);
			checkInput();
			break;
		// 清空手机号的输入
		case R.id.clear_pwd:
			pwd_edit.setText("");
			break;
		// 清空输入的密码
		case R.id.clear_pwd2:
			pwd_agin_edit.setText("");
			break;
		// 密码的隐藏显示
		case R.id.show_pwd:
			if (!isPwdShow) {
				show_pwd.setImageResource(R.drawable.hmm_login_eye_open);
				isPwdShow = true;
				pwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				Selection.setSelection((Spannable) pwd_edit.getText(), pwd_edit
						.getText().toString().length());
			} else {
				show_pwd.setImageResource(R.drawable.hmm_login_eye_close);
				isPwdShow = false;
				pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				Selection.setSelection((Spannable) pwd_edit.getText(), pwd_edit
						.getText().toString().length());
			}
			break;
		case R.id.show_pwd2:
			if (!isPwdShow2) {
				show_pwd2.setImageResource(R.drawable.hmm_login_eye_open);
				isPwdShow2 = true;
				pwd_agin_edit
						.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				Selection.setSelection((Spannable) pwd_agin_edit.getText(),
						pwd_agin_edit.getText().toString().length());
			} else {
				show_pwd2.setImageResource(R.drawable.hmm_login_eye_close);
				isPwdShow2 = false;
				pwd_agin_edit.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				Selection.setSelection((Spannable) pwd_agin_edit.getText(),
						pwd_agin_edit.getText().toString().length());
			}
			break;
		case R.id.agree_us:
			startActivity(new Intent(getActivity(), HmmServiceActivity.class));
			break;
		default:
			break;
		}
	}

	private boolean isPwdShow = false;
	private boolean isPwdShow2 = false;

	private void checkInput() {
		yanzheng = yanzheng_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		pwd_agin = pwd_agin_edit.getText().toString();
		if (yanzheng.length() != 6) {
			CommonUtils.setAttention(attention, "请输入6位验证码");
			return;
		} else if (pwd.length() < 6 || pwd.length() > 12) {
			CommonUtils.setAttention(attention, "请输入6-12位密码");
			return;
		} else if (!CommonUtils.isPassWord(pwd)) {
			CommonUtils.setAttention(attention, "密码必须位数字和字母的组合");
			return;
		} else if (!pwd.equals(pwd_agin)) {
			CommonUtils.setAttention(attention, "两次输入的密码不一致");
		} else {
			doRegist();
		}
	}

	private void doRegist() {
		dialog = CommonUtils.dialog(this, "请稍后...");
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "";
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("code", yanzheng));
				params.add(new BasicNameValuePair("password", pwd));
				if(getMap() != null){
					params.add(new BasicNameValuePair("accessToken", getMap().get("access_token")));
					params.add(new BasicNameValuePair("openId", getMap().get("openid")));
					params.add(new BasicNameValuePair("idType", getMap().get("idtype")));
					params.add(new BasicNameValuePair("unionId", getMap().get("unionid")));
				}
				if (isRegist) {
					result = HttpUtils.postCommon(UrlUtil.REGIST_URL, params);
				} else {
					result = HttpUtils
							.postCommon(UrlUtil.RESET_PWD_URL, params);
				}
				HMessage hMessage = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = hMessage;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private void getYanZheng() {
		// 验证码倒计时，不可点击
		get_yanzheng.setClickable(false);
		get_yanzheng.setTextColor(getResources().getColor(R.color.huise));
		get_yanzheng.setTimes(80);
		get_yanzheng.beginRun();
		// 加密
		msg = CommonUtils.md5(phone + "hmm");
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("msg", msg));
				if(isRegist){
					params.add(new BasicNameValuePair("smsType", "register"));
				}else{
					params.add(new BasicNameValuePair("smsType", "reset"));
				}
				String result = HttpUtils.postCommon(UrlUtil.GET_CODE_URL,
						params);
				HMessage issucess = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(2);
				msg.obj = issucess;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				HMessage result = (HMessage) msg.obj;
				if (result.getCode() != null) {
					if (result.getCode() == 200) {
						// if (isRegist) {
						doLogin(result);
						// } else {
						// sendBroadcast(new
						// Intent(AppConstant.MESSAGE_BROADCAST_FORGET_OK_ACTION));
						// finish();
						//
						// }
					} else {
						CommonUtils.setAttention(attention, result.getMessage());
					}
				} else {
					CommonUtils.setAttention(attention, "网络连接异常，请检查网络");
				}
				break;
			case 2:
				HMessage code_result = (HMessage) msg.obj;
				if (code_result.getCode() != null) {
					if (code_result.getCode() == 200) {
						CommonUtils.setAttention(attention, "验证码发送成功！");
					} else if (code_result.getCode() == 5005) {
						get_yanzheng.stopRun();
						get_yanzheng.setText("获取验证码");
						get_yanzheng.setClickable(false);
						get_yanzheng.setTextColor(getResources().getColor(
								R.color.huise));
						CommonUtils.setAttention(attention,
								code_result.getMessage());
					} else {
						isTimeEnd();
						CommonUtils.setAttention(attention, "验证码发送失败！");
					}
				} else {
					CommonUtils.setAttention(attention, "网络连接异常，请检查网络");
				}
				break;
			case 5:
				goodsDao.deleteAll();
				sendBroadcast(new Intent(
						AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
				// DoJumpUtils.doJump(RegistActivity.this, MainActivity.class);
				finish();
				break;
			default:
				break;
			}
		}

	};

	private void doLogin(HMessage hMessage) {
		User user = new User();
		user.setUserId(0);
		user.setToken(hMessage.getTag());
		user.setIsBind(false);
		user.setExpired(DateUtils.turnToDate(hMessage.getTime()));
		user.setLast_login(DateUtils.getCurrentDate());
		KKApplication application = (KKApplication) getApplication();
		application.setLoginUser(user);
		// 登录用户存储到本地sql
		userDao.deleteAll();
		userDao.insert(user);
		if (goodsDao.queryBuilder().build().list() != null
				&& goodsDao.queryBuilder().build().list().size() > 0) {
			sendShoppingCar();
		} else {
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
			finish();
		}
		setMap(null);
	}

	private UserDao userDao;
	private ShoppingGoodsDao goodsDao;
	private User user;

	@Override
	public void isTimeEnd() {
		// 倒计时结束，获取验证码可以点击
		get_yanzheng.setClickable(true);
		get_yanzheng.setText("获取验证码");
		get_yanzheng.setTextColor(getResources().getColor(R.color.yellow));
	}

	private List<ShoppingGoods> list;
	
	private void sendShoppingCar() {
		user = getUser();
		list = goodsDao.queryBuilder().build().list();
		if (list != null && list.size() > 0) {
			toObject();
			new Thread(new Runnable() {
				@Override
				public void run() {
					HttpUtils.post(UrlUtil.GET_CAR_LIST_URL, array, "id-token",
							user.getToken());
					Message msg = mHandler.obtainMessage(5);
					mHandler.sendMessage(msg);
				}
			}).start();
		}
	}

	private JSONArray array;

	private void toObject() {
		try {
			array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				ShoppingGoods goods = list.get(i);
				JSONObject object = new JSONObject();
				object.put("cartId", goods.getCartId());
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// private void doLogin() {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// List<NameValuePair> params = new ArrayList<NameValuePair>();
	// params.add(new BasicNameValuePair("name", phone));
	// params.add(new BasicNameValuePair("password", pwd));
	// params.add(new BasicNameValuePair("code", "-1"));
	// String result = HttpUtils.postCommon(UrlUtil.LOGIN_URL, params);
	// HMessage loginInfo = DataParser.paserResultMsg(result);
	// Message msg = mHandler.obtainMessage(4);
	// msg.obj = loginInfo;
	// mHandler.sendMessage(msg);
	// }
	// }).start();
	// }

}
