package com.kakao.kakaogift.activity.login;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.kakao.kakaogift.R;
import com.kakao.kakaogift.activity.base.BaseActivity;
import com.kakao.kakaogift.application.KKApplication;
import com.kakao.kakaogift.dao.ShoppingGoodsDao;
import com.kakao.kakaogift.dao.UserDao;
import com.kakao.kakaogift.data.AppConstant;
import com.kakao.kakaogift.data.DataParser;
import com.kakao.kakaogift.data.UrlUtil;
import com.kakao.kakaogift.entity.HMessage;
import com.kakao.kakaogift.entity.ShoppingGoods;
import com.kakao.kakaogift.entity.User;
import com.kakao.kakaogift.http.VolleyHttp;
import com.kakao.kakaogift.http.VolleyHttp.VolleyJsonCallback;
import com.kakao.kakaogift.utils.ActionBarUtil;
import com.kakao.kakaogift.utils.CommonUtils;
import com.kakao.kakaogift.utils.DateUtils;
import com.kakao.kakaogift.utils.HttpUtils;
import com.kakao.kakaogift.utils.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @author eric
 * 登陆界面
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText phone_edit;
	private TextView pwd_edit;
	private TextView login;
	private TextView regist;
	private TextView forget;
	private String phone;
	private String pwd;
	private String code = "-1";
	private ProgressDialog dialog;
	private TextView attention;
	private UserDao userDao;
	private ShoppingGoodsDao goodsDao;
	private User user;
	private AlertDialog imgDialog;

	private boolean isDialogShow = false;
	private boolean isPwdShow = false;
	private ImageView clear_phone;
	private ImageView clear_pwd;
	private ImageView show_pwd;
	//友盟三方登陆
	private int loginFrom = 0;
	private UMShareAPI mShareAPI;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.login_layout);
		ActionBarUtil.setActionBarStyle(this, "账号登录");
		initView();
		registerReceivers();
		dialog = CommonUtils.dialog(this, "正在登录，请稍等...");
		mShareAPI = UMShareAPI.get(this);
	}
	/*
	 * 初始化控件
	 */
	private void initView() {
		phone_edit = (EditText) findViewById(R.id.phone_num);
		pwd_edit = (TextView) findViewById(R.id.pwd);
		forget = (TextView) findViewById(R.id.forget);
		login = (TextView) findViewById(R.id.login);
		regist = (TextView) findViewById(R.id.regist);
		attention = (TextView) findViewById(R.id.attention);
		clear_phone = (ImageView) findViewById(R.id.clear_phone);
		clear_pwd = (ImageView) findViewById(R.id.clear_pwd);
		show_pwd = (ImageView) findViewById(R.id.show_pwd);
		forget.setOnClickListener(this);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
		clear_phone.setOnClickListener(this);
		clear_pwd.setOnClickListener(this);
		show_pwd.setOnClickListener(this);
		findViewById(R.id.qq).setOnClickListener(this);
		findViewById(R.id.weixin).setOnClickListener(this);
		findViewById(R.id.sina).setOnClickListener(this);
		userDao = getDaoSession().getUserDao();
		goodsDao = getDaoSession().getShoppingGoodsDao();
		phone_edit.addTextChangedListener(phoneWatcher);
		pwd_edit.addTextChangedListener(pwdWatcher);
		User loginUser = userDao.queryBuilder().build().unique();
		if (loginUser != null) {
			phone_edit.setText(loginUser.getPhone());
			Selection.setSelection((Spannable) phone_edit.getText(), phone_edit
					.getText().toString().length());
		}
		if (getIntent().getStringExtra("phone") != null) {
			ActionBarUtil.setActionBarStyle(this, "绑定手机号");
			phone_edit.setText(getIntent().getStringExtra("phone"));
			findViewById(R.id.other).setVisibility(View.GONE);
			findViewById(R.id.other_login).setVisibility(View.GONE);
		}
	}

	// 手机号输入的监听
	TextWatcher phoneWatcher = new TextWatcher() {
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
				clear_phone.setVisibility(View.VISIBLE);
			} else {
				clear_phone.setVisibility(View.INVISIBLE);
			}
		}

	};
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.forget:
			CommonUtils.doJump(this, ForgetPwdActivity.class);
			break;
		case R.id.login:
			// 关闭键盘
			CommonUtils.closeBoardIfShow(this);
			checkInput();
			break;
		case R.id.regist:
			CommonUtils.doJump(this, CheckPhoneActivity.class);
			break;
		case R.id.refresh:
			loadImg();
			break;
		case R.id.besure:
			code = codeEditText.getText().toString();
			code_attention.setVisibility(View.GONE);
			// 检查图形校验码的格式
			if (code.length() != 4 || !CommonUtils.isJiaoYan(code)) {
				code_attention.setText("验证码格式不正确");
				code_attention.setVisibility(View.VISIBLE);
				return;
			} else {
				imgDialog.dismiss();
				isDialogShow = false;
				doLogin();
			}
			break;
		case R.id.cancle:
			imgDialog.dismiss();
			isDialogShow = false;
			break;
		// 清空手机号的输入
		case R.id.clear_phone:
			phone_edit.setText("");
			break;
		// 清空输入的密码
		case R.id.clear_pwd:
			pwd_edit.setText("");
			break;
		// 密码的隐藏显示
		case R.id.show_pwd:
			if (!isPwdShow) {
				show_pwd.setImageDrawable(getResources().getDrawable(
						R.drawable.hmm_login_eye_open));
				isPwdShow = true;
				pwd_edit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				Selection.setSelection((Spannable) pwd_edit.getText(), pwd_edit
						.getText().toString().length());
			} else {
				show_pwd.setImageDrawable(getResources().getDrawable(
						R.drawable.hmm_login_eye_close));
				isPwdShow = false;
				pwd_edit.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				Selection.setSelection((Spannable) pwd_edit.getText(), pwd_edit
						.getText().toString().length());
			}
			break;
		case R.id.qq:
			loginFrom = 1;
			dialog.show();
			doOtherLogin(SHARE_MEDIA.QQ);
			break;
		case R.id.weixin:
			loginFrom = 0;
			dialog.show();
			doOtherLogin(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.sina:
			loginFrom = 2;
			dialog.show();
			doOtherLogin(SHARE_MEDIA.SINA);
			break;
		default:
			break;
		}
	}

	
	//微信   qq登陆 新浪微博登陆
	private void doOtherLogin(SHARE_MEDIA platform) {
		// mShareAPI = UMShareAPI.get(this);
		if (!mShareAPI.isInstall(this, platform)) {
			dialog.dismiss();
			ToastUtils.Toast(this, "请安装客户端");
			return;
		}
		mShareAPI.doOauthVerify(this, platform, umAuthListener);
	}
	//友盟三方登陆回调监听
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action,
				Map<String, String> data) {
			if(loginFrom == 0){
				data.put("idtype", "WO");
			}else if(loginFrom == 1){
				data.put("idtype", "Q");
			}else{
				data.put("idtype", "S");
			}
			checkOtherLogin(data);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			dialog.dismiss();
			ToastUtils.Toast(getApplicationContext(), "登陆失败");
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			dialog.dismiss();
			ToastUtils.Toast(getApplicationContext(), "登陆取消");
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}

	//校验三方登陆是否登陆过
	private void checkOtherLogin(final Map<String, String> data) {
		String url = "";
		if(loginFrom == 0){
			url = UrlUtil.WEIXIN_CHECK + data.get("unionid") + "&openId=" + data.get("openid");
		}else if(loginFrom == 1){
			url = UrlUtil.QQ_CHECK + data.get("openid");
		}else if(loginFrom == 2){
			url = UrlUtil.WEIBO_CHECK + data.get("uid");
		}
		VolleyHttp.doGetRequestTask(url, new VolleyJsonCallback() {

			@Override
			public void onSuccess(String result) {
				dialog.dismiss();
				HMessage loginInfo = DataParser.paserResultMsg(result);
				if (loginInfo.getCode() == 4003) {
					Intent intent = new Intent(LoginActivity.this,
							BindPhoneActivity.class);
					setMap(data);
					startActivity(intent);
				} else if (loginInfo.getCode() == 200) {
					loginSuccess(loginInfo);
				}
			}

			@Override
			public void onError() {
				dialog.dismiss();
				ToastUtils.Toast(LoginActivity.this, "登录失败，请检查您的网络");
			}
		});
	}
	//服务器请求验证码
	@SuppressLint("ShowToast")
	private void loadImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = HttpUtils.getImg(UrlUtil.GET_IMG_CODE
						+ Math.round(Math.random() * 1000000));
				Message msg = mHandler.obtainMessage(4);
				msg.obj = bitmap;
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	//校验输入
	private void checkInput() {
		phone = phone_edit.getText().toString();
		pwd = pwd_edit.getText().toString();
		if (!CommonUtils.isPhoneNum(phone)) {
			// setAttention("请输入正确的手机号");
			CommonUtils.setAttention(attention, "请输入正确的手机号");
			return;
		} else if (pwd.length() < 6) {
			CommonUtils.setAttention(attention, "密码至少应为6位");
		} else if (!CommonUtils.isPassWord(pwd)) {
			CommonUtils.setAttention(attention, "密码为数字和字母组合");
			return;
		} else {
			doLogin();
		}
	}
	//请求服务器，进行登陆操作
	private void doLogin() {
		dialog = CommonUtils.dialog(this, "正在登录，请稍等...");
		dialog.show();
		submitTask(new Runnable() {
			@Override
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("phone", phone));
				params.add(new BasicNameValuePair("password", pwd));
				params.add(new BasicNameValuePair("code", code));
				if (getMap() != null) {
					params.add(new BasicNameValuePair("accessToken", getMap()
							.get("access_token")));
					if(getMap().get("idtype").equals("S") || getMap().get("idtype").equals("B")){
						params.add(new BasicNameValuePair("openId", getMap().get(
								"uid")));
					}else{
						params.add(new BasicNameValuePair("openId", getMap().get(
							"openid")));
					}
					params.add(new BasicNameValuePair("idType", getMap().get(
							"idtype")));
					if(getMap().get("idtype").equals("WO"))
						params.add(new BasicNameValuePair("unionId", getMap().get("unionid")));
				}
				String result = HttpUtils.postCommon(UrlUtil.LOGIN_URL, params);
				HMessage loginInfo = DataParser.paserResultMsg(result);
				Message msg = mHandler.obtainMessage(1);
				msg.obj = loginInfo;
				mHandler.sendMessage(msg);
			}
		});
	}
	//登陆成功之后的操作
	private void loginSuccess(HMessage result) {
		User user = new User();
		user.setPhone(phone);
		user.setUserId(0);
		user.setToken(result.getTag());
		user.setIsBind(false);
		user.setExpired(DateUtils.turnToDate(result.getTime()));
		user.setLast_login(DateUtils.getCurrentDate());
		KKApplication application = (KKApplication) getApplication();
		application.setLoginUser(user);
		//极光推送   设置设备别名
		JPushInterface.setAlias(LoginActivity.this, result.getUserId(), null);
		// 登录用户存储到本地sql
		userDao.deleteAll();
		userDao.insert(user);
		if (goodsDao.queryBuilder().list() != null
				&& goodsDao.queryBuilder().list().size() > 0) {
			sendShoppingCar();
		} else {
			sendBroadcast(new Intent(AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
		}
		setMap(null);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				code = "-1";
				HMessage result = (HMessage) msg.obj;
				if (result.getCode() != null) {
					if (result.getCode() == 200) {
						loginSuccess(result);
					} else if (result.getCode() == 4001) {
						if (!isDialogShow)
							showDialog();
						loadImg();
					} else {
						CommonUtils
								.setAttention(attention, result.getMessage());
					}
				} else {
					CommonUtils.setAttention(attention, "网络连接异常，请检查网络");
				}
				dialog.dismiss();
				break;
			case 2:
				HMessage hmsg = (HMessage) msg.obj;
				if (hmsg.getCode() == 200) {
					goodsDao.deleteAll();
				}
				sendBroadcast(new Intent(
						AppConstant.MESSAGE_BROADCAST_LOGIN_ACTION));
				break;
			case 4:
				Bitmap bitmap = (Bitmap) msg.obj;
				setDialogImg(bitmap);
				break;
//			case 5:
//				String json = (String) msg.obj;
//				try {
//		            if (json != null) {
//		            	JSONObject object = new JSONObject(json);
//			            Map<String, String> data = new HashMap<String, String>();
//			            data.put("idtype", "B");
//			            data.put("uid", object.getString("uid"));
//			            data.put("access_token", baidu.getAccessTokenManager().getAccessToken());
//			            checkOtherLogin(data);
//					}
//					} catch (JSONException e) {
//						e.printStackTrace();
//		            }
//				break;

			default:
				break;
			}
		}

	};

	private List<ShoppingGoods> list;
	//本地购物车数据 提交至服务器
	private void sendShoppingCar() {
		user = getUser();
		list = goodsDao.queryBuilder().list();
		if (list != null && list.size() > 0) {
			toObject();
			new Thread(new Runnable() {
				@Override
				public void run() {
					String result = HttpUtils.post(UrlUtil.GET_CAR_LIST_URL,
							array, "id-token", user.getToken());
					HMessage hMessage = DataParser.paserResultMsg(result);
					Message msg = mHandler.obtainMessage(2);
					msg.obj = hMessage;
					mHandler.sendMessage(msg);
				}
			}).start();
		}
	}

	private JSONArray array;
	//数据格式转化
	private void toObject() {
		try {
			array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				ShoppingGoods goods = list.get(i);
				JSONObject object = new JSONObject();
				object.put("cartId", 0);
				object.put("skuId", goods.getGoodsId());
				object.put("amount", goods.getGoodsNums());
				object.put("state", goods.getState());
				object.put("skuType", goods.getSkuType());
				object.put("skuTypeId", goods.getSkuTypeId());
				object.put("orCheck", goods.getOrCheck());
				//购物车数据来源,1登陆后同步,2详细页面点击加入购物车,3点击购物车列表页操作(增删减)
				object.put("cartSource", 1);
				array.put(object);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private ImageView jiaoyanImg;
	private EditText codeEditText;
	private TextView code_attention;

	/*
	 * 初始化安全校验的弹窗
	 */
	@SuppressLint("InflateParams")
	private void showDialog() {
		isDialogShow = true;
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
		view.findViewById(R.id.cancle).setOnClickListener(this);
		view.findViewById(R.id.besure).setOnClickListener(this);
		view.findViewById(R.id.refresh).setOnClickListener(this);
	}
	//设置校验图片
	private void setDialogImg(Bitmap bitmap) {
		jiaoyanImg.setImageBitmap(bitmap);
	}

	private MyBroadCastReceiver netReceiver;
	//广播注册
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
				LoginActivity.this.finish();
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
